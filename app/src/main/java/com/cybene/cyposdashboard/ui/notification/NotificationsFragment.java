package com.cybene.cyposdashboard.ui.notification;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.NotificationsAdapter;
import com.cybene.cyposdashboard.utils.data.NotificationModel;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.interfaces.AddOrRemoveCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsFragment extends Fragment implements NotificationsAdapter.OnNotificationClickListener {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private final List<NotificationModel> notificationList = new ArrayList<>();
    private Db db;
    private LinearLayout emptyState;
    private AddOrRemoveCallbacks callbacks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.rv_notifications);
        emptyState = view.findViewById(R.id.empty_state);
        db = new Db(getContext());

        if (getActivity() instanceof AddOrRemoveCallbacks) {
            callbacks = (AddOrRemoveCallbacks) getActivity();
        }

        setupRecyclerView();
        loadNotifications();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new NotificationsAdapter(notificationList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NotificationModel notification = adapter.getItem(position);

                if (direction == ItemTouchHelper.LEFT) {
                    // Delete
                    db.deleteNotification(notification.getId());
                    Toast.makeText(getContext(), "Notification deleted", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Archive
                    db.archiveNotification(notification.getId());
                    Toast.makeText(getContext(), "Notification archived", Toast.LENGTH_SHORT).show();
                }

                loadNotifications();
                if (callbacks != null) {
                    callbacks.onReadNotification(); // Trigger badge update
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadNotifications() {
        notificationList.clear();
        Cursor cursor = db.getAllNotifications();
        if (cursor != null && cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex("id");
            int serverIdIdx = cursor.getColumnIndex("server_id");
            int titleIdx = cursor.getColumnIndex("title");
            int messageIdx = cursor.getColumnIndex("message");
            int typeIdx = cursor.getColumnIndex("type");
            int isReadIdx = cursor.getColumnIndex("is_read");
            int isArchivedIdx = cursor.getColumnIndex("is_archived");
            int createdAtIdx = cursor.getColumnIndex("created_at");
            int payloadIdx = cursor.getColumnIndex("payload");

            do {
                notificationList.add(new NotificationModel(
                        idIdx != -1 ? cursor.getInt(idIdx) : 0,
                        serverIdIdx != -1 ? cursor.getString(serverIdIdx) : "",
                        titleIdx != -1 ? cursor.getString(titleIdx) : "",
                        messageIdx != -1 ? cursor.getString(messageIdx) : "",
                        typeIdx != -1 ? cursor.getString(typeIdx) : "",
                        isReadIdx != -1 ? cursor.getInt(isReadIdx) : 0,
                        isArchivedIdx != -1 ? cursor.getInt(isArchivedIdx) : 0,
                        createdAtIdx != -1 ? cursor.getString(createdAtIdx) : "",
                        payloadIdx != -1 ? cursor.getString(payloadIdx) : ""
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter.updateData(notificationList);

        if (notificationList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNotificationClick(NotificationModel notification) {
        db.markAsRead(notification.getId());
        loadNotifications();
        if (callbacks != null) {
            callbacks.onReadNotification();
        }
        
        // Navigation to details would go here
        // For now, just a toast or simple dialog to show it's working
        Toast.makeText(getContext(), "Clicked: " + notification.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotificationLongClick(NotificationModel notification, View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenu().add("Mark as read");
        popup.getMenu().add("Archive");
        popup.getMenu().add("Delete");

        popup.setOnMenuItemClickListener(item -> {
            if (Objects.equals(item.getTitle(), "Mark as read")) {
                db.markAsRead(notification.getId());
            } else if (Objects.equals(item.getTitle(), "Archive")) {
                db.archiveNotification(notification.getId());
            } else if (Objects.equals(item.getTitle(), "Delete")) {
                db.deleteNotification(notification.getId());
            }
            loadNotifications();
            if (callbacks != null) {
                callbacks.onReadNotification();
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifications();
    }
}
