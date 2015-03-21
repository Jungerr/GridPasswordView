package com.jungly.gridpasswordview.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.pswtype_sp)
    Spinner pswtypeSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.app_name);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
