package com.example.todomap;

public class Task {

    String title;
    String type;
    String description;
    String time;
    String address;
    Double latitude;
    Double longitude;
    Integer status;

    public Task(String title, String type, String description, String time,
                String address, Double latitude, Double longitude, Integer status) {

        this.title = title;
        this.type = type;
        this.description = description;
        this.time = time;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;

    }
}