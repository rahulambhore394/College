package com.developer_rahul.agrideal.HomeFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer_rahul.agrideal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView tvWeather, tvCity, tvDate;
    private ImageView imgWeather;
    private CardView weatherCard;
    private FusedLocationProviderClient fusedLocationClient;

    private final String API_KEY = "dab3af44de7d24ae7ff86549334e45bd";  // Replace with your OpenWeather API Key

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI elements
        tvWeather = view.findViewById(R.id.tvTemperature);
        tvCity = view.findViewById(R.id.tvCity);
        tvDate = view.findViewById(R.id.tvDate);
        imgWeather = view.findViewById(R.id.imgWeather);
        weatherCard = view.findViewById(R.id.card_weather);

        // Initialize Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Set up Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
                activity.getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            }

            // Update custom action bar dynamically
            View customView = activity.getSupportActionBar().getCustomView();
            ImageView profileImage = customView.findViewById(R.id.user_photo_profile);
            TextView userName = customView.findViewById(R.id.tv_name);

            // Set data dynamically
            profileImage.setImageResource(R.drawable.profile_icon2); // Replace with actual dynamic image
            userName.setText("Rahul"); // Replace with actual username
        }

        // Fetch weather and location automatically
        getCurrentLocation();
        setCurrentDate();

        return view;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String cityName = getCityName(latitude, longitude);
                    getWeather(latitude, longitude, cityName);
                } else {
                    Toast.makeText(getActivity(), "Unable to get location!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getCityName(double latitude, double longitude) {
        String cityName = "Unknown Location";
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeather(double latitude, double longitude, String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject main = jsonObject.getJSONObject("main");
                            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

                            double temp = main.getDouble("temp");
                            String temperature = String.format(Locale.US, "%.1f°C", temp);
                            String description = weather.getString("main");

                            tvCity.setText(cityName);
                            tvWeather.setText(temperature);

                            // Set custom weather icon
                            setWeatherIcon(description);

                            // Change background color based on weather
                            setWeatherBackground(description);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error parsing data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Weather data not available!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.add(stringRequest);
    }

    private void setWeatherIcon(String description) {
        switch (description.toLowerCase()) {
            case "clear":
            case "clear sky":
                imgWeather.setImageResource(R.drawable.icon_sun);
                break;
            case "clouds":
            case "few clouds":
            case "scattered clouds":
                imgWeather.setImageResource(R.drawable.icon_clouds);
                break;
            case "rain":
            case "shower rain":
                imgWeather.setImageResource(R.drawable.icon_rain);
                break;
            case "thunderstorm":
                imgWeather.setImageResource(R.drawable.icon_thunderstrom);
                break;
            case "snow":
                imgWeather.setImageResource(R.drawable.icon_snow);
                break;
            default:
                imgWeather.setImageResource(R.drawable.icon_default);
                break;
        }
    }

    private void setWeatherBackground(String description) {
        switch (description.toLowerCase()) {
            case "clear":
            case "clear sky":
                weatherCard.setBackgroundResource(R.drawable.bg_sunny);
                break;
            case "clouds":
            case "few clouds":
            case "scattered clouds":
                weatherCard.setBackgroundResource(R.drawable.bg_cloudy);
                break;
            case "rain":
            case "shower rain":
                weatherCard.setBackgroundResource(R.drawable.bg_rainy);
                break;
            default:
                weatherCard.setBackgroundResource(R.drawable.bg_default);
                break;
        }
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        tvDate.setText(currentDate);
    }
}
