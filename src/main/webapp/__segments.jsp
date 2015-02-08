<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${param.template eq 'top' }">
<div id="top">
<header>
  <h1 id="logo"><a href="${basePath}/" target="_self"><span>LoveFF.cn</span><img src="${staticPath}/sc/imgs/logo1.png" alt="" /></a></h1>
 <div id="info">
 <span id="admin-login">
   <a href="${basePath}/admin/" target="_self">博客管理</a> |
   <a href="javascript:;" onclick="logout('${basePath}/admin/?tp=logout_');" target="_self">退出</a>
</span>
 <span class="tagline">Never, never, never, never give up.</span>
</div></header></div>
</c:if>

<c:if test="${param.template eq 'aside' }">
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
 <li><a href="${basePath}/category/${foo.slug}/" target="_self"><strong>${foo.name}(${foo.count})</strong></a></li>
 </c:if>
 <c:if test="${not (hit_cat.catId eq foo.catId)}">
 <li><a href="${basePath}/category/${foo.slug}/" target="_self">${foo.name}(${foo.count})</a></li>
 </c:if>
 </c:forEach>
</ul>
</section>
<c:if test="${empty param.hide_commons}">
 <section>
 <h3>最新回复</h3>
 <div class="ds-recent-comments" data-num-items="5" data-show-avatars="1" data-show-time="1" data-show-title="1" data-show-admin="1" data-excerpt-length="70"></div>
</section>
</c:if>
 <section>
 <h3>友情链接</h3>
 <ul><li><a href="http://devnote.cn/" rel="friend" title="开发笔记" target="_blank" onclick="javascript:;">DevNote</a></li>
 <li><a href="http://www.majunwei.com/" rel="friend" title="小马House" target="_blank" onclick="javascript:;">小马House</a></li>
 <li><a href="http://my.oschina.net/obj" rel="friend" title="开源中国" target="_blank" onclick="javascript:;">开源中国</a></li>
</ul>
</section>
</aside>
</c:if>

<c:if test="${param.template eq 'footer' }">
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
</c:if>
