package test;

import hff.elegant.blog.commons.Paging;
import hff.elegant.blog.commons.search.HiLucene;
import hff.elegant.blog.commons.search.Searchable;
import hff.elegant.blog.pojo.Post;

import java.util.List;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;


public class TestApp2 {

    static String INDEX_PATH = "d:/lucene/d/../data/index";

    public static void main(String[] args) throws Exception {
        
        HiLucene lu = HiLucene.init(INDEX_PATH);
//        lu.add(getS1());
//        lu.add(getS2());
//        lu.update(getS1());
//        lu.update(getS2());
        
        String queryString = "记录";

        BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
                BooleanClause.Occur.SHOULD,
                BooleanClause.Occur.SHOULD};
        
        Query query = HiLucene.makeQuery(new String[] {"title", "excerpt", "content"}, flags, queryString, 2);

        Paging page = new Paging();
        page.setPageIndex(1);
        page.setPageSize(10);

        List<Long> list = lu.find(Post.class, query, null, null, page);

        System.out.println(list);
    }

    public static Searchable getS1() {
        Post p = new Post();
        p.setId(10001);
        p.setTitle("标题AAA");
        p.setContent("内容AAA");
        return p;
    }

    public static Searchable getS2() {
        Post p = new Post();
        p.setId(20002);
        p.setTitle("标题BBB");
        p.setContent("内容BBB");
        return p;
    }
}
