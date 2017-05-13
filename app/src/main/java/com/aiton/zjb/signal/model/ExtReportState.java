package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

public class ExtReportState implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String workModel;
    private String workStatus;
    private String controlModel;
    private int currentSchedule;
    private int currentTimePattern;
    private int currentStagePattern;
    private int stageCount;
    private int currentStage;
    private int stageTotalTime;
    private int stageRunTime;
    private List<Integer> listChannelRedStatus;
    private List<Integer> listChannelYellowStatus;
    private List<Integer> listChannelGreenStatus;
    private List<Integer> listChannelFlashStatus;
    private int cycleTime;
    private Boolean resportTscStatusFlag = false;
    private int basetime;

    public int getBasetime() {
        return basetime;
    }

    public void setBasetime(int basetime) {
        this.basetime = basetime;
    }

    public String getWorkModel() {
        return workModel;
    }

    public void setWorkModel(String workModel) {
        this.workModel = workModel;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getControlModel() {
        return controlModel;
    }

    public void setControlModel(String controlModel) {
        this.controlModel = controlModel;
    }

    public int getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(int currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public int getCurrentTimePattern() {
        return currentTimePattern;
    }

    public void setCurrentTimePattern(int currentTimePattern) {
        this.currentTimePattern = currentTimePattern;
    }

    public int getCurrentStagePattern() {
        return currentStagePattern;
    }

    public void setCurrentStagePattern(int currentStagePattern) {
        this.currentStagePattern = currentStagePattern;
    }

    public int getStageCount() {
        return stageCount;
    }

    public void setStageCount(int stageCount) {
        this.stageCount = stageCount;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getStageTotalTime() {
        return stageTotalTime;
    }

    public void setStageTotalTime(int stageTotalTime) {
        this.stageTotalTime = stageTotalTime;
    }

    public int getStageRunTime() {
        return stageRunTime;
    }

    public void setStageRunTime(int stageRunTime) {
        this.stageRunTime = stageRunTime;
    }

    public List<Integer> getListChannelRedStatus() {
        return listChannelRedStatus;
    }

    public void setListChannelRedStatus(List<Integer> listChannelRedStatus) {
        this.listChannelRedStatus = listChannelRedStatus;
    }

    public List<Integer> getListChannelYellowStatus() {
        return listChannelYellowStatus;
    }

    public void setListChannelYellowStatus(List<Integer> listChannelYellowStatus) {
        this.listChannelYellowStatus = listChannelYellowStatus;
    }

    public List<Integer> getListChannelGreenStatus() {
        return listChannelGreenStatus;
    }

    public void setListChannelGreenStatus(List<Integer> listChannelGreenStatus) {
        this.listChannelGreenStatus = listChannelGreenStatus;
    }

    public List<Integer> getListChannelFlashStatus() {
        return listChannelFlashStatus;
    }

    public void setListChannelFlashStatus(List<Integer> listChannelFlashStatus) {
        this.listChannelFlashStatus = listChannelFlashStatus;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(int cycleTime) {
        this.cycleTime = cycleTime;
    }

    public Boolean getResportTscStatusFlag() {
        return resportTscStatusFlag;
    }

    public void setResportTscStatusFlag(Boolean resportTscStatusFlag) {
        this.resportTscStatusFlag = resportTscStatusFlag;
    }

}
