package neonconcept.sanikapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class reg_vendor extends AppCompatActivity implements View.OnClickListener{

    Button saveNLogin, toLogin;
    EditText fnamex,lnamex,unamex,emailx,hnamex,pwdx,conpwdx;
    private static final String REGISTER_URL = "http://192.168.0.101/minion/vendorreg.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vendor);

        if(!isConnected()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("You are not connected");
            alert.setMessage("Please connect to reliable internet connection to work with this App.");
            alert.show();
            //finish();
        }

        saveNLogin = (Button) findViewById(R.id.saveNLogin); // Initialize button
        toLogin = (Button) findViewById(R.id.dirLogin);

        /*
        Initialize data text fields. (Edit Text Fields)
         */
        fnamex = (EditText) findViewById(R.id.vlog_fname);
        lnamex = (EditText) findViewById(R.id.vlog_lname);
        unamex = (EditText) findViewById(R.id.vlog_uname);
        emailx = (EditText) findViewById(R.id.vlog_email);
        hnamex = (EditText) findViewById(R.id.vlog_hname);
        pwdx = (EditText) findViewById(R.id.vlog_password);
        conpwdx = (EditText) findViewById(R.id.vlog_conf_password);

        saveNLogin.setOnClickListener(this); // Set click listener to button (Listener is implemented).
        toLogin.setOnClickListener(this); // Set click listener to Direct login button
    }

    @Override
    public void onClick(View view) {
        if (view == saveNLogin){
            /*
            Get Values from text fields
             */
            String fname = fnamex.getText().toString();
            String lname = lnamex.getText().toString();
            String uname = unamex.getText().toString();
            String email = emailx.getText().toString();
            String hname = hnamex.getText().toString();
            String pwd = pwdx.getText().toString();
            String confpwd = conpwdx.getText().toString();

            /*
            Validate inputs
             */
            String tmessage = "";
            if(fname.length() < 2)
                tmessage = "First Name Field Is Required";
            if(lname.length() < 2)
                tmessage = "Last Name Field Is Required";
            if(uname.length() < 2)
                tmessage = "User Name Field Is Required";
            if(email.length() < 2)
                tmessage = "Email Address Field Is Required";
            if(hname.length() < 2)
                tmessage = "Hotel Name Field Is Required";
            if(pwd.length() < 2)
                tmessage = "Password Field Is Required";
            if(confpwd.length() < 2)
                tmessage = "Confirm Password Field Is Required";

            if(!pwd.equals(confpwd)){
                tmessage = "Password must equal to confirm password";
            }

            if(!tmessage.equals("")){
                /*
                Show error alert if have an error
                 */
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("You got an error!");
                alert.setMessage(tmessage);
                alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }else{
                try {
                    /*
                    Save new vendor on remote DB
                     */
                    registerVendor(fname,lname,hname,uname,pwd,email);
                }catch (Exception e){
                    Toast.makeText(this,"You got a critical error. Please contact your system administrator.",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }else if(view == toLogin){
            /*
            Send user directly to the login. Parse empty values for username and password
             */
            Intent login = new Intent(this, neonconcept.sanikapp.login.class);
            login.putExtra("username","");
            login.putExtra("pawd","");
            startActivity(login);
        }
    }

    private void registerVendor(String fname, String lname, String hotel,String username, String password, String email) {
        final String urlSuffix = "fname="+fname+"&lname="+lname+"&hotel="+hotel+"&username="+username+"&password="+password+"&email="+email;
        class RegVendor extends AsyncTask<String, String[] , String>{

            @Override
            protected String doInBackground(String... strings) {

                try {
                    /*
                    Create full url and replace spaces to %20 to stop invalidation
                     */
                    String valswithurl = strings[0]+"?"+urlSuffix;
                    valswithurl = valswithurl.replaceAll(" ", "%20");

                    /*
                    Open URL Connection
                     */
                    URL url = new URL(valswithurl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    /*
                    Read retrieved response
                     */
                    String returned = df.readLine();

                    /*
                    Parse response text to json
                     */
                    JSONObject jobject = new JSONObject(returned);
                    if(jobject.getBoolean("error")){
                    }else{
                        return jobject.getString("username")+","+jobject.getString("password")+","+jobject.getString("Message");
                    }
                return jobject.getString("Message");
                }catch (Exception e){
                    e.printStackTrace();
                }
                return "Uh-Oh We got an error while processing this!";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.contains(",")){
                    // No error
                    String[] splitted = s.split(",");
                    String username = splitted[0];
                    String password = splitted[1];
                    String message = splitted[2];

                    Intent login = new Intent(getApplicationContext(), neonconcept.sanikapp.login.class);
                    login.putExtra("username",username);
                    login.putExtra("pawd",password);
                    startActivity(login);
                }else{
                    showError(s);
                }

            }
        }
        RegVendor regven = new RegVendor();
        regven.execute(REGISTER_URL);
    }

    private void showError(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("You got an error!");
        alert.setMessage(message);
        alert.show();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
