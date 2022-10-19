class Number {
    int numerator;
    int denominator;

    public Number() {
    }

    public Number(int numerator) {
        this.numerator = numerator;
        this.denominator = 1;
    }

    public Number(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        if (this.denominator < 0) {
            this.numerator *= -1;
            this.denominator *= -1;
        }
    }

    @Override
    public String toString() {
        if (this.denominator == 1) {
            return numerator + "";
        } else return numerator + "/" + denominator;
    }
}
//运算符类