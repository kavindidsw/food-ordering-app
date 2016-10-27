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

        Intent curint = getIntent();
        String parse_un = curint.getStringExtra("username");
        String parse_pw = curint.getStringExtra("pawd");

        uname_te.setText(parse_un);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == login){
            try {
                String uname = uname_te.getText().toString();
                String pwd = pwd_te.getText().toString();
                authrnticate(uname, pwd);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void authrnticate(String email, String password){
        final String parameters = "?email="+email+"&password="+password;
        class Authenticate extends AsyncTask<String, String, String>{

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String encoded = parameters.replaceAll(" ","%20");
                    String furl = LOGIN_URL+encoded;
                    System.out.println(furl);

                    URL url = new URL(furl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String returned = df.readLine();
                    JSONObject job = new JSONObject(returned);

                    if(job.getBoolean("error")){
                        return job.getBoolean("error")+","+job.getString("message");
                    }else{
                        showHotels();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getApplicationContext(),"before excecute",Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onPostExecute(String s) {
                if(s.contains(",")) {
                    String message = s.split(",")[1];
                    showError(message);
                }else{
                    showHotels();
                }
            }
        }
        Authenticate auth = new Authenticate();
        auth.execute();
    }

    private void showHotels() {
        Intent int_f = new Intent(this,reg_vendor.class);
        startActivity(int_f);
    }

    private void showError(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("AWW-SANP!");
        alert.setMessage(message);
        alert.show();
    }
}
