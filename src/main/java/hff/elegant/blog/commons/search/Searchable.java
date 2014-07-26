package hff.elegant.blog.commons.search;

import java.util.List;

/**
 * 可搜索对象
 * User: Winter Lau
 * Date: 13-1-10
 * Time: 上午11:31
 */
public interface Searchable extends Comparable<Searchable> {

    /**
     * 文档的唯一编号
     * @return 文档id
     */
    public long id();
    public void setId(long id);

    /**
     * 要存储的字段
     * @return 返回字段名列表
     */
    public List<String> storeFields();

    /**
     * 要进行分词索引的字段
     * @return 返回字段名列表
     */
    public List<String> indexFields();
}
