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
import com.cybene.cyposdashboard.utils.items.DashboardItem;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LARGE = 0;
    private static final int TYPE_SMALL = 1;

    private final Context context;
    private final List<DashboardItem> items;

    public DashboardAdapter(Context context, List<DashboardItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isLargeCard() ? TYPE_LARGE : TYPE_SMALL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LARGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_large, parent, false);
            return new LargeCardViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_card, parent, false);
            return new SmallCardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DashboardItem item = items.get(position);
        if (holder instanceof LargeCardViewHolder) {
            ((LargeCardViewHolder) holder).bind(item);
        } else if (holder instanceof SmallCardViewHolder) {
            ((SmallCardViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ----------------------------------------------------------
    // LARGE CARD VIEW HOLDER (handles 5 amounts)
    // ----------------------------------------------------------
    static class LargeCardViewHolder extends RecyclerView.ViewHolder {
        TextView title, totalSales, totalCash, totalMpesa, totalCreditCard, totalCheque;

        LargeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.largeTitle);
            totalSales = itemView.findViewById(R.id.totalSalesAmount);
            totalCash = itemView.findViewById(R.id.totalCashSalesAmount);
            totalMpesa = itemView.findViewById(R.id.totalMpesaAmount);
            totalCreditCard = itemView.findViewById(R.id.totalCreditCardAmount);
            totalCheque = itemView.findViewById(R.id.totalChequeAmount);
        }

        void bind(DashboardItem item) {
            title.setText(item.getTitle());

            totalSales.setText("Total Sales: " + item.getExtra("total"));
            totalCash.setText("Cash: " + item.getExtra("cash"));
            totalMpesa.setText("Mpesa: " + item.getExtra("mpesa"));
            totalCreditCard.setText("Credit: " + item.getExtra("credit"));
            totalCheque.setText("Expenses: " + item.getExtra("expenses"));
        }
    }

    // ----------------------------------------------------------
    // SMALL CARD VIEW HOLDER
    // ----------------------------------------------------------
    static class SmallCardViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;

        SmallCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            amount = itemView.findViewById(R.id.cardAmount);
        }

        void bind(DashboardItem item) {
            title.setText(item.getTitle());
            amount.setText(item.getExtra("amount")); // updated to use getExtra
            amount.setTextColor(ContextCompat.getColor(itemView.getContext(), item.getColorRes()));
        }
    }
}
