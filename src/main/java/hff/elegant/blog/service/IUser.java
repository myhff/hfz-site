package hff.elegant.blog.service;

/**
 * @author ZhangYajun <br />
 * 
 * @since 
 */
public interface IUser {

    byte ROLE_GENERAL = 5;

    int getId();

    String getPwd();

    String getUnick();
}
