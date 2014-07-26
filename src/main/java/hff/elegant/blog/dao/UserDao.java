package hff.elegant.blog.dao;

import hff.elegant.blog.dao.db.SqlTools;
import hff.elegant.blog.dao.db.SqlTools.DbResultHandler;
import hff.elegant.blog.pojo.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDao {

    /** 根据ID查询 */
    public User getById(int id) {
        String sql = String.format("select t1.* from hf_users t1 where t1.id = %s", id);
        return SqlTools.query(sql, new DbResultHandler<User>() {
            @Override
            public User handle(ResultSet rs) throws SQLException {
                if (!rs.next())
                    return null;
                User foo = new User();
                foo.setId(rs.getInt("ID"));
                foo.setEmail(rs.getString("EMAIL"));
                foo.setPassword(rs.getString("PASSWORD"));
                foo.setNicename(rs.getString("NICENAME"));
                foo.setUrl(rs.getString("URL"));
                foo.setRegTime(rs.getTimestamp("REG_TIME"));
                return foo;
            }
        });
    }

    public User getByEmail(String email) {
        String sql = String.format("select t1.* from hf_users t1 where t1.email = '%s'", email);
        return SqlTools.query(sql, new DbResultHandler<User>() {
            @Override
            public User handle(ResultSet rs) throws SQLException {
                if (!rs.next())
                    return null;
                User foo = new User();
                foo.setId(rs.getInt("ID"));
                foo.setEmail(rs.getString("EMAIL"));
                foo.setPassword(rs.getString("PASSWORD"));
                foo.setNicename(rs.getString("NICENAME"));
                foo.setUrl(rs.getString("URL"));
                foo.setRegTime(rs.getTimestamp("REG_TIME"));
                return foo;
            }
        });
    }

}
