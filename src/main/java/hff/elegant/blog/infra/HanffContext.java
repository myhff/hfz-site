package hff.elegant.blog.infra;

/**
 * 
 * @author ZhangYajun
 * 
 * @since 
 */
public class HanffContext {

    static class SingletonHolder {
        static HanffContext m_inst = new HanffContext();
    }

    public static HanffContext getContext() {
        return SingletonHolder.m_inst;
    }
}

class Config {
    
}