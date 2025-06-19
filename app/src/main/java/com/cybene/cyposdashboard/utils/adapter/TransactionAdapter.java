package com.cybene.cyposdashboard.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.data.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private final List<Transaction> transactions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title, time, amount;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.transactionIcon);
            title = itemView.findViewById(R.id.transactionTitle);
            time = itemView.findViewById(R.id.transactionTime);
            amount = itemView.findViewById(R.id.transactionAmount);
        }
    }

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.title.setText(transaction.getTitle());
        holder.time.setText(transaction.getTime());
        holder.amount.setText(transaction.getFormattedAmount());

        int iconRes = transaction.isSale() ? R.drawable.ic_sale : R.drawable.ic_buy;
        int colorRes = transaction.isSale() ? R.color.green_success : R.color.red_warning;

        holder.icon.setImageResource(iconRes);
        holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), colorRes));
        holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), colorRes));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
