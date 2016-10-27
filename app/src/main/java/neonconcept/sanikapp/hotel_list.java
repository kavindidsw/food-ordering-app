package neonconcept.sanikapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class hotel_list extends AppCompatActivity implements AdapterView.OnItemLongClickListener{

    String FETCH_HOTELS = "http://192.168.0.101/minion/gethotels.php"; // Get hotels PHP
    TextView userTV;
    ArrayAdapter<ListOption> list_data;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        /*
        Initialize values
         */
        userTV = (TextView) findViewById(R.id.tv_username);

        String vendor_name = getIntent().getStringExtra("username"); // Get user name from intent that set from login form
        userTV.setText(vendor_name);

        list = (ListView) findViewById(R.id.hotelList);
        list_data = new ArrayAdapter<ListOption>(this, android.R.layout.simple_list_item_1);

        String vendor_id = getIntent().getStringExtra("vendor_id");

        // fetch registered hotels
        fetchHotels(vendor_id);

        list.setOnItemLongClickListener(this);
    }

    private void fetchHotels(final String vendor_id) {
        class FetchHotels extends AsyncTask<String, String, String>{
            final String params = "?vendor="+vendor_id;
            @Override
            protected String doInBackground(String... strings) {

                try{
                    /*
                    Request process
                     */
                    String full_url = FETCH_HOTELS+params;
                    URL url = new URL(full_url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    /*
                    Handle response
                     */
                    String returned = df.readLine();
                    JSONObject job = new JSONObject(returned);

                    JSONArray hotels = job.getJSONArray("hotels");
                    for (int i = 0; i < hotels.length(); i++) {
                        int vendor = Integer.parseInt(hotels.getJSONObject(i).getString("VendorId"));
                        int hotel_id = Integer.parseInt(hotels.getJSONObject(i).getString("HotelId"));
                        String hname = hotels.getJSONObject(i).getString("HotelName");
                        String reg_no = hotels.getJSONObject(i).getString("RegistrationNo");
                        String addr = hotels.getJSONObject(i).getString("Address");
                        String email = hotels.getJSONObject(i).getString("Email");
                        String phone = hotels.getJSONObject(i).getString("PhoneNumber");
                        String pland = hotels.getJSONObject(i).getString("LandLine");
                        String desc = hotels.getJSONObject(i).getString("Description");
                        String openhrs = hotels.getJSONObject(i).getString("Opening Hours");
                        String pcode = hotels.getJSONObject(i).getString("Postalcode");

                        list_data.add(new ListOption(hotel_id,vendor,hname,reg_no,addr,email,phone,pland,desc,openhrs,pcode));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getApplicationContext(),"Retrieving hotels...",Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onPostExecute(String s) {
                list.setAdapter(list_data);
                if(list_data.isEmpty()){
                    Toast.makeText(getApplicationContext(),"No hotels registered for you.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Retrieved "+list_data.getCount()+" hotels registered for you.",Toast.LENGTH_SHORT).show();
                }
            }
        }

        FetchHotels fhs = new FetchHotels();
        fhs.execute();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        ListOption lop = (ListOption) adapterView.getItemAtPosition(i);
        alert.setTitle(lop.getHotel_name());

        Context context = this.getBaseContext();

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,0,0,50);

        final TextView reg_no = new TextView(this);
        reg_no.setText("Registration Number : "+lop.getReg_no());

        final TextView addr = new TextView(this);
        addr.setText("Address : "+lop.getAddress());

        final TextView pcode = new TextView(this);
        pcode.setText("Postal Code : "+lop.getPostal_code());

        final TextView pno = new TextView(this);
        pno.setText("Phone Number : "+lop.getPhone());

        final TextView lpno = new TextView(this);
        lpno.setText("Land Line : "+lop.getPhoneLand());

        final TextView descr = new TextView(this);
        descr.setText("Description : "+lop.getDescription());

        final TextView ophrs = new TextView(this);
        ophrs.setText("Open Hours : "+lop.getOpen_hrs());

        layout.addView(reg_no);
        layout.addView(addr);
        layout.addView(pcode);
        layout.addView(pno);
        layout.addView(lpno);
        layout.addView(descr);
        layout.addView(ophrs);

        alert.setView(layout);
        alert.show();
        return false;
    }
}
