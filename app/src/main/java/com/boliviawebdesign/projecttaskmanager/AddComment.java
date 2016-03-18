package com.boliviawebdesign.projecttaskmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddComment extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);



        Button btnAddComment = (Button) this.findViewById(R.id.btnAddComment);
        final EditText commentContentText = (EditText) findViewById(R.id.commentContentText);



        btnAddComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = getIntent(); // this is just for example purpose
                Bundle myBundle = myIntent.getExtras();

                String params[] = {commentContentText.getText().toString(), myBundle.getString("post_id")
                };


                new ProcessAddComment().execute(params);
            }
        });













    }




    /************************************************************************/

    private class ProcessAddComment extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = getResources().getString(R.string.server_address)+"/add_comment";

            Hashtable hashparams =new Hashtable();

            hashparams.put("post_content",params[0]);
            hashparams.put("parent_id",params[1]);

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {


            startActivity(new Intent(AddComment.this, MainActivity.class));


        } // onPostExecute() end
    } // ProcessJSON class end


}


