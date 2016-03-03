package com.boliviawebdesign.projecttaskmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddPost = (Button) this.findViewById(R.id.btnAdd);
        final EditText postNameText = (EditText) findViewById(R.id.postNameText);
        final EditText postDescriptionText = (EditText) findViewById(R.id.postDescriptionText);



        btnAddPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                String params[] = { postNameText.getText().toString(),
                        postDescriptionText.getText().toString()
                };


                new ProcessJSON().execute(params);
            }
        });

    }




    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = "http://192.168.43.82/ptm/index.php/services/add_comment";

            Hashtable hashparams =new Hashtable();

            hashparams.put("post_name",params[0]);
            hashparams.put("post_description",params[1]);

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {
            TextView tv= (TextView) findViewById(R.id.viewResults);
            //..........Process JSON DATA................
            if(stream !=null) {
                try {

                    tv.setText(stream);

                }catch(Exception ex){
                    ex.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end
}
