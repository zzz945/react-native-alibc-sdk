
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

import android.util.Log;

public class RNAlibcSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static final String TAG = "RNAlibcSdkModule";

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      CallbackContext.onActivityResult(requestCode, resultCode, intent);
    }
  };

  public RNAlibcSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNAlibcSdk";
  }

  /**
  * 初始化
  */
  @ReactMethod
  public void init(final Callback successCallback, final Callback failCallback) {
      AlibcTradeSDK.asyncInit(reactContext, new AlibcTradeInitCallback() {
        @Override
        public void onSuccess() {
            successCallback.invoke();
        }

        @Override
        public void onFailure(int code, String msg) {
            failCallback.invoke(code, msg);
        }
    }); 
  }
            
  /**
  * 登录
  */
  @ReactMethod
  public void login(final Callback successCallback, final Callback failCallback) {
      AlibcLogin alibcLogin = AlibcLogin.getInstance();

      alibcLogin.showLogin(getCurrentActivity(), new AlibcLoginCallback() {
          @Override
          public void onSuccess() {
            Session session = AlibcLogin.getInstance().getSession();
            Log.v(TAG, "获取淘宝用户信息: "+ AlibcLogin.getInstance().getSession());
            WritableMap map = Arguments.createMap();
            map.putString("nick", session.nick);
            map.putString("avatarUrl", session.avatarUrl);
            map.putString("openId", session.openId);
            map.putString("openSid", session.openSid);
            successCallback.invoke(map);
          }
          @Override
          public void onFailure(int code, String msg) {
            Log.v(TAG, "login fail");
            failCallback.invoke(code, msg);
          }
      });
  }


  /**
  * 退出登录
  */
  @ReactMethod
  public void logout(final Callback successCallback, final Callback failCallback) {
      AlibcLogin alibcLogin = AlibcLogin.getInstance();

      alibcLogin.logout(getCurrentActivity(), new LogoutCallback() {
          @Override
          public void onSuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFailure(int code, String msg) {
            failCallback.invoke(msg);
          }
      });
  }
}