package com.example.zt_orders;

public class ArchiveFiles {
    public String id_archive;
    public String arch_product;
    public String material_arch;
    public String price_arch;
    public String prepay_archive;
    public String arch_customer;
    public String acc_date_arch;
    public String exec_date_arch;
    public String arch_worker;
    public String comment;

    public ArchiveFiles() {
    }

    public ArchiveFiles(String id_archive,
                        String arch_product,
                        String material_arch,
                        String price_arch,
                        String prepay_archive,
                        String arch_customer,
                        String acc_date_arch,
                        String exec_date_arch,
                        String arch_worker,
                        String comment) {
        this.id_archive = id_archive;
        this.arch_product = arch_product;
        this.material_arch = material_arch;
        this.price_arch = price_arch;
        this.prepay_archive = prepay_archive;
        this.arch_customer = arch_customer;
        this.acc_date_arch = acc_date_arch;
        this.exec_date_arch = exec_date_arch;
        this.arch_worker = arch_worker;
        this.comment = comment;
    }
}
