package com.changxiao.simpletestdemo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN;

public class LocalWebActivity extends AppCompatActivity {

    private final static String JsInterfaceName = "JsInterface";
    final static String REG_TITLE       = "<script>(.+)</script>";

    private String content = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "  <title>ximalaya</title>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no\">\n" +
            "  <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "  <link rel=\"stylesheet\" type=\"text/css\" href=\"./style.css\">\n" +
            "  <script src=\"./flexible.js\"></script>\n" +
            "  <script src=\"./underscore.min.js\"></script>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"container\"></div>\n" +
            "<script id=\"t_album\" type=\"text/template\">\n" +
            "  <div class=\"j-card\">\n" +
            "    <div class=\"title\">\n" +
            "      <span class=\"txt\">主讲人</span>\n" +
            "    </div>\n" +
            "    <div class=\"intro\">\n" +
            "      <%= personalDescription %>\n" +
            "    </div>\n" +
            "    <div class=\"load-more\">\n" +
            "      <div class=\"mask\"></div>\n" +
            "      <p class=\"load-txt\">展开全部</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <div class=\"j-card\">\n" +
            "    <div class=\"title\"><span class=\"txt\">简介</span></div>\n" +
            "    <%=introRich%>\n" +
            "    <div class=\"load-more\">\n" +
            "      <div class=\"mask\"></div>\n" +
            "      <p class=\"load-txt\">展开全部</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "\n" +
            "  <div class=\"j-card comment-box\">\n" +
            "    <div class=\"title\"><span class=\"txt\">听友说</span></div>\n" +
            "    <p class=\"comment-num mgb-10\">共<%=comments.totalCount%>条评论</p>\n" +
            "    <ul>\n" +
            "      <%for(var i = 0; i < comments.list.length; i++){%>\n" +
            "      <li class=\"comment-item\">\n" +
            "        <p class=\"comment-content\"><%=comments.list[i].content%></p>\n" +
            "        <div class=\"clear\">\n" +
            "          <p class=\"star-bar fl\">\n" +
            "            <span class=\"star-grade\"><span class=\"star-graded\" style=\"width:<%=(comments.list[i].album_score / 5) * 100 %>%\"></span></span>\n" +
            "            <span class=\"c09 grade\"><%=comments.list[i].album_score.toFixed(1)%></span>\n" +
            "          </p>\n" +
            "          <p class=\"fr user\"><span class=\"nickname\"><%=comments.list[i].nickname%></span><span class=\"userPic\" style=\"background-image: url(<%=comments.list[i].smallHeader%>)\"></span></p>\n" +
            "        </div>\n" +
            "      </li>\n" +
            "      <%}%>\n" +
            "    </ul>\n" +
            "    <div class=\"load-more\">\n" +
            "      <div class=\"mask\"></div>\n" +
            "      <p class=\"load-txt\">展开全部</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <%if (!!groupInfo){%>\n" +
            "  <div class=\"j-card\">\n" +
            "    <div class=\"title\"><span class=\"txt\">加入付费专享群</span></div>\n" +
            "    <div class=\"sub-title\">购买专辑，与主讲人亲密互动</div>\n" +
            "    <div class=\"join-group clear\">\n" +
            "      <div class=\"head-pic fl\" style=\"background-image: url(<%=groupInfo.coverPath%>)\"></div>\n" +
            "      <div class=\"content fl\">\n" +
            "        <div class=\"group-title\"><%=groupInfo.name%></div>\n" +
            "        <div class=\"tags\">\n" +
            "          <span class=\"tag-pay tag\"></span>\n" +
            "          <span class=\"tag-num tag\"><%=groupInfo.memberCount%>人</span>\n" +
            "        </div>\n" +
            "        <div class=\"intro ellipsis\"><%=groupInfo.intro%></div>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "    <div class=\"load-more\">\n" +
            "      <div class=\"mask\"></div>\n" +
            "      <p class=\"load-txt\">展开全部</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <%}%>\n" +
            "  <div class=\"j-card\">\n" +
            "    <div class=\"title\"><span class=\"txt\">购买须知</span></div>\n" +
            "    <%=buyNotes%>\n" +
            "    <div class=\"load-more\">\n" +
            "      <div class=\"mask\"></div>\n" +
            "      <p class=\"load-txt\">展开全部</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <%if(!!other_content){%>\n" +
            "  <div class=\"j-card\">\n" +
            "    <div class=\"title\"><span class=\"txt\">其他说明</span></div>\n" +
            "    <%=other_content%>\n" +
            "    <div class=\"load-more\">\n" +
            "      <div class=\"mask\"></div>\n" +
            "      <p class=\"load-txt\">展开全部</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <%}%>\n" +
            "</script>\n" +
            "<script>\n" +
            "    var data  = {\n" +
            "<script type=\"text/javascript\">\n" +
            "    function isIos () {\n" +
            "      var u = navigator.userAgent\n" +
            "      var isiOS = !!u.match(/\\(i[^;]+;( U;)? CPU.+Mac OS X/)\n" +
            "      return isiOS\n" + "    }\n" +
            "    function bridgePostMsg (url) {\n" + "      if (isIos()) {\n" +
            "        window.location = url\n" + "      } else {\n" +
            "        var ifr = document.createElement('iframe')\n" +
            "        ifr.style.display = 'none'\n" + "        ifr.setAttribute('src', url)\n" +
            "        document.body.appendChild(ifr)\n" + "        setTimeout(function () {\n" +
            "          document.body.removeChild(ifr)\n" + "        }, 1000)\n" + "      }\n" +
            "    }\n" + "    function _getHybridUrl (params) {\n" +
            "      var url = 'webnotify://', paramStr = ''\n" +
            "      url += params.path + '?t=' + new Date().getTime()\n" +
            "      if (params.callback) {\n" + "        url += '&callback=' + params.callback\n" +
            "        delete params.callback\n" + "      }\n" + "      if (params.param) {\n" +
            "        paramStr = typeof params.param == 'object' ? JSON.stringify(params.param) : params.param\n" +
            "        url += '&param=' + encodeURIComponent((paramStr))\n" + "      }\n" +
            "      return url\n" + "    }\n" + "\n" + "    function requestHybrid (params) {\n" +
            "      var ts = (new Date().getTime())\n" + "      var method = 'hybrid_' + ts\n" +
            "      var tempFn\n" + "      if (params.callback) {\n" + "        tempFn = params.callback\n" +
            "        params.callback = method\n" + "        window[method] = function (data) {\n" +
            "          tempFn(data)\n" + "          delete window[method]\n" + "        }\n" +
            "      }\n" + "      bridgePostMsg(_getHybridUrl(params))\n" + "    }\n" + "\n" +
            "    window['get_album_data'] = function (data) {\n" + "\n" + "    }\n" + "\n" +
            "    var compiler = _.template(document.getElementById('t_album').innerHTML)\n" +
            "    var html = compiler(data)\n" + "    var container = document.querySelector('#container')\n" +
            "    container.innerHTML=html\n" + "    ;(function () {\n" +
            "      var commNum = document.querySelector('.comment-box .comment-num')\n" +
            "      var joinGroup = document.querySelector('.join-group')\n" +
            "      if (!!commNum) {\n" + "        commNum.addEventListener('click', function () {\n" +
            "          requestHybrid({\n" + "            path: 'open_comment'\n" + "          })\n" +
            "        })\n" + "      }\n" + "      if (!!joinGroup) {\n" +
            "        joinGroup.addEventListener('click', function () {\n" +
            "          requestHybrid({\n" + "            path: 'open_group'\n" +
            "          })\n" + "        })\n" + "      }\n" +
            "      window.addEventListener('load', loaded)\n" +
            "      var loadMores = document.querySelectorAll('.load-more')\n" +
            "      loadMores.forEach(function (loadMore) {\n" +
            "        loadMore.style.display = 'none'\n" + "        var loadTxt = loadMore.querySelector('.load-txt')\n" +
            "        loadTxt.addEventListener('click', function () {\n" +
            "          loadMore.parentNode.style.height = 'auto'\n" +
            "          loadMore.style.display = 'none'\n" + "          setTimeout(function () {\n" +
            "            requestHybrid({\n" + "              path: 'post_height'\n" + "            })\n" +
            "          }, 10)\n" + "        })\n" + "      })\n" + "      function loaded () {\n" +
            "        var cards = document.querySelectorAll('.j-card')\n" +
            "        var dpr = parseInt(document.querySelector('html').getAttribute('data-dpr'))\n" +
            "        cards.forEach(function (card) {\n" + "          if (card.offsetHeight / dpr > 620 ) {\n" +
            "            card.style.height = 620 * dpr + 'px'\n" + "            card.querySelector('.load-more').style.display = 'block'\n" +
            "          }\n" + "        })\n" + "      }\n" + "    })()\n" + "  </script>\n" + "</body>\n" + "</html>\n";

    private WebView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_web);

        mContent = (WebView) findViewById(R.id.wv_content);

        WebSettings settings = mContent.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
            settings.setSupportZoom(false);
            settings.setBuiltInZoomControls(false);
            settings.setUseWideViewPort(true); // 让图片自适应屏幕
            settings.setLayoutAlgorithm(SINGLE_COLUMN);
            settings.setLoadWithOverviewMode(true);
            settings.setDomStorageEnabled(true);
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            settings.setAppCacheEnabled(false);
            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    settings.setMediaPlaybackRequiresUserGesture(false);
                } catch (Throwable e) {
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContent.setWebContentsDebuggingEnabled(true);
        }
        try {
            mContent.addJavascriptInterface(new JsInterface(), JsInterfaceName); //JS交互
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //加载本地HTML页面
        String path = "file:///android_asset/albumTemplate" + File.separator + "index.html";
//        mContent.loadUrl(path);


        String data = "";
        try {
            // 读取assets目录下的文件需要用到AssetManager对象的Open方法打开文件
            InputStream is = getAssets().open("albumTemplate/index.html");
            // loadData()方法需要的是一个字符串数据所以我们需要把文件转成字符串
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            is.close();
            data = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        String title = getTitle(data);
        data = getData(data);
        mContent.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String s = gson.toJson(new ToJson());
        //        showContent();
    }

    private void showContent() {
        String data = "";
        try {
            // 读取assets目录下的文件需要用到AssetManager对象的Open方法打开文件
            InputStream is = getAssets().open("albumTemplate/data.json");
            // loadData()方法需要的是一个字符串数据所以我们需要把文件转成字符串
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            is.close();
            data = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContent.loadDataWithBaseURL(null, getContent(data), "text/html", "utf-8", null);
    }

    public static String getTitle(String html) {
        String str = null;
        Matcher matcher = Pattern.compile(REG_TITLE).matcher(html);
        if (matcher.find()) {
            str = matcher.group(1);
        }

        return str;
    }

    private String getData(String content) {
        String data = "";
        try {
            // 读取assets目录下的文件需要用到AssetManager对象的Open方法打开文件
            InputStream is = getAssets().open("albumTemplate/data.json");
            // loadData()方法需要的是一个字符串数据所以我们需要把文件转成字符串
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            is.close();
            data = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content.contains("var data")) {
            data = content.replace("var data", "var data = " + data);
        }
        return data;
    }

    public static class ToJson {
        public String name = "";
        public String sex = "s";
        public String age;
        public int ss;
    }

    public String getContent(String data) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>ximalaya</title>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no\">\n" +
                "  <meta name=\"format-detection\" content=\"telephone=no\">\n" +
                "  <link rel=\"stylesheet\" type=\"text/css\" href=\"./style.css\">\n" +
                "  <script src=\"./flexible.js\"></script>\n" +
                "  <script src=\"./underscore.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"container\"></div>\n" +
                "<script id=\"t_album\" type=\"text/template\">\n" +
                "  <div class=\"j-card\">\n" +
                "    <div class=\"title\">\n" +
                "      <span class=\"txt\">主讲人</span>\n" +
                "    </div>\n" +
                "    <div class=\"intro\">\n" +
                "      <%= personalDescription %>\n" +
                "    </div>\n" +
                "    <div class=\"load-more\">\n" +
                "      <div class=\"mask\"></div>\n" +
                "      <p class=\"load-txt\">展开全部</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <div class=\"j-card\">\n" +
                "    <div class=\"title\"><span class=\"txt\">简介</span></div>\n" +
                "    <%=introRich%>\n" +
                "    <div class=\"load-more\">\n" +
                "      <div class=\"mask\"></div>\n" +
                "      <p class=\"load-txt\">展开全部</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "  <div class=\"j-card comment-box\">\n" +
                "    <div class=\"title\"><span class=\"txt\">听友说</span></div>\n" +
                "    <p class=\"comment-num mgb-10\">共<%=comments.totalCount%>条评论</p>\n" +
                "    <ul>\n" +
                "      <%for(var i = 0; i < comments.list.length; i++){%>\n" +
                "      <li class=\"comment-item\">\n" +
                "        <p class=\"comment-content\"><%=comments.list[i].content%></p>\n" +
                "        <div class=\"clear\">\n" +
                "          <p class=\"star-bar fl\">\n" +
                "            <span class=\"star-grade\"><span class=\"star-graded\" style=\"width:<%=(comments.list[i].album_score / 5) * 100 %>%\"></span></span>\n" +
                "            <span class=\"c09 grade\"><%=comments.list[i].album_score.toFixed(1)%></span>\n" +
                "          </p>\n" +
                "          <p class=\"fr user\"><span class=\"nickname\"><%=comments.list[i].nickname%></span><span class=\"userPic\" style=\"background-image: url(<%=comments.list[i].smallHeader%>)\"></span></p>\n" +
                "        </div>\n" +
                "      </li>\n" +
                "      <%}%>\n" +
                "    </ul>\n" +
                "    <div class=\"load-more\">\n" +
                "      <div class=\"mask\"></div>\n" +
                "      <p class=\"load-txt\">展开全部</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <%if (!!groupInfo){%>\n" +
                "  <div class=\"j-card\">\n" +
                "    <div class=\"title\"><span class=\"txt\">加入付费专享群</span></div>\n" +
                "    <div class=\"sub-title\">购买专辑，与主讲人亲密互动</div>\n" +
                "    <div class=\"join-group clear\">\n" +
                "      <div class=\"head-pic fl\" style=\"background-image: url(<%=groupInfo.coverPath%>)\"></div>\n" +
                "      <div class=\"content fl\">\n" +
                "        <div class=\"group-title\"><%=groupInfo.name%></div>\n" +
                "        <div class=\"tags\">\n" +
                "          <span class=\"tag-pay tag\"></span>\n" +
                "          <span class=\"tag-num tag\"><%=groupInfo.memberCount%>人</span>\n" +
                "        </div>\n" +
                "        <div class=\"intro ellipsis\"><%=groupInfo.intro%></div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class=\"load-more\">\n" +
                "      <div class=\"mask\"></div>\n" +
                "      <p class=\"load-txt\">展开全部</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <%}%>\n" +
                "  <div class=\"j-card\">\n" +
                "    <div class=\"title\"><span class=\"txt\">购买须知</span></div>\n" +
                "    <%=buyNotes%>\n" +
                "    <div class=\"load-more\">\n" +
                "      <div class=\"mask\"></div>\n" +
                "      <p class=\"load-txt\">展开全部</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <%if(!!other_content){%>\n" +
                "  <div class=\"j-card\">\n" +
                "    <div class=\"title\"><span class=\"txt\">其他说明</span></div>\n" +
                "    <%=other_content%>\n" +
                "    <div class=\"load-more\">\n" +
                "      <div class=\"mask\"></div>\n" +
                "      <p class=\"load-txt\">展开全部</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <%}%>\n" +
                "</script>\n" +
                "<script>\n" +
                "    var data  = {\n" + data +
                "<script type=\"text/javascript\">\n" +
                "    function isIos () {\n" +
                "      var u = navigator.userAgent\n" +
                "      var isiOS = !!u.match(/\\(i[^;]+;( U;)? CPU.+Mac OS X/)\n" +
                "      return isiOS\n" + "    }\n" +
                "    function bridgePostMsg (url) {\n" + "      if (isIos()) {\n" +
                "        window.location = url\n" + "      } else {\n" +
                "        var ifr = document.createElement('iframe')\n" +
                "        ifr.style.display = 'none'\n" + "        ifr.setAttribute('src', url)\n" +
                "        document.body.appendChild(ifr)\n" + "        setTimeout(function () {\n" +
                "          document.body.removeChild(ifr)\n" + "        }, 1000)\n" + "      }\n" +
                "    }\n" + "    function _getHybridUrl (params) {\n" +
                "      var url = 'webnotify://', paramStr = ''\n" +
                "      url += params.path + '?t=' + new Date().getTime()\n" +
                "      if (params.callback) {\n" + "        url += '&callback=' + params.callback\n" +
                "        delete params.callback\n" + "      }\n" + "      if (params.param) {\n" +
                "        paramStr = typeof params.param == 'object' ? JSON.stringify(params.param) : params.param\n" +
                "        url += '&param=' + encodeURIComponent((paramStr))\n" + "      }\n" +
                "      return url\n" + "    }\n" + "\n" + "    function requestHybrid (params) {\n" +
                "      var ts = (new Date().getTime())\n" + "      var method = 'hybrid_' + ts\n" +
                "      var tempFn\n" + "      if (params.callback) {\n" + "        tempFn = params.callback\n" +
                "        params.callback = method\n" + "        window[method] = function (data) {\n" +
                "          tempFn(data)\n" + "          delete window[method]\n" + "        }\n" +
                "      }\n" + "      bridgePostMsg(_getHybridUrl(params))\n" + "    }\n" + "\n" +
                "    window['get_album_data'] = function (data) {\n" + "\n" + "    }\n" + "\n" +
                "    var compiler = _.template(document.getElementById('t_album').innerHTML)\n" +
                "    var html = compiler(data)\n" + "    var container = document.querySelector('#container')\n" +
                "    container.innerHTML=html\n" + "    ;(function () {\n" +
                "      var commNum = document.querySelector('.comment-box .comment-num')\n" +
                "      var joinGroup = document.querySelector('.join-group')\n" +
                "      if (!!commNum) {\n" + "        commNum.addEventListener('click', function () {\n" +
                "          requestHybrid({\n" + "            path: 'open_comment'\n" + "          })\n" +
                "        })\n" + "      }\n" + "      if (!!joinGroup) {\n" +
                "        joinGroup.addEventListener('click', function () {\n" +
                "          requestHybrid({\n" + "            path: 'open_group'\n" +
                "          })\n" + "        })\n" + "      }\n" +
                "      window.addEventListener('load', loaded)\n" +
                "      var loadMores = document.querySelectorAll('.load-more')\n" +
                "      loadMores.forEach(function (loadMore) {\n" +
                "        loadMore.style.display = 'none'\n" + "        var loadTxt = loadMore.querySelector('.load-txt')\n" +
                "        loadTxt.addEventListener('click', function () {\n" +
                "          loadMore.parentNode.style.height = 'auto'\n" +
                "          loadMore.style.display = 'none'\n" + "          setTimeout(function () {\n" +
                "            requestHybrid({\n" + "              path: 'post_height'\n" + "            })\n" +
                "          }, 10)\n" + "        })\n" + "      })\n" + "      function loaded () {\n" +
                "        var cards = document.querySelectorAll('.j-card')\n" +
                "        var dpr = parseInt(document.querySelector('html').getAttribute('data-dpr'))\n" +
                "        cards.forEach(function (card) {\n" + "          if (card.offsetHeight / dpr > 620 ) {\n" +
                "            card.style.height = 620 * dpr + 'px'\n" + "            card.querySelector('.load-more').style.display = 'block'\n" +
                "          }\n" + "        })\n" + "      }\n" + "    })()\n" + "  </script>\n" + "</body>\n" + "</html>\n";
    }

    class JsInterface {
        @JavascriptInterface
        public String getData() {
            String data = "";
            try {
                // 读取assets目录下的文件需要用到AssetManager对象的Open方法打开文件
                InputStream is = getAssets().open("albumTemplate/data.json");
                // loadData()方法需要的是一个字符串数据所以我们需要把文件转成字符串
                int length = is.available();
                byte[] buffer = new byte[length];
                is.read(buffer);
                is.close();
                data = new String(buffer, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }
}
