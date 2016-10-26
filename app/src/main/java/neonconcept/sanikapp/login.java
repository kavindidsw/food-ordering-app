package neonconcept.sanikapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity implements View.OnClickListener {

    Button login;
    EditText uname_te, pwd_te;
    DB db;

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
                Cursor data = db.search("SELECT * FROM vendor WHERE uname='"+uname+"' AND pwd='"+pwd+"' LIMIT 1");
                if(data.moveToNext()){
                    Toast.makeText(this,"You Logged In",Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("AWW-SANP!");
                    alert.setMessage("Error username or password. Please be sure your credentials are correct!");
                    alert.show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
