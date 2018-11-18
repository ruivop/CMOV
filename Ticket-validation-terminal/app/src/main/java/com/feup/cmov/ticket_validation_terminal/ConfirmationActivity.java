package com.feup.cmov.ticket_validation_terminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConfirmationActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static String serverIp = "192.168.0.101";

    TextView infos;
    ImageView image;
    TextView confirmationTitle;
    Button goBackBtn;
    String content;
    int resultHasError = -1;
    boolean hascChekedQR = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation);
        infos = findViewById(R.id.info);
        image = findViewById(R.id.imageView);
        confirmationTitle = findViewById(R.id.confirmation_title);
        goBackBtn = findViewById(R.id.return_button);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmationActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        if(savedInstanceState != null) {
            if (savedInstanceState.getInt("hascChekedQR") == 1)
                this.hascChekedQR = true;
            else
                this.hascChekedQR = false;

            resultHasError = savedInstanceState.getInt("resultHasError");
            resultOfRestCall();

            infos.setText(savedInstanceState.getString("infos"));
        }

        if(!hascChekedQR) {
            scan();
            hascChekedQR = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("hascChekedQR", hascChekedQR ? 1 : 0);
        outState.putInt("resultHasError", resultHasError);
        outState.putString("infos", infos.getText().toString());
    }

    private void scan() {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE","QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                act.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                content = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");

                String[] args = {serverIp, content};
                ChUser restProcess = new ChUser();
                restProcess.execute(args);
            }
        }
    }

    protected void resultOfRestCall() {
        if(resultHasError == 0) {
            confirmationTitle.setText(R.string.confirmed);
            image.setImageResource(R.drawable.ic_check_black_24dp);
        } else if(resultHasError == 1) {
            confirmationTitle.setText(R.string.error);
            image.setImageResource(R.drawable.ic_clear_black_24dp);
        }
    }

    private void writeText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infos.setText(text);
            }
        });
    }

    private class ChUser extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String address = strings[0];
            String id = strings[1];
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://" + address + ":3000/tickets/validate/" + id);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                /*String payload = "\"" + uname + "\"";
                appendText("payload: " + payload);
                outputStream.writeBytes(payload);*/
                outputStream.flush();
                outputStream.close();

                // get response
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {
                    resultHasError = 0;

                    try {
                        java.util.Scanner s = new java.util.Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
                        String body = s.hasNext() ? s.next() : "";
                        if(body.compareTo("false") == 0) {
                            writeText("Error for server " + serverIp + ":\n Ticket Alredy used");
                            resultHasError = 1;
                            return null;
                        }
                        if(body.compareTo("Not Valid Ticket") == 0) {
                            writeText("Error for server " + serverIp + ":\n This is not a ticket");
                            resultHasError = 1;
                            return null;
                        }
                        String[] words = body.split(",");
                        for (String word : words) {
                            if (word.split(":")[0].compareTo("\"performance\"") == 0) {
                                writeText("Performance: " + word.split(":")[1]);
                            }
                        }
                    } catch (IOException e) {
                        writeText("Error for server " + serverIp + ":\n The ticket is not valid");
                        resultHasError = 1;
                    }
                } else {
                    writeText("Error for server " + serverIp + ":\n Code: " + responseCode);
                    resultHasError = 1;
                }
            }
            catch (IOException e) {
                writeText("Error for server " + serverIp + ":\n Comunicating with the server");
                resultHasError = 1;
            }
            catch (Exception e) {
                writeText("Error for server " + serverIp + ":\n" + e.toString());
                resultHasError = 1;
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            System.out.println("123 : " + resultHasError);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("234 : " + resultHasError);
            resultOfRestCall();
        }
    }
}
