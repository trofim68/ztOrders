package com.example.zt_orders;

public class BankClass {

    public int bankCalcPlus(int bank, int plus) {
        return bank + plus;
    }
    public int bankCancelPlus(int bank, int plus) {
        return bank - plus;
    }
    public int plusesCount(int plus, int pluses) {
        return plus + pluses;
    }
    public int cancelPlus(int plus, int pluses) {
        return plus - pluses;
    }

    public int bankCalcMinus(int bank, int minus) {
        return bank - minus;
    }
    public int minusesCount(int minus, int minuses) {
        return minus + minuses;
    }
}
