package hff.elegant.blog.dao;

import hff.elegant.blog.commons.Paging;
import hff.elegant.blog.dao.db.SqlTools;
import hff.elegant.blog.dao.db.SqlTools.DbKeyHolder;
import hff.elegant.blog.dao.db.SqlTools.DbPreparedStatementCreator;
import hff.elegant.blog.dao.db.SqlTools.DbResultHandler;
import hff.elegant.blog.pojo.Catalog;
import hff.elegant.blog.pojo.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章DAO
 * 
 * @author ZhangYajun
 * 
 * @since 7-10, 2014
 */
public class PostDao {

    /** 总数 */
    public int count(int type) {
        String sql = String.format("select count(t1.id) from hf_posts t1 where t1.type = %s", type);
        return SqlTools.queryForInt(sql);
    }

    /** 根据ID查询 */
    public Post getById(long postId) {
        String sql = String.format("select t1.*, t2.* from hf_posts t1 left join hf_catalogs t2 on t1.cat_id = t2.cat_id where t1.id = %s", postId);

        return SqlTools.query(sql, new DbResultHandler<Post>() {
            @Override
            public Post handle(ResultSet rs) throws SQLException {
                if (!rs.next())
                    return null;

                Post foo = new Post();
                foo.setId(rs.getLong("ID"));
                foo.setCatId(rs.getLong("CAT_ID"));
                foo.setTitle(rs.getString("TITLE"));
                foo.setContent(rs.getString("CONTENT"));
                foo.setExcerpt(rs.getString("EXCERPT"));
                foo.setTags(rs.getString("TAGS"));
                foo.setType(rs.getInt("TYPE"));
                foo.setOriginUrl(rs.getString("ORIGIN_URL"));
                foo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                foo.setModifiedTime(rs.getTimestamp("MODIFIED_TIME"));
                foo.setCommentCount(rs.getInt("COMMENT_COUNT"));
                foo.setViewCount(rs.getInt("View_COUNT"));
                foo.setAsTop(rs.getInt("AS_TOP"));

                Catalog cat = null;
                foo.setCatalog(cat = new Catalog());
                cat.setCatId(rs.getLong("CAT_ID"));
                cat.setName(rs.getString("NAME"));
                cat.setSlug(rs.getString("SLUG"));
                cat.setParent(rs.getLong("PARENT"));
                cat.setSortOrder(rs.getInt("SORT_ORDER"));
                return foo;
            }
        });
    }

    /**
     * 根据ID查询
     * 
     * @return Post[3] [上一篇, 文章, 下一篇]
     */
    public Post[] getByIdAndAdjacent(final long postId) {
        StringBuilder sb = new StringBuilder(480);
        sb.append("select t2.*, t3.* from (");
        sb.append(" (select t1.* from hf_posts t1 where t1.create_time < (select create_time from hf_posts where id = ").append(postId).append(") and t1.type != 2 order by t1.create_time desc limit 1)");
        sb.append("  union all");
        sb.append(" (select t1.* from hf_posts t1 where t1.id = ").append(postId).append(")");
        sb.append("  union all");
        sb.append(" (select t1.* from hf_posts t1 where t1.create_time > (select create_time from hf_posts where id = ").append(postId).append(") and t1.type != 2 order by t1.create_time asc limit 1)");
        sb.append(") t2 left join hf_catalogs t3 on t2.cat_id = t3.cat_id");

        return SqlTools.query(sb.toString(), new DbResultHandler<Post[]>() {
            @Override
            public Post[] handle(ResultSet rs) throws SQLException {
                Post[] list3 = new Post[3];
                Post foo = null;
                Catalog cat = null;
                while (rs.next()) {
                    foo = new Post(rs.getLong("ID"));

                    if (foo.getId() == postId)
                    list3[1] = foo;
                    else if (foo.getId() != postId && list3[2] == null && list3[1] != null)
                    list3[2] = foo;
                    else
                    list3[0] = foo;

                    foo.setCatId(rs.getLong("CAT_ID"));
                    foo.setTitle(rs.getString("TITLE"));
                    foo.setContent(rs.getString("CONTENT"));
                    foo.setExcerpt(rs.getString("EXCERPT"));
                    foo.setTags(rs.getString("TAGS"));
                    foo.setType(rs.getInt("TYPE"));
                    foo.setOriginUrl(rs.getString("ORIGIN_URL"));
                    foo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                    foo.setModifiedTime(rs.getTimestamp("MODIFIED_TIME"));
                    foo.setCommentCount(rs.getInt("COMMENT_COUNT"));
                    foo.setViewCount(rs.getInt("View_COUNT"));
                    foo.setAsTop(rs.getInt("AS_TOP"));

                    foo.setCatalog(cat = new Catalog());
                    cat.setCatId(rs.getLong("CAT_ID"));
                    cat.setName(rs.getString("NAME"));
                    cat.setSlug(rs.getString("SLUG"));
                    cat.setParent(rs.getLong("PARENT"));
                    cat.setSortOrder(rs.getInt("SORT_ORDER"));
                }
                return list3;
            }
        });
    }

    /** 根据分类查询文章 */
    public List<Post> listPostByCatId(long catId, Paging page, int type) {
        String sql = String.format("select t1.*, t2.* from hf_posts t1 left join hf_catalogs t2 on t1.cat_id = t2.cat_id where t1.cat_id = %s and t1.type = %s",
                        catId, type);
        // 设置总记录数
        page.setTotalRecordsNumber(SqlTools.queryForInt(String.format("select count(*) from hf_posts t1 left join hf_catalogs t2 on t1.cat_id = t2.cat_id where t1.cat_id = %s and t1.type = %s", catId, type)));
        return SqlTools.query(page.paginal(sql), new DbResultHandler<List<Post>>() {
                @Override
                public List<Post> handle(ResultSet rs) throws SQLException {
                    ArrayList<Post> list = new ArrayList<Post>();
                    Post foo = null;
                    Catalog cat = null;
                    while (rs.next()) {
                        foo = new Post();
                        foo.setId(rs.getLong("ID"));
                        foo.setCatId(rs.getLong("CAT_ID"));
                        foo.setTitle(rs.getString("TITLE"));
                        foo.setContent(rs.getString("CONTENT"));
                        foo.setExcerpt(rs.getString("EXCERPT"));
                        foo.setTags(rs.getString("TAGS"));
                        foo.setType(rs.getInt("TYPE"));
                        foo.setOriginUrl(rs.getString("ORIGIN_URL"));
                        foo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                        foo.setModifiedTime(rs.getTimestamp("MODIFIED_TIME"));
                        foo.setCommentCount(rs.getInt("COMMENT_COUNT"));
                        foo.setViewCount(rs.getInt("View_COUNT"));
                        foo.setAsTop(rs.getInt("AS_TOP"));

                        foo.setCatalog(cat = new Catalog());
                        cat.setCatId(rs.getLong("CAT_ID"));
                        cat.setName(rs.getString("NAME"));
                        cat.setSlug(rs.getString("SLUG"));
                        cat.setParent(rs.getLong("PARENT"));
                        cat.setSortOrder(rs.getInt("SORT_ORDER"));

                        list.add(foo);
                    }
                    return list;
                }
            });
    }

    /** 分页查询 */
    public List<Post> listPosts(Paging page, int type) {
        String sql = "select t1.*, t2.* from hf_posts t1 left join hf_catalogs t2 on t1.cat_id = t2.cat_id where t1.type = " + type + " order by as_top desc, t1.create_time desc";
        // 设置总记录数
        page.setTotalRecordsNumber(SqlTools.queryForInt("select count(*) from hf_posts t1 left join hf_catalogs t2 on t1.cat_id = t2.cat_id where t1.type = " + type + " order by as_top desc, t1.create_time desc"));
        return SqlTools.query(page.paginal(sql), new DbResultHandler<List<Post>>() {
                @Override
                public List<Post> handle(ResultSet rs) throws SQLException {
                    ArrayList<Post> list = new ArrayList<Post>();
                    Post foo = null;
                    Catalog cat = null;
                    while (rs.next()) {
                        foo = new Post();
                        foo.setId(rs.getLong("ID"));
                        foo.setCatId(rs.getLong("CAT_ID"));
                        foo.setTitle(rs.getString("TITLE"));
                        foo.setContent(rs.getString("CONTENT"));
                        foo.setExcerpt(rs.getString("EXCERPT"));
                        foo.setTags(rs.getString("TAGS"));
                        foo.setType(rs.getInt("TYPE"));
                        foo.setOriginUrl(rs.getString("ORIGIN_URL"));
                        foo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                        foo.setModifiedTime(rs
                                .getTimestamp("MODIFIED_TIME"));
                        foo.setCommentCount(rs.getInt("COMMENT_COUNT"));
                        foo.setViewCount(rs.getInt("VIEW_COUNT"));
                        foo.setAsTop(rs.getInt("AS_TOP"));

                        foo.setCatalog(cat = new Catalog());
                        cat.setCatId(rs.getLong("CAT_ID"));
                        cat.setName(rs.getString("NAME"));
                        cat.setSlug(rs.getString("SLUG"));
                        cat.setParent(rs.getLong("PARENT"));
                        cat.setSortOrder(rs.getInt("SORT_ORDER"));
                        list.add(foo);
                    }
                    return list;
                }
            });
    }

    /** 根据ID查询 */
    public List<Post> listPosts(Long[] idValues) {
        String sql = "select t1.*, t2.* from hf_posts t1 left join hf_catalogs t2 on t1.cat_id = t2.cat_id where t1.type != 2 ";
        String q = "";
        if (null != idValues) {
            for (Long foo : idValues)
            q += foo + ",";
        }
        if (q.length() > 0) {
            q = q.substring(0, q.length() - 1);
            sql += "and t1.id in (" + q + ") ";
        } else {
            return new ArrayList<Post>();
        }
        sql += "order by as_top desc, t1.create_time desc";

        return SqlTools.query(sql, new DbResultHandler<List<Post>>() {
            @Override
            public List<Post> handle(ResultSet rs) throws SQLException {
                ArrayList<Post> list = new ArrayList<Post>();
                Post foo = null;
                Catalog cat = null;
                while (rs.next()) {
                    foo = new Post();
                    foo.setId(rs.getLong("ID"));
                    foo.setCatId(rs.getLong("CAT_ID"));
                    foo.setTitle(rs.getString("TITLE"));
                    foo.setContent(rs.getString("CONTENT"));
                    foo.setExcerpt(rs.getString("EXCERPT"));
                    foo.setTags(rs.getString("TAGS"));
                    foo.setType(rs.getInt("TYPE"));
                    foo.setOriginUrl(rs.getString("ORIGIN_URL"));
                    foo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                    foo.setModifiedTime(rs.getTimestamp("MODIFIED_TIME"));
                    foo.setCommentCount(rs.getInt("COMMENT_COUNT"));
                    foo.setViewCount(rs.getInt("VIEW_COUNT"));
                    foo.setAsTop(rs.getInt("AS_TOP"));

                    foo.setCatalog(cat = new Catalog());
                    cat.setCatId(rs.getLong("CAT_ID"));
                    cat.setName(rs.getString("NAME"));
                    cat.setSlug(rs.getString("SLUG"));
                    cat.setParent(rs.getLong("PARENT"));
                    cat.setSortOrder(rs.getInt("SORT_ORDER"));
                    list.add(foo);
                }
                return list;
            }
        });
    }

    /** 保存 */
    public int save(Post foo) {
        final String sql = "insert into hf_posts (cat_id, title, content, excerpt, tags, type, origin_url, create_time, modified_time, comment_count, view_count, as_top) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        final Object params[] = { 
                foo.getCatId(), 
                foo.getTitle(),
                foo.getContent(), 
                foo.getExcerpt(), 
                foo.getTags(),
                foo.getType(), 
                foo.getOriginUrl(), 
                foo.getCreateTime(),
                foo.getModifiedTime(), 
                foo.getCommentCount(),
                foo.getViewCount(), 
                foo.getAsTop() 
            };
        DbKeyHolder kh = new DbKeyHolder();
        int result = SqlTools.update(new DbPreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn)
                    throws SQLException {
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return SqlTools.fillStatement(stmt, params);
            }
        }, kh);
        if (kh.getKey() != null) { // 返回主键ID
            foo.setId(Integer.parseInt(kh.getKey().toString()));
        }
        return result;
    }

    /** 更新 */
    public int update(Post foo) {
        final String sql = "update hf_posts set cat_id = ?, title = ?, content = ?, excerpt = ?, tags = ?, type = ?, origin_url = ?, modified_time = ?, as_top = ? where id = ?";
        final Object params[] = { 
                foo.getCatId(), 
                foo.getTitle(),
                foo.getContent(), 
                foo.getExcerpt(), 
                foo.getTags(),
                foo.getType(), 
                foo.getOriginUrl(), 
                foo.getModifiedTime(),
                foo.getAsTop(), 
                foo.getId() 
            };
        return SqlTools.update(sql, params);
    }

    /** 更新评论总数 */
    public int updateCommentCount(long postId, int num) {
        return SqlTools.update("update hf_posts set comment_count = ? where id = ?", num, postId);
    }

    /** 更新访问次数 */
    public int updateViewCount(long postId, int num) {
        return SqlTools.update(
                "update hf_posts set view_count = ? where id = ?", num, postId);
    }

    /** 更新创建时间 */
    public int updateCreateTime(long postId, Date createTime) {
        return SqlTools.update("update hf_posts set create_time = ? where id = ?", createTime, postId);
    }

    /** 删除 */
    public int deleteById(long postId) {
        return SqlTools.update("delete from hf_posts where id = ?", postId);
    }
}
