package com.daiyan;

import android.content.Context;
import android.webkit.WebView; 
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.page.AlibcAddCartPage;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMiniDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.ResultType;
import com.alibaba.baichuan.android.trade.model.TradeResult;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class AlibcTradeWebView extends WebView {
    private static final String TAG = "AlibcTradeWebView";
    ThemedReactContext mContext;

	public AlibcTradeWebView(ThemedReactContext context) {
		super(context.getCurrentActivity());
        this.getSettings().setJavaScriptEnabled(true);   
        mContext = context;
	}

    public void show(String itemId, AlibcShowParams showParams, AlibcTaokeParams taokeParams, Map<String, String> exParams) {
        AlibcTrade.show(mContext.getCurrentActivity(), 
                        this,
                        null,
                        null,
                        new AlibcDetailPage(itemId),
                        showParams,
                        taokeParams, 
                        exParams,
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
                event.putArray("orders", Arguments.fromArray(tradeResult.payResult.paySuccessOrders));
            }else { 
                event.putString("type", "no type");
            }
            mContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    getId(),
                    "topChange",
                    event);
            }
          @Override
          public void onFailure(int code, String msg) {
            WritableMap event = Arguments.createMap();
            event.putInt("code", code);
            event.putString("msg", msg);
            mContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    getId(),
                    "topChange",
                    event);
          }
      });
    }
}