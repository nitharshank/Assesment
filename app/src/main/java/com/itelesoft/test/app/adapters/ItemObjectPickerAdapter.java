package com.itelesoft.test.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.itelesoft.test.app.R;
import com.itelesoft.test.app.dtos.ItemObject;

import java.util.ArrayList;
import java.util.List;

public class ItemObjectPickerAdapter extends RecyclerView.Adapter<ItemObjectPickerAdapter.MyViewHolder> implements Filterable {

    private List<ItemObject> itemObjectList;
    private List<ItemObject> filteredList;
    private ItemObjectPickerAdapter.CustomFilter mFilter;
    private OnClickListener clickListener;

    public ItemObjectPickerAdapter(List<ItemObject> itemObjectList, OnClickListener clickListener) {
        this.itemObjectList = itemObjectList;
        this.filteredList = new ArrayList<>();
        this.clickListener = clickListener;
        filteredList.addAll(itemObjectList);
        mFilter = new ItemObjectPickerAdapter.CustomFilter(ItemObjectPickerAdapter.this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_item_object, parent, false);

        return new ItemObjectPickerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ItemObject selectedItemObject = filteredList.get(position);
        holder.tvName.setText(selectedItemObject.getName());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public MyViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.row_item_item_object_tv_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = MyViewHolder.this.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onClicked(filteredList.get(position));
                    }
                }
            });
        }
    }

    public class CustomFilter extends Filter {
        private ItemObjectPickerAdapter mAdapter;

        private CustomFilter(ItemObjectPickerAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(itemObjectList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final ItemObject mWords : itemObjectList) {
                    if (mWords.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public interface OnClickListener {
        void onClicked(ItemObject itemObject);
    }

}