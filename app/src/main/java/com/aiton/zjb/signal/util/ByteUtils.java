package com.aiton.zjb.signal.util;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 14-2-14.
 */
public class ByteUtils {
    /**
     * 将一维数组，转换成二维数据，也就是每条记录
     * @param bytes
     * @param row
     * @param column
     * @return
     */
    public static byte[][] oneArrayToTwoArray(byte[] bytes, int row, int column){
        byte[][] bta = new byte[row][column];
        for(int i=0;i<bytes.length;i++){
            bta[i /column][i% column] = bytes[i];
        }
        return bta;
    }
    //long类型转成byte数组
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();//将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }
    //byte数组转成long
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }
    /**
     * 注释：int到字节数组的转换！
     *
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();//将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }
    /**
     * 注释：字节数组到int的转换！
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[3] & 0xff;// 最低位
        int s1 = b[2] & 0xff;
        int s2 = b[1] & 0xff;
        int s3 = b[0] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    /**
     * 将Byte数组转成无符号整型
     * @param b
     * @return
     */
    public static int bytesUInt(byte b){
        return b&0xff;
    }

    /**
     * 注释：short到字节数组的转换！
     *
     * @param number
     * @return
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();//将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 注释：字节数组到short的转换！
     *
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }
    /**   * float转换byte   *   * @param bb   * @param x   * @param index   */
    public static byte[] floatTobyteArray(float v) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        byte[] ret = new byte[4];
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(v);
        bb.get(ret);
        return ret;
    }
    /**   * 通过byte数组取得float   *   * @param bb   * @param index   * @return   */
    public static float byteArrayToFloat(byte[] v) {
        ByteBuffer bb = ByteBuffer.wrap(v);
        FloatBuffer fb = bb.asFloatBuffer();
        return fb.get();
    }

    /**   * double转换byte   *   * @param bb   * @param x   * @param index   */
    public static byte[] doubleToByteArray(double x) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        byte[] ret = new byte[8];
        DoubleBuffer fb = bb.asDoubleBuffer();
        fb.put(x);
        bb.get(ret);
        return ret;
    }

    /**   * 通过byte数组取得float   *   * @param bb   * @param index   * @return   */
    public static double byteArrayToDouble(byte[] b) {
        ByteBuffer bb = ByteBuffer.wrap(b);
        DoubleBuffer fb = bb.asDoubleBuffer();
        return fb.get();
    }

    /**
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String byteArrayToStringByUTF8(byte[] bytes) throws Exception{
        return new String(bytes,"UTF-8");
    }

    /**
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String byteArrayToStringByISO(byte[] bytes) throws Exception{
        return new String(bytes,"ISO-8859-1");
    }

    /**
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static byte[] stringToByteArrayByUTF8(String s) throws Exception{
        return s.getBytes("UTF-8");
    }

    /**
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static byte[] stringToByteArrayByISO(String s) throws  Exception{
        return s.getBytes("ISO-8859-1");
    }
    public static byte[] stringToByteArrayByASCII(String s) throws Exception{
        return s.getBytes("US-ASCII");
    }

    /**
     * 数组转换成十六进制字符串
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 把数字字符转化成数字数组
     * @param binaryStrFromByteArr
     * @return
     */
    public static int[] byteArrToIntArr(String binaryStrFromByteArr){
        char[] digitNumberArray = binaryStrFromByteArr.toCharArray();
        int[] digitArry = new int[digitNumberArray.length];
        for (int i = 0; i < digitNumberArray.length; i++) {
            digitArry[i] = digitNumberArray[i]-'0';
        }
        return digitArry;
    }

    /**
     * 把字节数组转化成二进制字符串
     * @param bArr
     * @return
     */
    public static String getBinaryStrFromByteArr(byte[] bArr){
        String result ="";
        for(byte b:bArr ){
            result += getBinaryStrFromByte(b);
        }
        return result;
    }

    /**
     * 把byte转化成2进制字符串
     * @param b
     * @return
     */
    public static String getBinaryStrFromByte(byte b){
        String result ="";
        byte a = b; ;
        for (int i = 0; i < 8; i++){
            byte c=a;
            a=(byte)(a>>1);//每移一位如同将10进制数除以2并去掉余数。
            a=(byte)(a<<1);
            if(a==c){
                result="0"+result;
            }else{
                result="1"+result;
            }
            a=(byte)(a>>1);
        }
        return result;
    }

    /**
     * 将二进制字符串转换回字节
     * @param bString
     * @return
     */
    public static byte bit2byte(String bString){
        byte result=0;
        for(int i=bString.length()-1,j=0;i>=0;i--,j++){
            result+=(Byte.parseByte(bString.charAt(i)+"")*Math.pow(2, j));
        }
        return result;
    }

    /**
     * 合并两个byte数组
     */
    public static byte[] byteMerger(byte[] bytes1,byte[] bytes2){
        byte[] bytes3 = new byte[bytes1.length+bytes2.length];
        System.arraycopy(bytes1,0,bytes3,0,bytes1.length);
        System.arraycopy(bytes2,0,bytes3,bytes1.length,bytes2.length);
        return bytes3;
    }

    /**
     * 把16进制字符串转换成字节数组
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        hex = hex.toUpperCase();
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
}
