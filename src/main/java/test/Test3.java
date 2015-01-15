package test;

import hff.elegant.blog.util.FileCancatUtils;
import hff.elegant.blog.util.Md5Utils;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public class Test3 {

    public static void main(String[] args) {
        String _uri = "/";
        String queryStr = "?in_1.txt,in_2.txt,in_3.txt?t=201401160211"; 
        if (StringUtils.isNotBlank(queryStr) && queryStr.startsWith("?")) {
            //request.getSession().getServletContext().getRealPath("/");
            String path = "d:";

            String hexName = Md5Utils.MD5Encode(queryStr).substring(0, 9);

            String[] resources = queryStr.substring(1).split(",");
            for (int i = 0; i < resources.length; i++) {
                resources[i] = path + _uri + resources[i];
                if (resources[i].indexOf("?") > 0) {
                    resources[i] = resources[i].substring(0, resources[i].indexOf("?"));
                }
            }
            String suffix = resources[0].substring(resources[0].lastIndexOf("."));

            char sep = File.separatorChar;
            //String output = path + sep + "sc" + sep + "temp" + sep + hexName + suffix;
            String output = path + sep + hexName + suffix;
            FileCancatUtils.mergeFiles(output, resources);
            //request.getRequestDispatcher("/sc/temp/" + hexName + suffix).forward(request, response);
            System.out.println("file:///D:/" + hexName + suffix);
            return;
        }
    }
}
