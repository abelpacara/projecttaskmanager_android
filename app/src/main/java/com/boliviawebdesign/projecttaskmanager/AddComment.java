package com.boliviawebdesign.projecttaskmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.util.Hashtable;

public class AddComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);



        Button btnAddComment = (Button) this.findViewById(R.id.btnAddComment);
        final EditText commentContentText = (EditText) findViewById(R.id.commentContentText);



        btnAddComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String params[] = {commentContentText.getText().toString()};
                new ProcessJSON().execute(params);
            }
        });
    }

    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = "http://192.168.134.200/ptm/index.php/services/add_comment";

            Hashtable hashparams =new Hashtable();

            hashparams.put("post_content",params[0]);

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {
            startActivity(new Intent(AddComment.this, MainActivity.class));
        } // onPostExecute() end
    } // ProcessJSON class end

}
