package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class VatCodeWiseSalesGrid {
    @SerializedName("code")
    private String code;
    @SerializedName("sales")
    private String sales;

    public String getCode() {
        return code;
    }

    public String getSales() {
        return sales;
    }
}
