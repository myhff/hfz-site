package hff.elegant.blog.dao.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * 连接管理
 * @author ZhangYajun
 * @since  9-24, 2013 <br /> */
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    /** 将连接绑定到本地线程变量中 */
    private static final ThreadLocal<Connection> conns = new ThreadLocal<Connection>();

    /** 数据源 */
    private static javax.sql.DataSource _source;

    private static boolean show_sql = true;

    static {
        initDataSource(null);
    }

    /**
     * 初始化连接池
     * @param props
     */
    private final static void initDataSource(Properties props) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
             BoneCPDataSource ds = new BoneCPDataSource();
             ds.setJdbcUrl("jdbc:mysql://localhost:3306/hfdb?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
            ds.setUsername("root");
            ds.setPassword("123456");

            _source = ds;
            logger.info("Using DataSource : " + _source.getClass().getName());
        }
        catch (Exception e) {
            throw new DbException(e);
        }
    }

    /** 获取数据源 */
    static final DataSource getDataSource() {
        return _source;
    }

    /** 断开连接池 */
    static final void closeDataSource(){
        try {
            _source.getClass().getMethod("close").invoke(_source);
        }
        catch (NoSuchMethodException e){
            // Ignored..
        }
        catch (Exception e) {
            logger.error("Unabled to destroy DataSource!!! ", e);
        }
    }

    /** 获取连接 */
    public static final Connection getConnection() throws SQLException {
        Connection connection = conns.get();
        if (connection == null || connection.isClosed()) {
            connection = _source.getConnection();
            conns.set(connection);
        }
        return (show_sql && !Proxy.isProxyClass(connection.getClass()))?
                new _DebugConnection(connection).getConnection():connection;
    }

    /**
     * 释放本地线程变量里的数据库连接
     * Close the ThreadLocal's JDBC Connection and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     */
    public static final void closeConnection() {
        Connection connection = conns.get();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        catch (SQLException ex) {
            logger.debug("Could not close JDBC Connection", ex);
        }
        catch (Throwable ex) {
            // We don't trust the JDBC driver: It might throw RuntimeException or Error.
            logger.debug("Unexpected exception on closing JDBC Connection", ex);
        }
        conns.set(null);
    }

    /**
     * 用于跟踪执行的SQL语句，JDK 动态代理，显示执行的SQL语句
     */
    static class _DebugConnection implements InvocationHandler {
        private static final Logger logger = LoggerFactory.getLogger(_DebugConnection.class);
        private Connection connection = null;

        public _DebugConnection(Connection connection) {
            this.connection = connection;
        }

        /**
         * Returns the connection.
         * @return Connection
         */
        public Connection getConnection() {
            return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(), 
                    connection.getClass().getInterfaces(), this);
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if("prepareStatement".equals(method)) {
                    logger.debug("[SQL] >>> {}", args[0]);
                    PreparedStatement pstmt = (PreparedStatement) m.invoke(connection, args);
                    return new _DebugPreparedStatement(pstmt).getPreparedStatement();
                }
                return m.invoke(connection, args);
            }
            catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    } //#class _DebugConnection

    /**
     * 用于跟踪执行的SQL语句参数
     */
    static class _DebugPreparedStatement implements InvocationHandler {
        private static final Logger logger = LoggerFactory.getLogger(_DebugPreparedStatement.class);
        private PreparedStatement pstmt = null;

        private ArrayList<String> paramValues = new ArrayList<String>();

        public _DebugPreparedStatement(PreparedStatement pstmt) {
            this.pstmt = pstmt;
        }

        /**
         * Returns the pstmt.
         * @return PreparedStatement
         */
        public PreparedStatement getPreparedStatement() {
            return (PreparedStatement) Proxy.newProxyInstance(pstmt.getClass().getClassLoader(), 
                    pstmt.getClass().getInterfaces(), this);
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if(method.startsWith("setObject")) {
                    paramValues.add( String.valueOf(args[1]) );
                }
                if(method.startsWith("execute") && !paramValues.isEmpty()) {
                    logger.debug("[SQL] >>> parameters{}", paramValues); paramValues.clear();
                }
                return m.invoke(pstmt, args);
            }
            catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    } //#class _DebugConnection
}
