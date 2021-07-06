package com.rm.rmjbm.model.documentlov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PhyInvNos {

    @SerializedName("item")
    @Expose
    private List<String> item = null;

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }

}