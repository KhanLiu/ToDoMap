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

    public Task(Integer _id, String title, String type, String description, String time,
                String address, Double latitude, Double longitude) {

        this._id = _id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.time = time;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Integer get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}