package com.centennialdesigns.funsheet;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFetcher {

    final private String mUserKey = "156c3769-5138-4cee-aeef-bfb7e377ae3f";
    final private String mProjectKey = "63e283e0-4d20-4e67-b307-e2dc967ed5e0";

    public interface OnCardsReceivedListener {
        void onCardsReceived(List<Card> cards);
        void onErrorReceived(VolleyError error);
    }

    private Context mContext;
    public DataFetcher(Context context) {
        mContext = context;
    }

    public void getCards(@Nullable String location, final OnCardsReceivedListener listener) {

        final List<Card> cards = new ArrayList<>();
        final TextView mTextView = new TextView(mContext);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://funsheet.centennialdesigns.com/activities?lat=35.2468&long=-91.7337";

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
                                //card.setRating(BigDecimal.valueOf(jsonObject.getDouble("rating")).floatValue());
                                String tagsArray = jsonObject.getString("tags");
                                List<String> tags = new ArrayList<String>();
                                String[] array = tagsArray.split("(,\\s)");
                                for (String tag : array) {
                                    tags.add(tag);
                                }
                                card.setTags(tags);
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
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                params.put("JsonStub-User-Key", mUserKey);
//                params.put("JsonStub-Project-Key", mProjectKey);
//
//                return params;
//            }
//        };
//        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
//                20000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }
}
