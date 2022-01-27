package com.example.zt_orders;

public class HistoryExpenses{
    String expense_date;
    String expense_name;
    String expense_summ;
    String expense_fio;

    public HistoryExpenses() {
    }

    public HistoryExpenses(String expense_date, String expense_name, String expense_summ, String expense_fio) {
        this.expense_date = expense_date;
        this.expense_name = expense_name;
        this.expense_summ = expense_summ;
        this.expense_fio = expense_fio;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public String getExpense_name() {
        return expense_name;
    }

    public String getExpense_summ() {
        return expense_summ;
    }

    public String getExpense_fio() {
        return expense_fio;
    }
}
