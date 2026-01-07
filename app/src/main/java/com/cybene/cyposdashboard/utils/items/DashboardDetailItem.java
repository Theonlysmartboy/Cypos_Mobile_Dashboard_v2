package com.cybene.cyposdashboard.utils.items;

public class DashboardDetailItem {
    private final String title;
    private final Double amount;
    private final int colorRes;

    public DashboardDetailItem(String title, Double amount, int colorRes) {
        this.title = title;
        this.amount = amount;
        this.colorRes = colorRes;
    }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public int getColorRes() { return colorRes; }
}
