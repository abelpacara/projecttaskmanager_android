package com.boliviawebdesign.projecttaskmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity {

    private TextView logTextView;
    private TextView userEmailEditText;
    private TextView userPasswordEditText;


    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //####################################################################
        session = new Session(LoginActivity.this); //in oncreate
        //and now we set sharedpreference then use this like


        Button loginBtn = (Button) this.findViewById(R.id.loginBtn);
        logTextView = (TextView) this.findViewById(R.id.logTextView);


        logTextView.setText(session.getUserId());


        userEmailEditText = (EditText) this.findViewById(R.id.userEmailEditText);
        userPasswordEditText = (EditText) this.findViewById(R.id.userPasswordEditText);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String params[] = new String[2];
                params[0] = new StringBuilder(userEmailEditText.getText()).toString();

                params[1] = new StringBuilder(userPasswordEditText.getText()).toString() ;


                new ProcessLogin().execute(params);
            }
        });


    }


    private class ProcessLogin extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {

            String text = "";
            BufferedReader reader = null;

            String stringURL = getResources().getString(R.string.server_address) + "/login";
            //String stringURL = "http://192.168.134.200/ptm/index.php/services/view_list_comments";

            Hashtable hashparams = new Hashtable();
            hashparams.put("user_email", params[0] );
            hashparams.put("user_password", params[1] );

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }


        protected void onPostExecute(String stream) {

            try{

                JSONObject obj = new JSONObject(stream);

                String user_id = obj.getString("user_id");

                if(user_id != null && !user_id.equalsIgnoreCase("null")){
                    session.setUserId(user_id);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    logTextView.append("Login o password invalido intente con otro");
                }


            }
            catch(JSONException ex){
                ex.printStackTrace();
            }


        }
    }
}
