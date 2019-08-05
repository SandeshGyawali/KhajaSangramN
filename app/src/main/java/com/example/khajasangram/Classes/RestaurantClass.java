package com.example.khajasangram.Classes;

public class RestaurantClass {
    private String r_id;
    private String r_name;
    private String r_address;
    private String r_contact;

    public RestaurantClass(String r_id, String r_name, String r_address, String r_contact) {
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_address = r_address;
        this.r_contact = r_contact;
    }

    public String getR_id() {
        return r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public String getR_address() {
        return r_address;
    }

    public String getR_contact() {
        return r_contact;
    }
}





