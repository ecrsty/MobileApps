package ru.mirea.solovevave.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.solovevave.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getDataBtn.setOnClickListener(v -> onGetDataBtnClick(v));
    }

    public void onGetDataBtnClick(View v) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadIpInfo().execute();
        } else Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
    }

    private class BaseHttpRequestTask extends AsyncTask<Void, Void, String> {
        private final String address;
        private final String requestMethod;
        public BaseHttpRequestTask(String address, String requestMethod) {
            this.address = address;
            this.requestMethod = requestMethod;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return MakeRequest();
            } catch (IOException | RuntimeException e) {
                return null;
            }
        }

        private String MakeRequest() throws IOException, RuntimeException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod(requestMethod);
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read;
                    while ((read = inputStream.read()) != -1)
                        bos.write(read);
                    bos.close();
                    data = bos.toString();
                } else data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                connection.disconnect();
            } catch (IOException e) {
                Log.d(TAG, "Error: "+e.toString());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }

    private class DownloadIpInfo extends BaseHttpRequestTask {
        public DownloadIpInfo() {
            super("https://ipinfo.io/json", "GET");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.ipTextView.setText("ip: загрузка...");
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
            try {
                JSONObject responseJson = new JSONObject(result);
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String[] loc = responseJson.getString("loc").split(",");
                float latitude = Float.parseFloat(loc[0]);
                float longitude = Float.parseFloat(loc[1]);
                Log.d(TAG, "latitude: " + latitude + "; longitude: " + longitude);

                binding.ipTextView.setText("ip: " + ip);
                binding.cityTextView.setText("Город: " + city);
                binding.regionTextView.setText("Регион: " + region);

                new DownloadWeatherInfo(latitude, longitude).execute();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            super.onPostExecute(result);
        }
    }

    private class DownloadWeatherInfo extends BaseHttpRequestTask {
        private static final String apiKey = "ae53760321911b952e3646887ead8c6d";
        public DownloadWeatherInfo(float latitude, float longitude) {
            super(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=%s",
                    latitude, longitude, apiKey), "GET");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.weatherTextView.setText("Погода: загрузка...");
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, result);
            try {
                JSONObject responseJson = new JSONObject(result);
                String weather = responseJson.getJSONArray("weather").getJSONObject(0).getString("description");
                String temperature = String.format("%.2f", responseJson.getJSONObject("main").getDouble("temp") - 270.15);
                binding.weatherTextView.setText("Погода: " +  weather + "\nТемпература: " + temperature);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            super.onPostExecute(result);
        }
    }
}