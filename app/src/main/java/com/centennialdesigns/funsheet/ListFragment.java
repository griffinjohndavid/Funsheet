package com.centennialdesigns.funsheet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListFragment extends Fragment {
    public interface OnCardSelectedListener {
        void onCardSelected(Card card);
    }

    private List<Card> mCards = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private OnCardSelectedListener mListener;

    public static ListFragment newInstance(int id) {

        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("cardID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getCards(mRecyclerView, null);
    }

    public void getCards(final RecyclerView recyclerView, @Nullable final String location) {

        if (Utility.isNetworkAvailable(getContext())) {
            DataFetcher fetcher = new DataFetcher(getContext());
            fetcher.getCards(location, new DataFetcher.OnCardsReceivedListener() {
                @Override
                public void onCardsReceived(List<Card> newCards) {

                    if(recyclerView.getAdapter() == null) {
                        CardAdapter adapter = new CardAdapter(newCards);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        CardAdapter cardAdapter = (CardAdapter) mRecyclerView.getAdapter();
                        cardAdapter.setCards(newCards);
                        cardAdapter.notifyDataSetChanged();
                        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeRefreshLayout);

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onErrorReceived(VolleyError error) {

                }
            });
        } else {

        }

    }

    public Card getRandomCard(RecyclerView recyclerView){
        if(recyclerView.getAdapter() == null)
            return null;
        CardAdapter adapter = (CardAdapter) recyclerView.getAdapter();
        List<Card> cards = adapter.getCards();
        int rand = new Random().nextInt(cards.size());
        return cards.get(rand);
    }

    private class CardHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Card mCard;

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private RatingBar mRatingBar;
        private TextView mTagsTextView;
        private TextView mDistanceView;

        public CardHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_card, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.decriptionTextView);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            mTagsTextView = (TextView) itemView.findViewById(R.id.tagsTextView);
            mDistanceView = (TextView) itemView.findViewById(R.id.distanceTextView);
        }

        public void bind(Card card) {
            mCard = card;

            mTitleTextView.setText(mCard.getTitle());
            mDescriptionTextView.setText(Html.fromHtml(mCard.getDescription()));
            Linkify.addLinks(mDescriptionTextView, Linkify.WEB_URLS);

            mRatingBar.setRating(mCard.getRating());

            mTagsTextView.setText(mCard.getTags());
            mDistanceView.setText(mCard.getDistance());
        }

        @Override
        public void onClick(View view) {
            // Tell MainActivity what card was clicked
            mListener.onCardSelected(mCard);
        }
    }

    public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        private List<Card> mCards;

        public CardAdapter(List<Card> cards) {
            mCards = cards;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CardHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            Card card = mCards.get(position);
            holder.bind(card);
        }

        @Override
        public int getItemCount() {
            return mCards.size();
        }

        public void setCards(List<Card> cards) {
            mCards = cards;
        }
        public void clearCards(){
            mCards.clear();
        }
        public final List<Card> getCards(){
            return mCards;
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnCardSelectedListener) {
            mListener = (OnCardSelectedListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnCardSelectedListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }
}
