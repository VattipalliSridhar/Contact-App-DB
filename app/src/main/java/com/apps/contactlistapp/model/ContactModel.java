package com.apps.contactlistapp.model;

public class ContactModel {

    private String c_id;
    private String c_name;
    private String c_number;
    private String c_email;
    private String c_image;

    public ContactModel(String c_id, String c_name, String c_number, String c_email, String c_image) {
        this.c_id = c_id;
        this.c_name = c_name;
        this.c_number = c_number;
        this.c_email = c_email;
        this.c_image = c_image;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_image() {
        return c_image;
    }

    public void setC_image(String c_image) {
        this.c_image = c_image;
    }

    public String getC_number() {
        return c_number;
    }

    public void setC_number(String c_number) {
        this.c_number = c_number;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }
}
