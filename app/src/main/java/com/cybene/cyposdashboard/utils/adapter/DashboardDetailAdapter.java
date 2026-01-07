package com.cybene.cyposdashboard.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.items.DashboardDetailItem;

import java.util.List;

public class DashboardDetailAdapter extends RecyclerView.Adapter<DashboardDetailAdapter.ViewHolder> {

    private final Context context;
    private List<DashboardDetailItem> items;

    public DashboardDetailAdapter(Context context, List<DashboardDetailItem> items) {
        this.context = context;
        this.items = items;
    }
    public void updateItems(List<DashboardDetailItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardDetailItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.amount.setText(item.getAmount().toString());
        holder.amount.setTextColor(ContextCompat.getColor(context, item.getColorRes()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            amount = itemView.findViewById(R.id.cardAmount);
        }
    }
}
