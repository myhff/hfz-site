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
<link href="${staticPath}/sc/??sh309/styles/shCoreDefault.css,global.css?t=201502081612" rel="stylesheet" />
  <!--[if lt IE 9]>
<script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script><![endif]-->
</head>
</head>
<body>

<jsp:include page="${'/__segments.jsp?template=top'}" />

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

<jsp:include page="${'/__segments.jsp?template=aside'}" />
</div><!-- /#main -->

<jsp:include page="${'/__segments.jsp?template=footer'}" />
</body>
</html><!--[if !IE]>|xGv00|f7a14134f03df8c4cb51c1307ee33cf1<![endif]-->