<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset=utf-8 />
<title>测试 &raquo; 评论管理</title>
<link rel="stylesheet" href="${staticPath}/sc/test.css" />
  <!--[if lt IE 9]>
    <script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script>
<![endif]-->
<script src="${staticPath}/sc/js/jquery-1.11.1.min.js"></script>
<script src="${staticPath}/admin/sc/dialog/artDialog.js?skin=blue"></script>
<script src="${staticPath}/admin/sc/dialog/plugins/iframeTools.js"></script>
</head>
<body class="admin">

<aside>
 <ul>
 <li><a href="${basePath}/admin/list/">文章列表</a></li>
 <li><a href="${basePath}/admin/new-blog/">发表博文</a></li>
 <li><a href="${basePath}/admin/drafts/">草稿箱(${requestScope.draftsCount})</a></li>
 <li><a href="${basePath}/admin/blog-catalogs/">分类列表</a></li>
 <li><a href="${basePath}/admin/blog-comments/">评论管理</a></li>
 <li><a href="${basePath}/admin/attachments/">图片上传</a></li>
 <li style="margin-top: 15px;"><a href="${basePath}/">返回博客</a></li>
</ul>
</aside>

<div id="content">
  <section>
    <strong><i style="background:#00CC99;">&nbsp;</i>&nbsp;评论列表</strong>
    <div>none</div>
</section>
</div><!-- /#content -->
</body>
</html>