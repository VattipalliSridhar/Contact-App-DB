package com.apps.contactlistapp.view.classes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;
    NetworkInfo activeNetworkInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public boolean isNetworkAvailable() {
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void snakeBarView(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public  void setPreferLogin(String TAG, String value) {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(SharePreferenceConstant.LOGIN_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TAG, value);
        editor.apply();
    }

    public String getPreferLogin(String TAG) {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(SharePreferenceConstant.LOGIN_PREFERENCES, MODE_PRIVATE);
        String value = preferences.getString(TAG, "");
        return value;
    }

    public void setPreference(String TAG, String value) {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(SharePreferenceConstant.MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TAG, value);
        editor.apply();

    }

    public String getPreference(String TAG) {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(SharePreferenceConstant.MY_PREFERENCES, MODE_PRIVATE);
        String value = preferences.getString(TAG, "");
        return value;
    }

    public  void clearAllPreference() {
        SharedPreferences.Editor editor = this.getApplicationContext().getSharedPreferences(SharePreferenceConstant.MY_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public  void clearLoginPreference() {

        SharedPreferences.Editor editor = this.getApplicationContext().getSharedPreferences(SharePreferenceConstant.LOGIN_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();


    }
    public static void setSpinnerValueFromMap(Context context, Spinner spinner, String[] values_array, String value) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, values_array);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(Integer.parseInt(value),false);

    }
}
