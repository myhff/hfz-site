package hff.elegant.blog.infra.mvc;

import hff.elegant.blog.infra.mvc.ActionBindContext.ActionBind;
import hff.elegant.blog.infra.mvc.ActionBindContext.URLMapping;

import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Execute URL Mapping -> ActionBind
 * 
 * 支持{id}_suffix.html, 暂不支持prefix_{id}_suffix.html
 * 
 * @author ZhangYajun
 * 
 * @since
 */
public class ActionServFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ActionServFilter.class);

    private static javassist.ClassPool classPool = javassist.ClassPool.getDefault();

    private static ServletContext servletContext;

    private static String basePackage = null;

    public void init(FilterConfig config) throws ServletException {
        String s = config.getServletContext().getInitParameter("hfzConfigLocation");
        if (s != null && (s =s.trim()).startsWith("classpath:")) {
            if ((s = s.substring(10)).endsWith(".properties"))
                s = s.substring(0, s.length() - 11);
            basePackage = ResourceBundle.getBundle(s, Locale.getDefault()).getString("scan-base-package");
            if (basePackage != null && basePackage.trim().length() > 0)
                basePackage += ".*";
            else 
                basePackage = null;
        }
        servletContext = config.getServletContext();
        String path = ResourceBundle.getBundle(s, Locale.getDefault()).getString("domain");
        servletContext.setAttribute("basePath", path + servletContext.getContextPath());
        String path2 = ResourceBundle.getBundle(s, Locale.getDefault()).getString("static.path");
        servletContext.setAttribute("staticPath", path2);
        String path3 = ResourceBundle.getBundle(s, Locale.getDefault()).getString("index.path");
        servletContext.setAttribute("indexPath", path3);
    }

    private static class LazyHolder {
        static final Multimap<String, Object> actionInsts = ArrayListMultimap.create();
        static {
            if (null != basePackage) {
                logger.info("scan-base-package is {}", basePackage);
            }
            scanClassPath();
        }
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String _uri = (String) req.getAttribute("javax.servlet.include.servlet_path");
        if (_uri == null) {
            _uri = getServletPath(request);
            _uri = _uri != null && !"".equals(_uri) ? _uri : null;
        }
        if (_uri == null) {
            _uri = request.getRequestURI().substring(request.getContextPath().length());
        }
        if (!_uri.endsWith("/") && _uri.indexOf(".") == -1) {
            String redirectUrl = request.getRequestURL().toString() + '/';
            if (null != request.getQueryString()) {
                redirectUrl += '/' + request.getQueryString();
            }
            response.setStatus(301);  
            response.setHeader( "Location", redirectUrl);  
            response.setHeader( "Connection", "close" );  
            return; 
        }
        try {
            _uri = new String(_uri.getBytes("ISO8859_1"), "UTF8");
        }
        catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        Collection<Object> multimap = check(_uri, request);
        if (null == multimap) {
            chain.doFilter(request, response);
            return;
        }
        ActionBindContext.initial(servletContext, req, resp);
        logger.debug("URI: " + _uri);// + " -> " + info);
        boolean result = service(multimap, request, response);
        if (!result)
        chain.doFilter(req, resp);
    }

    private Collection<Object> check(String _uri, HttpServletRequest req) {
        Matcher m = Pattern.compile("\\/page\\/\\d{1,5}\\/").matcher(_uri);
        String page = m.find() ? m.group(0) : null;
        _uri = _uri.replaceAll("page\\/\\d{1,5}\\/", "");
        Integer pageIndex = 1;
        if (null != page) {
            try {
                pageIndex = Integer.parseInt(page.substring(page.indexOf("/", 1) + 1, page.length() - 1));
            }
            catch (Exception e) {
                return null;
            }
        }
        req.setAttribute("pageIndex", pageIndex);
        Collection<Object> multimap = LazyHolder.actionInsts.get(_uri);
        if (multimap.size() == 0) {
            Set<String> keySet = LazyHolder.actionInsts.keySet();
            for (String foo : keySet) {
                if (foo.split("/").length == _uri.split("/").length) {
                    String[] v1 = foo.split("/");
                    String[] v2 = _uri.split("/");
                    String temp[] = new String[v1.length];
                    for (int i = 0; i < v1.length; i++) {
                        int startIdx = v1[i].indexOf("{");
                        int endIdx = v1[i].indexOf("}");
                        String sec1 = v1[i].replaceAll("\\{.*\\}", "");
                        String sec2 = v2[i].replace(sec1, "");
                        if (startIdx > -1 && endIdx > 0) {
                            if ("".equals(sec1)) {
                                if (v2[i].indexOf(".") == -1) {
                                    temp[i] = v2[i];
                                }
                            } else {
                                temp[i] = v1[i].replaceAll("\\{.*\\}", sec2);
                            }
                        } else {
                            temp[i] = v1[i];
                        }
                    }
                    boolean flag = true;
                    String[] paramsKey   = new String[v1.length];
                    String[] paramsValue = new String[v1.length];
                    for (int i = 0; i < v1.length; i++) {
                        if (v2[i].equals(temp[i])) {
                            int startIdx = v1[i].indexOf("{");
                            int endIdx = v1[i].indexOf("}");
                            String sec1 = v1[i].replaceAll("\\{.*\\}", "");
                            String sec2 = v2[i].replace(sec1, "");
                            if (startIdx > -1 && endIdx > 0) {
                                if ("".equals(sec1)) {
                                    paramsKey[i]   = v1[i].substring(startIdx + 1, endIdx);
                                    paramsValue[i] = v2[i];
                                } else {
                                    paramsKey[i]   = v1[i].substring(startIdx + 1, endIdx);
                                    paramsValue[i] = sec2;
                                }
                            }
                        } else {
                            flag = false;
                        }
                    }
                    if (flag) {
                        for (int i = 0; i < paramsKey.length; i++) {
                            if (null != paramsKey[i])
                            req.setAttribute(paramsKey[i], paramsValue[i]);
                        }
                        multimap = LazyHolder.actionInsts.get(foo);
                        break;
                    }
                }
            }
        }
        return multimap.size() != 0 ? multimap : null;
    }

    private boolean service(Collection<Object> multimap, HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException {
        Object actIns = null;
        String actMethod = null;
        try {
            for (Object foo : multimap) {
                if (foo instanceof String)
                actMethod = (String) foo;
                else 
                actIns = foo;
            }
            Method m = actIns.getClass().getMethod(actMethod);
            m.setAccessible(true);
            Object content = m.invoke(actIns);
            return "404".equals(content) ? false : true;

        } catch (Throwable e) {
            logger.error("", e.getCause());
        } 
        servletContext.getRequestDispatcher("/WEB-INF/500.jsp").forward(req, resp);
        return true;
    }

    private static String getServletPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String requestUri = request.getRequestURI();

        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            if (requestUri.indexOf(servletPath) > -1)
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
        }
        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

        if (startIndex > endIndex) {
            endIndex = startIndex;
        }
        return requestUri.substring(startIndex, endIndex);
    }

    private static void fillClasses(final File file) {
        if (file.isDirectory()) {
            for (File foo : file.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || pathname.getName().endsWith(".class") || 
                        pathname.getName().endsWith(".jar");
                }})) {
                fillClasses(foo);
            }
        } else if (file.getName().endsWith(".class")) {
            processClassFile(file);
        } else if (file.getName().endsWith(".jar")) {
            processJarFile(file);
        }
    }

    private static void fillClass(String className) {
        try {
            javassist.CtClass ctc = classPool.get(className);
            Object bind = null;
            if ((bind = ctc.getAnnotation(ActionBind.class)) != null) {
                final Class<?> clazz = Class.forName(className, Boolean.FALSE, ActionServFilter.class.getClassLoader());
                javassist.CtMethod[] methods = ctc.getMethods();
                Object obj = clazz.newInstance();
                logger.debug(bind.toString());
                for (javassist.CtMethod m : methods) {
                    URLMapping anno = (URLMapping) m.getAnnotation(URLMapping.class);
                    if (null != anno) {
                        LazyHolder.actionInsts.put(anno.value(), obj);
                        LazyHolder.actionInsts.put(anno.value(), m.getName());
                        logger.debug("Mapped: [" + anno.value() + "] onto " + LazyHolder.actionInsts.get(anno.value()));
                    }
                    
                }
            }
        } catch (Throwable ex) { }
    }

    private static void processClassFile(File file) {
        final String filePathWithDot = file.getAbsolutePath().replace(File.separator, ".");
        final String className = filePathWithDot.substring(filePathWithDot.indexOf("classes.") + 8).replace(".class", "");
        fillClass(className);
    }

    private static void processJarFile(File file) {
        try {
            classPool.appendClassPath(file.toString());
            for (ZipEntry entry : Collections.list(new ZipFile(file).entries())) {
                if (entry.getName().endsWith(".class")) {
                    final String className = entry.getName().replace("/", ".").replace(".class", "");
                    if (className.indexOf(".action") > 0) {
                        if (null != basePackage) {
                            boolean y = Pattern.compile(basePackage).matcher(className).matches();
                            if (!y)
                            continue;
                        }
                        fillClass(className);
                    }
                }
            }
        } catch (Throwable ex) { }
    }

    private static void scanClassPath() {
        String path = servletContext.getRealPath("/WEB-INF/lib/");
        URL u = ActionServFilter.class.getProtectionDomain().getClassLoader().getResource("");
        fillClasses(new File(path));
        try {
            File dir = new File(u.toURI());
            classPool.appendClassPath(dir.toString());
            fillClasses(dir);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            classPool = null;
        }
    }
}