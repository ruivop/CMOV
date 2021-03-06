package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.RSA;
import org.feup.cmov.customerapp.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;


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

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        context = this;

        SharedPreferences sp1 = this.getSharedPreferences("Register", MODE_PRIVATE);
        String testRegister = sp1.getString("PublicKey",null);
       /* if(testRegister != null){
            Intent intent = new Intent(context, PerformancesActivity.class);
            startActivity(intent);
        }*/


        Button register_btn = findViewById(R.id.register_button);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((RegisterActivity) context).updateFields())
                    return;
                if(!HttpUtils.isNetworkAvailable(context)) {
                    Toast.makeText(context, "Must have internet connection to register", Toast.LENGTH_LONG).show();
                    return;
                }
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

    public boolean updateFields() {
        try {
        costumerName = editName.getText().toString();
        NIF = Long.parseLong(editNIF.getText().toString());
        type = CreditCardType.valueOf(getResources().getResourceEntryName(editType.getCheckedRadioButtonId()));
        creditCardNumber = Long.parseLong(editCreditCardNumber.getText().toString());


            DateFormat format = new SimpleDateFormat("dd-mm-yy");
            creditCardValidity = format.parse(editcreditCardValidity.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Error Creating. Try modifying the fields", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }

        try {
            generateKeys();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void generateKeys() throws Exception {

        Map<String, Object> keyMap = RSA.initKey();
        final String publicKey = RSA.getPublicKey(keyMap);
        final String privateKey = RSA.getPrivateKey(keyMap);

        final String NIFString = String.valueOf(NIF);
        final String TypeString = type.toString();
        final String CCNString = String.valueOf(creditCardNumber);
        final String CCVString = creditCardValidity.toString();

        String byteString = costumerName + "//" + NIFString + "//" + TypeString + "//" + CCNString + "//" + CCVString;

        byte[] Data = byteString.getBytes();

        try {
            byte[] encodedData = RSA.encryptByPublicKey(Data,publicKey);

            RequestParams requestParams = new RequestParams();
            requestParams.put("publicKey", publicKey);

            HttpUtils.postByUrl(HttpUtils.BASE_URL+ "users", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    System.out.println("Body: " + new String(responseBody));

                    JSONObject jsonobj = null;
                    String id = "";
                    try {
                        jsonobj = new JSONObject(new  String(responseBody));
                        id = jsonobj.getString("_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences sp = getSharedPreferences("Register", MODE_PRIVATE);
                    SharedPreferences.Editor Ed = sp.edit();
                    Ed.putString("Id",id);
                    Ed.putString("PublicKey",publicKey);
                    Ed.putString("PrivateKey",privateKey);
                    Ed.putString("Name",costumerName);
                    Ed.putString("NIF",NIFString);
                    Ed.putString("CCType",TypeString);
                    Ed.putString("CCNumber",CCNString);
                    Ed.putString("CCValidity",CCVString);
                    Ed.commit();

                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PerformancesActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(context, "Could not created account", Toast.LENGTH_SHORT).show();
                    System.out.println("Error on purchasing the tickets " + statusCode + ": " + error.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
