
package com.daiyan;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Arguments;

import android.app.Activity;
import android.content.Intent;

import android.support.annotation.Nullable;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcAddCartPage;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMiniDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.ResultType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.taobao.applink.util.TBAppLinkUtil;

import java.util.HashMap;
import java.util.Map;

import android.webkit.WebView; 
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.List;
import android.util.Log;

public class RNAlibcSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static final String TAG = "RNAlibcSdkModule";

  private final static String NOT_LOGIN = "not login";
  private final static String INVALID_TRADE_RESULT = "invalid trade result";
  private final static String INVALID_PARAM = "invalid";

  private Map<String, String> exParams;//yhhpass参数
  private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
  private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      CallbackContext.onActivityResult(requestCode, resultCode, intent);
    }
  };

  static private RNAlibcSdkModule mRNAlibcSdkModule = null;
  static public RNAlibcSdkModule sharedInstance(ReactApplicationContext context) {
    if (mRNAlibcSdkModule == null)
      return new RNAlibcSdkModule(context);
    else
      return mRNAlibcSdkModule;
  }

  public RNAlibcSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);

    alibcShowParams = new AlibcShowParams(OpenType.Auto, false);
    exParams = new HashMap<>();
    exParams.put(AlibcConstants.ISV_CODE, "rnappisvcode");
  }

  @Override
  public String getName() {
    return "RNAlibcSdk";
  }

  /**
  * 初始化
  */
  @ReactMethod
  public void init(final String pid, final Boolean forceH5, final Callback callback) {
      this.alibcTaokeParams = new AlibcTaokeParams(pid, "", "");
      AlibcTradeSDK.asyncInit(reactContext, new AlibcTradeInitCallback() {
        @Override
        public void onSuccess() {
            AlibcTradeSDK.setForceH5(forceH5);
            callback.invoke(null, "init success");
        }

        @Override
        public void onFailure(int code, String msg) {
            WritableMap map = Arguments.createMap();
            map.putInt("code", code);
            map.putString("msg", msg);
            callback.invoke(map);
        }
    }); 
  }
            
  /**
  * 登录
  */
  @ReactMethod
  public void login(final Callback callback) {
      AlibcLogin alibcLogin = AlibcLogin.getInstance();

      alibcLogin.showLogin(getCurrentActivity(), new AlibcLoginCallback() {
          @Override
          public void onSuccess() {
            Session session = AlibcLogin.getInstance().getSession();
            WritableMap map = Arguments.createMap();
            map.putString("nick", session.nick);
            map.putString("avatarUrl", session.avatarUrl);
            map.putString("openId", session.openId);
            map.putString("openSid", session.openSid);
            callback.invoke(null, map);
          }
          @Override
          public void onFailure(int code, String msg) {
            WritableMap map = Arguments.createMap();
            map.putInt("code", code);
            map.putString("msg", msg);
            callback.invoke(map);
          }
      });
  }

  @ReactMethod
  public void isLogin(final Callback callback) {
      callback.invoke(null, AlibcLogin.getInstance().isLogin());
  }

  @ReactMethod
  public void getUser(final Callback callback) {
      if (AlibcLogin.getInstance().isLogin()) {
        Session session = AlibcLogin.getInstance().getSession();
        WritableMap map = Arguments.createMap();
        map.putString("nick", session.nick);
        map.putString("avatarUrl", session.avatarUrl);
        map.putString("openId", session.openId);
        map.putString("openSid", session.openSid);
        callback.invoke(null, map);
      } else {
        callback.invoke(NOT_LOGIN);
      }
        
  }

  /**
  * 退出登录
  */
  @ReactMethod
  public void logout(final Callback callback) {
      AlibcLogin alibcLogin = AlibcLogin.getInstance();

      alibcLogin.logout(getCurrentActivity(), new LogoutCallback() {
          @Override
          public void onSuccess() {
            callback.invoke(null, "logout success");
          }

          @Override
          public void onFailure(int code, String msg) {
            WritableMap map = Arguments.createMap();
            map.putInt("code", code);
            map.putString("msg", msg);
            callback.invoke(msg);
          }
      });
  }

  @ReactMethod
  public void show(final ReadableMap param, final Callback callback) {
    String type = param.getString("type");
    switch(type){
      case "detail":
        this._show(new AlibcDetailPage(param.getString("payload")), callback);
        break;
      case "url":
        this._show(new AlibcPage(param.getString("payload")), callback);
        break;
      case "shop":
        this._show(new AlibcShopPage(param.getString("payload")), callback);
        break;
      case "orders":
        ReadableMap payload = param.getMap("payload");
        this._show(new AlibcMyOrdersPage(payload.getInt("orderType"), payload.getBoolean("isAllOrder")), callback);
        break;
      case "addCard":
        this._show(new AlibcAddCartPage(param.getString("payload")), callback);
        break;
      case "mycard":
        this._show(new AlibcMyCartsPage(), callback);
        break;
      default: 
        callback.invoke(INVALID_PARAM);
        break;
    }
  }

  public void showInWebView(final WebView webview, WebViewClient webViewClient, final ReadableMap param) {
    String type = param.getString("type");
    switch(type){
      case "detail":
        this._showInWebView(webview, webViewClient, new AlibcDetailPage(param.getString("payload")));
        break;
      case "url":
        this._showInWebView(webview, webViewClient, new AlibcPage(param.getString("payload")));
        break;
      case "shop":
        this._showInWebView(webview, webViewClient, new AlibcShopPage(param.getString("payload")));
        break;
      case "orders":
        ReadableMap payload = param.getMap("payload");
        this._showInWebView(webview, webViewClient, new AlibcMyOrdersPage(payload.getInt("orderType"), payload.getBoolean("isAllOrder")));
        break;
      case "addCard":
        this._showInWebView(webview, webViewClient, new AlibcAddCartPage(param.getString("payload")));
        break;
      case "mycard":
        this._showInWebView(webview, webViewClient, new AlibcMyCartsPage());
        break;
      default: 
        WritableMap event = Arguments.createMap();
        event.putString("type", INVALID_PARAM);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                webview.getId(),
                "onTradeResult",
                event);
        break;
    }
  }

  private void _showInWebView(final WebView webview, WebViewClient webViewClient, final AlibcBasePage page) {
    AlibcTrade.show(getCurrentActivity(), 
                        webview,
                        webViewClient,
                        null,
                        page,
                        this.alibcShowParams,
                        this.alibcTaokeParams, 
                        this.exParams,
                        new AlibcTradeCallback() {
          @Override
          public void onTradeSuccess(TradeResult tradeResult) {
            Log.v("ReactNative", TAG + ":onTradeSuccess");
            WritableMap event = Arguments.createMap();
            //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            if(tradeResult.resultType.equals(ResultType.TYPECART)){
                event.putString("type", "card");
            }else if (tradeResult.resultType.equals(ResultType.TYPEPAY)){
                event.putString("type", "pay");
                event.putString("orders", "" + tradeResult.payResult.paySuccessOrders);
            }else { 
                event.putString("type", INVALID_PARAM);
            }
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    webview.getId(),
                    "onTradeResult",
                    event);
            }
          @Override
          public void onFailure(int code, String msg) {
            WritableMap event = Arguments.createMap();
            event.putString("type", "error");
            event.putInt("code", code);
            event.putString("msg", msg);
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    webview.getId(),
                    "onTradeResult",
                    event);
          }
      });
  }

  private void _show(AlibcBasePage page, final Callback callback) {
    AlibcTrade.show(getCurrentActivity(), 
                        page, 
                        this.alibcShowParams,
                        this.alibcTaokeParams, 
                        this.exParams,
                        new AlibcTradeCallback() {
          @Override
          public void onTradeSuccess(TradeResult tradeResult) {
            Log.v("ReactNative", TAG + ":onTradeSuccess");
            //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            if(tradeResult.resultType.equals(ResultType.TYPECART)){
              //加购成功
              WritableMap map = Arguments.createMap();
              map.putString("type", "card");
              callback.invoke(null, map);
            }else if (tradeResult.resultType.equals(ResultType.TYPEPAY)){
              //支付成功
              WritableMap map = Arguments.createMap();
              map.putString("type", "pay");
              map.putString("orders", "" + tradeResult.payResult.paySuccessOrders);
              callback.invoke(null, map);
            }else {
              callback.invoke(INVALID_TRADE_RESULT);
            }
          }
          @Override
          public void onFailure(int code, String msg) {
            WritableMap map = Arguments.createMap();
            map.putString("type", "error");
            map.putInt("code", code);
            map.putString("msg", msg);
            callback.invoke(msg);
          }
      });
  }
}