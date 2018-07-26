package 重构改善既有代码的设计.unit1;

public class Test {

    public static void main(String[] args) {
        Customer c1     = new Customer("c1");
        Movie    movie1 = new Movie("movie1", Movie.PriceCode.CHILDRENS);
        Movie    movie2 = new Movie("movie2", Movie.PriceCode.NEW_RELEASE);
        c1.addRental(new Rental(movie1, 2));
        c1.addRental(new Rental(movie2, 3));
        String s = c1.statement();
        System.out.println(s);
    }

}
