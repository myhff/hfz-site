<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<title>LoveFF${title}</title>
<meta name="keywords" content="Linux, Python, Hadoop, 虚拟化, 云计算, 个人博客" />
<meta name="description" content="Java个人博客" />
<link href="${staticPath}/sc/??sh309/styles/shCoreDefault.css,global.css?t=201502081612" rel="stylesheet" />
  <!--[if lt IE 9]>
<script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script><![endif]-->
</head>
<body>

<jsp:include page="${'/__segments.jsp?template=top'}" />

<div id="nav-wrap"><nav>
 <ul>
 <li><a href="${basePath}/" class="${empty hit_cat?'active':''}">首页</a></li>
 <li><a href="${basePath}/about/">关于本博客</a></li><c:if test="${not empty hitCatSlug}">
 <li><a href="${basePath}/category/${hitCatSlug}/" class="${not empty hit_cat?'active':''}">${hitCatName}</a></li></c:if></ul></nav>
</div>

<div id="main">
<div id="content">
<c:if test="${not empty keywords}">
<article class="post">
<small style="font-family:arial;">找到约 ${page.totalRecordsNumber} 条结果 （用时约 ${seconds} 秒）当前第 ${page.pageIndex} 页，共 ${page.pageCount} 页</small>
<hr />
</article>
</c:if>
<c:if test="${empty listPosts and empty keywords}">
<article class="post">
<small>存档分类 '${hit_cat.name}' 没有文章${keywords}</small>
<hr />
</article>
</c:if>

<c:forEach var="foo" items="${listPosts}">
 <article class="post">
  <header>
   <h2 class="title"><a href="${basePath}/post/${foo.id}.html" rel="bookmark">${foo.title}</a></h2>
  <div class="post-cat">分类: ${foo.catalog.name}&nbsp;${not empty foo.tags ? '关键字:' : ''}&nbsp;${foo.tags}</div>
</header>
<div class="blog-content">${foo.excerpt}</div>
<div class="post-info">
<span><fmt:formatDate value="${foo.createTime}" pattern="yyyy-MM-dd HH:mm" /> | 浏览(${foo.viewCount}) | 评论(${foo.commentCount})</span> | <a href="${basePath}/post/${foo.id}.html">阅读全文</a>
</div><hr />
</article>

</c:forEach>

<c:if test="${not empty page}">
<!-- 分页 -->
<div class="paging">
 <c:if test="${page.pageIndex eq 2 and empty hit_cat}">
 <a href="${basePath}/${not empty keywords ? '?search=' : ''}${not empty keywords ? keywords : ''}">第一页</a>
</c:if>
 <c:if test="${page.pageIndex gt 2 and empty hit_cat}">
 <a href="${basePath}/${not empty keywords ? '?search=' : ''}${not empty keywords ? keywords : ''}">第一页</a>
 <a href="${basePath}/page/${page.pageIndex-1}/${not empty keywords ? '?search=' : ''}${not empty keywords ? keywords : ''}">上一页</a>
</c:if>
 <c:if test="${page.pageIndex eq 2 and not empty hit_cat}">
 <a href="${basePath}/category/${hit_cat.slug}/">第一页</a>
</c:if>
 <c:if test="${page.pageIndex gt 2 and not empty hit_cat}">
 <a href="${basePath}/category/${hit_cat.slug}/">第一页</a>
 <a href="${basePath}/category/${hit_cat.slug}/page/${page.pageIndex-1}/">上一页</a>
</c:if>
 <c:if test="${page.pageIndex lt page.pageCount and empty hit_cat}">
 <a href="${basePath}/page/${page.pageIndex+1}/${not empty keywords ? '?search=' : ''}${not empty keywords ? keywords : ''}">下一页</a>
 <a href="${basePath}/page/${page.pageCount}/${not empty keywords ? '?search=' : ''}${not empty keywords ? keywords : ''}">最后一页</a>
</c:if>
 <c:if test="${page.pageIndex lt page.pageCount and not empty hit_cat}">
 <a href="${basePath}/category/${hit_cat.slug}/page/${page.pageIndex+1}/">下一页</a>
 <a href="${basePath}/category/${hit_cat.slug}/page/${page.pageCount}/">最后一页</a>
</c:if>
</div><!-- /.paging -->
</c:if>
</div><!-- /#content -->

<jsp:include page="${'/__segments.jsp?template=aside'}" />
</div><!-- /#main -->

<jsp:include page="${'/__segments.jsp?template=footer'}" />
</body>
</html><!--[if !IE]>|xGv00|f7a14134f03df8c4cb51c1307ee33cf1<![endif]-->