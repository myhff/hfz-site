<%@ page pageEncoding="UTF8" session="false" trimDirectiveWhitespaces="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset=utf-8 />
<title>测试 &raquo; 发表博文</title>
<link rel="stylesheet" href="${staticPath}/sc/test.css" />
  <!--[if lt IE 9]>
    <script src="//cdn.staticfile.org/html5shiv/3.7/html5shiv.min.js"></script>
<![endif]-->
<script src="${staticPath}/sc/js/jquery-1.11.1.min.js"></script>
<script src="${staticPath}/sc/js/jquery.form.js"></script>
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
<form id="myform" method="post">
  <c:if test="${empty article}">
  <input type='hidden' id='hdn_blog_id' name='draft' value='0' />
  </c:if>
  <c:if test="${not empty article}">
  <input type='hidden' name='id' value='${article.id}'/>
  <input type='hidden' name='draft' value='${draft}'/>
  </c:if>

  <section>
    <strong><i style="background:#428BD1;">&nbsp;</i>&nbsp;博客标题</strong>
    <div class="blog-title">
     <input type="text" name="title" required="required" placeholder="博客标题" value="${article.title}" class="input-xxlarge" />
   </div>
</section>

  <section>
    <strong><i style="background:#FF9966;">&nbsp;</i>&nbsp;博客摘要</strong>
    <div class="blog-excerpt">
     <textarea name="excerpt" placeholder="一段好的摘要能为你的博客添分加彩，同时也是对阅读者的一个非常好的引导。">${article.excerpt}</textarea>
   </div>
</section>

  <section>
    <strong><i style="background:#00CC99;">&nbsp;</i>&nbsp;博客正文</strong>
   <textarea id="elm1" name="content" style="width:100%">
   </textarea>
</section>

  <section>
    <strong><i style="background:#CC66CC;">&nbsp;</i>&nbsp;关键字</strong>
    <div class="blog-tags">
     <input type="text" name="tags" placeholder="为你的博客设置关键字，用逗号隔开，最多5个哦" value="${article.tags}" class="input-xxlarge" />
   </div>
</section>

  <section>
  <c:if test="${empty article or draft}">
    <button id="stash-bt">保存草稿</button>
    <button id="save-bt">发布</button>
  </c:if>
  <c:if test="${not empty article and not draft}">
    <button id="save-bt">保存修改</button>
  </c:if>
    &nbsp;&nbsp;&nbsp;
    <label>选择分类：</label>
    <select class='select_box' name="catId" id="blog-catalogs">
    <option value="">选择分类</option>
    <c:forEach var="foo" items="${requestScope.listCatalogs}">
    	<c:if test="${article.catId eq foo.catId}">
		<option value='${foo.catId}' selected='selected'>${foo.name}</option>
		</c:if>
    	<c:if test="${not (article.catId eq foo.catId)}">
		<option value='${foo.catId}'>${foo.name}</option>
		</c:if>
    </c:forEach>
	</select>
    
    <span id="err-msg" style="color:red;"></span>
</section>
</form><!-- /tag:form -->
</div><!-- /#content -->

<script id="cc_content" type="text/plain">
${article.content}
</script>
<script src="${staticPath}/sc/tinymce_4.1.7/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
  selector: "#elm1",
  plugins: [
    "autolink link image charmap preview", //autosave
    "visualblocks visualchars code fullscreen insertdatetime media",
    "autoresize table contextmenu directionality emoticons textcolor paste textcolor colorpicker textpattern syntax"
  ],
  toolbar: "styleselect fontselect fontsizeselect | forecolor backcolor | " +
     "bold italic | alignleft aligncenter alignright | bullist numlist | link image emoticons blockquote | syntax",
  autoresize_min_height: 200,
  autoresize_max_height: 300,
  menubar: true,
  statusbar : true,
  convert_urls: false,
  auto_focus: "elm1",
  fixed_toolbar_container: "#mytoolbar",
  toolbar_items_size: 'small',
  handle_event_callback : function(event) {
	  //alert(3);
  },
  //width: 748,
  
  font_formats: '宋体=宋体,SimSun;黑体=黑体;微软雅黑=微软雅黑;隶书=隶书;楷体=楷体,楷体_GB2312,SimKai;幼圆=幼圆;'+
  'Arial=arial,helvetica,sans-serif;'+
  'Comic Sans MS=comic sans ms,sans-serif;'+
  'Arial Black=arial black,avant garde;'+
  'Courier New=courier new,courier;'+
  'Tahoma=tahoma,arial,helvetica,sans-serif;Times New Roman=times new roman,times;Verdana=verdana,geneva;'
  ,
  language:'zh_CN'
});
$(window).load(function() {
	var a = document.getElementById('cc_content').innerHTML;
	//alert(a);
	tinymce.get('elm1').setContent(a);
});
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);;
    };
};
function TinyMceContentFilter(editorId) {
  //var filStr = tinymce.get(editorId).getBody().innerHTML;
}
Date.prototype.format = function (fmt) {
  var o = {
 "M+": this.getMonth() + 1, //月份 
 "d+": this.getDate(), //日 
 "h+": this.getHours(), //小时 
 "m+": this.getMinutes(), //分 
 "s+": this.getSeconds(), //秒 
 "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
 "S": this.getMilliseconds() //毫秒 
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
};
var Blog = {
  warn : true,
  hasSave : false,
  sysn : function(){},
  title : $('input[type=text][name=title]'),
  content : $('textarea[name=content]'),
  savebt:{
    set:function(){ $("#save-bt").html("正在发表"); },
    reset:function(){ $("#save-bt").html("发布"); }
  },
  stashbt:{
    set:function(){ $("#stash-bt").html("保存草稿中"); },
    reset:function(){ $("#stash-bt").html("保存草稿"); }
  },
  stash:function(){
    var tip = $("#err-msg");
    tip.html("");
    Blog.sysn();
    Blog.stashbt.set();
    $("#myform").ajaxForm({
      dataType:'json',
      type:'POST',
      url:'${basePath}/admin/new-blog/?tp=save_as_draft_',
      success:function(json){
        if (json.msg) {
          tip.html(json.msg);
          Blog.warn = true;
        } else if (json.draft) {
          $('#hdn_blog_id').val(json.draft);
            tip.html("草稿保存成功："+new Date().format("hh:mm:ss"));
            Blog.warn = false;
          }
          Blog.stashbt.reset();
        }
      });
  },
  save:function(){
    if(Blog.hasSave)
      return;
    Blog.hasSave = true;
    var tip = $("#err-msg");
    tip.html("");
    Blog.sysn();
    Blog.savebt.set();
    $("#myform").ajaxForm({
      dataType:'json',
      type:'POST',
      url:"${basePath}/admin/new-blog/?tp=save_",
      success:function(json){
        if (json.msg) {
          tip.html(json.msg);
          Blog.warn = false;
          Blog.hasSave = false;
        } else if(json.id) {
          Blog.hasSave = true;
          Blog.warn = false;
          location.href = "${basePath}/post/"+json.id+'.html';
        }
        Blog.savebt.reset();
      }
    });
  },
  leave:function(){
    if (!Blog.warn)
      return;
    var title = $('input[type=text][name=title]').val();
    if (title.length==0) {
      var body = $('textarea[name=content]').val();
      if (body.length == 0)
        return;
      }
      return "确认放弃此文章";
    }
  };
  $("#save-bt").click(function(){
    /* 验证博客分类是否选定 */
    var msg = document.getElementById("err-msg");
    var selectbox = document.getElementById("blog-catalogs");
    if (selectbox.value == 0) {
      msg.innerHTML = "<font color = 'red'>未选择博客分类</>";
      return false;
    } 
    Blog.save();
  });
  $("#stash-bt").click(function(){
	TinyMceContentFilter('elm1');
    Blog.stash();
});
</script>
</body>
</html>