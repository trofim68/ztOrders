package com.example.zt_orders;

public class HistoryProfit {
    String history_id;
    String history_date;
    String history_product;
    String history_price;

    public HistoryProfit() {
    }

    public HistoryProfit(String history_id,
                         String history_date,
                         String history_product,
                         String history_price) {
        this.history_id = history_id;
        this.history_date = history_date;
        this.history_product = history_product;
        this.history_price = history_price;
    }

    public String getHistory_id() { return history_id; }

    public String getHistory_date() {
        return history_date;
    }

    public String getHistory_product() {
        return history_product;
    }

    public String getHistory_price() {
        return history_price;
    }
}
