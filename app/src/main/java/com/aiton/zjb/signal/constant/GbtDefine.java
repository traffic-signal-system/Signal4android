package com.aiton.zjb.signal.constant;

/**
 * Created by Administrator on 14-2-11.
 */
public class GbtDefine {
    /// <summary>
    /// 信号机相关端口配置常量
    /// </summary>
    public static int BROADCAST_PORT = 8808;//8808
    public static int GBT_PORT = 5435;
    public static String TSC_DATA = "tscData";
    public static String TSC_INFO = "CurrentTsc";
    public static String SELECTED_DETECTOR = "SelectedDetector";
    public static String SELECTED_PHASE_OVERLAP = "SelectedPhase";
    public static String SELECTED_PHASE_OVERLAP_TYPE = "Phase_OverlapPhase";
    public static String SELECTED_PHASE_OVERLAP_TYPE_PHASE = "Type_Phase";
    public static String SELECTED_PHASE_OVERLAP_TYPE_OVERLAPPHASE = "Type_OverlapPhase";
    public static byte LAMP_RED = 0x0;
    public static byte LAMP_YELLOW = 0x1;
    public static byte LAMP_GREEN = 0x2;
    /// <summary>
    /// 心跳相关配置常数
    /// </summary>

    public static int RUNTASTIC_HEART_RATE_TIME = 5000;
    public static boolean RUN_THREAD_FLAG = false;
    public static byte[] SET_TIMING = { (byte)0x81, (byte)0x86, 0x00, 0x00, 0x00, 0x00, 0x00 };
    /// <summary>
    /// ******************GBT20999 国标中相关对应对象************************************************
    /// </summary>
    public static byte SET_REQUEST_RESPONSE = (byte)0x81;
    public static byte SET_REQUEST_NO_RESPONSE = (byte)0x82;
    public static byte GET_REQUEST = (byte)0x80;
    public static byte SELF_REPORT = (byte)0x83;
    public static byte ERROR_RESPONSE = (byte)0x86;
    public static byte GET_RESPONSE = (byte)0x84;
    public static byte SET_RESPONSE = (byte)0x85;

    public static byte PUBLIC_CONFIG_ID = (byte)0x81;
    public static byte PUBLIC_CONFIG_MODULE_MAX = (byte)0x82;
    public static byte PUBLIC_CONFIG_SYSNCH_TIME = (byte)0x83;
    public static byte PUBLIC_CONFIG_SYSNCH_FLAG = (byte)0x84;

    /// <summary>
    /// 模块表参数部分
    /// </summary>
    public static byte MODULE = (byte)0x85;
    public static byte[] GET_MODULE = { GET_REQUEST, MODULE, 0x00 };
    public static byte[] SET_MODULE_RESPONSE = { SET_REQUEST_RESPONSE, MODULE, 0x00 };
    public static byte[] SET_MODULE_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, MODULE, 0x00 };

    /// <summary>
    /// 公共配置部分
    /// </summary>
    public static byte PUBLIC_TIME_PUBLIC_TIME = (byte)0x86;
    public static byte PUBLIC_TIME_TIME_ZONE = (byte)0x87;
    public static byte PUBLIC_TIME_LOCAL_TIME =(byte)0x88;
    public static byte PUBLIC_TIME_PLAN_MAX=(byte)0x89;
    public static byte PUBLIC_TIME_SCHEDULE_MAX = (byte)0x8a;
    public static byte PUBLIC_TIME_SCHEDULE_EVENTID_MAX = (byte)0x8b;
    public static byte PUBLIC_TIME_ACTIVITY_SCHEDULE_ID = (byte)0x8c;
    /// <summary>
    /// 时基表参数部分
    /// </summary>
    public static byte PLAN = (byte)0x8d;
    public static byte[] GET_PLAN = { GET_REQUEST, PLAN, 0x00 };
    public static byte[] SET_PLAN_RESPONSE = { SET_REQUEST_RESPONSE, PLAN, 0x00 };
    public static byte[] SET_PLAN_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, PLAN, 0x00 };
    public static int PLAN_BYTE_SIZE = 9;

    /// <summary>
    /// 时段表相关部分
    /// </summary>
    public static byte SCHEDULE =  (byte)0x8E ;
    public static byte[] GET_SCHEDULE = { GET_REQUEST, SCHEDULE, 0x00 };
    public static byte[] SET_SCHEDULE_RESPONSE = { SET_REQUEST_RESPONSE, SCHEDULE, 0x00 };
    public static byte[] SET_SCHEDULE_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, SCHEDULE, 0x00 };
    public static int SCHEDULE_BYTE_SIZE = 8;
    public static int SCHEDULE_RESULT_LEN = 16;
    public static int SCHEDULE_EVENT_RESULT_LEN = 48;
    //日志类型最大行
    public static byte REPORT_MAX_EVT_TYPE = (byte)0x8F ;
    //日志最大行
    public static byte REPORT_MAX_LOG =  (byte)0x90 ;

    /// <summary>
    /// 日志类型参数部分
    /// </summary>
    public static byte EVENT_TYPE = (byte)0x91;
    public static byte[] GET_EVENT_TYPE = { GET_REQUEST, EVENT_TYPE, 0x00 };
    public static byte[] SET_EVENT_TYPE_RESPONSE = { SET_REQUEST_RESPONSE, EVENT_TYPE, 0x00 };
    public static byte[] SET_EVENT_TYPE_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, EVENT_TYPE, 0x00 };
    //public static int EVENT_TYPE_BYTE_SIZE = 10;
    /// <summary>
    /// 日志参数部分
    /// </summary>
    public static byte EVENT_LOG = (byte)0x92;
    public static byte[] GET_EVENT_LOG = { GET_REQUEST, EVENT_LOG, 0x00 };
    public static byte[] SET_EVENT_LOG_RESPONSE = { SET_REQUEST_RESPONSE, EVENT_LOG, 0x00 };
    public static byte[] SET_EVENT_LOG_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, EVENT_LOG, 0x00 };
    public static int EVENT_LOG_BYTE_SIZE = 10;
    //相位表最大行数据
    public static byte PHASE_MAX = (byte)0x93;
    //相位状态组最大数
    public static byte PHASE_STATE_GROUP_MAX = (byte)0x94;


    /// <summary>
    /// 模块表参数部分
    /// </summary>
    public static byte PHASE =  (byte)0x95 ;
    public static byte[] GET_PHASE = { GET_REQUEST, PHASE, 0x00 };
    public static byte[] SET_PHASE_RESPONSE = { SET_REQUEST_RESPONSE, PHASE, 0x00 };
    public static byte[] SET_PHASE_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, PHASE, 0x00 };
    public static int PAHSE_BYTE_SIZE = 12;
    public static byte PHASE_OUTPUT_STATE = (byte)0x96;

    /// <summary>
    /// 相位冲突部分
    /// </summary>
    public static byte COLLISION = (byte)0x97;
    public static byte[] GET_COLLISION = { GET_REQUEST, COLLISION, 0x00 };
    public static byte[] SET_COLLISION_RESPONSE = { SET_REQUEST_RESPONSE, COLLISION, 0x00 };
    public static byte[] SET_COLLISION_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, COLLISION, 0x00 };
    public static int COLLISION_BYTE_SIZE = 5;  //原国标为3个字节，进行扩展到5个字节，第1个表示ID，第2 到5个表示32位表示的冲突，
    public static int GBT20999_COLLISION_BYTE_SIZE = 3;

    /// <summary>
    /// 检测器部分
    /// </summary>
    // 检测器最大表，包括行人按钮
    public static byte DETECTOR_MAX = (byte)0x98;
    // 检测器状态组最大表
    public static byte DETECTOR_STATE_GROUP_MAX = (byte)0x99;
    //检测器流水号，每个采集周期顺序加1，循环计数
    public static byte DETECTOR_DATA_SERIAL_NUMBER = (byte)0x9a;
    //数据采集周期
    public static byte DETECTOR_GATHER_CYCLE = (byte)0x9b;
    //活动检测器总数
    public static byte DETECTOR_ACTIVITY_MAX = (byte)0x9c;
    //脉冲数据流水号，每个采集周期加1 循环计数
    public static byte DETECTOR_PULSE_SERIAL_NUMBER = (byte)0x9d;
    //脉冲采集周期
    public static byte DETECTOR_PULSE_GATHER_CYCLE = (byte)0x9e;
    //检测器表
    public static byte DETECTOR = (byte)0x9f;
    public static byte[] GET_DETECTOR = { GET_REQUEST, DETECTOR, 0x00 };
    public static byte[] SET_DETECTOR_RESPONSE = { SET_REQUEST_RESPONSE, DETECTOR, 0x00 };
    public static byte[] SET_DETECTOR_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, DETECTOR, 0x00 };
    public static int DETECTOR_BYTE_SIZE = 9;
    public static int DETECTOR_RESULT_LEN = 48;
    /// <summary>
    /// 检测器状态表部分
    /// </summary>
    public static byte DETECTOR_STATE = (byte)0xa0;
    public static int DETECTOR_STATE_BYTE_SIZE = 3;
    /// <summary>
    /// 交通检测流量数据表
    /// </summary>
    public static byte DETECT_DATA = (byte)0xa1;
    public static int DETECT_DATA_BYTE_SIZE = 7;

    /// <summary>
    /// 检测器报警表
    /// </summary>
    public static byte DETECT_ALERT = (byte)0xa2;
    //检测器报警表的字节长度
    public static int DETECT_ALERT_BYTE_SIZE = 3;

    //启动时闪光控制时间，在掉电恢复后出现。掉电恢复具体包括情况有设备定义。在这期间，硬件黄闪和信号灯监视是不活动的。
    public static byte UNIT_START_FLASH_CTRL_TIME = (byte)0xa3;
    //启动时全红控制时间，在掉电后恢复出现，并紧跟在闪光阶段之后，掉电恢复具体包括哪些情况由设备定义。在这期间，硬件黄闪和信号灯监视是不活动的
    public static byte UNIT_START_ALLRED_CTRL_TIME = (byte)0xa4;
    //当前信号机控制状态；未知模式-1；系统协调控制-2；主机协调控制-3；手动面板控制-4；时段表控制-5；线缆协调-6
    public static byte UNIT_CURRENT_TSC_CTRL_STATUS = (byte)0xa5;
    //当前的闪光控制模式，未知闪光原因-1；当前处于闪光控制状态-2；自动闪光控制（软件控制）-3；本地手动黄闪-4；故障监视引起闪光-5；信号冲突引起闪光-6；启动时闪光控制-7
    public static byte UNIT_CURRENT_FLASH_CTRL_MODE = (byte)0xa6;
    //信号机报警2，bit6-7 reserved;bit5即将停止运行，停机计时器开始计时；bit3-4 reserved；电池不足 bit 1;bit 0 重起
    public static byte UNIT_TSC_ALERT2 = (byte)0xa7;
    //信号机报警 bit 7 coordactive; bit 6 localfree 本地单点控制；bit5 local flash 本地黄闪控制；bit4 MMU闪光；bit3-0 reserved;
    public static byte UNIT_TSC_ALERT1 = (byte)0xa8;
    //信号机报警摘要 bit7 紧急报警-停止运行；bit6 非紧急报警；bit5检测器故障；bit4 reserved；bit3 强制本地控制，不允许信号机协调控制；bit2 reserved；bit1 t&f flash
    //bit0 reserved
    public static byte UNIT_TSC_ALERT_SUMMARY = (byte)0xa9;
    //允许运程控制实体激活信号机的某些功能，bit7 允许辉度控制；bit6有缆协调设置为1 ，表示作为有缆协调的发起机
    // bit5 reserved  bit0 是否作为控制主要
    public static byte UNIT_ALLOW_REMOTE_TSC_FUNCTION = (byte)0xaa;
    // u闪光频率  bit7-bit3 reserved；bit 2 0.5 hz；bit1 1hz；bit 0  2hz
    public static byte UNIT_FLASH_RATE = (byte)0xab;
    //辉度控制开户时间
    public static byte UNIT_LUMINANCE_START_TIIME = (byte)0xac;
    //辉度控制关闭时间
    public static byte UNIT_LUMINANCE_CLOSE_TIME = (byte)0xad;

    /// <summary>
    /// 通道部分
    /// </summary>
    //最大通道
    public static byte CHANNEL_MAX = (byte)0xae;
    public static byte CHANNEL_STATE_GROUP = (byte)0xaf;
    public static byte CHANNEL =  (byte)0xb0 ;
    public static byte[] GET_CHANNEL = { GET_REQUEST, CHANNEL, 0x00 };
    public static byte[] SET_CHANNEL_RESPONSE = { SET_REQUEST_RESPONSE, CHANNEL, 0x00 };
    public static byte[] SET_CHANNEL_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, CHANNEL, 0x00 };
    public static int CHANNEL_BYTE_SIZE = 4;

    /// <summary>
    /// 通道状态表
    /// </summary>
    public static byte CHANNEL_STATUS = (byte)0xb1;

    /// <summary>
    /// 控制相关参数
    /// </summary>
    //配时方案数
    public static byte TIMEPATTERN_COUNT = (byte)0xb2;
    //最大阶段配时表数
    public static byte STAGETIME_MAX = (byte)0xb3;
    //最大阶段数
    public static byte STAGE_MAX = (byte)0xb4;
    //手动控制方案
    public static byte CTRL_MANUAL_SCHEME = (byte)0xb5;
    //系统控制方案
    public static byte CTRL_SYSTEM_SCHEME = (byte)0xb6;
    //控制方式 1-关灯；2-闪光；3-全红；6-感应；8-单点优化；11-主从线控；12-系统优化（联网控制）；13-干预线控
    public static byte CTRL_MODE = (byte)0xb7;
    //公共周期时长   1-255 0或无效值表示使用配时方案中的周期
    public static byte PUBLIC_CYCLE = (byte)0xb8;
    //协调相位差
    public static byte ADUJEST_PAHSE = (byte)0xb9;
    //阶段状态
    public static byte STAGE_STATUS = (byte)0xba;
    //步进指令  0-进步开始、顺序步进、1-16指定步进
    public static byte STEP_COMMAND = (byte)0xbb;
    //降级模式  0 表示无降级；1-13表示降级控制方式中的一种
    public static byte DEGRADATION_MODE = (byte)0xbc;
    //降级基准方案表，每个字节表示、该模式下的基准方案号；第0个字节表示控制方式0
    public static byte DEGRADATION_SCHEME = (byte)0xbd;
    //当前方案各个阶段时长，感应，步进，非配时方式为0
    public static byte SCHEME_EVERY_STAGE_TIME_LEN = (byte)0xbe;
    //当前方案各关键相位绿灯时长，用于动态分配绿信比
    public static byte SCHEME_EVERY_KEY_PHASE_GREEN_TIME_LEN = (byte)0xbf;

    /// <summary>
    /// 配时方案表参数
    /// </summary>
    public static byte PATTERN = (byte)0xc0;
    public static byte[] GET_PATTERN = { GET_REQUEST, PATTERN, 0x00 };
    public static byte[] SET_PATTERN_RESPONSE = { SET_REQUEST_RESPONSE, PATTERN, 0x00 };
    public static byte[] SET_PATTERN_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, PATTERN, 0x00 };
    public static int PATTERN_BYTE_SIZE= 5;

    /// <summary>
    /// 阶段配时表参数
    /// </summary>
    public static byte STAGEPATTERN = (byte)0xc1;
    public static byte[] GET_STAGEPATTERN = { GET_REQUEST, STAGEPATTERN, 0x00 };
    public static byte[] SET_STAGEPATTERN_RESPONSE = { SET_REQUEST_RESPONSE, STAGEPATTERN, 0x00 };
    public static byte[] SET_STAGEPATTERN_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, STAGEPATTERN, 0x00 };
    public static int STAGEPATTERN_BYTE_SIZE = 10;   //原国标中是8个字节， 其中AllowPhase为2个字节。改成32个位，四个字节。10个字节
    public static int STAGE_PATTERN_BYTE_SIZE_16 = 8;


    //优化相关参数 ，下载标志 ；1 开始下载 2结束下载
    public static byte OPTIMIZE_DOWNLOAD_PARAMETER = (byte)0xc2;

    //bit7-1 bi t0是否优选公共周期
    public static byte CONTROL_HOST_OPTION = (byte)0xc3;
    //两个字节长度数据，信号机基地址，如果 是多路口信号机，则代表基地址，每个路口有一个序号，从0开始
    public static byte ROAD_TSC_BASE_ADDRESS = (byte)0xc4;
    //路口数量，每个路口有一个信号机地址=信号机地址+序号
    public static byte ROAD_COUNT = (byte)0xc5;
    /// <summary>
    /// 跟随相位相关的常量定义
    /// </summary>
    //最大跟随相位
    public static byte OVERLAPPHASE_MAX = (byte)0xc6;
    //最大跟随相位状态表行数
    public static byte OVERLAPPHASE_STATUS_MAX = (byte)0xc7;
    /// <summary>
    /// 跟随相位部分
    /// </summary>
    public static byte OVERLAPPHASE = (byte)0xc8;
    public static byte[] GET_OVERLAPPHASE = { GET_REQUEST, OVERLAPPHASE, 0x00 };
    public static byte[] SET_OVERLAPPHASE_RESPONSE = { SET_REQUEST_RESPONSE, OVERLAPPHASE, 0x00 };
    public static byte[] SET_OVERLAPPHASE_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, OVERLAPPHASE, 0x00 };
    public static int OVERLAPPHASE_BYTE_SIZE = 71;      //原国标为39个字节，改成71，现在将包含相位与修正相位增加一倍。变成16-》32，
    public static int GBT20999_OVERLAPPHASE_BYTE_SIZE = 39;
    //跟随相位状态组表
    public static byte OVERLAPPHASE_STATUS = (byte)0xc9;
    //跟随相位状态表字节长度
    public static int OVERLAPPHASE_STATUS_BYTE_SIZE = 4;
    /// <summary>
    /// ******************GBT20999 国标中相关对应对象************************************************
    /// </summary>
    // 扩展相关对应对象
    /// <summary>
    /// ******************扩展相关对应对象************************************************
    /// </summary>
    /// <summary>
    /// *****************************联网降级相关配置常量***************************************
    /// </summary>
    public static byte[] DEGRADATION_UTC = { SET_REQUEST_NO_RESPONSE, (byte)0xE4, 0x00, 0x01 };
    public static byte[] SEND_DEGRADATION_MODE_ONE = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x01 };
    public static byte[] SEND_DEGRADATION_MODE_TWO = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x02 };
    public static byte[] SEND_DEGRADATION_MODE_THREE = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x03 };
    public static byte[] SEND_DEGRADATION_MODE_FOUR = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x04 };
    public static byte[] SEND_DEGRADATION_MODE_SIX = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x06 };
    public static byte[] SEND_DEGRADATION_MODE_SEVEN = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x07 };
    public static byte[] SEND_DEGRADATION_MODE_EIGHT = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x08 };
    public static byte[] SEND_DEGRADATION_MODE_NINE = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x09 };
    public static byte[] SEND_DEGRADATION_MODE_TEN = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x0a };
    public static byte[] SEND_DEGRADATION_MODE_ELEVEN = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x0b };
    public static byte[] SEND_DEGRADATION_MODE_TWELVE = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x0c };
    public static byte[] SEND_DEGRADATION_MODE_THREETH = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x0d };
    public static byte[] SEND_DEGRADATION_MODE_FOURTH = { SET_REQUEST_RESPONSE, (byte)0xbc, 0x00, 0x0e };
    public static byte[] SEND_DEGRADATION_MODEL_FIVE = { (byte)0x91, (byte)0xbc, 0x00, 0x05, (byte)0xbd, 0x00, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    /// <summary>
    /// *****************************联网降级相关配置常量***************************************
    /// </summary>
    //信号机状态上报
    public static byte[] REPORT_TSC_STATUS = { SET_REQUEST_RESPONSE, (byte)0xF7, 0x00, 0x01, (byte)0xF8, 0x00, 0x0A };
    //信号机状态上报取消
    public static byte[] REPORT_TSC_STATUS_CANCEL = { SET_REQUEST_RESPONSE, (byte)0xF7, 0x00, 0x01, (byte)0xF8, 0x00, 0x00 };
    //切入手动控制
    public static byte[] CTRL_MUNUAL = { SET_REQUEST_RESPONSE, (byte)0xB7, 0x00, 0x0A };
    //切换自动控制
    public static byte[] CTRL_SELF = { SET_REQUEST_RESPONSE, (byte)0xB6, 0x00, 0x00 };
    //下一步，如果最后一个字节为1-255的，显示指定步伐
    public static byte[] CTRL_NEXTSTEP_STATUS = { SET_REQUEST_RESPONSE, (byte)0xBA, 0x00, 0x00 };
    //下一步，如果最后一个字节为1-255的，跳步到指定步伐
    public static byte[] CTRL_NEXTSTEP = { SET_REQUEST_RESPONSE, STEP_COMMAND, 0x00, 0x00 };
    //下一相位
    public static byte[] CTRL_NEXTPHASE = { SET_REQUEST_RESPONSE, (byte)0xf2, 0x00, 0x01,0x00 };
    //下一方向
    public static byte[] CTRL_NEXTDIREC = { SET_REQUEST_RESPONSE, (byte)0xf2, 0x00, 0x02, 0x00 };
    //北方向
    public static byte[] CTRL_NORTH = { SET_REQUEST_RESPONSE,(byte) 0xf2, 0x00, 0x03, 0x01 };
    //东方向
    public static byte[] CTRL_EAST = { SET_REQUEST_RESPONSE,(byte) 0xf2, 0x00, 0x03, 0x02 };
    //南方向
    public static byte[] CTRL_SOUTH = { SET_REQUEST_RESPONSE, (byte)0xf2, 0x00, 0x03, 0x03 };
    //西方向
    public static byte[] CTRL_WEST = { SET_REQUEST_RESPONSE,(byte) 0xf2, 0x00, 0x03, 0x04 };
    //校时
    public static byte[] TSC_DEV_TIMING = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01 };
    //生成序列号
    public static byte[] BUILD_SN = { SET_REQUEST_RESPONSE, (byte)0xE4, 0x00, 0x02 };
    //查询各个模块的状态
    public static byte[] MODULE_EVERYONE_STATUS = { GET_REQUEST, (byte)0xf9, 0x00 };
    //控制模块相关信息读取，如电压，温度
    public static byte[] GET_CONTROLLER_STATUS = { GET_REQUEST, (byte)0xf5, 0x00 };
    public static byte OPTION = (byte)0xf6;
    public static byte PSC_TSC = 0x03;
    public static byte PSC = 0x01;   // 一次过街
    public static byte TSC = 0x00;  //TSC模式
    public static byte PSC_2 = 0x02; //二次过街
    public static byte[] SET_PSC_1 = { SET_REQUEST_RESPONSE, OPTION, PSC_TSC, PSC, 0x01, 0x02, 0x00 };
    public static byte[] SET_PSC_2 = { SET_REQUEST_RESPONSE, OPTION, PSC_TSC, PSC_2, 0x01, 0x02, 0x03 };
    public static byte[] SET_TSC = { SET_REQUEST_RESPONSE, OPTION, PSC_TSC, TSC, 0x00, 0x00, 0x00 };

    public static byte[] SET_PSC_1_GREEN_TIME = { SET_REQUEST_RESPONSE, (byte)0xc1, (byte)0x84, 0x01, 0x02, 0x14 };
    public static byte[] SET_PSC_2_GREEN_TIME = { SET_REQUEST_RESPONSE, (byte)0xc1, (byte)0x84, 0x01, 0x03, 0x14 };
    // 方向相关的参数

    public static byte PHASE_DIREC = (byte)0xfa;
    public static byte[] GET_PHASE_DIREC = { GET_REQUEST, PHASE_DIREC, 0x00 };
    public static byte[] SET_PHASE_DIREC_RESPONSE = { SET_REQUEST_RESPONSE, PHASE_DIREC, 0x00 };
    public static byte[] SET_PHASE_DIREC_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, PHASE_DIREC, 0x00 };
    public static int PHASE_DIREC_BYTE_SIZE = 4;
    // 灯泡检测相关定义
    public static byte LAMP_CHECK = (byte)0xff;
    public static byte[] GET_LAMP_CHECK = { GET_REQUEST, LAMP_CHECK, 0x00 };
    public static byte[] SET_LAMP_CHECK_RESPONSE = { SET_REQUEST_RESPONSE, LAMP_CHECK, 0x00 };
    public static byte[] SET_LAMP_CHECK_NO_RESPONE = { SET_REQUEST_NO_RESPONSE, LAMP_CHECK, 0x00 };
    public static int LAMP_CHECK_BYTE_SIZE = 2;
    public static byte LAMP_CHECK_LEN = 0x60;

    public static byte LAMP_BLOCK_CHECK_COLLISION = (byte)0xe3;
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_ONE = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x01 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_TWO = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x02 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_THREE = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x03 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_FOUR = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x04 };

    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_ONE_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x00,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_TWO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x01,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_THREE_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x02,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_FOUR_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x03,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_FIVE_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x04,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_SIX_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x05,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_SEVEN_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x06,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_EIGHT_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x07,0x0a };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_ONE_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x00,0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_TWO_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x01, 0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_THREE_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x02, 0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_FOUR_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x03, 0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_FIVE_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x04,0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_SIX_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x05, 0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_SEVEN_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x06, 0x05 };
    public static byte[] SET_LAMP_BLOCK_CHECK_COLLISION_EIGHT_NO_CHECK = { SET_REQUEST_RESPONSE, LAMP_BLOCK_CHECK_COLLISION, 0x00, 0x03, 0x07, 0x05 };
    // 倒计时开关

    public static byte COUNT_DOWN = (byte)0xf6;
    public static byte[] SET_COUNT_DOWN_CLOSE = { SET_REQUEST_RESPONSE, COUNT_DOWN, 0x09, 0x00, 0x03 };
    public static byte[] SET_COUNT_DOWN_OPEN_15 = { SET_REQUEST_RESPONSE, COUNT_DOWN, 0x09, 0x01, 0x01 };
    public static byte[] SET_COUNT_DOWN_OPEN_8 = { SET_REQUEST_RESPONSE, COUNT_DOWN, 0x09, 0x01, 0x02 };
    public static byte[] SET_COUNT_DOWN_OPEN_NORMAL = { SET_REQUEST_RESPONSE, COUNT_DOWN, 0x09, 0x01, 0x03 };
    // 扩展对象的记录长度定义
    public static int COLLISION_RESULT_LENGTH = 32;
    public static int PLAN_RESULT_LEN = 40;
    public static int CHANNEL_RESULT_LEN = 32;
    public static int PATTERN_RESULT_LEN = 32;
    public static int STAGEPATTERN_RESULT_LEN = 16;
    public static int STAGE_RESULT_LEN = 16;
    public static int OVERLAPPHASE_RESULT_LEN = 16;
    public static int PHASE_DIREC_RESULT_LEN = 80;
    public static int PHASE_BYTE_SIZE = 12;
    public static int PHASE_RESULT_LEN = 32;
    ////方向和转向定义
    //北
    public static final  byte NORTH_NONE1 = 0x00;  //调头
    public static final int I_NORTH_NONE1 = 0;
    public static final  byte NORTH_LEFT = 0x01;
    public static final int I_NORTH_LEFT = 1;
    public static final byte NORTH_STRAIGHT = 0x02;
    public static final int I_NORTH_STRAIGHT = 2;
    public static  final byte NORTH_LEFT_STRAIGHT = 0x03;
    public static final  byte NORTH_RIGHT = 0x04;
    public static final  int I_NORTH_RIGHT = 4;
    public static final  byte NORTH_OTHER = 0x05;
    public static final  int I_NORTH_OTHER = 5;
    public static  final byte NORTH_RIGHT_STRAIGHT = 0x06;
    public static  final byte NORTH_NONE2 = 0x07;
    public static  final int I_NORTH_NONE2 = 7;
    public static final  int I_NORTH_PEDESTRAIN_ONE = 8;
    public static final  int I_NORTH_PEDESTRAIN_TWO = 24;
    public static final  byte NORTH_PEDESTRAIN_ONE = 0x08;
    public static final  byte NORTH_PEDESTRAIN_TWO = 0x18;
    //东
    public static final  byte EAST_NONE1 = 0x40;  //调头
    public static final  int I_EAST_NONE1 = 64;  //调头
    public static final  byte EAST_LEFT = 0x41;
    public static final  int I_EAST_LEFT = 65;
    public static final  byte EAST_STRAIGHT = 0x42;
    public static final  int I_EAST_STRAIGHT = 66;
    public static final  byte EAST_LEFT_STRAIGHT = 0x43;
    public static final  byte EAST_RIGHT = 0x44;
    public static final  int  I_EAST_RIGHT = 68;
    public static final  byte EAST_OTHER = 0x45;
    public static final  int I_EAST_OTHER = 69;
    public static final  byte EAST_RIGHT_STRAIGHT = 0x46;
    public static final  byte EAST_NONE2 = 0x47;
    public static final  byte EAST_PEDESTRAIN_ONE = 0x48;
    public static  final byte EAST_PEDESTRAIN_TWO = 0x58;
    public static final  int I_EAST_NONE2 = 71;
    public static final  int I_EAST_PEDESTRAIN_ONE = 72;
    public static  final int I_EAST_PEDESTRAIN_TWO = 88;
    //东南
    //南
    public static final  byte SOUTH_NONE1 = (byte)0x80;  //调头
    public static final  int I_SOUTH_NONE1 = 128;  //调头
    public static final  byte SOUTH_LEFT = (byte)0x81;
    public static final  byte SOUTH_STRAIGHT = (byte)0x82;
    public static final  int I_SOUTH_LEFT = 129;
    public static final  int I_SOUTH_STRAIGHT = 130;
    public static final  byte SOUTH_LEFT_STRAIGHT = (byte)0x83;
    public static final  byte SOUTH_RIGHT = (byte)0x84;
    public static final  byte SOUTH_OTHER = (byte)0x85;
    public static final  int I_SOUTH_RIGHT = 132;
    public static final  int I_SOUTH_OTHER = 133;
    public static final  byte SOUTH_RIGHT_STRAIGHT = (byte)0x86;
    public static  final byte SOUTH_NONE2 = (byte)0x87;
    public static final  byte SOUTH_PEDESTRAIN_ONE = (byte)0x88;
    public static final  byte SOUTH_PEDESTRAIN_TWO = (byte)0x98;
    public static  final int I_SOUTH_NONE2 = 135;
    public static final  int I_SOUTH_PEDESTRAIN_ONE = 136;
    public static final  int I_SOUTH_PEDESTRAIN_TWO = 152;
    //西方
    public static final  byte WEST_NONE1 =(byte) 0xc0;  //调头
    public static final  byte WEST_LEFT = (byte)0xc1;
    public static  final byte WEST_STRAIGHT = (byte)0xc2;
    public static final  int I_WEST_NONE1 =192;  //调头
    public static final  int I_WEST_LEFT = 193;
    public static  final int I_WEST_STRAIGHT = 194;
    public static  final byte WEST_LEFT_STRAIGHT = (byte)0xc3;
    public static  final byte WEST_RIGHT = (byte)0xc4;
    public static  final byte WEST_OTHER =(byte) 0xc5;
    public static  final int I_WEST_RIGHT = 196;
    public static  final int I_WEST_OTHER =197;
    public static  final byte WEST_RIGHT_STRAIGHT = (byte)0xc6;
    public static  final byte WEST_NONE2 =(byte) 0xc7;
    public static  final byte WEST_PEDESTRAIN_ONE =(byte) 0xc8;
    public static final  byte WEST_PEDESTRAIN_TWO = (byte)0xd8;
    public static  final int I_WEST_NONE2 =199;
    public static  final int I_WEST_PEDESTRAIN_ONE =200;
    public static final  int I_WEST_PEDESTRAIN_TWO = 216;

    // 相位冲突设置过程中的定义
    public static int COLLISION_1_PHASE = 0x1;
    public static int COLLISION_2_PHASE = 0x2;
    public static int COLLISION_3_PHASE = 0x4;
    public static int COLLISION_4_PHASE = 0x8;
    public static int COLLISION_5_PHASE = 0x10;
    public static int COLLISION_6_PHASE = 0x20;
    public static int COLLISION_7_PHASE = 0x40;
    public static int COLLISION_8_PHASE = 0x80;
    public static int COLLISION_9_PHASE = 0x100;
    public static int COLLISION_10_PHASE = 0x200;
    public static int COLLISION_11_PHASE = 0x400;
    public static int COLLISION_12_PHASE = 0x800;

    public static int COLLISION_13_PHASE = 0x1000;
    public static int COLLISION_14_PHASE = 0x2000;
    public static int COLLISION_15_PHASE = 0x4000;
    public static int COLLISION_16_PHASE = 0x8000;
    public static int COLLISION_17_PHASE = 0xffff;
    public static int COLLISION_18_PHASE = 0x20000;
    public static int COLLISION_19_PHASE = 0x40000;
    public static int COLLISION_20_PHASE = 0x80000;
    public static int COLLISION_21_PHASE = 0x100000;
    public static int COLLISION_22_PHASE = 0x200000;
    public static int COLLISION_23_PHASE = 0x400000;
    public static int COLLISION_24_PHASE = 0x800000;

    public static int COLLISION_25_PHASE = 0x1000000;
    public static int COLLISION_26_PHASE = 0x2000000;
    public static int COLLISION_27_PHASE = 0x4000000;
    public static int COLLISION_28_PHASE = 0x8000000;
    public static int COLLISION_29_PHASE = 0x10000000;
    public static int COLLISION_30_PHASE = 0x20000000;
    public static int COLLISION_31_PHASE = 0x40000000;
    public static int COLLISION_32_PHASE = 0x80000000;

    public static int COLLISION_1_PHASE_0 = 0xFFFFFFFE;
    public static int COLLISION_2_PHASE_0 = 0xFFFFFFFD;
    public static int COLLISION_3_PHASE_0 = 0xFFFFFFFB;
    public static int COLLISION_4_PHASE_0 = 0xFFFFFFF7;
    public static int COLLISION_5_PHASE_0 = 0xFFFFFFEF;
    public static int COLLISION_6_PHASE_0 = 0xFFFFFFDF;
    public static int COLLISION_7_PHASE_0 = 0xFFFFFFBF;
    public static int COLLISION_8_PHASE_0 = 0xFFFFFF7F;
    public static int COLLISION_9_PHASE_0 = 0xFFFFFEFF;
    public static int COLLISION_10_PHASE_0 = 0xFFFFFDFF;
    public static int COLLISION_11_PHASE_0 = 0xFFFFFBFF;
    public static int COLLISION_12_PHASE_0 = 0xFFFFF7FF;
    public static int COLLISION_13_PHASE_0 = 0xFFFFEFFF;
    public static int COLLISION_14_PHASE_0 = 0xFFFFDFFF;
    public static int COLLISION_15_PHASE_0 = 0xFFFFBFFF;
    public static int COLLISION_16_PHASE_0 = 0xFFFF7FFF;
    public static int COLLISION_17_PHASE_0 = 0xFFFEFFFF;
    public static int COLLISION_18_PHASE_0 = 0xFFFDFFFF;
    public static int COLLISION_19_PHASE_0 = 0xFFFBFFFF;
    public static int COLLISION_20_PHASE_0 = 0xFFF7FFFF;
    public static int COLLISION_21_PHASE_0 = 0xFFEFFFFF;
    public static int COLLISION_22_PHASE_0 = 0xFFDFFFFF;
    public static int COLLISION_23_PHASE_0 = 0xFFBFFFFF;
    public static int COLLISION_24_PHASE_0 = 0xFF7FFFFF;
    public static int COLLISION_25_PHASE_0 = 0xFEFFFFFF;
    public static int COLLISION_26_PHASE_0 = 0xFDFFFFFF;
    public static int COLLISION_27_PHASE_0 = 0xFBFFFFFF;
    public static int COLLISION_28_PHASE_0 = 0xF7FFFFFF;
    public static int COLLISION_29_PHASE_0 = 0xEFFFFFFF;
    public static int COLLISION_30_PHASE_0 = 0xDFFFFFFF;
    public static int COLLISION_31_PHASE_0 = 0xBFFFFFFF;
    public static int COLLISION_32_PHASE_0 = 0x7FFFFFFF;

    ///倒计时对象
    public static byte CNTDOWNDEV = (byte)0xf1;
    public static byte[] GET_CNTDOWNDEV = {GET_REQUEST,CNTDOWNDEV,0x00};
    public static byte[] SET_CNTDOWNDEV_RESPONSE = { SET_REQUEST_RESPONSE, CNTDOWNDEV, 0x00 };
    public static byte[] SET_CNTDOWNDEV_NO_RESPONSE = { SET_REQUEST_NO_RESPONSE, CNTDOWNDEV, 0x00 };
    public static int CNTDOWNDEV_BYTE_SIZE = 8;
    public static int CNTDOWNDEV_RESULT_LEN = 32;

    /// 检测器功能设置

    public static byte OSCILLATOR_FREQUENCY_SENSITIVITY = (byte)0xe2;
    public static byte[] DETECTOR_SENSITIVITY = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x0b, 0x00 };
    public static byte[] DETECTOR_SENSITIVITY_1_1_8 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x0b, 0x00 };
    public static byte[] DETECTOR_SENSITIVITY_1_9_16 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x0c, 0x00 };
    public static byte[] DETECTOR_SENSITIVITY_2_1_8 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x0b, 0x01 };
    public static byte[] DETECTOR_SENSITIVITY_2_9_16 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x0c, 0x01 };
    public static byte OSCILLATOR_FREQUENCY = 0x19;
    public static byte[] DETECTOR_OSCILLATOR_FREQUENCY = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, OSCILLATOR_FREQUENCY, 0x00 };
    public static int DETECTOR_SENSITIVITY_BYTE_SIZE = 11;
    public static byte[] GET_DETECTOR_SENSITIVITY_1_1_8 = { GET_REQUEST, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x05, 0x00 };
    public static byte[] GET_DETECTOR_SENSITIVITY_1_9_16 = { GET_REQUEST, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x06, 0x00 };
    public static byte[] GET_DETECTOR_SENSITIVITY_2_1_8 = { GET_REQUEST, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x05, 0x01 };
    public static byte[] GET_DETECTOR_SENSITIVITY_2_9_16 = { GET_REQUEST, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x06, 0x01 };
    public static byte[] GET_DETECTOR_OSCILLATOR_FREQUENCY_1 = { GET_REQUEST, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x18, 0x00 };
    public static byte[] GET_DETECTOR_OSCILLATOR_FREQUENCY_2 = { GET_REQUEST, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x18, 0x01 };
    //灵敏度数值设置
    public static byte[] SET_DETECTOR_SENSITYVITY_DIG_1_1_7 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x15, 0x00 };
    public static byte[] SET_DETECTOR_SENSITYVITY_DIG_1_8_14 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x16, 0x00 };
    public static byte[] SET_DETECTOR_SENSITYVITY_DIG_1_15_16 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x17, 0x00 };
    public static byte[] SET_DETECTOR_SENSITYVITY_DIG_2_1_7 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x15, 0x01 };
    public static byte[] SET_DETECTOR_SENSITYVITY_DIG_2_8_14 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x16, 0x01 };
    public static byte[] SET_DETECTOR_SENSITYVITY_DIG_2_15_16 = { SET_REQUEST_RESPONSE, OSCILLATOR_FREQUENCY_SENSITIVITY, 0x00, 0x17, 0x01 };

    // 信号机通信及控制
    //查看信号机当前的状态。温度，湿度等
    public static byte[] TSC_MONITOR_STAUTS = { GET_REQUEST, (byte)0xff, 0x00 };

    public static byte[] TSC_CONTROL_REBOOT = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00,0x01 };
    public static byte[] TSC_CONTROL_SELF_CHECK = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, 0x02 };
    public static byte[] TSC_CONTROL_UPDATE = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, 0x04 };
    public static byte[] TSC_CONTROL_RESET_PROGRAM = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, 0x08 };
    public static byte[] TSC_CONTROL_CLEAR_SERIOUSNESS_EVENT = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, 0x10 };
    public static byte[] TSC_CONTROL_CLEAR_EVENT = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, 0x20 };
    public static byte[] TSC_CONTROL_DELETE_STATISTICS = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, 0x40 };
    public static byte[] TSC_CONTROL_DELETE_LOG = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x00, (byte)0x80 };
    public static byte[] TSC_CONTROL_COMMUNICATION = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, 0x01, 0x00 };
    public static byte[] TSC_CONTROL_EXTEND = { SET_REQUEST_RESPONSE, (byte)0xf6, 0x01, (byte)0x80, 0x00 };
     //检测器类型

    public static byte DETECTOR_TYPE_STATEGY_BUS = 0x14;
    public static byte DETECTOR_TYPE_REACTION_BUS = 0x44;
    public static byte DETECTOR_TYPE_TACTICS_BUS = 0x24;
    public static byte DETECTOR_TYPE_REQUEST_BUS = (byte)0x84;

    public static byte DETECTOR_TYPE_STATEGY_CAR = 0x11;
    public static byte DETECTOR_TYPE_REACTION_CAR = 0x41;
    public static byte DETECTOR_TYPE_TACTICS_CAR = 0x21;
    public static byte DETECTOR_TYPE_REQUEST_CAR = (byte)0x81;

    public static byte DETECTOR_TYPE_STATEGY_HUMAN = 0x18;
    public static byte DETECTOR_TYPE_REACTION_HUMAN = 0x48;
    public static byte DETECTOR_TYPE_TACTICS_HUMAN = 0x28;
    public static byte DETECTOR_TYPE_REQUEST_HUMAN =(byte) 0x88;

    public static byte DETECTOR_TYPE_STATEGY_BIKE = 0x12;
    public static byte DETECTOR_TYPE_REACTION_BIKE = 0x42;
    public static byte DETECTOR_TYPE_TACTICS_BIKE = 0x22;
    public static byte DETECTOR_TYPE_REQUEST_BIKE = (byte)0x82;
    /// 倒计时相关
    public static byte[] ECHO_TSC_COUNT_DOWN = { SET_REQUEST_RESPONSE, (byte)0xe6, 0x00, 0x01 };
    public static byte[] ECHO_TSC_COUNT_DOWN_CANCEL = { SET_REQUEST_RESPONSE, (byte)0xe6, 0x00, 0x00 };
    public static int ECHO_COUNT_DOWN_BYTE_SIZE = 5;
    public static int ECHO_COUNT_DOWN_RESULT_LEN = 8;

   /// 升级程序
    public static byte[] UPDATE_TSC_START = { SET_REQUEST_RESPONSE, (byte)0xe4, 0x00, 0x04, 0x01 };
    public static byte[] UPDATE_TSC_FINISH = { SET_REQUEST_RESPONSE, (byte)0xe4, 0x00, 0x04, 0x02 };
    public static byte[] UPDATE_TSC_REVERSE = { SET_REQUEST_RESPONSE, (byte)0xe4, 0x00, 0x04, 0x03 };
    //public static byte[] UPDATE_TSC_REVERSE = { SET_REQUEST_RESPONSE, 0xe4, 0x00, 0x04, 0x03 };
    public static String FTP_NAME = "root";
    public static String FTP_PASSWD = "aiton";

    public static byte[] UPDATE_DATABASE_START = { SET_REQUEST_RESPONSE, (byte)0xe4, 0x00, 0x05, 0x01 };
    public static byte[] UPDATE_DATABASE_FINISH = { SET_REQUEST_RESPONSE, (byte)0xe4, 0x00, 0x05, 0x02 };
    public static byte[] UPDATE_DATABASE_REVERSE = { SET_REQUEST_RESPONSE, (byte)0xe4, 0x00, 0x05, 0x03 };

}
