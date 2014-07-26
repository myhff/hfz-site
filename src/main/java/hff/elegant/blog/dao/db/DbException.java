package hff.elegant.blog.dao.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据库异常
 * @author ZhangYajun 
 * @since  9-24, 2013 <br /> */
@SuppressWarnings("serial")
public class DbException extends RuntimeException {

    private static final Log logger = LogFactory.getLog(DbException.class);

    public DbException(Throwable cause) {
        logger.error(cause.getMessage(), cause);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}