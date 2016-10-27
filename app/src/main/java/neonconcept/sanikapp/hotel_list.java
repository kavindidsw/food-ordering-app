package neonconcept.sanikapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

public class hotel_list extends AppCompatActivity {

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
                        list_data.add(new ListOption(Integer.parseInt(hotels.getJSONObject(i).getString("HotelId")), hotels.getJSONObject(i).getString("HotelName")));
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
}
