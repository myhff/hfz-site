package hff.elegant.blog.pojo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 评论
 * 
 * @author ZhangYajun
 *
 * @since
 */
public class Comment implements java.io.Serializable {
    private static final long serialVersionUID = 9001397532375045960L;

    int    id;         //AutoID
    int    postId;     //对应文章ID
    String name;       //评论者
    String email;      //评论者邮箱
    String url;        //评论者网址
    String content;    //评论正文
    Date   createDate; //评论时间
    String clientIp;   //评论者IP
    int    parent;     //父评论ID

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getClientIp() {
        return clientIp;
    }
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public int getParent() {
        return parent;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
