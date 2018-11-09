package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.RSA;
import org.feup.cmov.customerapp.utils.HttpUtils;
import org.json.JSONArray;

import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;


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

        try {
            generateKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void generateKeys() throws Exception {

        Map<String, Object> keyMap = RSA.initKey();
        String publicKey = RSA.getPublicKey(keyMap);
        String privateKey = RSA.getPrivateKey(keyMap);

        String NIFString = String.valueOf(NIF);
        String TypeString = type.toString();
        String CCNString = String.valueOf(creditCardNumber);
        String CCVString = creditCardValidity.toString();

        String byteString = costumerName + "//" + NIFString + "//" + TypeString + "//" + CCNString + "//" + CCVString;

        byte[] Data = byteString.getBytes();

        try {
            byte[] encodedData = RSA.encryptByPublicKey(Data,publicKey);
            RequestParams rp = new RequestParams();
            rp.put("userdata",encodedData);
            rp.add("publicKey", publicKey);

            HttpUtils.postByUrl("https://localhost:3000/users",rp,new JsonHttpResponseHandler() {


                public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
