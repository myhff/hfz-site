package hff.elegant.blog.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 数据访问工具类
 * @author ZhangYajun 
 * @since  9-25, 2013 <br /> */
public abstract class SqlTools {

    /** 获取连接*/
    public static Connection getConnection() {
        try {
            return ConnectionManager.getConnection();
        }
        catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /** 关闭连接 */
    public static void closeConnection() {
        ConnectionManager.closeConnection();
    }

    /** 关闭语句 */
    public static void close(Statement pstmt) {
        if (null != pstmt) {
            try {
                pstmt.close();
            }
            catch (SQLException e) {
                throw new DbException(e);
            }
        }
    }

    /** 关闭记录集 */
    public static void close(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            }
            catch (SQLException e) {
                throw new DbException(e);
            }
        }
    }

    /** 设置参数 */
    public static PreparedStatement fillStatement(PreparedStatement pstmt, Object...params) 
        throws SQLException {

        if (params == null)
            return pstmt;

        for (int i = 0; i < params.length; ++i) {
            pstmt.setObject(i+1, params[i]);
        }
        return pstmt;
    }

    /** 执行INSERT/UPDATE/DELETE语句 */
    public static final int update(Connection conn, final String sql, final Object...params) {
        return update(conn, new DbPreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement stmt = conn.prepareStatement(sql);
                fillStatement(stmt, params);
                return stmt;
            }
        });
    }

    /** 执行INSERT/UPDATE/DELETE语句 */
    public static final int update(String sql, Object...params) {
        try {
            return update(getConnection(), sql, params);
        }
        finally {
            closeConnection();
        }
    }

    /** 执行INSERT/UPDATE/DELETE语句 */
    public static final int update(DbPreparedStatementCreator pstmtCreator) {
        try {
            return update(getConnection(), pstmtCreator);

        } finally {
            closeConnection();
        }
    }

    /** 执行INSERT/UPDATE/DELETE语句 */
    public static final int update(DbPreparedStatementCreator pstmtCreator, DbKeyHolder kh) {
        try {
            return update(getConnection(), pstmtCreator, kh);

        } finally {
            closeConnection();
        }
    }

    /** 执行INSERT/UPDATE/DELETE语句 */
    private static final int update(Connection conn, DbPreparedStatementCreator pstmtCreator) {
        PreparedStatement stmt = null;
        try {
            stmt = pstmtCreator.createPreparedStatement(conn);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e);

        } finally {
            close(stmt);
        }
    }

    /** 执行INSERT/UPDATE/DELETE语句 */
    private static final int update(Connection conn, DbPreparedStatementCreator pstmtCreator, DbKeyHolder kh) {
        PreparedStatement stmt = null;
        try {
            stmt = pstmtCreator.createPreparedStatement(conn);
            int result = stmt.executeUpdate();

            if (null != kh) {
                ResultSet rs = stmt.getGeneratedKeys();
                kh.key = rs.next() ? rs.getObject(1) : null;
            }
            return result;

        } catch (SQLException e) {
            throw new DbException(e);

        } finally {
            close(stmt);
        }
    }

    /**
     * 查询
     * @param sql SQL语句
     * @param h   结果集解析器
     * @return    泛型
     */
    public static final <T> T query(Connection conn, String sql, DbResultHandler<T> h) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            rs = (stmt = conn.prepareStatement(sql)).executeQuery();
            return h.handle(rs);

        } catch (SQLException e) {
            throw new DbException(e);

        } finally {
            close(rs);
            close(stmt);
        }
    }

    /**
     * 查询
     * @param sql SQL语句
     * @param h   结果集解析器
     * @return    泛型
     */
    public static final <T> T query(String sql, DbResultHandler<T> h) {
        try {
            return query(getConnection(), sql, h);
        }
        finally {
            closeConnection();
        }
    }

    /**
     * 查询
     * @param sql SQL语句
     * @param h   结果集解析器
     * @return    泛型
     */
    public static final int queryForInt(Connection conn, String sql) {
        return query(conn, sql, new DbResultHandler<Integer>() {
            @Override
            public Integer handle(ResultSet rs) throws SQLException {
                if (!rs.next())
                    return 0;
                return rs.getInt(1);
            }
        }).intValue();
    }

    /**
     * 查询
     * @param sql SQL语句
     * @param h   结果集解析器
     * @return    泛型
     */
    public static final int queryForInt(String sql) {
        try {
            return queryForInt(getConnection(), sql);
        }
        finally {
            closeConnection();
        }
    }

    /**
     * 结果集解析器
     * 
     * @param <AppManager> 泛型对象
     */
    public static interface DbResultHandler<T> {
        public T handle(ResultSet rs) throws SQLException;
    }

    /**
     * 语句执行策略
     * 
     * @param <AppManager> 泛型对象
     */
    public static interface DbPreparedStatementCreator {
        PreparedStatement createPreparedStatement(Connection conn) throws SQLException;
    }

    /**
     * 持有主键引用
     * 
     * @param <AppManager> 泛型对象
     */
    public static class DbKeyHolder {
        ArrayList<HashMap<String, Object>> keyList = new ArrayList<HashMap<String, Object>>();
        @Deprecated
        public ArrayList<HashMap<String, Object>> getKeyList() {
            return keyList;
        }
        Object key = null;
        public Object getKey() {
            return key;
        }
    }
}