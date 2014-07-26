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
}
