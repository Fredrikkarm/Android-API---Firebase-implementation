package com.example.apicallerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherActivity extends AppCompatActivity {

    Button goBackButton, getWeatherButton;
    EditText enterCityField;
    TextView displayWeatherInfo;
    private final String APIKEY = "f5d72ad7d807f64c26c4f56afbbff9a5";
    private final String URL = "http://api.openweathermap.org/data/2.5/weather";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        goBackButton = (Button) findViewById(R.id.goBackButton);
        getWeatherButton = (Button) findViewById(R.id.getWeatherBtn);
        enterCityField = (EditText) findViewById(R.id.enterCity);
        displayWeatherInfo = (TextView) findViewById(R.id.weatherTV);

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempUrl = "";
                String city = enterCityField.getText().toString().trim();
                if(city.isEmpty()){
                    enterCityField.setError("Enter a city to get details");
                    enterCityField.requestFocus();
                    return;
                }
                tempUrl = URL + "?q=" + city + ",&appid=" + APIKEY;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("URLResponse", response);
                        String output = "";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            String cityName = jsonResponse.getString("name");

                            output += " Weather in " + cityName +
                                    "\n Temperature: " + df.format(temp) + "C" +
                                    "\n Wind: " + wind + "m/s" +
                                    "\n Description: " + description;
                            displayWeatherInfo.setText(output);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("URLResponse", error.toString());
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeatherActivity.this, LoggedInActivity.class));
            }
        });
    }
}