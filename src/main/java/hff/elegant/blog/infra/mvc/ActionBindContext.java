package hff.elegant.blog.infra.mvc;

import hff.elegant.blog.util.RequestUtils;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * ActionBindContext
 * 
 * @author ZhangYajun
 * 
 * @since
 */
public class ActionBindContext {
    private static final ThreadLocal<ActionBindContext> threadLocal = new ThreadLocal<ActionBindContext>();
    private static final String UTF_8 = "UTF-8";

    private ServletContext servletContext;
    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Cookie> cookies;

    public ServletContext getServletContext() { return servletContext; }
    public HttpSession getSession() { return session; }
    public HttpServletRequest getRequest() { return request; }
    public HttpServletResponse getResponse() { return response; }

    public Cookie getCookie(String name) { return cookies.get(name); }
    public void cookie(String name, String value, int max_age, boolean all_sub_domain) {
        RequestUtils.setCookie(request, response, name, value, max_age, all_sub_domain);
    }
    public void deleteCookie(String name, boolean all_domain) { RequestUtils.deleteCookie(request, response, name, all_domain); }

    public String header(String name) { return request.getHeader(name); }
    public void header(String name, String value) { response.setHeader(name, value); }
    public void header(String name, int value) { response.setIntHeader(name, value); }
    public void header(String name, long value) { response.setDateHeader(name, value); }

    public String ip(){
        return RequestUtils.getRemoteAddr(request);
    }


    private ActionBindContext() {
    }

    public static ActionBindContext getContext() {
        return threadLocal.get();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME) @Documented
    public @interface ActionBind {
        String value() default "";
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME) @Documented
    public @interface URLMapping {
        String value();
    }

    static ActionBindContext initial(ServletContext ctx, ServletRequest req, ServletResponse resp) {
        clear();
        ActionBindContext rc = new ActionBindContext();
        rc.servletContext = ctx;
        rc.request = _AutoEncodingRequest(new _ParameterRequest((HttpServletRequest) req));
        rc.response = (HttpServletResponse) resp;
        rc.response.setCharacterEncoding(UTF_8);
        rc.session = rc.request.getSession(false);
        rc.cookies = new HashMap<String, Cookie>();
        Cookie[] cookies = rc.request.getCookies();
        if(cookies != null) {
            for(Cookie ck : cookies)
            rc.cookies.put(ck.getName(), ck);
        }
        threadLocal.set(rc);
        return rc;
    }

    static void clear() {
        if (threadLocal.get() != null) {
        threadLocal.get().servletContext  = null;
        threadLocal.get().request  = null;
        threadLocal.get().response = null;
        threadLocal.get().session = null;
        threadLocal.get().cookies = null;
        }
        threadLocal.remove();
    }

    public static class _ParameterRequest extends HttpServletRequestWrapper {
        public HashMap<String, String> params = new HashMap<String, String>();

        public _ParameterRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            if (params.containsKey(name))
                return params.get(name);
            return super.getParameter(name);
        }
    }

    /** 设置编码 */
    private static HttpServletRequest _AutoEncodingRequest(HttpServletRequest req) {
        HttpServletRequest auto_encoding_req = req;
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            try {
                auto_encoding_req.setCharacterEncoding(UTF_8);
            } catch (UnsupportedEncodingException e) { }
        }
        return auto_encoding_req;
    }
}
