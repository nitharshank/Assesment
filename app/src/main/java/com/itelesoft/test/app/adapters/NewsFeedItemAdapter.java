package com.itelesoft.test.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.dtos.response.Article;
import com.itelesoft.test.app.interfaces.listeners.OnItemClickListener;
import com.itelesoft.test.app.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedItemAdapter extends RecyclerView.Adapter<NewsFeedItemAdapter.MyViewHolder> {

    private Activity mContext;
    private List<Article> mArticles;
    private OnItemClickListener<Article> mListener;

    public NewsFeedItemAdapter(Activity context, List<Article> articles, @NonNull OnItemClickListener<Article> listener) {
        this.mContext = context;
        this.mArticles = articles;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news_feed_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article article = mArticles.get(position);

        if (article.getTitle() != null && !article.getTitle().isEmpty())
            holder.mTitle.setText(article.getTitle());
        else holder.mTitle.setText(" - ");

        if (article.getDescription() != null && !article.getDescription().isEmpty())
            holder.mDescription.setText(article.getDescription());
        else holder.mDescription.setText(" - ");

        if (article.getPublishedAt() != null && !article.getPublishedAt().isEmpty())
            holder.mPublishedAt.setText(article.getPublishedAt());
        else holder.mPublishedAt.setText(" - ");

        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {

            Picasso.with(mContext).load(article.getUrlToImage()).transform(new CircleTransform()).into(holder.mThumbnail, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    //if (holder.mProgressBar != null) {
                    holder.mProgressBar.setVisibility(View.GONE);
                    //}
                }

                @Override
                public void onError() {
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            });

        } else {
            // TODO Image url not found-----
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mDescription, mPublishedAt;
        private ImageView mThumbnail;
        private ProgressBar mProgressBar;

        public MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.row_news_feed_item_tv_title);
            mDescription = view.findViewById(R.id.row_news_feed_item_tv_description);
            mPublishedAt = view.findViewById(R.id.row_news_feed_item_tv_date);
            mProgressBar = view.findViewById(R.id.row_news_feed_item_pb_loading);
            mThumbnail = view.findViewById(R.id.row_news_feed_item_iv_thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = NewsFeedItemAdapter.MyViewHolder.this.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        mListener.onItemClick(position, mArticles.get(position));
                }
            });
        }
    }

    public void addNewsFeed(List<Article> articles) {
        for (Article item : articles) {
            mArticles.add(item);
            notifyDataSetChanged(); // TODO--------
        }
    }

    public void setFilter(List<Article> filteredArticles) {
        mArticles = new ArrayList<>();
        mArticles.addAll(filteredArticles);
        notifyDataSetChanged();
    }

}
