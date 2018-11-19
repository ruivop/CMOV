package org.feup.cmov.customerapp.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.feup.cmov.customerapp.R;

public class OrderQrActivity extends AppCompatActivity {

    private ImageView qrCodeImageview;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_qr);

        ImageView iV = findViewById(R.id.qr_image);
        context = this;
        String text = getIntent().getStringExtra("text");
        try {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,200,200);

            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();

            final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }

            iV.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        findViewById(R.id.button_to_transactions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LastyTransactionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //adicionar um butao
    }
}
