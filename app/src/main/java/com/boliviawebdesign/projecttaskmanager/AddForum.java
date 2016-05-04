package com.boliviawebdesign.projecttaskmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AddForum extends AppCompatActivity {


    Spinner spinnerDropDown;
    String items[];
    String projects_ids[];
    int projectsSpinnerIndex = 0;


    DatabaseManager dbManager;
    PostsDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum);

        dbManager = new DatabaseManager(this);
        databaseHelper = PostsDatabaseHelper.getInstance(this);



        Button btnAddForum = (Button) this.findViewById(R.id.btnAddForum);
        final EditText forumContentText = (EditText) findViewById(R.id.forumContentText);



        btnAddForum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String params[] = {forumContentText.getText().toString()};
                new ProcessAddForum().execute(params);
            }
        });






        String params[] = new String[3];

        new ProcessLoadProjects().execute(params);
    }


    /************************************************************************/

    private class ProcessLoadProjects extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {


            String stringURL = getResources().getString(R.string.server_address) + "/view_list_projects";

            Hashtable hashparams = new Hashtable();

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {


            if (stream != null) {
                try {

                    JSONArray jsarray = new JSONArray(stream);
                    items = new String[jsarray.length()];
                    projects_ids = new String[jsarray.length()];


                    for (int i = 0; i < jsarray.length(); i++) {

                        JSONObject jsobj = jsarray.getJSONObject(i);

                        Map<String, String> item0 = new HashMap<String, String>(2);

                        projects_ids[i] = jsobj.getString("id_post");
                        items[i] = jsobj.getString("post_content");
                    }


                    spinnerDropDown = (Spinner) AddForum.this.findViewById(R.id.forum_spinner);


                    ArrayAdapter<String> adapter= new ArrayAdapter<String>(AddForum.this,
                            R.layout.spinner_item,
                            R.id.spinner_item_project,
                            items);


                    spinnerDropDown.setAdapter(adapter);

                    spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // Get select item
                            projectsSpinnerIndex = spinnerDropDown.getSelectedItemPosition();


                            Toast.makeText(getBaseContext(), "Seleccionaste el foro : " + items[projectsSpinnerIndex] + " " + projects_ids[projectsSpinnerIndex],
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });



                    ContentValues values = new ContentValues();
                    values.put("project_id", projects_ids[projectsSpinnerIndex]);
                    values.put("post_title", "forum 3");
                    values.put("post_content", "LOREM IPSUM 3");

                    /*dbManager.insert("posts", values);
                    String msg = dbManager.getTestInsert();
                    */

                    databaseHelper.addPost(values, "forum");

                    //databaseHelper.insert("posts", values);

                    String msg = databaseHelper.getTestInsert();


                    TextView myTextView = (TextView) AddForum.this.findViewById(R.id.textView);
                    myTextView.setText(msg);

                } catch (Exception ex) {

                }
            }

        } // onPostExecute() end
    } // ProcessJSON class end





    /************************************************************************/

    private class ProcessAddForum extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = getResources().getString(R.string.server_address)+"/add_forum";

            Hashtable hashparams =new Hashtable();

            hashparams.put("post_content",params[0]);
            hashparams.put("parent_id", projects_ids[projectsSpinnerIndex]);




            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {


            startActivity(new Intent(AddForum.this, MainActivity.class));


        } // onPostExecute() end
    } // ProcessJSON class end
}
