package com.example.weather_nhom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class currentWeather extends AppCompatActivity {

    EditText editTextSearch;
    Button btnChangeActivity;
    TextView textViewNation, textViewCity, textViewTemp, textViewStatus, textViewCloud, textViewHumidity,
            textViewWind, textViewTime, TextViewSunRise, TextViewSunSet, TextViewSucEp, TextViewMinTemp,
            TextViewMaxTemp, TextViewSeaLevel;
    ImageView img, imgSearch;
    SimpleDateFormat simpleDateFormat;
    Date date, dateSunrise, dataSunset;
    String API_KEY = "d88a1bd56ada022b563a4d29990af41f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editTextSearch.getText().toString();
                GetCurrentWeatherData(city);
            }
        });

        //default
        GetCurrentWeatherData("Hanoi");
        mapping();
    }

    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(currentWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ data +"&appid="+API_KEY+"&units=metric&lang=vi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("TAG", response);
                    String time = jsonObject.getString("dt");
                    String nameCity = jsonObject.getString("name");
                    String timezone = jsonObject.getString("timezone");
                    int intTimezone = Integer.valueOf(timezone);
                    textViewCity.setText(nameCity);

                    //chuyen doi ngay thang nam
                    long t = Long.valueOf(time);
                    date = new Date(t*1000);
                    simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm a");
                    String Time = simpleDateFormat.format(date);
                    textViewTime.setText(Time);

                    //get JSON array weather
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("description");
                    String icon = jsonObjectWeather.getString("icon");

                    Picasso.with(currentWeather.this).load("http://openweathermap.org/img/wn/"+icon+".png").into(img);
                    textViewStatus.setText(status);

                    //get JSON main
                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String temp = jsonObjectMain.getString("temp");
                    String humidity = jsonObjectMain.getString("humidity");
                    String temp_min = jsonObjectMain.getString("temp_min");
                    String temp_max = jsonObjectMain.getString("temp_max");
                    String pressure = jsonObjectMain.getString("pressure");
                    String sea_level = jsonObjectMain.getString("pressure");

                    Double atemp = Double.valueOf(temp);
                    String TempString = String.valueOf(atemp.intValue());
                    textViewTemp.setText(TempString + "°C");
                    textViewHumidity.setText(humidity+"%");
                    TextViewMinTemp.setText(temp_min + "°C");
                    TextViewMaxTemp.setText(temp_max + "°C");
                    TextViewSucEp.setText(pressure);
                    TextViewSeaLevel.setText(sea_level);

                    //get JSON Wind
                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String speed = jsonObjectWind.getString("speed");
                    textViewWind.setText(speed + "m/s");

                    //get JSON Cloud
                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                    String clouds = jsonObjectCloud.getString("all");
                    textViewCloud.setText(clouds + "%");

                    //get JSON sys
                    JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                    String country = jsonObjectSys.getString("country");
                    textViewNation.setText(country);


                    //sunrise
                    String sunrise = jsonObjectSys.getString("sunrise");
                    long timeSunRise = Long.valueOf(sunrise);
                    dateSunrise = new Date(timeSunRise*1000);
                    simpleDateFormat = new SimpleDateFormat("HH:mm a");
                    String SunRise = simpleDateFormat.format(dateSunrise);
                    TextViewSunRise.setText(SunRise);

                    //sunset
                    String sunset = jsonObjectSys.getString("sunset");
                    long timeSunSet = Long.valueOf(sunset);
                    dataSunset = new Date(timeSunSet*1000);
                    simpleDateFormat = new SimpleDateFormat("HH:mm a");
                    String SunSet = simpleDateFormat.format(dataSunset);
                    TextViewSunSet.setText(SunSet);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
    }

    public void mapping(){
        editTextSearch = (EditText) findViewById(R.id.editTextSearchCity);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        btnChangeActivity = (Button) findViewById(R.id.btnChangeActivity);
        textViewNation = (TextView) findViewById(R.id.textViewNation);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewTemp = (TextView) findViewById(R.id.textViewTemp);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewCloud = (TextView) findViewById(R.id.textViewCloud);
        textViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
        textViewWind = (TextView) findViewById(R.id.textViewWind);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        TextViewSunRise = (TextView) findViewById(R.id.textViewSunrise);
        TextViewSunSet = (TextView) findViewById(R.id.textViewSunset);
        TextViewMaxTemp = (TextView) findViewById(R.id.textViewMaxTemp);
        TextViewMinTemp = (TextView) findViewById(R.id.textViewMinTemp);
        TextViewSucEp = (TextView) findViewById(R.id.textViewPressure);
        TextViewSeaLevel = (TextView) findViewById(R.id.textViewSeaLevel);
        img = (ImageView) findViewById(R.id.imageIcon);
    }
}