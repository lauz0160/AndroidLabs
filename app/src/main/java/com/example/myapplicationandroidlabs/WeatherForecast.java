package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar progBar = findViewById(R.id.progressBar);
        progBar.setVisibility(View.VISIBLE);

        ForcastQuery req = new ForcastQuery();

        req.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

    }


    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();   }


    public class ForcastQuery extends AsyncTask<String, Integer, String> {


        String curTemp;
        String maxTemp;
        String minTemp;
        String uV;
        Bitmap pic;


        @Override
        protected String doInBackground(String... strings) {

            try{

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream response = urlConnection.getInputStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(response, "UTF-8");

                    int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        if (eventType == XmlPullParser.START_TAG) {
                            //If you get here, then you are pointing at a start tag
                            if (xpp.getName().equals("temperature")) {
                                //If you get here, then you are pointing to a <Weather> start tag
                                curTemp = xpp.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                            if (xpp.getName().equals("weather")) {
                                String fileName = xpp.getAttributeValue(null, "icon") + ".png";
                                FileInputStream fis = null;
                                if (fileExistance(fileName)) {
                                    try {
                                        fis = openFileInput(fileName);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("Weather Forecast", "Image found locally");
                                    pic = BitmapFactory.decodeStream(fis);
                                } else {
                                    URL imgurl = new URL("http://openweathermap.org/img/w/" + fileName);
                                    HttpURLConnection connection = (HttpURLConnection) imgurl.openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        Log.i("Weather Forecast", "Image downloaded");
                                        pic = BitmapFactory.decodeStream(connection.getInputStream());
                                        publishProgress(100);
                                    }

                                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    pic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                }
                            }
                        }
                        eventType = xpp.next(); //move to the next xml event and store it in a variable
                    }

                URL uvUrl = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection uvConnection = (HttpURLConnection) uvUrl.openConnection();
                uvConnection.connect();
                InputStream uvResponse = uvConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject uvReport = new JSONObject(result);
                uV= uvReport.getDouble("value")+"";




            }
            catch (Exception e){
                Log.e("Error", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            ProgressBar progBar = findViewById(R.id.progressBar);
            progBar.setVisibility(View.VISIBLE);
            progBar.setProgress(values[0]);

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            TextView curText = findViewById(R.id.currentTemp);
            curText.setText("   "+ getResources().getString(R.string.curTemp) +"   "+curTemp);
            TextView minText = findViewById(R.id.MinTemp);
            minText.setText("   "+ getResources().getString(R.string.minTemp )+"   "+minTemp);
            TextView maxText = findViewById(R.id.MaxTemp);
            maxText.setText("   "+getResources().getString( R.string.maxTemp )+"   "+maxTemp);
            TextView uVText = findViewById(R.id.UV);
            uVText.setText("   "+getResources().getString( R.string.uv) +"   "+uV);

            ProgressBar progBar = findViewById(R.id.progressBar);
            progBar.setVisibility(View.INVISIBLE);

            ImageView weatherPic = findViewById(R.id.weatherImage);
            weatherPic.setImageBitmap(pic);

            super.onPostExecute(s);
        }
    }
}