package com.aiton.zjb.signal.model;

/**
 * Created by Administrator on 2017/1/17.
 */
public class VideoInfo {
    private String servername;
    private String serverip;
    private String serverport;
    private String username;
    private String password;
    private String rebarks;

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public String getServerport() {
        return serverport;
    }

    public void setServerport(String serverport) {
        this.serverport = serverport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRebarks() {
        return rebarks;
    }

    public void setRebarks(String rebarks) {
        this.rebarks = rebarks;
    }

    public VideoInfo(String servername, String serverip, String serverport, String username, String password, String rebarks) {
        this.servername = servername;
        this.serverip = serverip;
        this.serverport = serverport;
        this.username = username;
        this.password = password;
        this.rebarks = rebarks;
    }
}
