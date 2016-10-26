package neonconcept.sanikapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class reg_vendor extends AppCompatActivity implements View.OnClickListener{

    Button saveNLogin, toLogin;
    EditText fnamex,lnamex,unamex,emailx,hnamex,pwdx,conpwdx;
    private static final String REGISTER_URL = "http://192.168.0.101/minion/vendorreg.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vendor);

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

        try {
            new DB(this);
        }catch (Exception e){
            e.printStackTrace();
        }

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
                    Save new vendor
                     */
                    String[] cols = {"fname","lname","uname","email","hname","pwd"};
                    String[] vals = {fname,lname,uname,email,hname,pwd};
                    new DB(this).insertData(cols,vals,"vendor");

                    /*
                    Show success message
                     */
                    Toast.makeText(this,"Successfully Registered. Click Log-In to go ahead..",Toast.LENGTH_LONG).show();

                    /*
                    Change intent, Redirect user to login page with username and password
                     */
                    registerVendor(fname,lname,hname,uname,pwd,email);
                    Intent login = new Intent(this, neonconcept.sanikapp.login.class);
                    login.putExtra("username",uname);
                    login.putExtra("pawd",pwd);
                    startActivity(login);

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
        System.out.println("parsed email is = "+email);
        String urlSuffix = "fname="+fname+"&lname="+lname+"&hotel="+hotel+"&username="+username+"&password="+password+"&email="+email;
        class RegVendor extends AsyncTask<String, String[] , String>{

            @Override
            protected String doInBackground(String... strings) {

                try {
                    String valswithurl = strings[0];
                    System.out.println("URL : "+valswithurl);
                    URL url = new URL(valswithurl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    System.out.println("read line : "+df.readLine());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Toast.makeText(getApplicationContext(),"before excecute",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(),"Post excecute",Toast.LENGTH_LONG).show();
            }
        }
        RegVendor regven = new RegVendor();
        regven.execute(REGISTER_URL+"?"+urlSuffix);
    }
}