package 重构改善既有代码的设计.unit1;

import java.util.function.Function;

public class Movie {

    public enum PriceCode {
        CHILDRENS(2, dayRented -> {
            double thisAmount = 0;
            thisAmount += 2;
            if (dayRented > 2) {
                thisAmount += (dayRented - 2) * 1.5;
            }
            return thisAmount;
        }, dayRented -> 1),
        REGULAR(0, dayRented -> {
            double thisAmount = 0;
            thisAmount += dayRented * 3;
            return thisAmount;
        }, dayRented -> 1),
        NEW_RELEASE(1, dayRented -> {
            double thisAmount = 0;
            thisAmount += 1.5;
            if (dayRented > 3) {
                thisAmount += (dayRented - 3) * 1.5;
            }
            return thisAmount;
        }, dayRented -> {
            int point = 1;
            if (dayRented > 1) {
                point += 1;
            }
            return point;
        }),;
        int                        code;
        Function<Integer, Double>  countPrince;
        Function<Integer, Integer> countPoint;

        PriceCode(int code, Function<Integer, Double> countPrince, Function<Integer, Integer> countPoint) {
            this.code = code;
            this.countPrince = countPrince;
            this.countPoint = countPoint;
        }
    }

    private String    _title;
    private PriceCode _priceCode;

    public Movie(String title, PriceCode priceCode) {
        _title = title;
        _priceCode = priceCode;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public PriceCode get_priceCode() {
        return _priceCode;
    }

    public void set_priceCode(PriceCode _priceCode) {
        this._priceCode = _priceCode;
    }

    public double countPrice(int day) {
        return _priceCode.countPrince.apply(day);
    }

    public int countPoint(int day) {
        return _priceCode.countPoint.apply(day);

    }

}
