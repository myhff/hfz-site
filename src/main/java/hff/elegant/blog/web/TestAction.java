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
import hff.elegant.blog.pojo.User;
import hff.elegant.blog.service.AppManager;
import hff.elegant.blog.service.UserManager;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 博客管理Action
 * 
 * @author ZhangYajun
 *
 * @since 
 */
@ActionBind
public class TestAction extends ActionDriven {
    private static final long serialVersionUID = -2166378046385898816L;

    /** 检测非登录 */
    private boolean nonLoginAndReditect() {
        User loginUser = new UserManager().getLoginUser(super.request());
        if (null == loginUser) {
            super.redirect(super.request().getContextPath() + "/admin/login.html");
            return true;
        }
        return false;
    }

    /** 过滤<br/> */
    private static String filterContent(String content) {
        if (null == content)
            return null;
        Pattern p = Pattern.compile("<pre[^>]*>.*<\\/pre>");
        Matcher m = p.matcher(content);
        String str = m.find() ? m.group(0) : null;
        if (null == str)
            return content;
        int first = str.indexOf(">") + 1;
        int num = content.indexOf(str);
        num = num + first;
        String prefixHtml = content.substring(0, num);
        str = str.substring(first, str.length() - 6);
        String suffixHtml = content.substring(prefixHtml.length() + str.length());
        str = str.replaceAll("<br\\s*\\/?>", "\n");
        return prefixHtml + str + suffixHtml;
    }

    /** 博文发布修改 */
    private void saveBlog() throws Exception {
        Post article = new Post();
        BeanUtils.populate(article, super.request().getParameterMap());

        if (StringUtils.isBlank(article.getTitle())) {
            super.output_json("msg", "文章标题不能为空");
            return;
        }
        if (StringUtils.isBlank(article.getContent())) {
            super.output_json("msg", "文章内容不能为空");
            return;
        }
        if (article.getId() == 0) {
            int hdnBlogId = super.getParameterInt("draft", 0);
            article.setId(hdnBlogId);
        }
        // 过滤<br>
        article.setContent(filterContent(article.getContent()));
        // 保存
        if (article.getId() == 0) {
            if (!super.getParameterString("tp", "").contains("as_draft_")) {
                // 发布原创
                article.setType(0);
            } else {
                // 保存草稿
                article.setType(2);
            }
            article.setCreateTime(new Date());
            article.setModifiedTime(article.getCreateTime());
            new PostDao().save(article);
            if (!super.getParameterString("tp", "").contains("as_draft_")) {
                // 建立索引
                String indexPath = (String) ActionBindContext.getContext().getServletContext().getAttribute("indexPath");
                HiLucene lu = HiLucene.init(indexPath);
                lu.add(article);
            }
        }
        // 更新
        else {
            if (Boolean.parseBoolean(super.getParameterString("draft", "false"))) {
                if (!super.getParameterString("tp", "").contains("as_draft_"))
                    article.setType(0);
                else 
                    article.setType(2);
                article.setModifiedTime(new Date());
                new PostDao().update(article);
                //如果已经是草稿，则更新创建时间
                new PostDao().updateCreateTime(article.getId(), article.getModifiedTime());
            } else {
                article.setModifiedTime(new Date());
                new PostDao().update(article);
            }
            if (!super.getParameterString("tp", "").contains("as_draft_")) {
                // 建立索引
                String indexPath = (String) ActionBindContext.getContext().getServletContext().getAttribute("indexPath");
                HiLucene lu = HiLucene.init(indexPath);
                lu.update(article);
            }
        }
        if (!super.getParameterString("tp", "").contains("as_draft_")) {
            // 重新加载aside缓存
            HomeAction.reload = 1;
            super.output_json("id", article.getId());
        } else {
            super.output_json("draft", article.getId());
        }
    }

    /** 文章列表 */
    @URLMapping("/admin/list/")
    public void m1() throws Exception {
        if (nonLoginAndReditect())
            return;

        if ("delete_".equals(super.getParameterString("tp", ""))) {
            int postId = super.getParameterInt("postId", -1);
            if (postId > 0)
            new PostDao().deleteById(postId);

            String indexPath = (String) ActionBindContext.getContext().getServletContext().getAttribute("indexPath");
            HiLucene lu = HiLucene.init(indexPath);
            lu.delete(new Post(postId));

            // 重新加载aside缓存
            HomeAction.reload = 1;

            super.redirect(super.request().getContextPath() + "/admin/list/");
            return;
        }

        // 分页
        Integer idx = (Integer) super.request().getAttribute("pageIndex");
        Paging page = new Paging().setPageIndex(idx.intValue()).setPageSize(15);
        super.request().setAttribute("page", page);

        List<Post> listPosts = new AppManager().listPosts(page, 0);
        super.request().setAttribute("listPosts", listPosts);

        // 草稿箱总数
        int draftsCount = new AppManager().count(2);
        super.request().setAttribute("draftsCount", draftsCount);

        super.forward("/admin/[B@1List.jsp");
    }

    /** 发布博文 */
    @URLMapping("/admin/new-blog/")
    public void m2() throws Exception {
        if (nonLoginAndReditect())
            return;

        if (super.getParameterString("tp", "").startsWith("save_")) {
            try {
                saveBlog();
            } catch (Exception e) {
                super.output_json("msg", "系统异常！");
                throw e;
            }
            return;
        }

        List<Catalog> list = new CatalogDao().listCatalogs();
        super.request().setAttribute("listCatalogs", list);

        // 草稿箱总数
        int draftsCount = new AppManager().count(2);
        super.request().setAttribute("draftsCount", draftsCount);

        super.forward("/admin/[B@1Blog.jsp");
    }

    /** 编辑博文 */
    @URLMapping("/admin/edit-blog/")
    public void m3() {
        if (nonLoginAndReditect())
            return;

        long draft = super.getParameterInt("draft_", -1);

        // 编辑草稿
        if (draft > 0) {
            Post article = new PostDao().getById(draft);

            super.request().setAttribute("article", article);
            super.request().setAttribute("draft", true);
        }
        // 编辑博文
        else {
            long id = super.getParameterInt("id", -1);

            Post article = new PostDao().getById(id);

            super.request().setAttribute("article", article);
            super.request().setAttribute("draft", false);
        }

        List<Catalog> list = new CatalogDao().listCatalogs();
        super.request().setAttribute("listCatalogs", list);

        // 草稿箱总数
        int draftsCount = new AppManager().count(2);
        super.request().setAttribute("draftsCount", draftsCount);

        super.forward("/admin/[B@1Blog.jsp");
    }

    /** 草稿箱 */
    @URLMapping("/admin/drafts/")
    public void m4() {
        if (nonLoginAndReditect())
            return;

        if ("delete_".equals(super.getParameterString("tp", ""))) {
            int postId = super.getParameterInt("postId", -1);
            if (postId > 0)
            new PostDao().deleteById(postId);

            super.redirect(super.request().getContextPath() + "/admin/drafts/");
            return;
        }

        // 分页
        Integer idx = (Integer) super.request().getAttribute("pageIndex");
        Paging page = new Paging().setPageIndex(idx.intValue()).setPageSize(15);
        super.request().setAttribute("page", page);

        List<Post> listPosts = new AppManager().listPosts(page, 2);
        super.request().setAttribute("listPosts", listPosts);

        // 草稿箱总数
        int draftsCount = new AppManager().count(2);
        super.request().setAttribute("draftsCount", draftsCount);

        super.forward("/admin/[B@4Drafts.jsp");
    }

    /** 分类列表 */
    @URLMapping("/admin/blog-catalogs/")
    public void m5() {
        if (nonLoginAndReditect())
            return;

        List<Catalog> list = new CatalogDao().listCatalogs();
        super.request().setAttribute("listCatalogs", list);

        // 草稿箱总数
        int draftsCount = new AppManager().count(2);
        super.request().setAttribute("draftsCount", draftsCount);

        super.forward("/admin/[B@2BlogCatalogs.jsp");
    }

    /** 评论列表 */
    @URLMapping("/admin/blog-comments/")
    public void m6() {
        if (nonLoginAndReditect())
            return;

        // 草稿箱总数
        int draftsCount = new AppManager().count(2);
        super.request().setAttribute("draftsCount", draftsCount);

        super.forward("/admin/[B@2BlogComments.jsp");
    }
}
