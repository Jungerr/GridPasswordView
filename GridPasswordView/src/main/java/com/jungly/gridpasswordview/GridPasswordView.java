package com.jungly.gridpasswordview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.EditText;
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
public class GridPasswordView extends LinearLayout implements PasswordView {
    private static final int DEFAULT_PASSWORDLENGTH = 6;
    private static final int DEFAULT_TEXTSIZE = 16;
    private static final String DEFAULT_TRANSFORMATION = "●";
    private static final int DEFAULT_LINECOLOR = 0xaa888888;
    private static final int DEFAULT_GRIDCOLOR = 0xffffffff;

    private ColorStateList textColor;
    private int textSize = DEFAULT_TEXTSIZE;

    private int lineWidth;
    private int lineColor;
    private int gridColor;
    private Drawable lineDrawable;
    private Drawable outerLineDrawable;

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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.gridPasswordView, defStyleAttr, 0);

        textColor = ta.getColorStateList(R.styleable.gridPasswordView_textColor);
        if (textColor == null)
            textColor = ColorStateList.valueOf(getResources().getColor(android.R.color.primary_text_light));
        int textSize = ta.getDimensionPixelSize(R.styleable.gridPasswordView_textSize, -1);
        if (textSize != -1) {
            this.textSize = Util.px2sp(context, textSize);
        }

        lineWidth = (int) ta.getDimension(R.styleable.gridPasswordView_lineWidth, Util.dp2px(getContext(), 1));
        lineColor = ta.getColor(R.styleable.gridPasswordView_lineColor, DEFAULT_LINECOLOR);
        gridColor = ta.getColor(R.styleable.gridPasswordView_gridColor, DEFAULT_GRIDCOLOR);
        lineDrawable = ta.getDrawable(R.styleable.gridPasswordView_lineColor);
        if (lineDrawable == null)
            lineDrawable = new ColorDrawable(lineColor);
        outerLineDrawable = generateBackgroundDrawable();

        passwordLength = ta.getInt(R.styleable.gridPasswordView_passwordLength, DEFAULT_PASSWORDLENGTH);
        passwordTransformation = ta.getString(R.styleable.gridPasswordView_passwordTransformation);
        if (TextUtils.isEmpty(passwordTransformation))
            passwordTransformation = DEFAULT_TRANSFORMATION;


        passwordType = ta.getInt(R.styleable.gridPasswordView_passwordType, 0);

        ta.recycle();

        passwordArr = new String[passwordLength];
        viewArr = new TextView[passwordLength];
    }

    private void initViews(Context context) {
        super.setBackgroundDrawable(outerLineDrawable);
        setShowDividers(SHOW_DIVIDER_NONE);
        setOrientation(HORIZONTAL);

        transformationMethod = new CustomPasswordTransformationMethod(passwordTransformation);
        inflaterViews(context);
    }

    private void inflaterViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.gridpasswordview, this);
        inputView = (ImeDelBugFixedEditText) findViewById(R.id.inputView);

        inputView.setMaxEms(passwordLength);
        inputView.addTextChangedListener(textWatcher);
        inputView.setDelKeyEventListener(onDelKeyEventListener);
        setCustomAttr(inputView);

        viewArr[0] = inputView;

        int index = 1;
        while (index < passwordLength) {
            View dividerView = inflater.inflate(R.layout.divider, null);
            LayoutParams dividerParams = new LayoutParams(lineWidth, LayoutParams.MATCH_PARENT);
            dividerView.setBackgroundDrawable(lineDrawable);
            addView(dividerView, dividerParams);

            TextView textView = (TextView) inflater.inflate(R.layout.textview, null);
            setCustomAttr(textView);
            LayoutParams textViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            addView(textView, textViewParams);

            viewArr[index] = textView;
            index++;
        }

        setOnClickListener(onClickListener);
    }

    private void setCustomAttr(TextView view) {
        if (textColor != null)
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
            forceInputViewGetFocus();
        }
    };

    private GradientDrawable generateBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(gridColor);
        drawable.setStroke(lineWidth, lineColor);
        return drawable;
    }

    private void forceInputViewGetFocus() {
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

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Deprecated
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
        if (listener == null)
            return;

        String currentPsw = getPassWord();
        listener.onChanged(currentPsw);

        if (currentPsw.length() == passwordLength)
            listener.onMaxLength(currentPsw);

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

    //TODO
    //@Override
    private void setError(String error) {
        inputView.setError(error);
    }

    /**
     * Return the text the PasswordView is displaying.
     */
    @Override
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
    @Override
    public void clearPassword() {
        for (int i = 0; i < passwordArr.length; i++) {
            passwordArr[i] = null;
            viewArr[i].setText(null);
        }
    }

    /**
     * Sets the string value of the PasswordView.
     */
    @Override
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
    @Override
    public void setPasswordVisibility(boolean visible) {
        for (TextView textView : viewArr) {
            textView.setTransformationMethod(visible ? null : transformationMethod);
            if (textView instanceof EditText) {
                EditText et = (EditText) textView;
                et.setSelection(et.getText().length());
            }
        }
    }

    /**
     * Toggle the enabled state of this view.
     */
    @Override
    public void togglePasswordVisibility() {
        boolean currentVisible = getPassWordVisibility();
        setPasswordVisibility(!currentVisible);
    }

    /**
     * Get the visibility of this view.
     */
    private boolean getPassWordVisibility() {
        return viewArr[0].getTransformationMethod() == null;
    }

    /**
     * Register a callback to be invoked when password changed.
     */
    @Override
    public void setOnPasswordChangedListener(OnPasswordChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPasswordType(PasswordType passwordType) {
        boolean visible = getPassWordVisibility();
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {

            case TEXT:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case TEXTVISIBLE:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case TEXTWEB:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }

        for (TextView textView : viewArr)
            textView.setInputType(inputType);

        setPasswordVisibility(visible);
    }

    @Override
    public void setBackground(Drawable background) {
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    @Override
    public void setBackgroundResource(int resid) {
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
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
