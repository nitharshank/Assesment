package com.itelesoft.test.app.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.database.model.TB_SearchHistory;
import com.itelesoft.test.app.interfaces.listeners.OnHistoryItemClickListener;
import com.itelesoft.test.app.interfaces.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {

    private Activity mContext;
    private List<TB_SearchHistory> mSearchHistoryList;
    private OnHistoryItemClickListener<TB_SearchHistory> mListener;

    public SearchHistoryAdapter(Activity context, List<TB_SearchHistory> searchHistoryList, @NonNull OnHistoryItemClickListener<TB_SearchHistory> listener) {
        this.mContext = context;
        this.mSearchHistoryList = searchHistoryList;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TB_SearchHistory searchHistory = mSearchHistoryList.get(position);

        Log.w("TEST", "----- "+searchHistory.getQueryText());

        if (searchHistory.getQueryText() != null && !searchHistory.getQueryText().isEmpty())
            holder.mSearchHistoryText.setText(searchHistory.getQueryText());
        else holder.mSearchHistoryText.setText(" - ");

    }

    @Override
    public int getItemCount() {
        return mSearchHistoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mSearchHistoryText;

        public MyViewHolder(View view) {
            super(view);

            mSearchHistoryText = view.findViewById(R.id.row_tv_search_history_text);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = SearchHistoryAdapter.MyViewHolder.this.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        mListener.onSearchItemClick(position, mSearchHistoryList.get(position));
                }
            });
        }
    }

    public void setFilter(List<TB_SearchHistory> filteredArticles) {
        mSearchHistoryList = new ArrayList<>();
        mSearchHistoryList.addAll(filteredArticles);
        notifyDataSetChanged();
    }

}
