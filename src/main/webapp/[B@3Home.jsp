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
<link href="${staticPath}/sc/??sh309/styles/shCoreDefault.css,global.css?t=201407240229" rel="stylesheet" />
  <!--[if lt IE 9]>
  <style>::selection { background:#c00!important; color:#fff!important; }</style>
<script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script><![endif]-->
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

<aside class="scroll-afloat">
 <section>
 <div id="search">
 <input id="q_keywords" value="${keywords}" x-webkit-speech="x-webkit-speech" placeholder="关键字..." /><button onclick="location.href='${basePath}/?search='+document.getElementById('q_keywords').value;">搜索</button>
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
 <div>&#xa9; 2014 loveff.cn ' <a href="http://www.miitbeian.gov.cn/" target="_blank">京ICP备14030666号-1</a>
 <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1253964422'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1253964422%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
</div>
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