package com.apps.contactlistapp.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.apps.contactlistapp.R;
import com.apps.contactlistapp.databinding.ActivityLoginBinding;
import com.apps.contactlistapp.databinding.ActivityMainBinding;
import com.apps.contactlistapp.view.classes.BaseActivity;
import com.apps.contactlistapp.view.classes.SharePreferenceConstant;
import com.apps.contactlistapp.view.classes.Utils;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements View.OnKeyListener {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if(getPreference( SharePreferenceConstant.LOGIN_SUCCESS).equals("1"))
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        binding.etOtpOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1)
                    binding.etOtpTwo.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etOtpOne.setOnKeyListener(this);

        binding.etOtpTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1)
                    binding.etOtpThree.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etOtpTwo.setOnKeyListener(this);
        binding.etOtpThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1)
                    binding.etOtpFour.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etOtpThree.setOnKeyListener(this);
        binding.etOtpFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(validation1()) {
                    if (s.length() == 1){

                        String otp1 =binding.etOtpOne.getText().toString().trim();
                        String otp2 = binding.etOtpTwo.getText().toString().trim();
                        String otp3 = binding.etOtpThree.getText().toString().trim();
                        String otp4 = binding.etOtpFour.getText().toString().trim();
                        String otp = otp1 + otp2 + otp3 + otp4;

                        if(otp.length()==4)
                        {
                            setPreference(SharePreferenceConstant.LOGIN_SUCCESS, "1");
                            setPreference(SharePreferenceConstant.USER_PIN, ""+s);
                            setPreference(SharePreferenceConstant.USER_NUMER, ""+binding.etMobileNo.getText().toString().trim());
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }



                    }else{

                    }
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etOtpFour.setOnKeyListener(this);
    }

    private boolean validation1() {
        if (!Utils.validatePhno(getApplicationContext(), binding.etMobileNo)) {
            return false;
        }

        return true;
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();

            switch (id) {
                case R.id.et_otp_one:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        //et_otp_one.requestFocus();
                        binding.etOtpOne.setText("");
                        return true;
                    }
                    break;
                case R.id.et_otp_two:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.etOtpOne.requestFocus();
                        binding.etOtpTwo.setText("");
                        return true;
                    }
                    break;

                case R.id.et_otp_three:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.etOtpTwo.requestFocus();
                        binding.etOtpThree.setText("");
                        return true;
                    }
                    break;

                case R.id.et_otp_four:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        binding.etOtpThree.requestFocus();
                        binding.etOtpFour.setText("");
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}