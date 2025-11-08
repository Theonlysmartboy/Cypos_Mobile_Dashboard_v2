package com.cybene.cyposdashboard.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.items.DashboardItem;

import java.util.List;
import java.util.Map;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LARGE = 0;
    private static final int TYPE_SMALL = 1;
    private static final int TYPE_COMPUTER = 2;

    private final Context context;
    private final List<DashboardItem> items;
    private final OnItemClickListener listener;

    // Listener interface
    public interface OnItemClickListener {
        void onItemClicked(String title);
    }

    public DashboardAdapter(Context context, List<DashboardItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        DashboardItem item = items.get(position);
        if (item.getSubList() != null && !item.getSubList().isEmpty()) {
            return TYPE_COMPUTER;
        }
        return item.isLargeCard() ? TYPE_LARGE : TYPE_SMALL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LARGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_large, parent, false);
            return new LargeCardViewHolder(view);
        } else if (viewType == TYPE_COMPUTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_computer_sales, parent, false);
            return new ComputerSalesViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_card, parent, false);
            return new SmallCardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DashboardItem item = items.get(position);

        if (item.getTitle().equals("Loading...")) {
            View shimmerView = holder.itemView.findViewById(R.id.shimmerBackground);
            shimmerView.setVisibility(View.VISIBLE);
            Animation shimmer = AnimationUtils.loadAnimation(context, R.anim.shimmer_translate);
            shimmerView.startAnimation(shimmer);
        } else {
            View shimmerView = holder.itemView.findViewById(R.id.shimmerBackground);
            shimmerView.clearAnimation();
            shimmerView.setVisibility(View.GONE);
        }

        if (holder instanceof LargeCardViewHolder) {
            ((LargeCardViewHolder) holder).bind(item);
        } else if (holder instanceof ComputerSalesViewHolder) {
            ((ComputerSalesViewHolder) holder).bind(item, context);
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
    class LargeCardViewHolder extends RecyclerView.ViewHolder {
        TextView title, totalSales, totalCash, totalMpesa, totalCreditCard, totalCheque;
        RecyclerView rvComputerSales;

        LargeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.largeTitle);
            totalSales = itemView.findViewById(R.id.totalSalesAmount);
            totalCash = itemView.findViewById(R.id.totalCashSalesAmount);
            totalMpesa = itemView.findViewById(R.id.totalMpesaAmount);
            totalCreditCard = itemView.findViewById(R.id.totalCreditCardAmount);
            totalCheque = itemView.findViewById(R.id.totalChequeAmount);
            rvComputerSales = itemView.findViewById(R.id.rvComputerSales);
        }

        void bind(DashboardItem item) {
            title.setText(item.getTitle());
            totalSales.setText(item.getExtra("total"));
            totalCash.setText(item.getExtra("cash"));
            totalMpesa.setText(item.getExtra("mpesa"));
            totalCheque.setText(item.getExtra("cheque"));
            totalCreditCard.setText(item.getExtra("card"));

            // Click listeners for each type
            totalCash.setOnClickListener(v -> listener.onItemClicked("Cash Sales"));
            totalMpesa.setOnClickListener(v -> listener.onItemClicked("MPESA Sales"));
            totalCheque.setOnClickListener(v -> listener.onItemClicked("Cheque Sales"));
            totalCreditCard.setOnClickListener(v -> listener.onItemClicked("Card Sales"));
            totalSales.setOnClickListener(v -> listener.onItemClicked("Total Sales"));

            // show nested list if available
            if (item.getSubList() != null && !item.getSubList().isEmpty()) {
                rvComputerSales.setVisibility(View.VISIBLE);
                rvComputerSales.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                rvComputerSales.setAdapter(new ComputerSalesAdapter(itemView.getContext(), item.getSubList(), listener));
            } else {
                rvComputerSales.setVisibility(View.GONE);
            }
        }
    }

    // ----------------------------------------------------------
    // SALES LARGE CARD VIEW HOLDER
    // ----------------------------------------------------------
    class ComputerSalesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView rvComputerSales;

        ComputerSalesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.largeTitle);
            rvComputerSales = itemView.findViewById(R.id.rvComputerSales);
        }

        void bind(DashboardItem item, Context context) {
            title.setText(item.getTitle());

            List<Map<String, String>> subList = item.getSubList();
            if (subList != null && !subList.isEmpty()) {
                rvComputerSales.setVisibility(View.VISIBLE);
                rvComputerSales.setLayoutManager(new LinearLayoutManager(context));
                rvComputerSales.setAdapter(new ComputerSalesAdapter(context, subList, listener));
            } else {
                rvComputerSales.setVisibility(View.GONE);
            }
        }
    }

    // ----------------------------------------------------------
    // SMALL CARD VIEW HOLDER
    // ----------------------------------------------------------
    class SmallCardViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;

        SmallCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            amount = itemView.findViewById(R.id.cardAmount);
        }

        void bind(DashboardItem item) {
            title.setText(item.getTitle());
            amount.setText(item.getAmount());
            amount.setTextColor(ContextCompat.getColor(itemView.getContext(), item.getColorRes()));

            itemView.setOnClickListener(v -> listener.onItemClicked(item.getTitle()));
        }
    }
}
