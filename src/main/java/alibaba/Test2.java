package alibaba;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

/**
 * Desc:
 * +题目二：网站分析用户行为
 * +假设有一个A站点，每次登陆的时候都会记录用户的登陆信息，对象类型是User，有3个字段：+
 * +userId,+userName,+loginTime
 * +需求是统计最近10天登陆最频繁的10个用户，排序并打印出这10个用户各登陆多少次
 * Author: DLJ
 * Date: 2017-08-10
 * Time: 20:02
 */
public class Test2 {

    public static void main(String args[]) {
        List<User> loginUsers = Lists.newArrayList();
        Date       current    = new Date();
        Date       target     = DateUtils.addDays(current, -10);
        List<User> filterUsers = Lists.newArrayList(Iterators.filter(loginUsers.iterator(), new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                Date loginTime = input.getLoginTime();
                return loginTime.after(target);
            }
        }));

        TreeMap<String, AnalysisBean> map = Maps.newTreeMap();
        for (User filterUser : filterUsers) {
            String userId = filterUser.getUserId();
            if (map.containsKey(userId)) {
                map.put(userId, new AnalysisBean(userId,1,filterUser.getUserName()));
            } else {
                AnalysisBean analysisBean = map.get(userId);
                map.put(userId, analysisBean.setCount(analysisBean.getCount() + 1));
            }
        }
        ArrayList<AnalysisBean> beans = Lists.newArrayList();
        for (Map.Entry<String, AnalysisBean> entry : map.entrySet()) {
            String       key   = entry.getKey();
            AnalysisBean value = entry.getValue();
            beans.add(value);
        }
        Collections.sort(beans, new Comparator<AnalysisBean>() {
            @Override
            public int compare(AnalysisBean o1, AnalysisBean o2) {
                if (o1.getCount() > o2.getCount()) {
                    return 1;
                } else if (o1.getCount() < o2.getCount()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        System.out.println(beans);

    }

    private static class AnalysisBean {
        private String  userId;
        private Integer count;
        private String  userName;


        public AnalysisBean(String userId, Integer count, String userName) {
            this.userId = userId;
            this.count = count;
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "AnalysisBean{" +
                    "userId='" + userId + '\'' +
                    ", count=" + count +
                    ", userName='" + userName + '\'' +
                    '}';
        }

        public String getUserId() {
            return userId;
        }

        public AnalysisBean setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Integer getCount() {
            return count;
        }

        public AnalysisBean setCount(Integer count) {
            this.count = count;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public AnalysisBean setUserName(String userName) {
            this.userName = userName;
            return this;
        }
    }

    private static class User {
        private String userId;
        private String userName;
        private Date   loginTime;

        public String getUserId() {
            return userId;
        }

        public User setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public User setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Date getLoginTime() {
            return loginTime;
        }

        public User setLoginTime(Date loginTime) {
            this.loginTime = loginTime;
            return this;
        }
    }
}
