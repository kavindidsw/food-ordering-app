package neonconcept.sanikapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class hotel_list extends AppCompatActivity {

    String FETCH_HOTELS = "http://192.168.0.101/minion/gethotels.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        fetchHotels("9");
    }

    private void fetchHotels(final String vendor_id) {
        class FetchHotels extends AsyncTask<String, String, String>{
            final String params = "?vendor="+vendor_id;
            @Override
            protected String doInBackground(String... strings) {

                try{
                    String full_url = FETCH_HOTELS+params;

                    URL url = new URL(full_url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader df = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String returned = df.readLine();
                    JSONObject job = new JSONObject(returned);

                    JSONArray hotels = job.getJSONArray("hotels");
                    for (int i = 0; i < hotels.length(); i++) {
                        System.out.println("hname : "+hotels.getJSONObject(i).getString("HotelName"));
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
                Toast.makeText(getApplicationContext(),"after excecute",Toast.LENGTH_SHORT).show();
            }
        }

        FetchHotels fhs = new FetchHotels();
        fhs.execute();
    }
}
