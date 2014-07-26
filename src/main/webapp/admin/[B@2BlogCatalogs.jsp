<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset=utf-8 />
<title>测试 &raquo; 分类管理</title>
<link rel="stylesheet" href="${staticPath}/sc/test.css" />
  <!--[if lt IE 9]>
    <script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script>
<![endif]-->
<style>
table { border-collapse: collapse; font-family: Verdana,sans-serif,宋体; }
table th { background: #aaa; }
table th, table td { border: 1px solid #ddd; padding: 3px 8px; font-size: 10.5pt; }
table td.idx, table td.num, table td.opts { text-align: center; }
h3 { font-size: 10.5pt; margin: 15px 0 5px; }
h3 span { font-weight: normal; margin: 0 0 0 10px; color: #666666; }
form { font-size: 10.5pt; margin-bottom: 20px; }
form input { outline: 0; }
form input.SUBMIT {
  font-weight: bold; font-size: 12pt;
  padding: 2px 10px;
}
</style>
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
    <strong><i style="background:#428BD1;">&nbsp;</i>&nbsp;添加博客分类</strong>
<form id='CatalogForm' action='/action/blog/add_blog_catalog?space=203644&id=0' method='post'>
    <div id='error_msg' class='error_msg' style='display:none;'></div>
    <label>分类名称:</label><input id='txt_link_name' type='text' name='name' value='' size='15' tabindex='1'/>
    <label>唯一标识:</label><input id='txt_link_slug' type='text' name='slug' value='' size='10' tabindex='1'/>
    <label>排序值:</label><input type='text' name='sort_order' value='0' size='3'/>
    <span class="submit">
          <input type='submit' value='添加&nbsp;&raquo;' tabindex='3' class='BUTTON SUBMIT'/>
        </span>
</form>
</section>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

  <section>
    <strong><i style="background:#CC66CC;">&nbsp;</i>&nbsp;博客分类 <span>(点击分类名编辑)</span></strong>
<form class='BlogCatalogs'>
<table style="width:100%;">
	<tr>
		<th width="30">序号</th>
		<th width="120">分类名</th>
		<th width="120">唯一标识</th>
		<th width="30">文章</th>
		<th>&nbsp;</th>
		<th width="70">操作</th>
	</tr>
	<c:forEach var="foo" items="${requestScope.listCatalogs}">
	<tr id='catalog_${foo.catId}'>
		<td class='idx'>${foo.sortOrder}</td>
		<td class='name'>${foo.name}</td>
		<td>${foo.slug}</td>
		<td class='num'>${foo.count}</td>
		<td>&nbsp;</td>
		<td class='opts'>
			<a href='?edit_catalog=119909' title='点击修改博客分类'>修改</a>
			<a href='#' onclick="return delete_catalog(203644,119909);">删除</a>
		</td>
	</tr>
	</c:forEach>
</table>
</form>
</section>

</div><!-- /#content -->
</body>
</html>