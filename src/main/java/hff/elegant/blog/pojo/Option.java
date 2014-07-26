package hff.elegant.blog.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 存储WordPress系统选项和插件、主题配置
 * 
 * @author ZhangYajun
 *
 * @since 
 */
public class Option implements java.io.Serializable {
    private static final long serialVersionUID = 3248755890788672101L;

    int    optionId;    //AutoID
    int    blogId;      //博客ID，用于多用户博客，默认0
    String optionName;  //键名
    String optionValue; //键值
    String autoload;    //在系统载入时自动载入（yes/no）

    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
    public int getBlogId() {
        return blogId;
    }
    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    public String getOptionName() {
        return optionName;
    }
    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
    public String getOptionValue() {
        return optionValue;
    }
    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
    public String getAutoload() {
        return autoload;
    }
    public void setAutoload(String autoload) {
        this.autoload = autoload;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}
