package br.com.empessoa8.carcare.mask;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by elias on 05/07/2017.
 */

public abstract class MonetaryMask implements TextWatcher {
    private boolean isUpdating;
    private EditText mEditText;
    private NumberFormat mNF = NumberFormat.getCurrencyInstance();

    public MonetaryMask(EditText mEditText) {
        super();
        this.mEditText = mEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence cs, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {

        if (isUpdating) {
            isUpdating = false;
            return;
        }
        isUpdating = true;
        String str = cs.toString();
        boolean hasMask = (str.indexOf("R$") >= 0 && str.indexOf(".") >= 0 && str.indexOf(",") >= 0) ||
                (str.indexOf("R$") >= 0 && str.indexOf(",") >= 0);

        if (hasMask) {
            str = str.replaceAll("[R$]", "").replaceAll("[.]", "").replaceAll("[,]", "");
        }

        try {
            double value = (Double.parseDouble(str) / 100);
            str = mNF.format(value);
            mEditText.setText(str);
            mEditText.setSelection(str.length());
        } catch (Exception e) {
            e.printStackTrace();
            cs = "";
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}