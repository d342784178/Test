package 重构改善既有代码的设计.unit1;

public class Rental {

    private Movie _movie;
    private int   _daysRented;

    public Rental(Movie movie, int daysRented) {
        _movie = movie;
        _daysRented = daysRented;
    }

    public int getDaysRented() {
        return _daysRented;
    }

    public Movie getMovie() {
        return _movie;
    }

    /**
     * @return 总价
     */
    public double countPrice() {
        return getMovie().countPrice(getDaysRented());
    }

    /**
     * @return 积分
     */
    public int countPoint() {
        return getMovie().countPoint(getDaysRented());
    }

}
