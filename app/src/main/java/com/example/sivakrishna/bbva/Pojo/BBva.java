package com.example.sivakrishna.bbva.Pojo;

/**
 * Created by siva krishna on 8/24/2017.
 */




public class BBva {
    String address;
    String lat;
    String lng;
    String icon;
    String name;



    public BBva(String address, String lat, String lng, String icon, String name) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
        this.name = name;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

