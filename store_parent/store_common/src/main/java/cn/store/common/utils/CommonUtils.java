package cn.store.common.utils;

import cn.store.common.bean.ResponseBean;
import cn.store.common.constant.ErrorEnum;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    private static char md5Chars[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static boolean isNull(Object object) {
        if (object == null)
            return true;
        return false;
    }

    public static boolean isNotNull(Object object) {
        if (object == null)
            return false;
        return true;
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(new Date().getTime());
    }

    public static String getRandowUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()))
            return true;
        return false;
    }

    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str.trim()))
            return true;
        return false;
    }
    //中文转Unicode
    public static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
           String char_16 = Integer.toString(chars[i], 16);
           if (char_16.length()<4){
               returnStr += "\\u00"+char_16;
           }else {
               returnStr += "\\u"+char_16;
           }
        }
        return returnStr.toString();
    }
    //Unicode转中文
    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        StringBuffer returnStr = new StringBuffer();
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr.append((char) Integer.valueOf(strs[i], 16).intValue());
        }
        return returnStr.toString();
    }

    /**
     * 解析命令字符串
     *
     * @param command
     * @return
     * @author fan xiao chun
     * @date 2016年9月5日
     */
    public static Map<String, String> parseCommand(String command) {
        Map<String, String> cmdMap = new HashMap<String, String>();
        String[] items = command.split("&");
        for (String item : items) {
            int splitIndex = item.indexOf("=");
            String key = item.substring(0, splitIndex);
            String value = item.substring(splitIndex + 1);
            cmdMap.put(key, value);
        }
        return cmdMap;
    }

    /**
     * 四舍五入
     *
     * @param num
     * @param digit
     * @return
     * @author fan xiao chun
     * @date 2016年9月21日
     */
    public static float getRound(float num, int digit) {
        BigDecimal b = new BigDecimal(num);
        float f1 = b.setScale(digit, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentMillisecond() {
        return new Date().getTime();
    }

    /**
     * 按指定格式解析日期
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Timestamp parseDate(String date, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return new Timestamp(dateFormat.parse(date).getTime());
    }

    /**
     * 获取本机ip
     *
     * @return
     * @date 2017年3月12日
     * @author fanxiaochun
     */
    public static String getLocalIp() {
        try {
            if (isWindowsOS()) {
                return InetAddress.getLocalHost().getHostAddress();
            } else {
                return getLinuxLocalIp();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private static String toHexString(byte[] b) {
        char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String Bit32(String SourceString) {
        try {

            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(SourceString.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Bit16(String SourceString) {
        try {
            return Bit32(SourceString).substring(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getShortSide(String res) {
        String[] resArr = res.split("x");
        int width = Integer.parseInt(resArr[0]);
        int height = Integer.parseInt(resArr[1]);

        if (width > height)
            return height;

        return width;
    }

    /**
     * 获取短边长度
     *
     * @param width
     * @param height
     * @return
     */
    public static int getShortSide(int width, int height) {
        if (width > height)
            return height;

        return width;
    }

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes("UTF-8"));
        byte digest[] = md5.digest();
        char chars[] = toHexChars(digest);
        return new String(chars);
    }

    private static char[] toHexChars(byte digest[]) {
        char chars[] = new char[digest.length * 2];
        int i = 0;
        byte abyte0[] = digest;
        int j = abyte0.length;
        for (int k = 0; k < j; k++) {
            byte b = abyte0[k];
            char c0 = md5Chars[(b & 0xf0) >> 4];
            chars[i++] = c0;
            char c1 = md5Chars[b & 0xf];
            chars[i++] = c1;
        }

        return chars;
    }


    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本地Host名称
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                            ip = ipaddress;
                        }
                    }
                }
            }
        }

        return ip;
    }


    /**
     * 文件大小格式化
     *
     * @param bSize b单位
     * @return
     */
    public static String sizeFormat(long bSize) {
        String size = "";
        float kbSize = bSize / 1024.0f;
        if (kbSize >= 1.0f) {
            float mbSize = kbSize / 1024.0f;
            if (mbSize >= 1.0f) {
                float gbSize = mbSize / 1024.0f;
                if (gbSize >= 1.0f) {
                    size = getRound(gbSize, 2) + "G";
                } else {
                    size = getRound(mbSize, 2) + "M";
                }
            } else {
                size = getRound(kbSize, 2) + "K";
            }
        } else {
            size = getRound(bSize, 2) + "B";
        }
        return size;
    }

    /**
     * 相比返回最大值
     *
     * @param width
     * @param height
     * @return
     */
    public static int max(int width, int height) {
        if (width > height)
            return width;
        return height;
    }

    /**
     * 将秒转换成固定格式时分秒输出
     *
     * @param second
     * @param separator
     * @return
     * @author fan xiao chun
     */

    public static String secondTimeFormat(long second, String separator) {
        int secondCardinal = 1;
        int minuteCardinal = secondCardinal * 60;
        int hourCardinal = minuteCardinal * 60;

        String hourStr = null;
        String minuteStr = null;
        String secondStr = null;

        long hour = 0;
        long minute = 0;
        String timeStr = null;

        if ((hour = second / hourCardinal) > 0)
            second = second - hourCardinal * hour;
        if ((minute = second / minuteCardinal) > 0)
            second = second - minuteCardinal * minute;

        hourStr = (String.valueOf(hour).length() == 1) ? "0" + hour : hour + "";
        minuteStr = (String.valueOf(minute).length() == 1) ? "0" + minute : minute + "";
        secondStr = (String.valueOf(second).length() == 1) ? "0" + second : second + "";
        return hourStr + separator + minuteStr + separator + secondStr;
    }

    /**
     * 将固定格式时分秒输出转换成秒
     *
     * @param timeStr
     * @param separator
     * @return
     * @author fan xiao chun
     */
    public static long timeStrToSecond(String timeStr, String separator) {
        int secondCardinal = 1;
        int minuteCardinal = secondCardinal * 60;
        int hourCardinal = minuteCardinal * 60;

        String[] timeArr = timeStr.split(separator);
        long hour = Long.parseLong(timeArr[0]);
        long minute = Long.parseLong(timeArr[1]);
        long second = Long.parseLong(timeArr[2]);
        return hour * hourCardinal + minute * minuteCardinal + second;
    }

    /**
     * 获取异常日志
     *
     * @param ex
     * @return
     */
    public static String getExceptioniInformation(Throwable ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
            return null;
        }
        return ret;
    }


    /**
     * 返回当前时间,格式HH:mm:ss new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
     *
     * @return String 当前时间
     */
    public static String getCurrentDate(String formatStr) {
        String time = null;
        try {
            DateFormat myformat = new SimpleDateFormat(formatStr);
            time = myformat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static boolean isRequestSuccess(ResponseBean responseBean) {
        if (ErrorEnum.SUCCESS.getErrorCode() == responseBean.getErrorCode()) {
            return true;
        }
        return false;
    }


    /**
     * 将字符串匹配替换成占位符形式
     *
     * @param str
     * @param separator
     * @return
     */
    public static String format(String str, String regex, String separator) {
        String temp = "";
        String[] pathArr = null;
        if (str.startsWith(separator)) {
            temp = separator;
            pathArr = str.replaceFirst(separator, "").split(separator);
        } else {
            pathArr = str.split(separator);
        }
        int perchIndex = 0;
        String path = null;
        for (int i = 0; i < pathArr.length; i++) {
            path = pathArr[i];
            if (path.matches("\\d+") || path.indexOf("%2C") >= 0 || path.indexOf(",") >= 0) {
                //path = path.replaceFirst("(\\d+)", String.format("%s%d%s", "{", perchIndex, "}"));
                path = "{" + perchIndex + "}";
                perchIndex++;
            }
            temp += path;
            if (i != pathArr.length - 1) {
                temp += separator;
            }
        }
        return temp;
    }


    /**
     * 判断是否是网络地址
     *
     * @param url
     */
    public static boolean isHttpUrl(String url) {
        if (isNotEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return true;
            }
        }
        return false;
    }

    public static Timestamp getTimestamp(String timeStr, String fromPattern) {
        Timestamp dateTime = null;
        try {
            SimpleDateFormat formatter = null;
            if (isEmpty(fromPattern)) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                formatter = new SimpleDateFormat(fromPattern);
            }
            Date day = formatter.parse(timeStr);
            dateTime = new Timestamp(day.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 获取HTML-IMG的SRC值
     */
    public static List<String> getSrcValue(String text) {
        List<String> srcList = new ArrayList<String>();
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(text);
        while (m_img.find()) {
            Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
            Matcher m_src = p_src.matcher(m_img.group(2));
            if (m_src.find()) {
                String str_src = m_src.group(3);
                srcList.add(str_src);
            }
        }
        return srcList;
    }

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Long amount) throws Exception {
        if (!amount.toString().matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        int flag = 0;
        String amString = amount.toString();
        if (amString.charAt(0) == '-') {
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if (amString.length() == 1) {
            result.append("0.0").append(amString);
        } else if (amString.length() == 2) {
            result.append("0.").append(amString);
        } else {
            String intString = amString.substring(0, amString.length() - 2);
            for (int i = 1; i <= intString.length(); i++) {
                if ((i - 1) % 3 == 0 && i != 1) {
                    result.append(",");
                }
                result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
            }
            result.reverse().append(".").append(amString.substring(amString.length() - 2));
        }
        if (flag == 1) {
            return "-" + result.toString();
        } else {
            return result.toString();
        }
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static String changeY2F(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

}
