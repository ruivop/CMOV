package pt.up.fe.apm.cafeteriaterminal;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String orderString = this.getIntent().getStringExtra("orderlist");
        String[] separated = orderString.split(";");

        setContentView(R.layout.activity_confirm);

        ListView lv = (ListView) findViewById(R.id.order_list);
        List<String> array_list3 = new ArrayList<String>();
        String productlist = "";
        String costlist = "";
        Double totalPrice = 0.0;
        int nVouchers =0;
        int nPVouchers = 0;
        String userId = separated[0];
        for(int i = 1; i < separated.length-1; i++){
            String[] separated2 = separated[i].split(":");

            if(separated2[0].equals("Coffee Voucher")){
                nVouchers = Integer.parseInt(separated2[1]);
            }
            else if(separated2[0].equals("Popcorn Voucher")){
                nPVouchers = Integer.parseInt(separated2[1]);
            }
                productlist = productlist + "-" + separated2[0];
                costlist = costlist + "-" + separated2[1];




            array_list3.add(separated2[0] + " - " + separated2[1]);





        }
        String[] separatedFinal = separated[separated.length-1].split(":");
        totalPrice = Double.parseDouble(separatedFinal[1]);

        totalPrice = totalPrice - (totalPrice * (0.05 * nVouchers));
        array_list3.add("Total Price - " + totalPrice + "$");

        final String plist = productlist;
        final String clist = costlist;
        final Double finalPrice = totalPrice;
        final String finalUserId = userId;
        final int numberVouchers = nVouchers;
        final int numberPVouchers = nPVouchers;

        //System.out.println(array_list);
        //System.out.println(array_list2);
        System.out.println(array_list3);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                array_list3 );

        lv.setAdapter(arrayAdapter);



        Button but_qr = findViewById(R.id.yes_button);
        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String urlParameters = "product=" + plist + "&number=" + clist + "&price=" + String.valueOf(finalPrice) + "&userid=" + finalUserId ;
                    sendData(urlParameters, finalUserId,numberVouchers,numberPVouchers);
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    //startActivity(intent);

            }
    });

        Button but_qr2 = findViewById(R.id.no_button);
        but_qr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
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

    private void sendData(String urlParameters, String userid, int nVouchers, int nPVouchers){
        try{




            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL urlVoucher = new URL("http://hello.localtunnel.me:3000/vouchers/user/" + userid);
            HttpURLConnection con = (HttpURLConnection) urlVoucher.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            InputStream inputStream2 = new BufferedInputStream(con.getInputStream());
            String ResponseData2 = convertStreamToString(inputStream2);
            System.out.println(ResponseData2);
            inputStream2.close();


            String[] separated = ResponseData2.split(":");

            System.out.println(separated);

            int nAvailableVouchers = 0;
            ArrayList<String> ids = new ArrayList<>();
            ArrayList<String> idsp = new ArrayList<>();

            for(int i = 0; i<separated.length; i++){
                if(separated[i].contains("_id") && separated[i+2].contains("coffee")){
                    System.out.println(separated[i] + " AND " + separated[i+1]);
                    String[] sp = separated[i+1].split(",");
                    System.out.println(sp[0]);
                    ids.add(sp[0].replaceAll("\"",""));
                }
                if(separated[i].contains("_id") && separated[i+2].contains("popcorn")){
                    System.out.println(separated[i] + " AND " + separated[i+1]);
                    String[] sp = separated[i+1].split(",");
                    System.out.println(sp[0]);
                    idsp.add(sp[0].replaceAll("\"",""));
                }

            }

            if((ids.size() < nVouchers) && (idsp.size() < nPVouchers) ){
                Toast.makeText(this.getLayoutInflater().getContext(),"Not Enough Vouchers Validated", Toast.LENGTH_SHORT).show();
                return;
            }else{

                for(int i = 0; i < nVouchers; i++){
                    URL url = new URL("http://hello.localtunnel.me:3000/vouchers/" + ids.get(i));
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestProperty(
                            "Content-Type", "application/x-www-form-urlencoded" );
                    httpCon.setRequestMethod("DELETE");
                    System.out.println(httpCon.getResponseCode());
                }
                for(int i = 0; i < nPVouchers; i++){
                    URL url = new URL("http://hello.localtunnel.me:3000/vouchers/" + idsp.get(i));
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestProperty(
                            "Content-Type", "application/x-www-form-urlencoded" );
                    httpCon.setRequestMethod("DELETE");
                    System.out.println(httpCon.getResponseCode());
                }

            }


            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            URL url = new URL("http://hello.localtunnel.me:3000/orders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
            inputStream.close();
        }
        catch(Exception e){

            e.printStackTrace();

        }

        return;

    }

}




