package neonconcept.sanikapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity implements View.OnClickListener {

    Button login;
    EditText uname_te, pwd_te;
    DB db;
    private final String LOGIN_URL = "http://192.168.0.101/minion/loginuser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DB(this);

        uname_te = (EditText) findViewById(R.id.etUsername);
        pwd_te = (EditText) findViewById(R.id.etPassword);

        /*
        Get username and password if it a new vendor account
         */
        Intent curint = getIntent();
        String parse_un = curint.getStringExtra("username");
        String parse_pw = curint.getStringExtra("pawd");

        /*
        Set values to usrname and password field that got from intent
         */
        uname_te.setText(parse_un);
        pwd_te.setText(parse_pw);

        /*
        Initialize login button and add onClick Listener
         */
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == login){
            try {
                /*
                Get username and password from view
                 */
                String uname = uname_te.getText().toString();
                String pwd = pwd_te.getText().toString();

                authrnticate(uname, pwd); // Authentication process via Async Task
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void authrnticate(String email, String password){
        final String parameters = "?email="+email+"&password="+password; // Parameters that need for authentication
        class Authenticate extends AsyncTask<String, String, String>{

            @Override
            protected String doInBackground(String... strings) {
                try{
                    /*
                    Replace spaces to %20 in URL avoid broking URL
                     */
                    String encoded = parameters.replaceAll(" ","%20");
                    String furl = LOGIN_URL+encoded;
                    System.out.println(furl);

                    /*
                    Create connection and read response via Buffered Reader.
                     */
                    URL url = new URL(furl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    /*
                    Read response and create new json object from retrieved string.
                     */
                    String returned = df.readLine();
                    JSONObject job = new JSONObject(returned);

                    if(job.getBoolean("error")){
                        // If response have an error. Request is handled from PHP
                        return job.getBoolean("error")+","+job.getString("message");
                    }else{
                        // Show hotels view if authentication process completed
                        showHotels(job.getString("vname"),job.getString("vendor"));
                        return "nothing";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getApplicationContext(),"Processing",Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onPostExecute(String s) {
                if(s.contains(",")) {
                    // Show the error if have one
                    String message = s.split(",")[1];
                    showError(message);
                }
            }
        }
        Authenticate auth = new Authenticate();
        auth.execute();
    }

    private void showHotels(String name, String vendor_id) {
        /*
        Show hotels intent
         */
        Intent int_f = new Intent(getApplicationContext(),hotel_list.class);
        int_f.putExtra("username",name);
        int_f.putExtra("vendor_id",vendor_id);
        startActivity(int_f);
        finish();
    }

    private void showError(String message) {
        /*
        Show error from alert dialog.
         */
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("AWW-SANP!");
        alert.setMessage(message);
        alert.show();

    }
}
