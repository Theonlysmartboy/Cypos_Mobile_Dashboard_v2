package com.cybene.cyposdashboard.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cybene.cyposdashboard.R;

import java.util.List;
import java.util.Map;

public class ComputerSalesAdapter extends RecyclerView.Adapter<ComputerSalesAdapter.ViewHolder> {

    private final Context context;
    private final List<Map<String, String>> computerSales;
    private final DashboardAdapter.OnItemClickListener listener;

    public ComputerSalesAdapter(Context context, List<Map<String, String>> computerSales,
                                DashboardAdapter.OnItemClickListener listener) {
        this.context = context;
        this.computerSales = computerSales;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_computer_sale, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> item = computerSales.get(position);
        holder.computerName.setText(item.get("ComputerName"));
        holder.totalSales.setText(item.get("TotalSales"));

        // Trigger click callback when a row is clicked
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(item.get("ComputerName"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return computerSales.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView computerName, totalSales;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            computerName = itemView.findViewById(R.id.tvComputerName);
            totalSales = itemView.findViewById(R.id.tvTotalSales);
        }
    }
}
