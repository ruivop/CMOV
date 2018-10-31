package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.feup.cmov.customerapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

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
                Intent intent = new Intent(context, PerformancesActivity.class);
                startActivity(intent);
            }
        });

        editName = findViewById(R.id.edit_name);
        editNIF = findViewById(R.id.edit_NIF);
        editType = findViewById(R.id.credit_card_type);
        editCreditCardNumber = findViewById(R.id.credit_card_edit_number);
        editcreditCardValidity = findViewById(R.id.credit_card_edit_validity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
    }

    public void updateFields() {
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

    public void generateKeys(){

    }


}
