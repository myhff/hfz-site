package hff.elegant.blog.commons;

import static java.lang.String.format;

/**
 * 分页处理
 * 
 * @author ZhangYajun <br /> */
public final class Paging {
    private static final String t_2 = "%s LIMIT %s, %s";

    /** 获取分页SQL语句 */
    public String paginal(String sql) {
        int s = (index - 1) * size;
        if (s < 0)
            s = 0;
        return format(t_2, sql, s, size);
    }
 
    private int index = 1;               // 页索引
 
    private int size  = 10;              // 页大小
 
    private int totalRecordsNumber = 0;  // 总记录
 
    public int getPageCount() {          // 总页数
        return (int) Math.ceil((double) totalRecordsNumber / size);
    }
 
    public int getPageIndex() {
        return index;
    }
    public Paging setPageIndex(int index) {
        this.index = index > 1 ? index : 1;
        return this;
    }
 
    public int getPageSize() {
        return size;
    }
    public Paging setPageSize(int size) {
        this.size = size > 0 ? size : this.size;
        return this;
    }
 
    public int getTotalRecordsNumber() {
        return totalRecordsNumber;
    }
    public void setTotalRecordsNumber(int totalRecordsNumber) {
        this.totalRecordsNumber = totalRecordsNumber;
    }
}
