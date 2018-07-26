package 重构改善既有代码的设计.unit1;

public class Test {

    public static void main(String[] args) {
        Customer c1 = new Customer("c1");
        String   s  = c1.statement();
        System.out.println(s);
    }

}
