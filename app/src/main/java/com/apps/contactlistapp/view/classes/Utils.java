package com.apps.contactlistapp.view.classes;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Utils {

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static boolean validatePhno(Context context, EditText textInputEditText) {
        String regexp = "^[6789]\\d{9}$";
        String phno = textInputEditText.getText() + "";
        if (phno.isEmpty()) {
            //textInputEditText.setError("Please Enter Mobile Number");
            //textInputEditText.requestFocus();
            Utils.showToastMessage(context, "Please enter mobile number");

            return false;
        } else if (phno.length() > 10 || !phno.matches(regexp)) {
            //0 tex
            // tInputEditText.setError(" Please Enter valid Mobile number");
            Utils.showToastMessage(context, "Please enter valid mobile number");
            return false;
        }

        return true;
    }
}
