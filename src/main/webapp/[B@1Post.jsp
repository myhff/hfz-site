<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<title>LoveFF &raquo; ${one.title}</title>
<meta name="keywords" content="${one.tags}" />
<meta name="description" content="${one.excerpt}" />
<link href="${staticPath}/sc/??sh309/styles/shCoreDefault.css,global.css?t=201407240229" rel="stylesheet" />
  <!--[if lt IE 9]>
<script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script><![endif]-->
</head>
</head>
<body>

<div id="top">
<header>
  <h1 id="logo"><a href="${basePath}/">LoveFF.cn</a></h1>
 <div id="info"><span class="tagline">Never, never, never, never give up.</span>
<span id="admin-login">
  <a href="${basePath}/admin/">博客管理</a> |
 <a href="javascript:;" onclick="logout('${basePath}/admin/?tp=logout_');">退出</a>
</span>
</div></header></div>

<div id="nav-wrap"><nav>
 <ul>
 <li><a href="${basePath}/">首页</a></li>
 <li><a href="${basePath}/about/">关于本博客</a></li><c:if test="${not empty hitCatSlug}">
 <li><a href="${basePath}/category/${hitCatSlug}/">${hitCatName}</a></li></c:if></ul></nav>
</div>


<div id="main">
<div id="content">
<article class="post" style="border:0; padding-bottom:0;">
<header>
<h2 class="blog-title"><span class="t">${one.title}</span></h2>
<div class="post-cat">分类: ${one.catalog.name}
<span style="margin-left:30px;"><fmt:formatDate value="${one.createTime}" pattern="yyyy-MM-dd HH:mm" /> | 浏览(${one.viewCount}) | <a href="#respond">评论(${one.commentCount})</a></span>
</div>
<hr />
<c:if test="${not empty one.excerpt or not empty one.tags}">
<section class="blog-excerpt">
<c:if test="${not empty one.excerpt}">
<em class="corner">摘要</em>${one.excerpt}
</c:if>
<c:if test="${not empty one.excerpt and not empty one.tags}">
<span style="display:block;height:8px;"></span>
</c:if>
<%-- 标签  --%>
<c:if test="${not empty one.tags}">
<div class="BlogTags">
<c:forEach var="tag" items="${fn:split(one.tags, ',')}">
<c:if test="${not empty fn:trim(tag)}">
<a href="${basePath}/?search=${fn:trim(tag)}" class="tag" target="_black">${fn:trim(tag)}</a>
</c:if>
</c:forEach>
</div><!-- /.BlogTags -->
</c:if>
</section>
</c:if>
</header>
<%-- 文章内容  --%>
<div class="blog-content" style="clear:both;">${one.content}</div>
</article><!-- /.post -->

<c:if test="${not empty listRelatedPosts}">
<section class="BlogRelated">
<strong>相关文章阅读：</strong>
<c:forEach var="foo" items="${listRelatedPosts}">
<ul>
<c:if test="${fn:length(foo.title) > 20}">
<li><span class="date"><fmt:formatDate value="${foo.createTime}" pattern="yyyy/MM/dd" /></span><a href="${basePath}/post/${foo.id}.html" title="${foo.title}">${fn:substring(foo.title, 0, 20)} ...</a></li>
</c:if>
<c:if test="${ fn:length(foo.title) <= 20}">
<li><span class="date"><fmt:formatDate value="${foo.createTime}" pattern="yyyy/MM/dd" /></span><a href="${basePath}/post/${foo.id}.html" title="${foo.title}">${fn:substring(foo.title, 0, 20)}</a></li>
</c:if>
</ul>
</c:forEach>
</section>
</c:if>

<section class="BlogLinks">
<ul>
<c:if test="${not empty one_last}">
<li class="prev"><a href="${basePath}/post/${one_last.id}.html" title="上一篇：${one_last.title}">« 上一篇</a></li>
</c:if>
<c:if test="${not empty one_next}">
<li class="next"><a href="${basePath}/post/${one_next.id}.html" title="下一篇：${one_next.title}">下一篇 »</a></li>
</c:if>
</ul>
</section>

  <!-- 评论 -->
<div class="ds-thread" data-thread-key="${foo.id}" data-title="${one.title}" data-url="${basePath}/post/${one.id}.html"></div>
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
 <li><a href="${basePath}/category/${foo.slug}/"><strong>${foo.name}(${foo.count})</strong></a></li>
 </c:if>
 <c:if test="${not (hit_cat.catId eq foo.catId)}">
 <li><a href="${basePath}/category/${foo.slug}/">${foo.name}(${foo.count})</a></li>
 </c:if>
 </c:forEach>
</ul>
</section>
 <section>
 <h3>最新回复</h3>
 <div class="ds-recent-comments" data-num-items="5" data-show-avatars="1" data-show-time="1" data-show-title="1" data-show-admin="1" data-excerpt-length="70"></div>
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

 <footer>
 <div>&#xa9; 2014 loveff.cn ' <a href="http://www.miitbeian.gov.cn/" target="_blank">京ICP备14030666号-1</a></div>
</footer>
<div id="qrcode" style="z-index: 999;cursor: pointer; position: fixed; bottom: 30px; left: 10px;"><img style="border: 1px solid #ddd;" width="95" height="95" src="${staticPath}/sc/imgs/qrcode_home.png" alt="扫描二维码即可访问本页" title="扫描二维码即可访问本页"></div>

<script src="${staticPath}/sc/js/??/jquery-1.11.1.min.js,lazyload-min.js"></script>
<script>
  var duoshuoQuery = {short_name:"loveff"};
  LazyLoad.js(['${staticPath}/sc/??sh309/scripts/shCore.js,sh309/scripts/shAutoloader.js,js/loveff.js', 'http://static.duoshuo.com/embed.js']);
</script>
<div id="backToTop" class="offScreen">返回顶部</div>
</body>
</html><!--[if !IE]>|xGv00|f7a14134f03df8c4cb51c1307ee33cf1<![endif]-->