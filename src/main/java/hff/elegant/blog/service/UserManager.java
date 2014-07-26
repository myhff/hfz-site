package hff.elegant.blog.service;

import hff.elegant.blog.dao.UserDao;
import hff.elegant.blog.infra.mvc.ActionBindContext;
import hff.elegant.blog.pojo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import test.TestApp;

/**
 * @author ZhangYajun <br />
 * 
 * @since
 */
public class UserManager {

    /**
     * 保存登录信息
     * @param req
     * @param res
     * @param user
     * @param save
     */
    public void saveUserInCookie(IUser user, boolean save) {
        String new_value = _GenLoginKey(user, ActionBindContext.getContext().ip(), 
                ActionBindContext.getContext().header("user-agent"));
        int max_age = save ? 86400 * 30 : -1;
        ActionBindContext.getContext().deleteCookie(TestApp.COOKIE_LOGIN, true);
        ActionBindContext.getContext().cookie(TestApp.COOKIE_LOGIN,new_value,max_age,true);
        ActionBindContext.getContext().deleteCookie("unick", true);
        ActionBindContext.getContext().cookie("unick",TestApp._encode(user.getUnick()),max_age,true);
    }
 
    public void deleteUserInCookie() {
        ActionBindContext.getContext().deleteCookie(TestApp.COOKIE_LOGIN, true);
    }

    /**
     * 生成用户登录标识字符串
     * @param user
     * @param ip
     * @param user_agent
     * @return
     */
    public static String _GenLoginKey(IUser user, String ip, String user_agent) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getId());
        sb.append('|');
        sb.append(user.getPwd());
        sb.append('|');
        sb.append(ip);
        sb.append('|');
        sb.append((user_agent==null)?0:user_agent.hashCode());
        sb.append('|');
        sb.append(System.currentTimeMillis());
        return TestApp._Encrypt(sb.toString());
    }

    /**
     * 登陆
     */
    public User login(String email, String pwd, String ip) throws Exception {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(pwd))
            return null;
        User user = new UserDao().getByEmail(email);
        // pwd要shaHex加密
        if ((user != null && StringUtils.equalsIgnoreCase(user.getPassword(),pwd))) {
            return user;
        }
        return null;
    }

    /**
     * 返回当前登录用户的资料
     */
    public User getLoginUser(HttpServletRequest req) {
        User loginUser = (User) req.getAttribute(TestApp.G_USER);
        if(loginUser == null){
            //从Cookie中解析出用户id
            IUser cookie_user = getUserFromCookie();
             if(cookie_user == null) return null;
            User user = new UserDao().getById(cookie_user.getId());
            if(user != null && StringUtils.equalsIgnoreCase(user.getPassword(), cookie_user.getPwd())) {
                postLogin(req, user);
                return user;
            }
        }
        return loginUser;
    }

    public void postLogin(HttpServletRequest req, User user) {
        // TODO : 更新登陆日志
        // ..
        req.setAttribute(TestApp.G_USER, user);
    }

    /**
     * 从cookie中读取保存的用户信息
     * 
     * @param req
     * @return
     */
    public IUser getUserFromCookie() {
        try {
            Cookie cookie = ActionBindContext.getContext().getCookie(TestApp.COOKIE_LOGIN);
            if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
                return userFromUUID(cookie.getValue());
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 从cookie中读取保存的用户信息
     * @param req
     * @return
     */
    @SuppressWarnings("unused")
    public IUser userFromUUID(String uuid) {
        if(StringUtils.isBlank(uuid))
            return null;
        String ck = TestApp._Decrypt(uuid);
        final String[] items = StringUtils.split(ck, '|');
        if(items.length == 5){
            String ua = ActionBindContext.getContext().header("user-agent");
            int ua_code = (ua==null)?0:ua.hashCode();
            int old_ua_code = Integer.parseInt(items[3]);
            if(ua_code == old_ua_code){
                return new IUser() {
                    public boolean IsBlocked() { return false; }
                    public int getId() { return NumberUtils.toInt(items[0], -1); }
                    public String getPwd() { return items[1]; }
                    public byte getRole() { return IUser.ROLE_GENERAL; }
                    public String getUnick() { return null; }
                };
            }
        }
        return null;
    }
}
