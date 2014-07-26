package hff.elegant.blog.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 存储分类
 * 
 * @author ZhangYajun
 *
 * @since 
 */
public class Catalog implements java.io.Serializable {
    private static final long serialVersionUID = 4189803474959647484L;

    long   catId;     //AutoID
    String name;      //分类名
    String slug;      //缩略名
    long   parent;    //父分类
    int    sortOrder; //显示顺序
    int    count;     //文章总数
    
    public long getCatId() {
        return catId;
    }
    public void setCatId(long catId) {
        this.catId = catId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public long getParent() {
        return parent;
    }
    public void setParent(long parent) {
        this.parent = parent;
    }
    public int getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
