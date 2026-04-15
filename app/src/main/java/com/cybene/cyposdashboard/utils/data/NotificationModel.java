package com.cybene.cyposdashboard.utils.data;

public class NotificationModel {
    private final int id;
    private final String serverId;
    private final String title;
    private final String message;
    private final String type;
    private final int isRead;
    private final int isArchived;
    private final String createdAt;
    private final String payload;

    public NotificationModel(int id, String serverId, String title, String message, String type, int isRead, int isArchived, String createdAt, String payload) {
        this.id = id;
        this.serverId = serverId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.isArchived = isArchived;
        this.createdAt = createdAt;
        this.payload = payload;
    }

    public int getId() { return id; }
    public String getServerId() { return serverId; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getType() { return type; }
    public int getIsRead() { return isRead; }
    public int getIsArchived() { return isArchived; }
    public String getCreatedAt() { return createdAt; }
    public String getPayload() { return payload; }
}
