package test;

import hff.elegant.blog.dao.db.SqlTools;
import hff.elegant.blog.util.CryptUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 
 * @author ZhangYajun
 *
 * @since 
 */
public class TestApp {

    public static void testMapping() throws Exception {
        /*
        System.out.println(DigestUtils.md2("index"));
        */
        SqlTools.queryForInt("select count(*) from hf_posts");
        /*
        String path = "http://hanfeizi.com/category/python/page/22341/";
        System.out.println(path);
        Matcher m = Pattern.compile("\\/page\\/\\d{1,5}\\/").matcher(path);
        String page = m.find() ? m.group(0) : null;
        path = path.replaceAll("page\\/\\d{1,5}\\/", "");
        System.out.println(path);
        if (null != page) {
            String pageNum = page.substring(page.indexOf("/", 1) + 1, page.length() - 1);
            System.out.println(pageNum);
        }
        */
    }

    public static void main(String[] args) {
        String key = "a1b2c3d4e5f6g7|xmasmail@126.com|3|sii3|fdks";
        System.out.println(_Encrypt(key));
        System.out.println(_Decrypt("Zz9nKfmkC37HSRyjhh9Fza6GXKsEFKCIZhiQlE8XteeszYPGZUkpQrmKETf%2BYDbN"));

        String key2 = "MVthcTbXaofy6ZjqUtes3ubd6P3MJ887k%2BPo0CA2243qFoQpemItu6CHI7hFhvoz";
        System.out.println(_Decrypt(key2));

        System.out.println(_encode("昵称jd_xmasma"));
        
        System.out.println(DigestUtils.md5Hex("..."));

        String str = "Hello World";  
        try{  
            byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));  
            System.out.println("RESULT: " + new String(encodeBase64));  
            System.out.println(Base64.decodeBase64("1213765179553087490"));
        } catch(UnsupportedEncodingException e){  
            e.printStackTrace();  
        }  
    }

    public static final String G_USER = "g_user";

    public final static int MAX_AGE = 86400 * 365;
    public final static String COOKIE_LOGIN = "skey";

    public final static byte[] E_KEY = new byte[]{'1','2','3','4','5','6','7','8'};

    public static String _encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String _decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 加密
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static String _Encrypt(String value) {
        byte[] data = CryptUtils.encrypt(value.getBytes(), E_KEY);
        try {
            return URLEncoder.encode(new String(Base64.encodeBase64(data)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 解密
     * @param value
     * @return
     * @throws Exception
     */
    public static String _Decrypt(String value) {
        try {
            value = URLDecoder.decode(value, "UTF-8");
            if(StringUtils.isBlank(value)) return null;
            byte[] data = Base64.decodeBase64(value.getBytes());
            return new String(CryptUtils.decrypt(data, E_KEY));
        } catch (UnsupportedEncodingException excp) {
            return null;
        }
    }

    /**
     * 加延迟时间
     */
    public static Date calculateDate(Date date, int day){
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 加延迟时间
     */
    public static Date calculateDate2(Date date, int minutes){
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
