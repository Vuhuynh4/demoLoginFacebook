package com.mservice.demologinfacebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PageInfo extends BaseInfo{

    private List<String> tasks;

    @JsonProperty("tasks")
    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
}
