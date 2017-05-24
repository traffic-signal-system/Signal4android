package com.aiton.zjb.signal.constant;

import android.content.pm.ActivityInfo;

/**
 * Created by 杰博 on 2016/10/20.
 */

public class Constant {
//    public static String IP = "www.aiton.com.cn";
    public static String IP = "192.168.1.119";
    public static int PORT= 18888;
    public static final String HOST = "http://"+IP+":8080/aiton-maintain-app-webapp/";
    public static int HengShuPing = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public static class Url {
        public static final String GET_SMS = HOST + "sendMessage";
        //检查服务器版本和是否存活
        public static final String CHECK_LIVE = HOST + "checkLiveForMaintainApp";
        //登录
        public static final String LOGIN = HOST + "login";
        //获取节点树
        public static final String GETNODEANDGROUPANDAREA = HOST + "getNodeAndGroupAndArea";
        //获取用户
        public static final String GET_USERS = HOST + "node/getUsers";
        //添加节点
        public static final String ADD_NODE = HOST + "node/addNode";
        //删除节点
        public static final String DELETE_NODE = HOST + "node/deleteNode";
        //修改节点
        public static final String EDIT_NODE = HOST + "node/updateNode";
        //添加域
        public static final String ADD_AREA = HOST + "area/addArea";
        //添加群
        public static final String ADD_GROUP = HOST + "group/addGroup";
        //删除域
        public static final String DELETE_AREA = HOST + "area/deleteArea";
        //删除群
        public static final String DELETE_GROUP = HOST + "group/deleteGroup";
        //修改域名
        public static final String EDIT_AREA = HOST + "area/updateArea";
        //开启信号机状态上报
        public static final String OPEN_SIGNAL_STATUE = HOST + "extReportStatus/open";
        //心跳返回（5秒一次10秒有效）
        public static final String HEART_BEAT = HOST + "extReportStatus/heartBeat";
        //关闭信号机状态上报
        public static final String CLOSE_SIGNAL_STATUE = HOST + "extReportStatus/close";
        //手控，自动切换
        public static final String CHANGE_CONTROL_TYPE = HOST + "control/changerControlType";
        //切换方向
        public static final String CHANGE_DIRECT = HOST + "control/changeDirect";
        //下方向操作
        public static final String NEXT_DIRECT = HOST + "control/nextDirect";
        //下相位操作
        public static final String NEXT_PHASE = HOST + "control/nextPhase";
        //下方向操作
        public static final String NEXT_STEP = HOST + "control/nextStep";
        //获取时基
        public static final String GET_TIME_BASE = HOST + "timeBase/getTimeBase";
        //获取时段
        public static final String GET_SCHEDULE = HOST + "schedule/getSchedule";
        //设置时基
        public static final String SET_TIMEBASE = HOST + "timeBase/setTimeBase";
        //获取配时方案
        public static final String GET_TIME_PATTERN = HOST + "timePattern/getTimePattern";
        //设置时段
        public static final String SET_SCHEDULE = HOST + "schedule/setSchedule";
        //获取阶段配时
        public static final String GET_STAGE_PATTERN = HOST + "stagePattern/getStagePattern";
        //设置配时方案
        public static final String SET_TIME_PATTERN = HOST + "timePattern/setTimePattern";
        //设置阶段配时
        public static final String SET_STAGE_PATTERN = HOST + "stagePattern/setStagePattern";
        //获取通道表
        public static final String SET_CHANNEL = HOST + "seniorPhase/setChannel";
        //设置通道表
        public static final String GET_CHANNEL = HOST + "seniorPhase/getChannel";
        //获取方向表
        public static final String GET_DIRECT = HOST + "seniorPhase/getDirect";
        //获取相位表
        public static final String GET_PHASE = HOST + "seniorPhase/getPhase";
        //设置方向表
        public static final String SET_DIRECT = HOST + "seniorPhase/setDirect";
        //设置相位表
        public static final String SET_PHASE = HOST + "seniorPhase/setPhase";
        //获取相位冲突表
        public static final String GET_PHASE_CONFLICT = HOST + "phaseConflict/getPhaseConflict";
        //设置相位冲突表
        public static final String SET_PHASE_CONFLICT = HOST + "phaseConflict/setPhaseConflict";
        //获取群的树结构
        public static final String GET_GROUP_AND_AREA = HOST + "getGroupAndArea";
        //获取群阶段配时表
        public static final String GET_GROUP_STAGE_PATTERN_BY_GROUPID = HOST + "groupStagePattern/getGroupStagePatternByGroupId";
        //设置群阶段配时
        public static final String SET_GROUP_STAGE_PATTERN = HOST + "groupStagePattern/setGroupStagePattern";
        //获取群配时方案表
        public static final String GET_GROUP_TIME_PATTERN = HOST + "groupTimePattern/getGroupTimePattern";
        //设置群配时方案
        public static final String SET_GROUP_TIME_PATTERN = HOST + "groupTimePattern/setGroupTimePattern";
        //获取群时基
        public static final String GET_GROUP_TIME_BASE = HOST + "groupTimeBase/getGroupTimeBase";
        //设置群时段
        public static final String SET_GROUP_TIME_BASE = HOST + "groupTimeBase/setGroupTimeBase";
        //获取群时段表
        public static final String GET_GROUP_SCHEDULE = HOST + "groupSchedule/getGroupSchedule";
        //设置群时段表
        public static final String SET_GROUP_SCHEDULE = HOST + "groupSchedule/setGroupSchedule";
        //群ID获取节点list
        public static final String GET_NODES_BY_GROUPID = HOST + "node/getNodesByGroupId";
        //设置群绿波周期
        public static final String SET_GROUP_CYCLE = HOST + "groupTimePattern/setGroupCycle";
        //添加摄像头
        public static final String ADD_VIDEO = HOST + "video/addVideo";
        //获取摄像头列表
        public static final String GET_VIDEOS_BY_NODEID = HOST + "video/getVideosByNodeId";
        //删除摄像头
        public static final String DELETE_VIDEO = HOST + "video/deleteVideo";
    }

    public static class PERMISSION {
        public static final int ACCESS_FINE_LOCATION = 1;
        public static final int WRITE_EXTERNAL_STORAGE = 2;
        public static final int READ_EXTERNAL_STORAGE = 3;
        public static final int READ_PHONE_STATE = 4;
        public static final int PERMISSION_READ_SMS = 5;
        public static final int RECEIVE_BOOT_COMPLETED = 6;
        public static final int RECEIVE_WRITE_SETTINGS = 7;
        public static final int RECEIVE_VIBRATE = 8;
        public static final int RECEIVE_DISABLE_KEYGUARD = 9;
        public static final int CALL_PHONE = 10;
        public static final int SYSTEM_ALERT_WINDOW = 11;
        public static final int ACCESS_COARSE_LOCATION = 12;

    }

    public static class IntentKey {
        public static final String IP = "ip";
        public static final String TONG_DAO_HAO = "TongDaoHao";
        public static final String XIANG_WEI_VIEW = "XiangWeiView";
        public static final String LOCATIONLAT = "locationLat";
        public static final String LOCATIONLng = "locationLng";
        public static final String NODE = "node";
        public static final String Area_LIST = "AreaList";
        public static final String GROUP_LIST = "groupList";
        public static final String GROUP_INFO = "groupInfo";
        public static final String SIGNAL_STATUE = "signalStatue";
        public static final String GROUP = "group";
        public static final String GROUP_NODE = "groupNode";
        public static final String CITY_CODE = "cityCode";
        public static final String SEARCH_ADD = "searchAdd";
        public static final String VIDEO_NAME= "videoName";
        public static final String VIDEO_IP= "videoIP";
        public static final String VIDEO_PORT= "videoPort";
        public static final String VIDEO_USER_NAEM= "videoUserName";
        public static final String VIDEO_USER_PASSWORD= "videoUserPassword";
        public static final String VIDEO_NOTE = "videoUserNote";
        public static final String VIDEO_INFO = "videoInfo";
    }

    public static class ACACHE {
        public static final String USER = "user";
        public static final String USERINFO = "userInfo";
        public static final String DEVICES_ID = "devicesID";
    }

    public static class BROADCASTRECEIVER {
        public static final String XIANGWEI = "xiangwei";
        public static final String TONGDAO = "tongDao";
        public static final String SIGNAL_STATU = "signalStatu";
    }

    public static class REQUEST_RETURN_CODE {
        public static final int CHOOSEADD = 1991;
        public static final int NODE_DETAIL = 1992;
        public static final int START_ADD = 1993;
        public static final int END_ADD = 1994;
        public static final int RESULT_ADD = 1995;
        public static final int VIDEO_INFO= 1996;
        public static final int EDIT_VIDEO_INFO= 1997;
    }
}
