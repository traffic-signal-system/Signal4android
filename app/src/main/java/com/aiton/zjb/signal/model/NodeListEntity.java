package com.aiton.zjb.signal.model;

import java.io.Serializable;

public class NodeListEntity implements Serializable{
    private int id;
    private int groupId;

    public NodeListEntity(int id, int groupId, String groupSequence, String deviceName, String ipAddress, int port, String deviceVersion, int linkType, int protocolType, double longitude, double latitude, int maintainAppUserId) {
        this.id = id;
        this.groupId = groupId;
        this.groupSequence = groupSequence;
        this.deviceName = deviceName;
        this.ipAddress = ipAddress;
        this.port = port;
        this.deviceVersion = deviceVersion;
        this.linkType = linkType;
        this.protocolType = protocolType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.maintainAppUserId = maintainAppUserId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    private String groupSequence;
    private String deviceName;
    private String ipAddress;
    private int port;
    private String deviceVersion;
    private int linkType;
    private int protocolType;
    private double longitude;
    private double latitude;
    private int maintainAppUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupSequence() {
        return groupSequence;
    }

    public void setGroupSequence(String groupSequence) {
        this.groupSequence = groupSequence;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getMaintainAppUserId() {
        return maintainAppUserId;
    }

    public void setMaintainAppUserId(int maintainAppUserId) {
        this.maintainAppUserId = maintainAppUserId;
    }
}