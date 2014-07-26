package hff.elegant.blog.service;

import hff.elegant.blog.commons.Paging;
import hff.elegant.blog.dao.PostDao;
import hff.elegant.blog.pojo.Post;

import java.util.List;


public class AppManager {
    //private static SimpleCache<String, Object> _cache = new SimpleCache<String, Object>(10);

    //@SuppressWarnings("unchecked")
    public List<Post> listPosts(Paging page, int type) {
        /*
        Object cache = _cache.get("list_posts");
        if (null != cache) {
            return (List<Post>) cache;
        }
        */
        List<Post> list = new PostDao().listPosts(page, type);
        //_cache.put("list_posts", list);
        return list;
    }

    public List<Post> listPosts(Long[] idValues) {
        List<Post> list = new PostDao().listPosts(idValues);
        return list;
    }

    /** 总数 */
    public int count(int type) {
        return new PostDao().count(type);
    }
}
