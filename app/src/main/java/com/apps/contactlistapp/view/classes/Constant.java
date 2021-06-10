package com.apps.contactlistapp.view.classes;

import com.apps.contactlistapp.model.ContactModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable {


    public static final String TABLE_CONTACT = "tbl_contact";

    public static final String NO_ID = "no_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String CONTACT_EMAIL = "contact_mail";
    public static final String CONTACT_IMAGE = "contact_img";

    public static ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();

}
