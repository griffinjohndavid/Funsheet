package com.centennialdesigns.funsheet;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFetcher {

    public interface OnCardsReceivedListener {
        void onCardsReceived(List<Card> cards);
        void onErrorReceived(VolleyError error);
    }

    public interface OnLoginSuccessListener {
        void onLoginSuccess(String message);
        void onLoginError(VolleyError error);
    }

    public interface OnRatingSentListener {
        void onRatingSent(String message);
        void onRatingError(VolleyError error);
    }


    private Context mContext;
    public DataFetcher(Context context) {
        mContext = context;
    }

    public void getCards(@Nullable String location, final OnCardsReceivedListener listener) {

        final List<Card> cards = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        double latitude = GPSData.getInstance(mContext).getLatitude();
        double longitude = GPSData.getInstance(mContext).getLongitude();

        String url = "https://funsheet.centennialdesigns.com/activities?lat=" + latitude + "&long=" + longitude;

        // Request a string response from the provided URL.
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Card card = new Card();
                                card.setId(jsonObject.getInt("id"));
                                card.setTitle(jsonObject.getString("name"));
                                card.setDescription(jsonObject.getString("description"));
                                card.setLatitude(jsonObject.getDouble("latitude"));
                                card.setLongitude(jsonObject.getDouble("longitude"));
                                card.setDistance(jsonObject.getDouble("distance"));
                                card.setRating(BigDecimal.valueOf(jsonObject.getDouble("rating")).floatValue());
                                card.setReviewCount(jsonObject.getInt("reviewCount"));
                                String tagsArray = jsonObject.getString("tags");
                                List<String> tags = new ArrayList<>();
                                String[] array = tagsArray.split("(,\\s)");
                                for (String tag : array) {
                                    tags.add(tag);
                                }
                                card.setTags(tags);
                                card.setPrice(jsonObject.getInt("price"));
                                cards.add(card);
                            }

                            listener.onCardsReceived(cards);
                        }
                        catch (Exception ex) {
                            Log.d("Error", "Error: " + ex.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorReceived(error);
            }
        });
        queue.add(jsonRequest);
    }

    public void login(final String username, final String password, final OnLoginSuccessListener listener) {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://funsheet.centennialdesigns.com/login";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            listener.onLoginSuccess(response);
                        }
                        catch (Exception ex) {
                            Log.d("Error", "Error: " + ex.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onLoginError(error);
            }
        }) {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void sendRating(final String username, final float rating, final int cardId, final OnRatingSentListener listener) {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://funsheet.centennialdesigns.com/rateactivity";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            listener.onRatingSent(response);
                        }
                        catch (Exception ex) {
                            Log.d("Error", "Error: " + ex.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRatingError(error);
            }
        }) {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("id", Integer.toString(cardId));
                params.put("rating", Float.toString(rating));
                return params;
            }
        };
        queue.add(stringRequest);
    }


}
