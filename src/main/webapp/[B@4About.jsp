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

<div id="top">
<header>
  <h1 id="logo"><a href="${basePath}/" target="_self">LoveFF.cn</a></h1>
 <div id="info"><span class="tagline">Never, never, never, never give up.</span>
<span id="admin-login">
  <a href="${basePath}/admin/" target="_self">博客管理</a> |
 <a href="javascript:;" onclick="logout('${basePath}/admin/?tp=logout_');" target="_self">退出</a>
</span>
</div></header></div>

<div id="nav-wrap"><nav>
 <ul>
 <li><a href="${basePath}/" target="_self">首页</a></li>
 <li><a href="${basePath}/about/" class="active" target="_self">关于本博客</a></li><c:if test="${not empty hitCatSlug}">
 <li><a href="${basePath}/category/${hitCatSlug}/" target="_self">${hitCatName}</a></li></c:if></ul></nav>
</div>

<div id="main" style="display:none;">
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

<aside class="scroll-afloat">
 <section>
 <div id="search">
 <input id="q_keywords" value="${keywords}" x-webkit-speech="x-webkit-speech" placeholder="搜索..." /><button onclick="location.href='${basePath}/?search='+document.getElementById('q_keywords').value;">好手气</button>
</div>
</section>
 <section>
 <h3>博客分类</h3>
 <ul><c:forEach var="foo" items="${list2}">
 <c:if test="${hit_cat.catId eq foo.catId}">
 <li><a href="${basePath}/category/${foo.slug}/" target="_self"><strong>${foo.name}(${foo.count})</strong></a></li>
 </c:if>
 <c:if test="${not (hit_cat.catId eq foo.catId)}">
 <li><a href="${basePath}/category/${foo.slug}/" target="_self">${foo.name}(${foo.count})</a></li>
 </c:if>
 </c:forEach>
</ul>
</section>
 <section>
 <h3>友情链接</h3>
 <ul><li><a href="http://devnote.cn/" rel="friend" title="开发笔记" target="_blank" onclick="javascript:;">DevNote</a></li>
 <li><a href="http://www.majunwei.com/" rel="friend" title="小马House" target="_blank" onclick="javascript:;">小马House</a></li>
 <li><a href="http://my.oschina.net/obj" rel="friend" title="开源中国" target="_blank" onclick="javascript:;">开源中国</a></li>
</ul>
</section>
</aside>
</div><!-- /#main -->

 <footer style="display:none;">
 <div>&#xa9; 2014 loveff.cn ' <a href="http://www.miitbeian.gov.cn/" target="_blank">京ICP备14030666号-1</a>
 <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1253964422'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1253964422%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
</div>
</footer>
<div id="qrcode" style="z-index: 999;cursor: pointer; position: fixed; bottom: 30px; left: 10px;"><img style="border: 1px solid #ddd;" width="95" height="95" src="${staticPath}/sc/imgs/qrcode_about.png" alt="扫描二维码即可访问本页" title="扫描二维码即可访问本页"></div>

<script src="${staticPath}/sc/js/??/jquery-1.11.1.min.js,lazyload-min.js"></script>
<script>
  $(document).ready(function() { $('#main').show(); $('footer').fadeIn('fast'); });
  LazyLoad.js(['${staticPath}/sc/??sh309/scripts/shCore.js,sh309/scripts/shAutoloader.js,js/loveff.js']);
</script>
<div id="backToTop" class="offScreen">返回顶部</div>
</body>
</html><!--[if !IE]>|xGv00|f7a14134f03df8c4cb51c1307ee33cf1<![endif]-->