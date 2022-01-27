package com.example.zt_orders;

public class Orders {
    public String id_order;
    public String name_product;
    public String material_product;
    public String price;
    public String prepay;
    public String fio_customer;
    public String accepted_date;
    public String executed_date;
    public String fio_worker;
    public String readiness;
    public String comment;

    public Orders() {
    }

    public Orders(String id_order,
                  String name_product,
                  String material_product,
                  String price,
                  String prepay,
                  String fio_customer,
                  String accepted_date,
                  String executed_date,
                  String fio_worker,
                  String readiness,
                  String comment) {
        this.id_order = id_order;
        this.name_product = name_product;
        this.material_product = material_product;
        this.price = price;
        this.prepay = prepay;
        this.fio_customer = fio_customer;
        this.accepted_date = accepted_date;
        this.executed_date = executed_date;
        this.fio_worker = fio_worker;
        this.readiness = readiness;
        this.comment = comment;
    }
}
