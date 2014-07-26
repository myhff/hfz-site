<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset=utf-8 />
<title>测试 &raquo; 图片上传</title>
<link rel="stylesheet" href="${staticPath}/sc/test.css" />
  <!--[if lt IE 9]>
    <script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script>
<![endif]-->
<script type="text/javascript">
function ajaxFileUpload() {
/*	
$("#loading").ajaxStart(function(){
  $(this).show();
}).ajaxComplete(function(){
  $(this).hide();
});
*/
$.ajaxFileUpload({
  url : '${basePath}/admin/attachments/?tp=new_',
  secureuri : false,
  fileElementId : 'fileToUpload',
  dataType : 'json',
  beforeSend : function() {
    $("#loading").show();
  },
  complete : function() {
    $("#loading").hide();
  },				
  success: function(data, status) {
    if (typeof(data.error) != 'undefined') {
      if (data.error != '') {
        alert(data.error);
      } else {
        alert(data.msg);
        $('#attach_url a').html(data.url).attr('href', data.url);
      }
    }
  },
  error: function(data, status, e) {
    alert(e);
  }
});
return false;
}
</script>
<script src="${staticPath}/sc/js/jquery-1.11.1.min.js"></script>
<script src="${staticPath}/sc/js/ajaxfileupload.js"></script>
<script src="${staticPath}/admin/sc/dialog/artDialog.js?skin=blue"></script>
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
    <strong><i style="background:#FF9966;">&nbsp;</i>&nbsp;图片上传</strong>

<form name="myform" action="" method="POST" enctype="multipart/form-data">
  <img id="loading" src="${staticPath}/sc/imgs/loading.gif" style="display:none;" />
  <input id="fileToUpload" type="file" size="45" name="fileToUpload" class="input" />
 <button class="button" id="buttonUpload" onclick="return ajaxFileUpload();">Upload</button>
</form>

 <p id="attach_url" style="color:blue;"><a href="${last_img}" target="_blank">${last_img}</a></p>

 <span>提示信息：</span>
 <ul style="margin: 0; padding-left: 32px;">
 <li class="note">上传文件请压缩后再上传，允许zip, rar, gz, tar, bz2, 7z, jar, war格式的压缩文件</li>
 <li class="note">上传图片推荐使用png, jpg, gif等类型</li>
 <li class="note">文件大小不能超过10MB</li>
</ul>
</section>
</div><!-- /#content -->
</body>
</html>