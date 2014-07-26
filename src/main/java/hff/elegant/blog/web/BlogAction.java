package hff.elegant.blog.web;

import hff.elegant.blog.commons.Paging;
import hff.elegant.blog.commons.search.HiLucene;
import hff.elegant.blog.dao.CatalogDao;
import hff.elegant.blog.dao.PostDao;
import hff.elegant.blog.infra.mvc.ActionBindContext;
import hff.elegant.blog.infra.mvc.ActionDriven;
import hff.elegant.blog.infra.mvc.ActionBindContext.ActionBind;
import hff.elegant.blog.infra.mvc.ActionBindContext.URLMapping;
import hff.elegant.blog.pojo.Catalog;
import hff.elegant.blog.pojo.Post;
import hff.elegant.blog.service.AppManager;
import hff.elegant.blog.util.RequestUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Query;

import test.TestApp;

/**
 * 博客Action
 * 
 * @author ZhangYajun
 *
 * @since 
 */
@ActionBind
public class BlogAction extends ActionDriven {
    private static final long serialVersionUID = -1773772161397288011L;

    /** 设置或读取点击分类Cookie */
    void setHitCatToCookie(Catalog cat) {
        String v = String.format("%s,%s", cat.getSlug(), cat.getName());
        RequestUtils.setCookie(super.request(), super.response(), "hit_cat", TestApp._encode(v), 24*60*60);

        super.request().setAttribute("hitCatSlug", cat.getSlug());
        super.request().setAttribute("hitCatName", cat.getName());
    }
    static void readHitCatFromCookie() {
        HttpServletRequest request = ActionBindContext.getContext().getRequest();
        String cooHitCat = RequestUtils.getCookieValue(request, "hit_cat");
        if (null != cooHitCat) {
            cooHitCat = TestApp._decode(cooHitCat);
            request.setAttribute("hitCatSlug", cooHitCat.split(",")[0]);
            request.setAttribute("hitCatName", cooHitCat.split(",")[1]);
        }
    }

    /** 相关文章 */
    void searchRelatedPosts(Post article, Paging page) throws Exception {
        String indexPath = (String) ActionBindContext.getContext().getServletContext().getAttribute("indexPath");
        HiLucene lu = HiLucene.init(indexPath);

        Query query = HiLucene.makeQuery("title", article.getTitle(), 5f);
        page.setPageSize(6);
        page.setTotalRecordsNumber(1);

        List<Long> list = lu.find(Post.class, query, null, null, page);
        list.remove(article.getId());

        List<Post> listPosts = new AppManager().listPosts(list.toArray(new Long[] {}));
        super.request().setAttribute("listRelatedPosts", listPosts);
    }

    /** 浏览文章 */
    @URLMapping("/post/{id}.html")
    public String m1() throws Exception {
        HomeAction.loadAside();

        readHitCatFromCookie();

        String id = (String) super.request().getAttribute("id");
        try {
            PostDao dao = new PostDao();
            Post[] ones = dao.getByIdAndAdjacent(Integer.parseInt(id));

            if (null == ones[1]) 
                return "404";

            super.request().setAttribute("one_last", ones[0]);
            super.request().setAttribute("one", ones[1]);
            super.request().setAttribute("one_next", ones[2]);

            this.searchRelatedPosts(ones[1], new Paging());

            // 更新浏览次数
            ones[1].setViewCount(ones[1].getViewCount()+1);
            dao.updateViewCount(ones[1].getId(), ones[1].getViewCount());

        } catch (NumberFormatException e) {
            return "404";
        }
        super.forward("/[B@1Post.jsp");
        return null;
    }

    /** 分类筛选 */
    @URLMapping("/category/{slug}/")
    public void m2() {
        HomeAction.loadAside();

        String slug = (String) super.request().getAttribute("slug");

        Catalog cat = new CatalogDao().getBySlug(slug);
        if (null == cat) {
            super.forward("/404");
            return;
        }

        setHitCatToCookie(cat);

        super.request().setAttribute("hit_cat", cat);
        super.request().setAttribute("title", " &raquo; " + cat.getName());
        super.request().setAttribute("sub_title", String.format("当前分类存档 &#8216;%s&#8216;", cat.getName()));

        // 分页
        Integer idx = (Integer) super.request().getAttribute("pageIndex");
        Paging page = new Paging().setPageIndex(idx.intValue());
        super.request().setAttribute("page", page);

        List<Post> listPosts = new PostDao().listPostByCatId(cat.getCatId(), page, 0);
        super.request().setAttribute("listPosts", listPosts);

        super.forward("/[B@3Home.jsp");
    }
}
