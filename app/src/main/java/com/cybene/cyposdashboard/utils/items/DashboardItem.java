package com.cybene.cyposdashboard.utils.items;

import java.util.HashMap;
import java.util.Map;

public class DashboardItem {
    private final String title;
    private final String amount;
    private final int colorRes;
    private final boolean isLargeCard;
    private final Map<String, String> extras = new HashMap<>();

    // For small cards
    public DashboardItem(String title, String amount, int colorRes) {
        this.title = title;
        this.amount = amount;
        this.colorRes = colorRes;
        this.isLargeCard = false;
    }

    // For large card
    public DashboardItem(String title, String amount, int colorRes, boolean isLargeCard) {
        this.title = title;
        this.amount = amount;
        this.colorRes = colorRes;
        this.isLargeCard = isLargeCard;
    }

    public String getTitle() { return title; }
    public String getAmount() { return amount; }
    public int getColorRes() { return colorRes; }
    public boolean isLargeCard() { return isLargeCard; }

    public void setExtra(String key, String value) {
        extras.put(key, value);
    }

    public String getExtra(String key) {
        return extras.getOrDefault(key, "0.00");
    }
}
