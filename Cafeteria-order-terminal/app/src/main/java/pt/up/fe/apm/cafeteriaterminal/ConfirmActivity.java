package pt.up.fe.apm.cafeteriaterminal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
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
        for(int i = 0; i < separated.length; i++){
            String[] separated2 = separated[i].split(":");

            array_list.add(separated2[0]);
            array_list2.add(separated2[1]);

        }

        System.out.println(array_list);
        System.out.println(array_list2);
    }
}
