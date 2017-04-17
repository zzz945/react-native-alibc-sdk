package com.daiyan;

import android.webkit.WebView;  

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.bridge.ReactApplicationContext;

import com.facebook.react.bridge.ReadableMap;

import android.util.Log;
public class AlibcTradeWebViewManager extends SimpleViewManager<WebView> {
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
	protected WebView createViewInstance(ThemedReactContext themedReactContext) {
		WebView view = new WebView(themedReactContext.getCurrentActivity());
		view.getSettings().setJavaScriptEnabled(true); 
		return view;
	}

	@ReactProp(name = "param")
	public void propSetParam(WebView view, ReadableMap param) {
		mModule.showInWebView(view, param);
	}
}