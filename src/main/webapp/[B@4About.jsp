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
<link href="${staticPath}/sc/??sh309/styles/shCoreDefault.css,global.css?t=201407240229" rel="stylesheet" />
  <!--[if lt IE 9]>
  <style>::selection { background:#c00!important; color:#fff!important; }</style>
<script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script><![endif]-->
<style> .pro_name a{color: #ff0000;} .osc_git_title{background-color: #edeef5;} .osc_git_box{background-color: #fafafa;} .osc_git_box{border-color: #d1d3e6;} .osc_git_info{color: #666;} .osc_git_main a{color: #435991;} </style>
<style>
 #content .pro_name a{color: #ff0000;}
 #content .osc_git_title{background-color: #edeef5;}
 #content .osc_git_box{background-color: #fafafa;}
 #content .osc_git_box{border-color: #d1d3e6;}
 #content .osc_git_info{color: #666;}
 #content .osc_git_main a{color: #435991;}
 #content .mini-icon { background: none; border: 0; }
 #content .osc_git_title { border-radius: 0; }
 #content .osc_git_box {
   border-radius: 0; border-color: #d1d3e6; margin-top:0; border: 0; background: white;
 }
 #content .osc_git_title { background: #E8E8D0; }
</style>
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
   <script src='http://git.oschina.net/object/learngit/widget_preview'></script>

<div class="post" style="border: 0;">
  <div class="blog-content">
    <p>本博客的内容：软件开发、云计算、互联网、生活感悟。</p>
    <p>联系我：xmasmail (AT) 126.com</p>
    <p>QQ群：205315604</p>
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