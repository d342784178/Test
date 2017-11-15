package jdk1_8;

import com.google.common.collect.Lists;
import rx.Observable;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-07
 * Time: 15:38
 */
public class FunctionDemo {

    public static void main(String args[]) {
        ArrayList<String> t = Lists.newArrayList("1", "2", "3", "4");

        //jdk8 流
        Optional<Integer> reduce = t.stream().map(Integer::valueOf).reduce((integer, integer2) ->
                integer + integer2);
        reduce.ifPresent(System.out::println);

        t.forEach(FunctionDemo::syso);

        //响应式编程
        //rxjava
        Observable.from(t).map(Integer::valueOf).reduce((integer, integer2) -> integer + integer2)
                .subscribe(System.out::println);
        Observable.from(t).subscribe(FunctionDemo::syso);

        Observable.from(t).collect(ArrayList::new, ArrayList::add).subscribe(System.out::println);
    }

    //函数式编程
    public static void syso(String s) {
        System.out.println(s);
    }
}
