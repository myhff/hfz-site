package hff.elegant.blog.web;

import hff.elegant.blog.infra.mvc.ActionBindContext;
import hff.elegant.blog.infra.mvc.ActionDriven;
import hff.elegant.blog.infra.mvc.ActionBindContext.ActionBind;
import hff.elegant.blog.infra.mvc.ActionBindContext.URLMapping;
import hff.elegant.blog.pojo.User;
import hff.elegant.blog.service.AppManager;
import hff.elegant.blog.service.UserManager;
import hff.elegant.blog.util.RequestUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import test.TestApp;


/**
 * 文件管理Action
 * 
 * @author ZhangYajun
 *
 * @since 
 */
@ActionBind
public class FileAction extends ActionDriven {
    private static final long serialVersionUID = -6672248077638097493L;

    /** 检测非登录 */
    private boolean nonLoginAndReditect() {
        /* 检测用户是否登录 */
        User loginUser = new UserManager().getLoginUser(super.request());
        if (null == loginUser) {
            super.redirect(super.request().getContextPath() + "/admin/login.html");
            return true;
        }
        return false;
    }

    /** 生成文件名 */
    private String generateFileName() {
        String formatDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 随机生成文件编号
        int random = new Random().nextInt(10000);
        return new StringBuffer().append(formatDate).append(random).toString();
    }

    /** 获取静态资源域名 */
    private String getClientPath() {
        ServletContext application = ActionBindContext.getContext().getServletContext();
        // 客户端访问图片的URL前缀
        String clientPath = (String) application.getAttribute("staticPath");
        if (StringUtils.isBlank(clientPath)) {
            clientPath = (String) application.getAttribute("basePath");
        }
        if (clientPath.endsWith("/")) {
            clientPath = clientPath.substring(0, clientPath.length() - 1);
        }
        return clientPath;
    }

    /** 设置或读取图片Cookie */
    private void setClientPathCookie(String clientPath) {
        RequestUtils.setCookie(super.request(), super.response(), "last_img", TestApp._encode(clientPath), 30*24*60*60);
    }
    private void readClientPathFromCookie() {
        HttpServletRequest request = ActionBindContext.getContext().getRequest();
        String cooHitCat = RequestUtils.getCookieValue(request, "last_img");
        if (null != cooHitCat) {
            cooHitCat = TestApp._decode(cooHitCat);
            request.setAttribute("last_img", cooHitCat.split(",")[0]);
        }
    }

    /** 文件上传 */
    @URLMapping("/admin/attachments/")
    public void m1() throws Exception {
        if (nonLoginAndReditect())
            return;

        // 页面
        if (!"new_".equals(super.getParameterString("tp", ""))) {
            readClientPathFromCookie();

            // 草稿箱总数
            int draftsCount = new AppManager().count(2);
            super.request().setAttribute("draftsCount", draftsCount);

            super.forward("/admin/[B@0File.jsp");
            return;
        }

        String[] keys = { "msg", "error", "url" };
        String[] vals = { "", "", "" };

        if (!ServletFileUpload.isMultipartContent(super.request())) {
            vals[1] = "the enctype must be multipart/form-data";
            super.output_json(keys, vals);
            return;
        }

        String path = ActionBindContext.getContext().getServletContext().getRealPath("/");

        // 上传文件目录
        File uploadPath = new File(path + File.separatorChar + "uploads");
        if (!uploadPath.exists())
             uploadPath.mkdirs();

        // 临时文件目录
        File tempPath = new File(path + File.separatorChar + "uploads" + File.separatorChar + "buffer");
        if (!tempPath.exists())
             tempPath.mkdirs();

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(4096);  // 设置缓冲区大小，这里是4kb
            factory.setRepository(tempPath); // 设置缓冲区目录

            ServletFileUpload upload = new ServletFileUpload(factory);

            upload.setSizeMax(4194304);      // 设置最大文件尺寸，这里是4MB

            List<FileItem> items = upload.parseRequest(super.request());

            Iterator<FileItem> itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();

                String fileName = item.getName(); // 获得文件名，包括路径
                if (StringUtils.isNotBlank(fileName)) {
                    File fullFile = new File(item.getName());
                    String filename = generateFileName() + fullFile.getName()
                            .substring(fullFile.getName().lastIndexOf(".")).toLowerCase();

                    vals[2] = getClientPath() + "/uploads/" + filename;
                    setClientPathCookie(vals[2]);
                    File savedFile = new File(uploadPath, filename);
                    item.write(savedFile);
                } else {
                    vals[1] = "No file has been uploaded.";
                }
            }
            vals[0] = "upload succeed";
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.output_json(keys, vals);
    }

}
