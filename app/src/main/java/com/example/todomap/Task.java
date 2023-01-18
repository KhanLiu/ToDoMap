package com.example.todomap;

public class Task {

    Integer _id;
    String title;
    String type;
    String description;
    String time;
    String address;
    Double latitude;
    Double longitude;
    Integer status;

    public Task(Integer _id, String title, String type, String description, String time,
                String address, Double latitude, Double longitude, Integer status) {

        this._id = _id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.time = time;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;

    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer get_id() {
        return _id;
    }

    public Integer getStatus() {
        return status;
    }
}