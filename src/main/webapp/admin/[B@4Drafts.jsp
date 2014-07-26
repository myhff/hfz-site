<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset=utf-8 />
<title>测试 &raquo; 草稿箱</title>
<link rel="stylesheet" href="${staticPath}/sc/test.css" />
  <!--[if lt IE 9]>
    <script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script>
<![endif]-->
<style>
table { border-collapse: collapse; font-family: Verdana,sans-serif,宋体; }
table th { background: #aaa; }
table th, table td { border: 1px solid #ddd; padding: 3px 8px; font-size: 10.5pt; padding: 4px; }
table td.idx, table td.catalog, table td.opts { text-align: center; }
table td a, table td a:visited { text-decoration: none; }
#content table td a:active, table td a:hover { text-decoration: underline; color: red; }
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
    <strong><i style="background:#00CC99;">&nbsp;</i>&nbsp;草稿箱</strong>

<table style="width: 100%;">
	<tr>
		<th width="30">ID</th>
		<th align="left">标题</th>
		<th width="70">操作</th>
	</tr>
	<c:forEach var="foo" items="${requestScope.listPosts}">
	<tr id='post_${foo.id}'>
		<td class='idx'>${foo.id}</td>
		<td class='title'><a href="${basePath}/admin/edit-blog/?draft_=${foo.id}">${foo.title}</a></td>
		<td class='opts'>
			<a href='${basePath}/admin/drafts/?tp=delete_&postId=${foo.id}' onclick="return confirm('确认要删除吗');">删除</a>
			<a href='${basePath}/admin/edit-blog/?draft_=${foo.id}' title='点击编辑文章'>编辑</a>
		</td>
	</tr>
	</c:forEach>

	<c:if test="${empty requestScope.listPosts}">
	<tr>
		<td colspan="5">空</td>
	</tr>
	</c:if>
</table>

<c:if test="${not empty page}">
<!-- 分页 -->
<div class="paging">
  <c:if test="${page.pageIndex eq 2}">
    <a href="${basePath}/admin/list/">第一页</a>
  </c:if>
  <c:if test="${page.pageIndex gt 2}">
    <a href="${basePath}/admin/list/">第一页</a>
    <a href="${basePath}/admin/list/page/${page.pageIndex-1}">上一页</a>
  </c:if>
  <c:if test="${page.pageIndex lt page.pageCount}">
    <a href="${basePath}/admin/list/page/${page.pageIndex+1}/">下一页</a>
    <a href="${basePath}/admin/list/page/${page.pageCount}/">最后一页</a>
  </c:if>
</div><!-- /.paging -->
</c:if>

</section>

</div><!-- /#content -->

<script type="text/javascript">
function a() {
  art.dialog.confirm('操作！', function(){
  });
}
</script>
</body>
</html>