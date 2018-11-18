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

import com.feup.cmov.ticket_validation_terminal.Classes.TicketMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static String serverIp = "192.168.0.101";

    TextView infos;
    ImageView image;
    TextView confirmationTitle;
    Button goBackBtn;

    String userPublicId;
    List<TicketMessage> content = new ArrayList<TicketMessage>();
    int contentIndexSending = 0;
    int resultHasError = -1;
    boolean hascChekedQR = false;
    int numberOfRejects = 0;
    String comunicationErrors = "";

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
            updateVisual();

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
                String recceived = data.getStringExtra("SCAN_RESULT");

                try {
                    String[] splitPublicIdFromMessage = recceived.split("-");
                    this.userPublicId = splitPublicIdFromMessage[0];

                    String receivedQrMessages[] = splitPublicIdFromMessage[1].split(",");
                    String date = null;
                    for(String message : receivedQrMessages){
                        String[] ticketStrings = message.split(":");
                        if(date != null && date.compareTo(ticketStrings[1]) != 0) {
                            infos.setText("All tickets must be on the same date");
                            resultHasError = 1;
                            updateVisual();
                            return;
                        }
                        if(date == null) {
                            date = ticketStrings[1];
                        }
                        this.content.add(new TicketMessage(ticketStrings[0], ticketStrings[1]));
                    }

                } catch (Exception e) {
                    infos.setText("Format of QR is not valid");
                    resultHasError = 1;
                    updateVisual();
                    return;
                }

                ChUser restProcess = new ChUser();
                restProcess.execute();
            }
        }
    }

    protected void resultOfRestCall(int hasError) {
        contentIndexSending++;

        if(hasError != 0) {
            resultHasError = 1;
            numberOfRejects++;
        }

        if(contentIndexSending == content.size()) {
            if(resultHasError == -1) {
                resultHasError = 0;
                infos.setText("All the " + contentIndexSending + " tickets were validated.");
            } else {
                resultHasError = 1;
                String error = "There are " + numberOfRejects + " rejected tickets, from "+ contentIndexSending + " total:\n";
                for(TicketMessage message : content) {
                    if(message.getErrorMessage().length() > 0) {
                        error +=  "   ->" + message.getErrorMessage() + "\n";
                    }
                }
                infos.setText(error);
            }
            updateVisual();
            return;
        }

        ChUser restProcess = new ChUser();
        restProcess.execute();
    }

    private void updateVisual() {
        if(resultHasError == 0) {
            confirmationTitle.setText(R.string.confirmed);
            image.setImageResource(R.drawable.ic_check_black_24dp);
        } else if(resultHasError == 1) {
            confirmationTitle.setText(R.string.error);
            image.setImageResource(R.drawable.ic_clear_black_24dp);
        }
    }

    private class ChUser extends AsyncTask<String, Void, Void> {
        private int hasError = -1;
        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            TicketMessage ticket = content.get(contentIndexSending);
            try {
                url = new URL("http://" + serverIp + ":3000/tickets/validate/" + ticket.getTicketId());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                String payload = "{\"customer\":\"" + userPublicId + "\"}";
                outputStream.writeBytes(payload);
                outputStream.flush();
                outputStream.close();

                // get response
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {
                    hasError = 0;

                    try {
                        java.util.Scanner s = new java.util.Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
                        String body = s.hasNext() ? s.next() : "";

                        System.out.println("body: " + body);

                        if(body.compareTo("Alredy Validated") == 0) {
                            ticket.writeText("Ticket Alredy used");
                            hasError = 1;
                            return null;
                        }else if(body.compareTo("Not Valid Ticket") == 0) {
                            ticket.writeText("This is not a ticket");
                            hasError = 1;
                            return null;
                        }else if(body.compareTo("Not the same user") == 0) {
                            ticket.writeText("Not the same user");
                            hasError = 1;
                            return null;
                        }
                        String[] words = body.split(",");
                        for (String word : words) {
                            if (word.split(":")[0].compareTo("\"performance\"") == 0) {
                                ticket.writeText("Accepted ticket for " + word.split(":")[1]);
                            }
                        }
                    } catch (IOException e) {
                        ticket.writeText("The ticket is not valid");
                        hasError = 1;
                    }
                } else {
                    ticket.writeText("Code: " + responseCode);
                    hasError = 1;
                }
            }
            catch (IOException e) {
                ticket.writeText("Comunicating with the server");
                hasError = 1;
            }
            catch (Exception e) {
                ticket.writeText(e.toString());
                hasError = 1;
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            resultOfRestCall(hasError);
        }
    }
}
