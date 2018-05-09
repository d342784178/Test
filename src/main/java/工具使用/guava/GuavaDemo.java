package 工具使用.guava;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * @author minjun
 * @ClassName: GuavaDemo
 * @Description: guava之函数编程
 * @date 2015年6月14日 下午7:06:43
 */
@SuppressWarnings("unused")
public class GuavaDemo {
    @Test
    public void joiner() {
        //字符串拼接
        ArrayList<String> strings = Lists.newArrayList("getSolution", null, "aa", "v");
        String            join    = Joiner.on(",").skipNulls().join(strings);

        System.out.println(join);
    }

    /**
     * @return void 返回类型
     * @exception
     * @Title: testSplitter
     * @Description: 将含有指定分隔符的字符串分开
     */
    @Test
    public void testSplitter() {
        String text = "英文|中文||韩文|法文|  xx   |";

        String s = Arrays.toString(text.split("\\|"));
        String s1 = Splitter.on("|")// 创建一个以"|"作为切分的Splitter
                .trimResults()// 对切分所有结果，去掉两边空格
                .split(text)// 将字符串text按照"|"切分开来
                .toString();


        //直接生成map key:value
        String text1     = "贝克汉姆:中场;罗纳尔多:前锋;布冯:门将;齐达内:中场";
        String delimiter = ";";

        Map<String, String> map = Splitter.on(delimiter)
                .withKeyValueSeparator(":").split(text1);
    }

    @Test
    public void testStrings() {
        StringBuilder text = new StringBuilder("helloworld");
        char          c    = 'x';
        for (int i = 0; i < 3; i++) {
            text.append(c);
        }
        Assert.assertEquals(text.toString(), "helloworldxxx");
        // 给字符串helloworld后面不断添加'x'字符，直到整个字符串长度达到了13。如果该长度没有helloworld长度大，则直接返回helloworld
        String result = Strings.padEnd("helloworld", 13, c);
        Assert.assertEquals(result, "helloworldxxx");
        // 如果是null，则转成""空字符串；否则返回原始字符串。这是一个推荐使用的函数
        Assert.assertEquals(Strings.nullToEmpty(null), "");
        // 如果字符串是""空字符串，则直接返回null
        Assert.assertNull(Strings.emptyToNull(""));
        // 如果字符串是""空串，或者是null，则返回true
        Assert.assertTrue(Strings.isNullOrEmpty("")
                && Strings.isNullOrEmpty(null));
    }

    @Test
    public void map() {
        ArrayList<String> strings = Lists.newArrayList("getSolution", "asf", "Zxcv");
        //链式 filter->flatmap
        Multiset<Integer> lengths = HashMultiset.create(
                FluentIterable.from(strings)
                        .filter(new Predicate<String>() {
                            public boolean apply(String string) {
                                return CharMatcher.JAVA_UPPER_CASE.matchesAllOf(string);
                            }
                        })
                        .transform(new Function<String, Integer>() {
                            public Integer apply(String string) {
                                return string.length();
                            }
                        }));
        //普通式
        Collection<Boolean> transform = Collections2.transform(strings, new Function<String, Boolean>() {
            @Override
            public Boolean apply(String input) {
                return true;
            }
        });
    }

    /**
     * @return void 返回类型
     * @exception
     * @Title: testPredicate
     * @Description: 不同于Function的apply改变对象，Predicate<T>是用来过滤对象T的
     */
    @Test
    public void testPredicate() {
        Classroom c1 = new Classroom(1, "三年级二班", Sets.newHashSet("jack", "tom",
                "kelly"));
        // 学生数量大于1
        Predicate<Classroom> number = new Predicate<Classroom>() {
            @Override
            public boolean apply(Classroom input) {
                return input.students.size() > 1;
            }
        };
        // 学生id小于3
        Predicate<Classroom> id = new Predicate<Classroom>() {
            @Override
            public boolean apply(Classroom input) {
                return input.id < 3;
            }
        };

        //判断c1是否满足两者条件
        Assert.assertTrue(Predicates.and(number, id).apply(c1));

        // 用新的数据填充map,并注入lookup函数
        Function<String, Classroom> lookup = getFunctionForDefaultMap();

        // 判断三年级二班的学生数量是否大于1
        //执行顺序number.apply(lookup.apply("三年级二班"))
        Assert.assertTrue(Predicates.compose(number, lookup).apply("三年级二班"));

        //判断c1是否在数组内
        Assert.assertTrue(Predicates.in(Lists.newArrayList(c1)).apply(c1));
    }

    @Test
    public void testSupplier() {
        // 使用memorize包装之后的Supplier，以后再从这个里面get()对象的时候，都能保证是同一个对象
        Supplier<Classroom> wrapped = Suppliers
                .memoize(new Supplier<Classroom>() {
                    @Override
                    public Classroom get() {
                        return new Classroom();
                    }
                });
        // 指定保持这个单例对象的时间，一旦过期，就会重新生成新的对象
//        Supplier<Classroom> w = Suppliers
//                .memoizeWithExpiration((new ClassroomSupplier()), 10L,
//                        TimeUnit.MINUTES);
        Classroom c1 = wrapped.get();

        Classroom c2 = wrapped.get();

        Assert.assertEquals(c1, c2);
    }


    @Test
    public void combineLatest() throws Exception {
        //类似rxjava的 CombineLatest
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Integer> future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("convert future 1.");
                return 1;
            }
        });
        ListenableFuture<Integer> future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("convert future 2.");
                //       throw new RuntimeException("----convert future 2.");
                return 2;
            }
        });
        ListenableFuture<List<Integer>> allFutures = Futures.allAsList(future1, future2);
        ListenableFuture<Boolean> transform = Futures.transform(allFutures, new AsyncFunction<List<Integer>,
                Boolean>() {
            @Override
            public ListenableFuture apply(List<Integer> results) throws Exception {
                return Futures.immediateFuture(String.format("success future:%d", results.size()));
            }
        });
        Futures.addCallback(transform, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                System.out.println(result.getClass());
                System.out.printf("success with: %s%n", result);
            }

            @Override
            public void onFailure(Throwable thrown) {
                System.out.printf("onFailure%s%n", thrown.getMessage());
            }
        });

        System.out.println(transform.get());
    }

    private Function<String, Classroom> getFunctionForDefaultMap() {
        Map<String, Classroom> map = Maps.newHashMap();
        map.put("三年级二班",
                new Classroom(1, "三年级二班", Sets.newHashSet("jack", "tom")));
        map.put("四年三班",
                new Classroom(2, "四年三班", Sets.newHashSet("owen", "jerry")));

        return Functions.forMap(map);
    }

    private class Classroom {
        private int id;

        private String name;

        private Set<String> students;

        public Classroom() {
        }

        public Classroom(int id, String name, Set<String> students) {
            super();
            this.id = id;
            this.name = name;
            this.students = students;
        }

    }

}