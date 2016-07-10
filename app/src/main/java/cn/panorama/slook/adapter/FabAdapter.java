package cn.panorama.slook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.ui.R;


public class FabAdapter extends RecyclerView.Adapter<FabAdapter.ViewHolder> {

    private ClickListener mListener;
    private List<MenuItem> mItems;

    public FabAdapter(ClickListener listener) {
        mItems = new ArrayList<>();
        mListener = listener;
    }

    public void setClickListener(ClickListener listener) {
        mListener = listener;
    }

    public void addItem(MenuItem item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fab_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MenuItem item;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void setData(MenuItem item) {
            this.item = item;
            textView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onAdapterItemClick(item);
            }
        }
    }

    public interface ClickListener {
        void onAdapterItemClick(MenuItem item);
    }
}
