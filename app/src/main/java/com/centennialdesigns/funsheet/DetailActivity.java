package com.centennialdesigns.funsheet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity
    implements RatingBar.OnRatingBarChangeListener,
    DataFetcher.OnRatingSentListener{

    public static final String PARCEL_ID = "parcel_id";
    private Card mCard;
    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton navigateButton = (FloatingActionButton) findViewById(R.id.fab);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent();
                mapIntent.setAction(Intent.ACTION_VIEW);
                String uriString = "geo:0,0?q="
                        + mCard.getLatitude() + "," + mCard.getLongitude()
                        + "(" + mCard.getTitle() + ")";
                mapIntent.setData(Uri.parse(uriString));
                startActivity(mapIntent);
            }
        });

        mCard = getIntent().getExtras().getParcelable(PARCEL_ID);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(mCard.getTitle());

        String price = "";
        if (mCard.getPrice() > 0) {
            price = new String(new char[mCard.getPrice()]).replace("\0", "$");
        } else {
            price = "Free";
        }

        TextView distanceTextView = (TextView) findViewById(R.id.detail_distance);
        distanceTextView.setText(mCard.getDistance());
        TextView priceTextView = (TextView) findViewById(R.id.detail_price);
        priceTextView.setText(price);
        TextView tagsTextView = (TextView) findViewById(R.id.detail_tags);
        tagsTextView.setText(mCard.getTags());
        TextView descriptionTextView = (TextView) findViewById(R.id.location_description);
        descriptionTextView.setText(mCard.getDescription());
        ImageView background = (ImageView) findViewById(R.id.toolbarBG);
        Picasso.with(getApplicationContext()).load("https://funsheet.centennialdesigns.com/img/" + mCard.getId() + ".jpg").into(background);

        mRatingBar = (RatingBar) findViewById(R.id.detail_rating);
        mRatingBar.setRating(mCard.getRating());
        mRatingBar.setOnRatingBarChangeListener(this);
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(!fromUser)
            return;

        //ratingBar.setRating(rating);
        //ratingBar.setIsIndicator(true);
        DataFetcher dataFetcher = new DataFetcher(this);

        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREF_NAME, 0);
        String user = prefs.getString(LoginActivity.USER_PREF_ID, "");

        dataFetcher.sendRating(user, rating, mCard.getId(), this);
    }

    @Override
    public void onRatingSent(String message) {
        if(message.equals("SUCCESS_REVIEW_SAVED")){
            mRatingBar.setIsIndicator(true);
            Toast.makeText(this, "Rating successfully sent", Toast.LENGTH_LONG).show();
        }
        else if(message.equals("ERROR_NOT_ADDED")){
            Toast.makeText(this, "Server Error: Rating not added", Toast.LENGTH_LONG).show();
            Log.d("Rating Error", message);
        }
    }

    @Override
    public void onRatingError(VolleyError error) {
        Toast.makeText(this, "Network Error: Rating not sent", Toast.LENGTH_LONG).show();
        Log.d("Rating Error", error.toString());
    }
}
