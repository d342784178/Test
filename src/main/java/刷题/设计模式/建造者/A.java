package 刷题.设计模式.建造者;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 11:14
 */

public class A {
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;

    private A(String a, String b, String c, String d, String e, String f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        start();
    }

    private void start() {
        System.out.println("启动了");
    }

    public static class Builder {
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;
        private String f;

        public A build() {
            return new A(a, b, c, d, e, f);
        }

        public String getA() {
            return a;
        }

        public Builder setA(String a) {
            this.a = a;
            return this;
        }

        public String getB() {
            return b;
        }

        public Builder setB(String b) {
            this.b = b;
            return this;
        }

        public String getC() {
            return c;
        }

        public Builder setC(String c) {
            this.c = c;
            return this;
        }

        public String getD() {
            return d;
        }

        public Builder setD(String d) {
            this.d = d;
            return this;
        }

        public String getE() {
            return e;
        }

        public Builder setE(String e) {
            this.e = e;
            return this;
        }

        public String getF() {
            return f;
        }

        public Builder setF(String f) {
            this.f = f;
            return this;
        }
    }
}
