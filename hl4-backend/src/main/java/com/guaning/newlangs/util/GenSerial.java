package com.guaning.newlangs.util;

import cn.dev33.satoken.util.SaResult;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenSerial {
private static final String Base32Alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    
    /**
     * 生成新的序列号    <br>
     * <p>生成规则：45位的数 （二进制）<br>
     *          标识位  + 数据位 + 校验位 <br>
     * 然后将55位的数映射到用 ABCDEFGHJKLMNPQRSTUVWXYZ23456789 表示的序列号，要映射到32个字符中就是每5位代表一个字符(2^5=32)，
     * 所有生成的序列号是 45/5=9位。
     *  
     * @param codeLen    code长度
     * @param flag    标识
     * @param flagBitLen 标识长度
     * @param checkBitLen 校验位长度
     * @return
     */
    public static String generateNewCode(int codeLen, int flag, int flagBitLen, int checkBitLen) {
        Long ret = 0L; // 长整形ID
        Random random = new Random();
        int checkModData = 1<<checkBitLen;
        int totalBitLen = codeLen*5;
        int dataBitLen = totalBitLen - checkBitLen - flagBitLen;
        long randData = (long)(1 + (1L<<dataBitLen - 1) * random.nextDouble());
        if(flagBitLen > 0){
            flag = flag & ((1<<flagBitLen) - 1);                    //防止越位，若16位标识则是 0xffff
            ret += (long)flag << (totalBitLen - flagBitLen);        //高位标志位
        }
        
        ret += randData << checkBitLen;                         // 中位数据位
        long checkNum = (ret >> checkBitLen) % checkModData;    //低位校验位
        ret += checkNum; // 1 - 7位 校验位
        return convertToBase32SerialCode(ret, codeLen);
    }
    
    public static String generateNewCode(int flag, int flagBitLen) {
        return generateNewCode(9, flag, flagBitLen, 7);        //生成码9位，活动id 16位
    }
    
    public static String generateNewCode(int flag) {
        int flagBitLen = 0;
        if(flag == 0){
            flagBitLen = 0;
        }else{
            flagBitLen = Integer.toBinaryString(flag).length();
        }
        return generateNewCode(9, flag, flagBitLen, 7);        //生成码9位
    }
    
    public static String generateNewCode() {
        return generateNewCode(9, 0, 0, 7);                    //生成码9位
    }
    
    /**
     * 
     * @param historyCodeSet    历史生成的序列号 集合
     * @param number
     * @param codeLen
     * @param flag
     * @param flagBitLen
     * @param checkBitLen
     * @return
     */
    public static Set<String> generateCodes(Set<String> historyCodeSet, int number, int codeLen, int flag, int flagBitLen, int checkBitLen){
        Set<String> generatedCodes = new HashSet<String>(number*4/3+1);
        if(historyCodeSet == null){
            historyCodeSet = new HashSet<String>(0);
        }
        while(generatedCodes.size()<number){
            String code = generateNewCode(codeLen, flag, flagBitLen, checkBitLen);
            if(!historyCodeSet.contains(code)){
                generatedCodes.add(code);
            }
            System.out.println(code);
        }
        return generatedCodes;
    }
    
    
    /**
     * 
     * @param historyCodeSet
     * @param number
     * @return
     */
    public static Set<String> generateCodes(Set<String> historyCodeSet, int number, int codeLen){
        return generateCodes(historyCodeSet, number, codeLen, 0, 0, 7);
    }
    
    /**
     * 
     * @param historyCodeSet
     * @param number
     * @return
     */
    public static Set<String> generateCodes(Set<String> historyCodeSet, int number){
        return generateCodes(historyCodeSet, number, 9, 0, 0, 7);
    }
    
 
    /**
     * 将随机数转换成BASE32编码 序列码
     * 
     * @return
     */
    private static String convertToBase32SerialCode(long longRandValue, int codeLen) {
        StringBuffer codeSerial = new StringBuffer(16);
        long tmpRandValue = longRandValue;
        for (int i = 0; i < codeLen; i++) {
            int code = (int) (tmpRandValue & 0x1F);
            char convertCode = Base32Alphabet.charAt(code);
            codeSerial.append(convertCode);
            tmpRandValue = tmpRandValue >> 5;
        }
        return codeSerial.reverse().toString();
    }
    
    
    /**
     * 将兑换码序列字符转化成数字。
     * 
     * @return
     */
    private static int convertBase32CharToNum(char ch) {
        int index = Base32Alphabet.indexOf(ch);
        return index;
    }
    
    /**
     * 将序列号转成长整数
     * 
     * @return
     */
    public static long convertBase32CharToNum(String serialCode) {
        long id = 0;
 
        for (int i = 0; i < serialCode.length(); i++) {
            int originNum = convertBase32CharToNum(serialCode.charAt(i));
            if(originNum == -1){
                return 0;
            }
            id = id << 5;
            id += originNum;
        }
        return id;
    }
 
    /**
     * 校验序列号是否合法
     * 
     * @param code
     * @return
     */
    public static boolean checkCodeValid(String code, int checkBitLen) {
        long id = 0;
        int checkModData = 1<<checkBitLen;
        for (int i = 0; i < code.length(); ++i) {
            long originNum = convertBase32CharToNum(code.charAt(i));
            if (originNum >= 32)
                return false; // 字符非法
            id = id<<5;
            id += originNum;
        }
 
        long data = id >> checkBitLen;
        long checkNum = id & (checkModData-1); // 最后7位是校验码
 
        if (data % checkModData == checkNum)
            return true;
 
        return false;
    }
    
    public static boolean checkCodeValid(String code) {
        if(code == null || code.length() == 0){
            return false;
        }
        return checkCodeValid(code, 7);
    }
    
    /**
     * 从序列号提取标识
     * 
     * @param code    序列号
     * @param flagBitLen    标识位长度
     * @return
     */
    public static Long getFlagFromCode(String code, int flagBitLen){
        long id = convertBase32CharToNum(code);
        return id >> (code.length()*5-flagBitLen);    
    }
    
    public static String createCode() {
        System.out.println(checkCodeValid("ARXX2BWTE"));
        long sTime = System.currentTimeMillis();
        long eTime = 0L; 
        
        Set<String> codes = generateCodes(null, 1, 10, 3, 2, 7);
        eTime = System.currentTimeMillis();
        System.out.println("耗时 " + (eTime-sTime)/1000 + "秒");
        sTime = eTime;
        
        Set<String> codes2 = generateCodes(codes, 2, 10, 0, 0, 7);
        codes2.size();
        eTime = System.currentTimeMillis();
        System.out.println("耗时 " + (eTime-sTime)/1000 + "秒");
        
        String code = generateNewCode(1,10);
        System.out.println("序列号: "+code);
        boolean checkRs = checkCodeValid(code);
        System.out.println("序列号" + code + "是否合法：" + checkRs);
        long acId = getFlagFromCode(code,10);
        long acId1=getFlagFromCode("5DJX9MXCJU",2);
        System.out.println("标识: " + acId);
        System.out.println("标识: " + acId1);
        long numCode = convertBase32CharToNum(code);
        System.out.println("数字序列号 " + numCode);
        
        return String.valueOf(numCode);
    }

}