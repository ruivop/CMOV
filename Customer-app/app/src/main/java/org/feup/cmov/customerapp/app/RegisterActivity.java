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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.model.RSA;
import org.feup.cmov.customerapp.utils.HttpUtils;
import org.json.JSONArray;
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

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String urlParameters  = "publicKey=" + publicKey;
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            String request = "http://hello.localtunnel.me:3000/users";
            URL url = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            conn.setUseCaches(false);
            byte[] outputBytes = urlParameters.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            String ResponseData = convertStreamToString(inputStream);
            System.out.println(ResponseData);

            JSONObject jsonobj = new JSONObject(ResponseData);
            String id = jsonobj.getString("_id");
            System.out.println(id);


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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
