package com.sobralapps.android.shop_bazarsmg.TextWatchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

public class MoneyTextWatcher implements TextWatcher {

    private String current = "";
    private EditText editText;

    public MoneyTextWatcher(EditText editText) {
        this.editText = editText;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(!s.toString().equals(current)){
            editText.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("[R$,.]", "");

            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

            current = formatted;
            editText.setText(formatted);
            editText.setSelection(formatted.length());

            editText.addTextChangedListener(this);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
