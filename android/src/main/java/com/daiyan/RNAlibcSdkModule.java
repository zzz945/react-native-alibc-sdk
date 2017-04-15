
package com.daiyan;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.WritableMap;
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
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.ResultType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.taobao.applink.util.TBAppLinkUtil;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class RNAlibcSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static final String TAG = "RNAlibcSdkModule";

  private final static String NOT_LOGIN = "not login";
  private final static String INVALID_TRADE_RESULT = "invalid trade result";

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

  public AlibcShowParams getShowParams() {
    return this.alibcShowParams;
  }

  public AlibcTaokeParams getTaokeParams() {
    return this.alibcTaokeParams;
  }

  public Map<String, String> getExParams() {
    return this.exParams;
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
  public void show(final String itemId, final Callback callback) {
      AlibcTrade.show(getCurrentActivity(), 
                        new AlibcDetailPage(itemId), 
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
              map.putArray("orders", Arguments.fromArray(tradeResult.payResult.paySuccessOrders));
              callback.invoke(null, map);
            }else {
              WritableMap map = Arguments.createMap();
              callback.invoke(INVALID_TRADE_RESULT);
            }
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
}