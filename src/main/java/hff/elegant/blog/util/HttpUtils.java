package hff.elegant.blog.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author ZhangYajun
 *
 * @since 
 */
public class HttpUtils {

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getHeader("Host");
        }
        if (StringUtils.isBlank(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip)) {
            ip = "0.0.0.0";
        }
        return ip;
    }

    public static String[] ShortText(String string) {
        String key = "XuLiang";                 //自定义生成MD5加密字符串前的混合KEY
        String[] chars = new String[]{          //要使用生成URL的字符
            "a","b","c","d","e","f","g","h", 
            "i","j","k","l","m","n","o","p", 
            "q","r","s","t","u","v","w","x", 
            "y","z","0","1","2","3","4","5", 
            "6","7","8","9","A","B","C","D", 
            "E","F","G","H","I","J","K","L", 
            "M","N","O","P","Q","R","S","T", 
            "U","V","W","X","Y","Z" 
        };

        String hex = Md5Utils.MD5Encode(key + string);
        int hexLen = hex.length();
        int subHexLen = hexLen / 8;
        String[] ShortStr = new String[4];

        for (int i = 0; i < subHexLen; i++) {
            String outChars = "";
            int j = i + 1;
            String subHex = hex.substring(i * 8, j * 8);
            long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);

            for (int k = 0; k < 6; k++) {
                int index = (int) (Long.valueOf("0000003D", 16) & idx);
                outChars += chars[index];
                idx = idx >> 5;
            }
            ShortStr[i] = outChars; 
        }
        return ShortStr;
    }

    public static void main(String[] args) {
        String url = "http://www.sunchis.com";
        for (String string : ShortText(url)) {
            System.out.println(string);
        } 
    }
}
