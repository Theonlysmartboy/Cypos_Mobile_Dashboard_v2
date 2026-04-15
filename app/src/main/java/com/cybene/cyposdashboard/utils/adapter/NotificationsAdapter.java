package com.cybene.cyposdashboard.utils.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.data.NotificationModel;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<NotificationModel> notificationList;
    private final OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationModel notification);
        void onNotificationLongClick(NotificationModel notification, View view);
    }

    public NotificationsAdapter(List<NotificationModel> notificationList, OnNotificationClickListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.time.setText(notification.getCreatedAt());

        if (notification.getIsRead() == 0) {
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.unreadIndicator.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onNotificationClick(notification));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onNotificationLongClick(notification, v);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void updateData(List<NotificationModel> newList) {
        this.notificationList = newList;
        notifyDataSetChanged();
    }

    public NotificationModel getItem(int position) {
        return notificationList.get(position);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, time;
        View unreadIndicator;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            message = itemView.findViewById(R.id.notification_message);
            time = itemView.findViewById(R.id.notification_time);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);
        }
    }
}
