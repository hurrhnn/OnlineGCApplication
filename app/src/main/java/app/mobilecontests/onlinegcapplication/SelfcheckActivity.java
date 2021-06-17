package app.mobilecontests.onlinegcapplication;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;


public class SelfcheckActivity extends AppCompatActivity {

    private String myUrl = "https://hcs.eduro.go.kr/";// 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)
    private final static String loginuri = "https://djehcs.eduro.go.kr/v2/validatePassword";
    private WebView mWebView;
    String searchSchoolUrl = "https://hcs.eduro.go.kr/v2/searchSchool?lctnScCode=01&schulCrseScCode=4&orgName=%EC%84%A0%EB%A6%B0%EC%9D%B8%ED%84%B0%EB%84%B7%EA%B3%A0%EB%93%B1%ED%95%99%EA%B5%90&loginType=school";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfcheck);

        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허// 용
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl(myUrl);//웹뷰 실행
        mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookie = cookieManager.getCookie(myUrl);
        System.out.println(cookie);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event); 
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }


}
