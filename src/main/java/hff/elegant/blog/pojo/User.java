package hff.elegant.blog.pojo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 会员
 * 
 * @author ZhangYajun
 *
 * @since 
 */
public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1112870896173281275L;

    int    id;            //用户编号
    String email;         //邮箱
    String password;      //密码
    String nicename;      //昵称
    String url;           //网址
    Date   regTime;       //注册时间

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNicename() {
        return nicename;
    }
    public void setNicename(String nicename) {
        this.nicename = nicename;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Date getRegTime() {
        return regTime;
    }
    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
