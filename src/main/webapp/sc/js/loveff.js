function $getCookie(name) {
  var reg = new RegExp("(^| )" + name + "(?:=([^;]*))?(;|$)"), 
  val = document.cookie.match(reg);
  return val ? (val[2] ? unescape(val[2]) : "") : null;
};
function $id(id) {
  return typeof(id) == "string" ? document.getElementById(id) : id;
};
function $isArray(source) {
  return '[object Array]' == Object.prototype.toString.call(source);
};
function $setCookie(name, value, expires, path, domain, secure) {
  var exp = new Date(),
      expires = arguments[2] || null,
      path = arguments[3] || "/",
      domain = arguments[4] || null,
      secure = arguments[5] || false;
  expires ? exp.setMinutes(exp.getMinutes() + parseInt(expires)) : "";
  document.cookie = name + '=' + escape(value) + (expires ? ';expires=' + exp.toGMTString() : '') + (path ? ';path=' + path : '') + (domain ? ';domain=' + domain : '') + (secure ? ';secure' : '');
};
function $strTrim(str, code) {
  var argus = code || "\\s";
  var temp = new RegExp("(^" + argus + "*)|(" + argus + "*$)", "g");
  return str.replace(temp, "");
};
function $xhrMaker() {
  var xhr;
  try {
    xhr = new XMLHttpRequest();
  } catch (e) {
    try {
      xhr = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
      try {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
      } catch (e) {
        xhr = null;
      }
    }
  };
  return xhr;
};
/*
(function() {
  var xhr = $xhrMaker(),
  url = '${basePath}/admin/?tp=login_';
  try {
    xhr.open('POST', url, false);
    xhr.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
    xhr.send('loginname=admin&pwd=123456');
  } catch (e) {
    alert(e);
  }
})();
*/
function utf8_decode(str_data) {
  var tmp_arr = [],
     i = 0,
    ac = 0,
    c1 = 0,
    c2 = 0,
    c3 = 0,
    c4 = 0;
  str_data += '';
  while (i < str_data.length) {
    c1 = str_data.charCodeAt(i);
    if (c1 <= 191) {
      tmp_arr[ac++] = String.fromCharCode(c1);
      i++;
    } else if (c1 <= 223) {
      c2 = str_data.charCodeAt(i + 1);
      tmp_arr[ac++] = String.fromCharCode(((c1 & 31) << 6) | (c2 & 63));
      i += 2;
    } else if (c1 <= 239) {
      c2 = str_data.charCodeAt(i + 1);
      c3 = str_data.charCodeAt(i + 2);
      tmp_arr[ac++] = String.fromCharCode(((c1 & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
      i += 3;
    } else {
      c2 = str_data.charCodeAt(i + 1);
      c3 = str_data.charCodeAt(i + 2);
      c4 = str_data.charCodeAt(i + 3);
      c1 = ((c1 & 7) << 18) | ((c2 & 63) << 12) | ((c3 & 63) << 6) | (c4 & 63);
      c1 -= 0x10000;
      tmp_arr[ac++] = String.fromCharCode(0xD800 | ((c1 >> 10) & 0x3FF));
      tmp_arr[ac++] = String.fromCharCode(0xDC00 | (c1 & 0x3FF));
      i += 4;
    }
  }
  return tmp_arr.join('');
}
$(document).ready(function() {
  var _skey = $getCookie('skey');
  if (_skey == null) {
  $('#admin-login').hide();
  } else {
  $('#admin-login').fadeIn('slow');
  }
});
function logout(url) {
  $.get(url, function(result){
  $('#admin-login').fadeOut('normal');
  });
}
/** 获取元素坐标 */
function findPosition(oElement) {
	var x2 = 0;
	var y2 = 0;
	var width = oElement.offsetWidth;
	var height = oElement.offsetHeight;
	//alert(width + "=" + height);
	if (typeof (oElement.offsetParent) != 'undefined') {
		for ( var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent) {
			posX += oElement.offsetLeft;
			posY += oElement.offsetTop;
		}
		x2 = posX + width;
		y2 = posY + height;
		return [ posX, posY, x2, y2 ];

	} else {
		x2 = oElement.x + width;
		y2 = oElement.y + height;
		return [ oElement.x, oElement.y, x2, y2 ];
	}
}
function processNode(te) {
    var rect = te.getBoundingClientRect();
    alert(te.tagName + ":" + rect.left + "," + rect.top + ", " + rect.right + "," + rect.bottom);
    for (var i = 0; i < te.children.length; i++) {
        processNode(te.children[i]);
    }
}
// 计算左边距
function doCalcLeft(selector) {
  var zb = findPosition($(selector).get(0));
  var leftOffset = parseInt(zb[2]);
  var selfMarginLeft = $(selector).css('margin-left');
  var selfMarginRight = $(selector).css('margin-right');
  return parseInt(leftOffset) + parseInt(selfMarginRight) - parseInt(selfMarginLeft) + 12;
}
var toTop = {
  setup: function() {
  var a = $(window).height() / 5;
  var isChrome = window.navigator.userAgent.indexOf("Chrome") !== -1;
  $(window).scroll(function() {
    (window.innerWidth ? window.pageYOffset : document.documentElement.scrollTop) >= a ? $("#backToTop").removeClass("offScreen") : $("#backToTop").addClass("offScreen");
    if (isChrome) {
      var scrollAFloat = 388;
      if ((window.innerWidth ? window.pageYOffset : document.documentElement.scrollTop) >= scrollAFloat) {
        if ($('#main .scroll-afloat').attr('class').indexOf('scroll-afloat-impl') == -1) {
          $('#main .scroll-afloat').css('left', doCalcLeft('#main .scroll-afloat')).addClass('scroll-afloat-impl');
        }
      } else {
        $('#main .scroll-afloat').removeClass('scroll-afloat-impl');
      }
    }
  });
  $("#backToTop").click(function() {
  $("html, body").animate({
    scrollTop: "0px"
  }, 100);
    return false;
  });
  }
};
$(document).ready(function() {
  toTop.setup();
});
document.onkeydown = function(e){ 
  var ev = document.all ? window.event : e;
  if (ev.keyCode == 13) {
    if($("#search input").is(":focus")) {
      $('#search button').click();
    }
  }
};
/* BLOG */
function path() {
  var args = arguments, result = [];
  for(var i = 0; i < args.length; i++)
  result.push(args[i].replace('@', '/sc/sh309/scripts/'));
  return result;
};
$(document).ready(function(){
 SyntaxHighlighter.autoloader.apply(null, path(
 'applescript           @shBrushAppleScript.js',
 'actionscript3 as3     @shBrushAS3.js',
 'bash shell            @shBrushBash.js',
 'coldfusion cf         @shBrushColdFusion.js',
 'cpp c                 @shBrushCpp.js',
 'obj-c objc            @shBrushObjC.js',
 'c# c-sharp csharp     @shBrushCSharp.js',
 'css                   @shBrushCss.js',
 'delphi pascal         @shBrushDelphi.js',
 'diff patch pas        @shBrushDiff.js',
 'erl erlang            @shBrushErlang.js',
 'groovy                @shBrushGroovy.js',
 'haxe hx               @shBrushHaxe.js',
 'java                  @shBrushJava.js',
 'jfx javafx            @shBrushJavaFX.js',
 'js jscript javascript @shBrushJScript.js',
 'perl pl               @shBrushPerl.js',
 'php                   @shBrushPhp.js',
 'text plain            @shBrushPlain.js',
 'py python             @shBrushPython.js',
 'ruby rails ror rb     @shBrushRuby.js',
 'scala	                @shBrushScala.js',
 'sql                   @shBrushSql.js',
 'vb vbnet              @shBrushVb.js',
 'xml xhtml xslt html   @shBrushXml.js'
 ));
 SyntaxHighlighter.all();
 SyntaxHighlighter.defaults['toolbar'] = true;
});