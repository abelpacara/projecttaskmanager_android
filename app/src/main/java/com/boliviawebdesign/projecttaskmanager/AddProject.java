package com.boliviawebdesign.projecttaskmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.util.Hashtable;

public class AddProject extends AppCompatActivity {

    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        //####################################################################
        session = new Session(AddProject.this); //in oncreate
        //and now we set sharedpreference then use this like


        Button btnAddProject = (Button) this.findViewById(R.id.btnAddProject);
        final EditText projectContentText = (EditText) findViewById(R.id.projectContentText);



        btnAddProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String params[] = {projectContentText.getText().toString()};
                new ProcessAddProject().execute(params);
            }
        });
    }


    /************************************************************************/
    private class ProcessAddProject extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){


            String stringURL = getResources().getString(R.string.server_address)+"/add_project";

            Hashtable hashparams =new Hashtable();

            hashparams.put("user_id",session.getUserId());

            hashparams.put("post_content",params[0]);

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {
            startActivity(new Intent(AddProject.this, MainActivity.class));
        } // onPostExecute() end
    } // ProcessJSON class end
}
