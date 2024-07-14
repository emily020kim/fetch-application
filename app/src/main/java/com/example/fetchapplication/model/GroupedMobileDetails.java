package com.example.fetchapplication.model;

import java.util.List;

public class GroupedMobileDetails {
    private int listId;
    private List<MobileDetails> mobileDetailsList;

    public GroupedMobileDetails(int listId, List<MobileDetails> mobileDetailsList) {
        this.listId = listId;
        this.mobileDetailsList = mobileDetailsList;
    }

    public int getListId() {
        return listId;
    }

    public List<MobileDetails> getMobileDetailsList() {
        return mobileDetailsList;
    }
}
