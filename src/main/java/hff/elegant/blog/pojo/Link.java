package hff.elegant.blog.pojo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 存储友情链接（Blogroll）
 * 
 * @author ZhangYajun
 *
 * @since
 */
public class Link implements java.io.Serializable {
    private static final long serialVersionUID = -3816789648510924151L;

    int    linkId;          //自增唯一ID
    String linkUrl;         //链接URL
    String linkName;        //链接标题
    String linkImage;       //连接图片
    String linkTarget;      //链接打开方式
    String linkDescription; //链接描述
    String linkVisible;     //是否可见（Y/N）
    String linkOwner;       //添加者用户ID
    String linkRating;      //评分等级
    Date   linkUpdated;     //未知
    String linkRel;         //XFN关系
    String linkNotes;       //XFN注释
    String linkRss;         //链接RSS地址

    public int getLinkId() {
        return linkId;
    }
    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getLinkName() {
        return linkName;
    }
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
    public String getLinkImage() {
        return linkImage;
    }
    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }
    public String getLinkTarget() {
        return linkTarget;
    }
    public void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget;
    }
    public String getLinkDescription() {
        return linkDescription;
    }
    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }
    public String getLinkVisible() {
        return linkVisible;
    }
    public void setLinkVisible(String linkVisible) {
        this.linkVisible = linkVisible;
    }
    public String getLinkOwner() {
        return linkOwner;
    }
    public void setLinkOwner(String linkOwner) {
        this.linkOwner = linkOwner;
    }
    public String getLinkRating() {
        return linkRating;
    }
    public void setLinkRating(String linkRating) {
        this.linkRating = linkRating;
    }
    public Date getLinkUpdated() {
        return linkUpdated;
    }
    public void setLinkUpdated(Date linkUpdated) {
        this.linkUpdated = linkUpdated;
    }
    public String getLinkRel() {
        return linkRel;
    }
    public void setLinkRel(String linkRel) {
        this.linkRel = linkRel;
    }
    public String getLinkNotes() {
        return linkNotes;
    }
    public void setLinkNotes(String linkNotes) {
        this.linkNotes = linkNotes;
    }
    public String getLinkRss() {
        return linkRss;
    }
    public void setLinkRss(String linkRss) {
        this.linkRss = linkRss;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
