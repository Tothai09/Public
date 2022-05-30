package com.example.weatherapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.example.weatherapp.adapter.Adapter_5days;
import com.example.weatherapp.adapter.WeatherAdapter;
import com.example.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static final String API_KEY = "d88a1bd56ada022b563a4d29990af41f";
    EditText edtCity;
    Button btnOK,btnStep;
    ImageView imgTT;
    TextView txtNameTP,txtNameQG,txtNhietdo,txtTT,txtCloud,txtWind,txtSteam,txtDate;
    String city ="";
    String cityIntent = "";
    WeatherAdapter weatherAdapter5days;
    ListView lvWeather5days;
    List<Weather> list1 = new ArrayList<>();
    RecyclerView recyclerView;
    Adapter_5days adapter_5days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        //lvWeather5days.setNestedScrollingEnabled(false);//tắt thanh trượt
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setNestedScrollingEnabled(false);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = edtCity.getText().toString().trim();
                if (city.equals("")){
                    city ="hanoi";
                }
                getJsonWeather(city);
                getJsonWeather5days(city);
            }
        });

        if (city == ""){
            getJsonWeather("hanoi");
            getJsonWeather5days("hanoi");
        }else{
            getJsonWeather(city);
            getJsonWeather5days(city);
        }

        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TempNextDay.class);
                intent.putExtra("city",cityIntent.toLowerCase(Locale.ROOT));
                startActivity(intent);
            }
        });

        //5days/3hour



    }

    public void getJsonWeather(String city){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY+"&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);

                            String icon = weatherObj.getString("icon");
                            String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";
                            Picasso.get().load(urlIcon).into(imgTT);
                            String temperState = weatherObj.getString("main");
                            txtTT.setText(temperState);

                            JSONObject main = response.getJSONObject("main");
                            String temp = main.getString("temp");//nhiet do
                            txtNhietdo.setText(temp+"°C");
                            String humidity = main.getString("humidity");//độ ẩm
                            txtSteam.setText(humidity+"%");

                            JSONObject wind = response.getJSONObject("wind");
                            String windSpeed = wind.getString("speed");//tốc độ gió
                            txtWind.setText(windSpeed+" m/s");

                            JSONObject cloud = response.getJSONObject("clouds");
                            String all = cloud.getString("all");// % mây
                            txtCloud.setText(all +" %");

                            String sNgay = response.getString("dt");
                            long lNgay = Long.parseLong(sNgay);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,dd-MM-yyyy HH:mm:ss");
                            Date date = new Date(lNgay*1000);
                            String currentTime = dateFormat.format(date);//ngày giờ cập nhật
                            txtDate.setText(currentTime);

                            String name = response.getString("name");//thành phố
                            cityIntent = name;
                            txtNameTP.setText("Thành phố "+name);

                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");//Quốc gia
                            txtNameQG.setText("Quốc gia "+country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố "+city, Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    public void getJsonWeather5days(String city){
        String url1 = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid="+API_KEY;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listData = response.getJSONArray("list");
                            for (int i = 0; i < listData.length(); i++) {
                                JSONObject weatherObj = listData.getJSONObject(i);

                                String sNgay = weatherObj.getString("dt_txt");
//                                long lNgay = Long.parseLong(sNgay);
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("   EEEE\ndd-MM-yyyy");
//                                Date date = new Date(lNgay*1000);
//                                String currentTime = dateFormat.format(date);//ngày giờ cập nhật

                                JSONObject main = weatherObj.getJSONObject("main");
                                String tempMax = main.getString("temp_max");// nhiệt độ max
                                String tempMin = main.getString("temp_min");// nhiệt độ min

                                JSONArray weatherArray = weatherObj.getJSONArray("weather");
                                JSONObject object = weatherArray.getJSONObject(0);
                                String icon = object.getString("icon");
                                String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";//icon thời tiết
                                String stateWeather = object.getString("description");//tt thời tiết


                                list1.add(new Weather(sNgay,stateWeather,tempMax,tempMin,urlIcon));
                                adapter_5days = new Adapter_5days(list1,MainActivity.this);
                                recyclerView.setAdapter(adapter_5days);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố "+city, Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void mapping() {
        edtCity = findViewById(R.id.edtCity);
        btnOK = findViewById(R.id.btnOK);
        btnStep = findViewById(R.id.btnStep);
        txtNameTP = findViewById(R.id.txtNameTP);
        txtNameQG = findViewById(R.id.txtNameQG);
        txtNhietdo = findViewById(R.id.txtNhietdo);
        txtTT = findViewById(R.id.txtTT);
        txtCloud = findViewById(R.id.txtCloud);
        txtWind = findViewById(R.id.txtWind);
        txtSteam = findViewById(R.id.txtSteam);
        txtDate = findViewById(R.id.txtDate);
        imgTT = findViewById(R.id.imgTT);
        recyclerView = findViewById(R.id.lvWeather5days);
    }
}