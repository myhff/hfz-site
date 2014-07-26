package hff.elegant.blog.dao;

import hff.elegant.blog.dao.db.SqlTools;
import hff.elegant.blog.dao.db.SqlTools.DbResultHandler;
import hff.elegant.blog.pojo.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogDao {

    /** 查询分类列表 */
    public List<Catalog> listCatalogs() {
        String sql = "select t1.*, count(t2.id) as count from hf_catalogs t1 left join hf_posts t2 on t1.cat_id = t2.cat_id and t2.type <> 2 group by t1.cat_id order by sort_order asc";
        return SqlTools.query(sql, new DbResultHandler<List<Catalog>>() {
            @Override
            public List<Catalog> handle(ResultSet rs) throws SQLException {
                ArrayList<Catalog> list = new ArrayList<Catalog>();
                Catalog foo = null;
                while (rs.next()) {
                    foo = new Catalog();
                    foo.setCatId(rs.getLong("CAT_ID"));
                    foo.setName(rs.getString("NAME"));
                    foo.setSlug(rs.getString("SLUG"));
                    foo.setParent(rs.getLong("PARENT"));
                    foo.setSortOrder(rs.getInt("SORT_ORDER"));
                    foo.setCount(rs.getInt("count"));
                    list.add(foo);
                }
                return list;
            }
        });
    }

    /** 根据slug查询 */
    public Catalog getBySlug(String slug) {
        String sql = String.format(
                "select * from hf_catalogs where slug = '%s'", slug);
        return SqlTools.query(sql, new DbResultHandler<Catalog>() {
            @Override
            public Catalog handle(ResultSet rs) throws SQLException {
                if (!rs.next())
                    return null;
                Catalog foo = new Catalog();
                foo.setCatId(rs.getLong("CAT_ID"));
                foo.setName(rs.getString("NAME"));
                foo.setSlug(rs.getString("SLUG"));
                foo.setParent(rs.getLong("PARENT"));
                foo.setSortOrder(rs.getInt("SORT_ORDER"));
                return foo;
            }
        });
    }

    /** 保存 */
    public int save(Catalog foo) {
        String sql = "insert into hf_catalogs (name, slug, parent, sort_order) values (?, ?, ?, ?)";
        Object[] args = { 
            foo.getName(), 
            foo.getSlug(), 
            foo.getParent(), 
            foo.getSortOrder() 
        };
        return SqlTools.update(sql, args);
    }

    /** 更新 */
    public int update(Catalog foo) {
        String sql = "update hf_catalogs set name = ?, slug = ?, parent = ? sort_order = ? where cat_id = ?";
        Object[] args = { 
            foo.getName(), 
            foo.getSlug(), 
            foo.getParent(),
            foo.getSortOrder(), 
            foo.getCatId() 
        };
        return SqlTools.update(sql, args);
    }

    /** 删除 */
    public int delete(long catId) {
        String sql = "delete from hf_catalogs where cat_id = ?";
        return SqlTools.update(sql, catId);
    }
}
