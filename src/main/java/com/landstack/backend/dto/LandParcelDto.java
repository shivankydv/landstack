package com.landstack.backend.dto;

public class LandParcelDto {

    private String ulpin;
    private String name;
    private String propertyType;
    private String plotReference;
    private Double area;
    private String areaUnit;
    private String address;
    private Double latitude;
    private Double longitude;
    private boolean isDemoData;

    // Getters and Setters

    public String getUlpin() {
        return ulpin;
    }
    public void setUlpin(String ulpin) {
        this.ulpin = ulpin;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPropertyType() {
        return propertyType;
    }
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    public String getPlotReference() {
        return plotReference;
    }
    public void setPlotReference(String plotReference) {
        this.plotReference = plotReference;
    }
    public Double getArea() {
        return area;
    }
    public void setArea(Double area) {
        this.area = area;
    }
    public String getAreaUnit() {
        return areaUnit;
    }
    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public boolean isDemoData() {
        return isDemoData;
    }
    public void setDemoData(boolean demoData) {
        isDemoData = demoData;
    }
}