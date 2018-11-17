package pt.up.fe.apm.cafeteriaterminal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
        List<String> array_list = new ArrayList<String>();
        List<String> array_list2 = new ArrayList<String>();
        List<String> array_list3 = new ArrayList<String>();
        for(int i = 0; i < separated.length; i++){
            String[] separated2 = separated[i].split(":");

            array_list.add(separated2[0]);
            array_list2.add(separated2[1]);

            array_list3.add(separated2[0] + " - " + separated2[1] + "$");


        }



        System.out.println(array_list);
        System.out.println(array_list2);
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

            }
    });

        Button but_qr2 = findViewById(R.id.no_button);
        but_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
