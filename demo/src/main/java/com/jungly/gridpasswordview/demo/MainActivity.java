package com.jungly.gridpasswordview.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Spinner;

import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.PasswordType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.gpv_normal)
    GridPasswordView gpvNormal;
    @InjectView(R.id.gpv_length)
    GridPasswordView gpvLength;
    @InjectView(R.id.gpv_transformation)
    GridPasswordView gpvTransformation;
    @InjectView(R.id.gpv_passwordType)
    GridPasswordView gpvPasswordType;
    @InjectView(R.id.gpv_customUi)
    GridPasswordView gpvCustomUi;
    @InjectView(R.id.gpv_normail_twice)
    GridPasswordView gpvNormalTwice;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.pswtype_sp)
    Spinner pswtypeSp;

    boolean isFirst = true;
    String firstPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        onPwdChangedTest();
    }

    @OnCheckedChanged(R.id.psw_visibility_switcher)
    void onCheckedChanged(boolean isChecked) {
        gpvPasswordType.togglePasswordVisibility();
    }

    @OnItemSelected(R.id.pswtype_sp)
    void onTypeSelected(int position) {
        switch (position) {
            case 0:
                gpvPasswordType.setPasswordType(PasswordType.NUMBER);
                break;

            case 1:
                gpvPasswordType.setPasswordType(PasswordType.TEXT);
                break;

            case 2:
                gpvPasswordType.setPasswordType(PasswordType.TEXTVISIBLE);
                break;

            case 3:
                gpvPasswordType.setPasswordType(PasswordType.TEXTWEB);
                break;
        }

    }

    // Test GridPasswordView.clearPassword() in OnPasswordChangedListener.
    // Need enter the password twice and then check the password , like Alipay
    void onPwdChangedTest(){
        gpvNormalTwice.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6 && isFirst){
                    gpvNormalTwice.clearPassword();
                    isFirst = false;
                    firstPwd = psw;
                }else if (psw.length() == 6 && !isFirst){
                    if (psw.equals(firstPwd)){
                        Log.d("MainActivity", "The password is: " + psw);
                    }else {
                        Log.d("MainActivity", "password doesn't match the previous one, try again!");
                        gpvNormalTwice.clearPassword();
                        isFirst = true;
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) { }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
