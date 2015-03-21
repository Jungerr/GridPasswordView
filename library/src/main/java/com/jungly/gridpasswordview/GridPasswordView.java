package com.jungly.gridpasswordview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jungly.gridpasswordview.imebugfixer.ImeDelBugFixedEditText;

/**
 * ●
 *
 * @author Jungly
 *         mail: jungly.ik@gmail.com
 * @date 15/3/5 21:30
 */
public class GridPasswordView extends LinearLayout {
    private static final int DEFAULT_passwordLength = 6;
    private static final int DEFAULT_TEXTSIZE = 16;
    private static final String DEFAULT_Transformation = "●";

    private ColorStateList textColor;
    private int textSize = DEFAULT_TEXTSIZE;
    private int dividerWidth;
    private Drawable dividerDrawable;
    private int passwordLength;

    //单字符
    private String passwordTransformation;
    private int passwordType;

    private ImeDelBugFixedEditText inputView;

    private String[] passwordArr;
    private TextView[] viewArr;

    private OnPasswordChangedListener listener;

    private PasswordTransformationMethod transformationMethod;

    public GridPasswordView(Context context) {
        this(context, null);
    }

    public GridPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, com.jungly.gridpasswordview.R.styleable.gridPasswordView, defStyleAttr, 0);

        textColor = ta.getColorStateList(com.jungly.gridpasswordview.R.styleable.gridPasswordView_textColor);
        if (textColor == null)
            textColor = ColorStateList.valueOf(getResources().getColor(android.R.color.primary_text_light));

        int textSize = ta.getDimensionPixelSize(com.jungly.gridpasswordview.R.styleable.gridPasswordView_textSize, 0);
        if (textSize != 0) {
            this.textSize = Util.px2sp(context, textSize);
        }

        dividerWidth = (int) ta.getDimension(com.jungly.gridpasswordview.R.styleable.gridPasswordView_dividerWidth, 1);

        dividerDrawable = ta.getDrawable(com.jungly.gridpasswordview.R.styleable.gridPasswordView_dividerColor);
        if (dividerDrawable == null)
            dividerDrawable = new ColorDrawable(0x88888888);

        passwordLength = ta.getInt(com.jungly.gridpasswordview.R.styleable.gridPasswordView_passwordLength, DEFAULT_passwordLength);
        passwordTransformation = ta.getString(com.jungly.gridpasswordview.R.styleable.gridPasswordView_passwordTransformation);
        if (TextUtils.isEmpty(passwordTransformation))
            passwordTransformation = DEFAULT_Transformation;

        passwordType = ta.getInt(com.jungly.gridpasswordview.R.styleable.gridPasswordView_passwordType, 0);

        ta.recycle();

        Drawable bg = getBackground();
        if (bg == null)
            setBackgroundResource(com.jungly.gridpasswordview.R.drawable.gridpassword_bg_default);

        setShowDividers(SHOW_DIVIDER_NONE);
        setOrientation(HORIZONTAL);

        passwordArr = new String[passwordLength];
        viewArr = new TextView[passwordLength];
    }

    private void initViews(Context context) {
        transformationMethod = new CustomPasswordTransformationMethod(passwordTransformation);
        inflaterViews(context);
    }

    private void inflaterViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(com.jungly.gridpasswordview.R.layout.gridpasswordview, this);
        inputView = (ImeDelBugFixedEditText) findViewById(com.jungly.gridpasswordview.R.id.inputView);

        inputView.setMaxLines(passwordLength);
        inputView.addTextChangedListener(textWatcher);
        inputView.setDelKeyEventListener(onDelKeyEventListener);
        setCustomAttr(inputView);

        viewArr[0] = inputView;

        int index = 1;
        while (index < passwordLength) {
            View dividerView = inflater.inflate(com.jungly.gridpasswordview.R.layout.divider, null);
            LayoutParams dividerParams = new LinearLayout.LayoutParams(dividerWidth, LayoutParams.MATCH_PARENT);
            dividerView.setBackgroundDrawable(dividerDrawable);
            addView(dividerView, dividerParams);

            TextView textView = (TextView) inflater.inflate(com.jungly.gridpasswordview.R.layout.textview, null);
            setCustomAttr(textView);
            LayoutParams textViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            addView(textView, textViewParams);

            viewArr[index] = textView;
            index++;
        }

        setOnClickListener(onClickListener);
    }

    private void setCustomAttr(TextView view) {
        view.setTextColor(textColor);
        view.setTextSize(textSize);

        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {

            case 1:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case 2:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case 3:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }
        view.setInputType(inputType);
        view.setTransformationMethod(transformationMethod);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            forceInputViewGetFouce();
        }
    };

    private void forceInputViewGetFouce() {
        inputView.setFocusable(true);
        inputView.setFocusableInTouchMode(true);
        inputView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputView, InputMethodManager.SHOW_IMPLICIT);
    }

    private ImeDelBugFixedEditText.OnDelKeyEventListener onDelKeyEventListener = new ImeDelBugFixedEditText.OnDelKeyEventListener() {

        @Override
        public void onDeleteClick() {
            for (int i = passwordArr.length - 1; i >= 0; i--) {
                if (passwordArr[i] != null) {
                    passwordArr[i] = null;
                    viewArr[i].setText(null);
                    notifyTextChanged();
                    break;
                } else {
                    viewArr[i].setText(null);
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null) {
                return;
            }

            String newStr = s.toString();
            if (newStr.length() == 1) {
                passwordArr[0] = newStr;
                notifyTextChanged();
            } else if (newStr.length() == 2) {
                String newNum = newStr.substring(1);
                for (int i = 0; i < passwordArr.length; i++) {
                    if (passwordArr[i] == null) {
                        passwordArr[i] = newNum;
                        viewArr[i].setText(newNum);
                        notifyTextChanged();
                        break;
                    }
                }
                inputView.removeTextChangedListener(this);
                inputView.setText(passwordArr[0]);
                inputView.setSelection(1);
                inputView.addTextChangedListener(this);
            }
        }
    };

    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                onDelKeyEventListener.onDeleteClick();
                return true;
            }
            return false;
        }
    };

    private void notifyTextChanged() {
        if (listener != null) {
            String currentPsw = getPassWord();
            listener.onChanged(currentPsw);

            if (currentPsw.length() == passwordLength)
                listener.onMaxLength(currentPsw);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putStringArray("passwordArr", passwordArr);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            passwordArr = bundle.getStringArray("passwordArr");
            state = bundle.getParcelable("instanceState");
            inputView.removeTextChangedListener(textWatcher);
            setPassword(getPassWord());
            inputView.addTextChangedListener(textWatcher);
        }
        super.onRestoreInstanceState(state);
    }

    public void setError(String error) {
        inputView.setError(error);
    }

    /**
     * Return the text the PasswordView is displaying.
     */
    public String getPassWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordArr.length; i++) {
            if (passwordArr[i] != null)
                sb.append(passwordArr[i]);
        }
        return sb.toString();
    }

    /**
     * Clear the passwrod the PasswordView is displaying.
     */
    public void clearPassword() {
        for (int i = 0; i < passwordArr.length; i++) {
            passwordArr[i] = null;
            viewArr[i].setText(null);
        }
    }

    /**
     * Sets the string value of the PasswordView.
     */
    public void setPassword(String password) {
        clearPassword();

        if (TextUtils.isEmpty(password))
            return;

        char[] pswArr = password.toCharArray();
        for (int i = 0; i < pswArr.length; i++) {
            if (i < passwordArr.length) {
                passwordArr[i] = pswArr[i] + "";
                viewArr[i].setText(passwordArr[i]);
            }
        }
    }

    /**
     * Set the enabled state of this view.
     */
    public void setPasswordVisibility(boolean visible) {
        for (TextView textView : viewArr){
            textView.setTransformationMethod(visible ? null : transformationMethod);
            if(textView instanceof EditText){
                EditText et = (EditText) textView;
                et.setSelection(et.getText().length());
            }
        }
            
    }

    /**
     * Toggle the enabled state of this view.
     */
    public void togglePasswordVisibility() {
        boolean currentVisible = viewArr[0].getTransformationMethod() == null;
        setPasswordVisibility(!currentVisible);
    }

    /**
     * Register a callback to be invoked when password changed.
     */
    public void setOnPasswordChangedListener(OnPasswordChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the password changed or is at the maximum length.
     */
    public interface OnPasswordChangedListener {

        /**
         * Invoked when the password changed.
         */
        public void onChanged(String psw);

        /**
         * Invoked when the password is at the maximum length.
         */
        public void onMaxLength(String psw);

    }
}
