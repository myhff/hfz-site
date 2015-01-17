package hff.elegant.blog.web;

import hff.elegant.blog.commons.Paging;
import hff.elegant.blog.commons.SimpleCache;
import hff.elegant.blog.commons.search.HiLucene;
import hff.elegant.blog.dao.CatalogDao;
import hff.elegant.blog.dao.UserDao;
import hff.elegant.blog.infra.mvc.ActionBindContext;
import hff.elegant.blog.infra.mvc.ActionDriven;
import hff.elegant.blog.infra.mvc.ActionBindContext.ActionBind;
import hff.elegant.blog.infra.mvc.ActionBindContext.URLMapping;
import hff.elegant.blog.pojo.Catalog;
import hff.elegant.blog.pojo.Post;
import hff.elegant.blog.pojo.User;
import hff.elegant.blog.service.AppManager;
import hff.elegant.blog.service.IUser;
import hff.elegant.blog.service.UserManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;


/**
 * 首页Action
 * 
 * @author ZhangYajun
 *
 * @since 
 */
@ActionBind
public class HomeAction extends ActionDriven {
    private static final long serialVersionUID = -9071696851530171069L;

    static SimpleCache<String, Object> _cache = new SimpleCache<String, Object>(10);

    static int reload = 0;

    /** 分类列表缓存 */
    static void loadAside() {
        HttpServletRequest request = ActionBindContext.getContext().getRequest();
        // 标签
        Object cat = _cache.get("list2");
        if (null != cat) {
            if (reload == 1) {
                reload = 0;
                List<Catalog> list = new CatalogDao().listCatalogs();
                _cache.put("list2", list);
                request.setAttribute("list2", list);
                return;
            }
            request.setAttribute("list2", cat);
        } else {
            List<Catalog> list = new CatalogDao().listCatalogs();
            _cache.put("list2", list);
            request.setAttribute("list2", list);
        }
    }

    /** 搜索 */
    void search(String keywords, Paging page) throws Exception {
        String indexPath = (String) ActionBindContext.getContext().getServletContext().getAttribute("indexPath");
        HiLucene lu = HiLucene.init(indexPath);

        BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
                BooleanClause.Occur.SHOULD,
                BooleanClause.Occur.SHOULD};
        
        Query query = HiLucene.makeQuery(new String[] {"title", "excerpt", "content"}, flags, keywords, 5f);
        page.setTotalRecordsNumber(1);

        long start = System.currentTimeMillis();
        List<Long> list = lu.find(Post.class, query, null, null, page);
        long end = System.currentTimeMillis();

        double seconds = (end - start) / 1000D;
        if (seconds <= 0)
            seconds = 0.01;
        //double j = new BigDecimal(seconds).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
        super.request().setAttribute("seconds", String.format("%.2f", seconds));

        List<Post> listPosts = new AppManager().listPosts(list.toArray(new Long[] {}));
        super.request().setAttribute("listPosts", listPosts);
        super.request().setAttribute("keywords", keywords);

        for (Post foo : listPosts) {
            foo.setTitle(HiLucene.highlight(foo.getTitle(), keywords));
            foo.setExcerpt(HiLucene.highlight(foo.getExcerpt(), keywords));
        }
    }

    /** 首页 */
    @URLMapping("/")
    public void execute() throws Exception {
        loadAside();

        BlogAction.readHitCatFromCookie();

        // 分页
        Integer idx = (Integer) super.request().getAttribute("pageIndex");
        Paging page = new Paging().setPageIndex(idx.intValue());
        super.request().setAttribute("page", page);

        String keywords = super.getParameterString("search", null);
        if (StringUtils.isNotEmpty(keywords)) {
            keywords = new String(keywords.getBytes("ISO-8859-1"), "UTF-8");
            this.search(keywords, page);
        } else {
            List<Post> listPosts = new AppManager().listPosts(page, 0);
            super.request().setAttribute("listPosts", listPosts);
        }

        super.forward("/[B@3Home.jsp");
    }

    /** 关于 */
    @URLMapping("/about/")
    public void about() throws Exception {
        loadAside();

        BlogAction.readHitCatFromCookie();

        super.forward("/[B@4About.jsp");
    }

    /** 博客管理入口 */
    @URLMapping("/admin/")
    public void admin() {
        if ("login_".equals(super.getParameterString("tp", ""))) {
            login();
            return;
        }
        if ("logout_".equals(super.getParameterString("tp", ""))) {
            new UserManager().deleteUserInCookie();
            super.print("ok");
            return;
        }
        super.redirect(super.request().getContextPath() + "/admin/list/");
    }

    /** 登录 */
    private void login() {
        String email = super.getParameterString("email", "");
        String pwd = super.getParameterString("pwd", "");
        String remember = super.getParameterString("remember", null);

        final User dbUser = new UserDao().getByEmail(email);
        // 用户不存在或密码错误
        if (null == dbUser || !dbUser.getPassword().equals(DigestUtils.md5Hex(pwd+'{'+email+'}'))) {
            super.print("0");
            return;
        }
        // 是否记住登陆
        boolean save = false;
        if ("1".equals(remember)) {
            save = true;
        }
        // Cookie用户接口
        IUser loginUser = new IUser() {
            public String getPwd() { return dbUser.getPassword(); }
            public int getId() { return dbUser.getId(); }
            public String getUnick() { return dbUser.getNicename(); }
        };
        new UserManager().saveUserInCookie(loginUser, save);

        super.print("1");
    }

    /** 注册 */
    public void reg() {
        super.printHtml("未实现<a href=\"javascript:;\" onclick=\"history.back();\">返回</a>");
    }
}
