package com.daiyan;

import android.webkit.WebView;  
import android.webkit.WebViewClient; 
import android.webkit.WebResourceRequest;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.ReactConstants;

import java.util.Map;
import android.graphics.Bitmap;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import javax.annotation.Nullable;
import android.util.Log;

public class AlibcTradeWebViewManager extends SimpleViewManager<WebView> {
	private final static String REACT_CLASS = "AlibcTradeWebView";

	public static final int COMMAND_GO_BACK = 1;
	public static final int COMMAND_GO_FORWARD = 2;
	public static final int COMMAND_RELOAD = 3;

	private RNAlibcSdkModule mModule;

	private class AlibcWebView extends WebView {
		private ReactContext mContext;
		AlibcWebView(ReactContext context){
			super(context.getCurrentActivity());
			mContext = context;
		}

		public ReactContext getReactContext(){
			return mContext;
		}
	}

	private class AlibcWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView webView, String url) {
			super.onPageFinished(webView, url);
			WritableMap event = Arguments.createMap();
			event.putBoolean("loading", false);
			event.putBoolean("error", false);
			event.putBoolean("canGoBack", webView.canGoBack());
			ReactContext reactContext = ((AlibcWebView)webView).getReactContext();
			reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(webView.getId(), "onStateChange", event);
		}

		@Override
		public void onPageStarted(WebView webView, String url, Bitmap favicon) {
			super.onPageStarted(webView, url, favicon);
			WritableMap event = Arguments.createMap();
			event.putBoolean("loading", true);
			event.putBoolean("error", false);
			event.putBoolean("canGoBack", webView.canGoBack());
			ReactContext reactContext = ((AlibcWebView)webView).getReactContext();
			reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(webView.getId(), "onStateChange", event);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("http://login.m.taobao.com/") || url.startsWith("http://") || url.startsWith("https://") ||
				url.startsWith("file://")) {
				return false;
			} else {
				return true;
			}
		}

		@Override
		public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
			Log.v("ReactNative", REACT_CLASS + " onReceivedError");
			super.onReceivedError(webView, errorCode, description, failingUrl);
			// do nothing
		}
	}

	AlibcTradeWebViewManager(RNAlibcSdkModule module) {
		mModule = module;
	}

	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@Override
	protected WebView createViewInstance(ThemedReactContext themedReactContext) {
		WebView view = new AlibcWebView(themedReactContext);
		view.getSettings().setJavaScriptEnabled(true); 
		return view;
	}

	@Override
	public Map getExportedCustomDirectEventTypeConstants() {
		return MapBuilder.of(
				"onStateChange", MapBuilder.of("registrationName", "onStateChange"));
	}

	@Override
	public @Nullable Map<String, Integer> getCommandsMap() {
		return MapBuilder.of(
			"goBack", COMMAND_GO_BACK,
			"goForward", COMMAND_GO_FORWARD,
			"reload", COMMAND_RELOAD
		);
	}

	@Override
    public void receiveCommand(WebView root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_GO_BACK:
				root.goBack();
				break;
			case COMMAND_GO_FORWARD:
				root.goForward();
				break;
			case COMMAND_RELOAD:
				root.reload();
				break;
			default:
            //do nothing!!!!
        }
    }

	@ReactProp(name = "param")
	public void propSetParam(WebView view, ReadableMap param) {

		mModule.showInWebView(view, new AlibcWebViewClient(), param);
	}
}