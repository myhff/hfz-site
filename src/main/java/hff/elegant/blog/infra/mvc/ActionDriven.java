package hff.elegant.blog.infra.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Action Helper
 * 
 * @author ZhangYajun
 * 
 * @since
 */
public abstract class ActionDriven implements java.io.Serializable {
    private static final long serialVersionUID = 5924996701406078435L;

    private static final String UTF_8 = "UTF-8";

    /** 默认方法 */
    public void execute() throws Exception {};

    /** 是否是回调 */
    public boolean isPostback() {
        return getParameterString("postback", null) != null;
    }

    /** 获取Request对象 */
    public HttpServletRequest request() {
        return ActionBindContext.getContext().getRequest();
    }

    /** 获取Response对象 */
    public HttpServletResponse response() {
        return ActionBindContext.getContext().getResponse();
    }

    /** 获取字符串型参数 */
    public String getParameterString(String name, String defaultValue) {
        String value = request().getParameter(name);
        return StringUtils.isNotBlank(value) ? value : defaultValue;
    }

    /** 获取整型参数 */
    public int getParameterInt(String name, int defaultValue) {
        String value = request().getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) { }
        }
        return defaultValue;
    }

    /** 请求转发 */
    public void forward(String uri) {
        try {
            request().getRequestDispatcher(uri).forward(request(), response());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
 
    /** 重定向 */
    public void redirect(String uri) {
        try {
            response().sendRedirect(uri);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出信息到浏览器
     * @param msg
     * @throws IOException
     */
    public void print(Object msg) {
        if(!UTF_8.equalsIgnoreCase(response().getCharacterEncoding()))
            response().setCharacterEncoding(UTF_8);
        try {
            response().setContentType("text/plain");
            response().getWriter().print(msg);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出信息到浏览器
     * @param msg
     * @throws IOException
     */
    public void printHtml(Object msg) {
        if(!UTF_8.equalsIgnoreCase(response().getCharacterEncoding()))
            response().setCharacterEncoding(UTF_8);
        try {
            response().setContentType("text/html");
            response().getWriter().print(msg);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void output_json(String[] key, Object[] value) throws IOException {
        StringBuilder json = new StringBuilder("{");
        for(int i=0;i<key.length;i++){
            if(i>0)
                json.append(',');
            boolean isNum = value[i] instanceof Number ;
            json.append("\"");
            json.append(key[i]);
            json.append("\":");
            if(!isNum) json.append("\"");
            json.append(value[i]);
            if(!isNum) json.append("\"");
        }
        json.append("}");
        print(json.toString());
    }

    public void output_json(String key, Object value) throws IOException {
        output_json(new String[]{key}, new Object[]{value});
    }
}