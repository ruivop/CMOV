package org.feup.cmov.customerapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends Activity {

    public enum CreditCardType {
        Visa, AmericanExpress, MasterCard
    }

    private String costumerName;
    private long NIF;
    private CreditCardType type;
    private long creditCardNumber;
    private Date creditCardValidity;

    private EditText editName;
    private EditText editNIF;
    private RadioGroup editType;
    private EditText editCreditCardNumber;
    private EditText editcreditCardValidity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final Context context = this;

        Button register_btn = findViewById(R.id.register_button);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity) context).updateFields();
            }
        });

        editName = findViewById(R.id.edit_name);
        editNIF = findViewById(R.id.edit_NIF);
        editType = findViewById(R.id.credit_card_type);
        editCreditCardNumber = findViewById(R.id.credit_card_edit_number);
        editcreditCardValidity = findViewById(R.id.credit_card_edit_validity);
    }

    public void updateFields(){
        costumerName = editName.getText().toString();
        NIF = Long.parseLong(editNIF.getText().toString());
        type = CreditCardType.valueOf(getResources().getResourceEntryName(editType.getCheckedRadioButtonId()));
        creditCardNumber = Long.parseLong(editCreditCardNumber.getText().toString());

        try {
            DateFormat format = new SimpleDateFormat("dd-mm-yy");
            creditCardValidity = format.parse(editcreditCardValidity.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
