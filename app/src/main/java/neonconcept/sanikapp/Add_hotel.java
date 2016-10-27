package neonconcept.sanikapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Add_hotel extends AppCompatActivity implements View.OnClickListener{

    Button btnNewHotel;
    EditText hnamex,regnox,addrx,emailx,pnox,landlinex,descx,openhx,postx;
    String REGHOTEL_URL = "http://192.168.0.101/minion/reghotel.php";
    String vendor_id, vendor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        btnNewHotel = (Button) findViewById(R.id.btnSaveHotel);
        btnNewHotel.setOnClickListener(this);

        hnamex = (EditText) findViewById(R.id.txt_hname);
        regnox = (EditText) findViewById(R.id.txt_regno);
        addrx = (EditText) findViewById(R.id.txt_addr);
        emailx = (EditText) findViewById(R.id.txt_email);
        pnox = (EditText) findViewById(R.id.txt_pno);
        landlinex = (EditText) findViewById(R.id.txt_lphone);
        openhx = (EditText) findViewById(R.id.txt_openhrs);
        postx = (EditText) findViewById(R.id.txt_postalcode);
        descx = (EditText) findViewById(R.id.txt_desc);

        vendor_id = getIntent().getStringExtra("vendor_id");
        vendor_name = getIntent().getStringExtra("vname");
    }

    @Override
    public void onClick(View view) {
        if(view == btnNewHotel){
            String hname = hnamex.getText().toString();
            String regno = regnox.getText().toString();
            String addr = addrx.getText().toString();
            String email = emailx.getText().toString();
            String pno = pnox.getText().toString();
            String landpno = landlinex.getText().toString();
            String open_hrs = openhx.getText().toString();
            String postCode = postx.getText().toString();
            String descr = descx.getText().toString();
            saveHotel(vendor_id,hname,regno,addr,email,pno,landpno,open_hrs,postCode,descr);
        }
    }

    private void saveHotel(String vendor,String hname, String regno, String addr, String email, String pno, String landpno, String open_hrs, String postCode,String descr) {
        final String para = "vendor="+vendor+"&hname="+hname+"&regno="+regno+"&address="+addr+"&email="+email+"&pno="+pno+"&fixedpno="+landpno+"&openhrs="+open_hrs+"&pcode="+postCode+"&descr="+descr;
        class RegHotel extends AsyncTask<String, String, String>{
            @Override
            protected String doInBackground(String... strings) {
                try{
                    String valswithurl = REGHOTEL_URL+"?"+para;
                    valswithurl = valswithurl.replaceAll(" ", "%20");
                    System.out.println(valswithurl);

                    URL url = new URL(valswithurl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String received_str = df.readLine();
                    JSONObject job = new JSONObject(received_str);
                    if(job.getBoolean("error")){
                        return "error,"+job.getString("Message");
                    }else{
                        return job.getString("Message");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                String[] strs = s.split(",");
                if(strs[0].equals("error")){
                    showError(strs[1]);
                }else{
                    Intent intent = new Intent(getApplicationContext(),hotel_list.class);
                    intent.putExtra("username",getIntent().getStringExtra("vname"));
                    intent.putExtra("vendor_id",vendor_id);
                    startActivity(intent);
                }
            }
        }
        RegHotel rhotel = new RegHotel();
        rhotel.execute();
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
