package com.daiyan;

import android.webkit.WebView;  

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.bridge.ReactApplicationContext;

import com.facebook.react.bridge.ReadableMap;

import android.util.Log;
public class AlibcTradeWebViewManager extends SimpleViewManager<AlibcTradeWebView> {
	private final static String REACT_CLASS = "AlibcTradeWebView";
	private RNAlibcSdkModule mModule;

	AlibcTradeWebViewManager(RNAlibcSdkModule module) {
		mModule = module;
	}

	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@Override
	protected AlibcTradeWebView createViewInstance(ThemedReactContext themedReactContext) {
		return new AlibcTradeWebView(themedReactContext);
	}

	@ReactProp(name = "itemId")
	public void propSetItemId(AlibcTradeWebView view, String itemId) {
		view.show(itemId,
					mModule.getShowParams(),
					mModule.getTaokeParams(),
					mModule.getExParams());
	}
}