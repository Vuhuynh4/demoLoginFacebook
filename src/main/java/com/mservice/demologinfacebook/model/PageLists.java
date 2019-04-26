package com.mservice.demologinfacebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PageLists {
    private List<PageInfo> data;

    public PageLists() {
    }

    @JsonProperty("data")
    public List<PageInfo> getData() {
        return data;
    }

    public void setData(List<PageInfo> data) {
        this.data = data;
    }
}
