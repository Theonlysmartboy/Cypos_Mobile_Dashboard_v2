package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class CashSalePaymentModeGrid {
    @SerializedName("mode")
    private String mode;
    @SerializedName("amount")
    private String amount;

    public String getMode() {
        return mode;
    }

    public String getAmount() {
        return amount;
    }
}
