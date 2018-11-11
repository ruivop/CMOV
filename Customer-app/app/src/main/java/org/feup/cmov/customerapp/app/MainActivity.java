package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.feup.cmov.customerapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;

        SharedPreferences sp1 = this.getSharedPreferences("Register", MODE_PRIVATE);
        String testRegister = sp1.getString("Id",null);
        if(testRegister != null){
            Intent intent = new Intent(context, PerformancesActivity.class);
            startActivity(intent);
        }


        Button but_qr = findViewById(R.id.register_btn);

        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

}
}
