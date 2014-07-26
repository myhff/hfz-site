package hff.elegant.blog.pojo;

import hff.elegant.blog.commons.search.Searchable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 存储文章（包括页面、上传文件、修订）
 * 
 * @author ZhangYajun
 *
 * @since 
 */
public class Post implements java.io.Serializable, Searchable {
    private static final long serialVersionUID = -5114523541049942149L;

    long   id;                  //自增唯一ID
    long   catId;               //分类编号
    String title;               //标题
    String content;             //正文
    String excerpt;             //摘录
    String tags;                //关键字
    int    type;                //博文类型 0原创，1转载，2草稿
    String originUrl;           //引用地址
    Date   createTime;          //发布时间
    Date   modifiedTime;        //修改时间
    int    commentCount;        //评论数
    int    viewCount;           //访问数
    int    asTop;               //置顶

    public Post() {
    }
    
    public Post(long id) {
        this.id = id;
    }

    Catalog catalog = new Catalog();

    public Catalog getCatalog() {
        return catalog;
    }
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getCatId() {
        return catId;
    }
    public void setCatId(long catId) {
        this.catId = catId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getExcerpt() {
        return excerpt;
    }
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getOriginUrl() {
        return originUrl;
    }
    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getViewCount() {
        return viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    public int getAsTop() {
        return asTop;
    }
    public void setAsTop(int asTop) {
        this.asTop = asTop;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    @Override
    public int hashCode() {
        System.out.println(37 * 17 + (int) (id ^ (id >>> 32)));
        return 37 * 17 + (int) (id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Post && ((Post) obj).id == this.id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }

    @Override
    public int compareTo(Searchable o) {
        return 0;
    }
    @Override
    public long id() {
        return getId();
    }
    @Override
    public List<String> storeFields() {
        return Arrays.asList("title");
    }
    @Override
    public List<String> indexFields() {
        return Arrays.asList("title", "excerpt", "content");
    }
}
