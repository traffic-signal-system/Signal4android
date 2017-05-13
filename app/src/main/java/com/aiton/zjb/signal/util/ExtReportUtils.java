package com.aiton.zjb.signal.util;

import com.aiton.zjb.signal.model.ExtReportState;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2014/10/4.
 */
public class ExtReportUtils {

    /**
     * 将字节数组转换成主动上报对象
     *
     * @param bytes
     * @return
     */
    public static ExtReportState byte2ReportState(byte[] bytes) {
        ExtReportState ers = new ExtReportState();
        ers.setControlModel(ExtReportUtils.reportStatusControlMode(bytes[3]));
        ers.setWorkModel(reportStatusWorkModel(bytes[3]));
        ers.setWorkStatus(reportStatusWorkStatus(bytes[3]));
        ers.setCurrentSchedule(bytes[4]);
        ers.setCurrentTimePattern(bytes[5]);
        ers.setCurrentStagePattern(bytes[6]);
        ers.setStageCount(bytes[7]);
        ers.setCurrentStage(bytes[8]);
        ers.setStageTotalTime(bytes[9]);
        ers.setStageRunTime(bytes[10]);
        ers.setListChannelRedStatus(reportStatusLamp(new byte[]{bytes[14], bytes[13], bytes[12], bytes[11]}));
        ers.setListChannelYellowStatus(reportStatusLamp(new byte[]{bytes[18], bytes[17], bytes[16], bytes[15]}));
        ers.setListChannelGreenStatus(reportStatusLamp(new byte[]{bytes[22], bytes[21], bytes[20], bytes[19]}));
        ers.setListChannelFlashStatus(reportStatusLamp(new byte[]{bytes[26], bytes[25], bytes[24], bytes[23]}));
        ers.setCycleTime(bytes[27]);
        ers.setBasetime(bytes[28]);
        return ers;
    }

    public static List<Integer> reportStatusLamp(byte[] bytes) {
        List<Integer> listLamp = new ArrayList<Integer>();
        int ui = ByteUtils.byteToInt(bytes);
        for (int i = 0; i < 32; i++) {
            int rs = (ui >> i) & 0x01;
            listLamp.add(rs);
        }
        // Collections.reverse(listLamp);
        return listLamp;
    }

    public static String reportStatusWorkModel(byte by) {

        String result = "";
        int f = by & 0x03;
        switch (f) {
            case 0:
                result = "信号机正常";
                break;
            case 1:
                result = "一次过街";
                break;
            case 2:
                result = "二次过街";
                break;
            case 3:
                result = "保留";
                break;
            default:
                break;
        }
        return result;
    }

    public static String reportStatusWorkStatus(byte by) {
        String result = "";
        int f = by & 0x0c;
        switch (f) {
            case 0:
                result = "关灯";
                break;
            case 4:
                result = "全红";
                break;
            case 8:
                result = "闪光";
                break;
            case 12:
                result = "标准";
                break;
            default:
                break;
        }
        return result;
    }

    public static String reportStatusControlMode(byte by) {
        String result = "";
        int f = by & 0xf0;
        switch (f) {
            case 16:
                result = "多时段";
                break;
            case 32:
                result = "系统优化";
                break;
            case 48:
                result = "无电线缆协调";
                break;
            case 96:
                result = "手动";
                break;
            case 112:
                result = "感应";
                break;
            case 160:
                result = "自适应";
                break;
            case 176:
                result = "面板控制";
                break;
            case 4:
                result = "标准";
                break;
            default:
                break;
        }
        return result;
    }

    public static boolean isShouDongControl(byte by) {
        boolean isSelfControl = false;
        switch (by) {
            case 0x1C:
                isSelfControl = false;
                break;
            case (byte) 0xBC:
                isSelfControl = true;
                break;
            case 0x6C:
                isSelfControl = true;
                break;
        }
        return isSelfControl;
    }
}
