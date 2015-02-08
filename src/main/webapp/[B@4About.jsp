<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<title>LoveFF &raquo; 关于</title>
<meta name="keywords" content="Linux, Python, Hadoop, 虚拟化, 云计算, 个人博客" />
<meta name="description" content="Java个人博客" />
<link href="${staticPath}/sc/??sh309/styles/shCoreDefault.css,global.css?t=201502081612" rel="stylesheet" />
  <!--[if lt IE 9]>
<script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script><![endif]-->
</head>
<base target="_blank">
<body>

<jsp:include page="${'/__segments.jsp?template=top'}" />

<div id="nav-wrap"><nav>
 <ul>
 <li><a href="${basePath}/" target="_self">首页</a></li>
 <li><a href="${basePath}/about/" class="active" target="_self">关于本博客</a></li><c:if test="${not empty hitCatSlug}">
 <li><a href="${basePath}/category/${hitCatSlug}/" target="_self">${hitCatName}</a></li></c:if></ul></nav>
</div>

<div id="main">
 <div id="content">
   <script src='http://git.oschina.net/myhff/learngit/widget_preview'></script>

<div class="post" style="border: 0;">
  <div class="blog-content">
    <p>本博客的内容：软件开发、云计算、互联网、生活感悟。</p>
    <p>Git演示：<a href="http://learngit.oschina.mopaas.com" style="text-decoration:underline;">http://learngit.oschina.mopaas.com</a></p>
    <p>Contact我：codeff (AT) 126.com</p>
    <p>
    <br />
    <a href="http://shang.qq.com/wpa/qunwpa?idkey=36e2949e3857b5a5b179f07575991a185ca7ba53e9147687ebfc04dedc174cd7">
    <img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="DevNote" title="DevNote" />
    </a>
    </p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
  </div>
</div><!-- /.post -->

</div><!-- /#content -->

<jsp:include page="${'/__segments.jsp?template=aside&hide_commons=1'}" />
</div><!-- /#main -->

<jsp:include page="${'/__segments.jsp?template=footer'}" />
</body>
</html><!--[if !IE]>|xGv00|f7a14134f03df8c4cb51c1307ee33cf1<![endif]-->