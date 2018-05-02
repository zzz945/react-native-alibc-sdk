
# react-native-alibc-sdk

基于阿里百川SDK， 封装出React Native接口， 方便在RN应用中集成阿里百川SDK的各种功能。目前由于工作中不涉及这方面和时间问题，已停止维护。

# 功能
1. 淘宝登录授权
2. 淘客参数设置
3. 通过手淘App和H5方式打开宝贝页面、购物车页面、订单页面，添加购物车页面和Url链接， 并获取交易回调信息，实现交易闭环。
4. 通过在react-native内嵌入WebView方式打开宝贝页面、购物车页面、订单页面，添加购物车页面和Url链接， 并获取交易回调信息，实现App内交易闭环。

# 快速开始

1. 进入阿里百川开发者控制台 -> 创建应用 -> 在我的产品后台开通百川电商SDK -> 在API申请开通初级电商能力和无线开放百川淘宝客
2. git clone https://github.com/zzz945/RNAlibcSdkDemo
3. cd RNAlibcSdkDemo; npm install
4. 用xcode打开RNAlibcSdkDemo/ios/下的工程， 配置URL Types为tbopen{AppKey}， 比如tbopen123456。 其中AppKey可以在百川后台查到
5. 在百川后台下载安全图片， 替换ios/yw_1222.jpg和android/app/src/main/res/drawable/yw_1222.jpg
6. 在RNAlibcSdkDemo／app.js中替换自己的pid参数， 当开通阿里妈妈后， pid参数可在百川后台查到
7. react-native run-ios(android)

![login](https://cloud.githubusercontent.com/assets/21496977/25235890/0975d240-2619-11e7-80c4-b18b521f8906.gif)
![show](https://cloud.githubusercontent.com/assets/21496977/25235905/13f00f1a-2619-11e7-83db-20c7a8d2c41a.gif)
![webview](https://cloud.githubusercontent.com/assets/21496977/25235918/1beae8e8-2619-11e7-8297-ab7e36b02faf.gif)

# DIY (适用于在现有RN工程基础上添加百川)

1. 进入阿里百川开发者控制台 -> 创建应用 -> 在我的产品后台开通百川电商SDK -> 在API申请开通初级电商能力和无线开放百川淘宝客。
2. npm i https://github.com/zzz945/react-native-alibc-sdk.git --save
3. react-native link react-native-alibc-sdk

## Ios （参考 http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.VWjqPl&treeId=129&articleId=105648&docType=1 以及Demo工程配置）

4. 配置URL Types为tbopen{AppKey}， 比如tbopen123456。
5. 在info.plist中,增加LSApplicationQueriesSchemes字段,并添加tbopen,tmall。
6. 配置ATS, 允许HTTP请求。
7. 上传BundleID， 获取安全图片放到工程目录底下， 并将安全图片加入工程(Build phases -> 
Copy Bundle Resources）。
8. 将node_modules/react-native-alibc-sdk/ios/AlibcTradeSDK/Frameworks和Reaources全部加入工程。参考Demo的工程配置， 添加其它依赖库。
9. 参考Demo的工程配置， 配置Framework Search Paths和Header Search Paths。
10. Other Linker flags中添加-lc++和-lstdc++。
11. 关闭bitcode（build settings -> build options）
12. 参考DEMO添加下面代码到AppDelegate.m, 让sdk处理应用跳转结果。
	```
	#import <AlibcTradeSDK/AlibcTradeSDK.h>
	...
	- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
		// 如果百川处理过会返回YES
		if ([[AlibcTradeSDK sharedInstance] application:application
								openURL: url
								sourceApplication: sourceApplication
								annotation: annotation]) {
			// 处理其他app跳转到自己的app
			return YES;
		}
		return NO;
	}


	//IOS9.0 系统新的处理openURL 的API
	- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url options:(NSDictionary<NSString *,id> *)options {
		//处理其他app跳转到自己的app，如果百川处理过会返回YES
	if ([[AlibcTradeSDK sharedInstance] application:application
											openURL: url
											options: options]) {
			return YES;
		}
		
		return NO;
	}
	...
	```

## Android （参考 http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.Qn05oE&treeId=129&articleId=105647&docType=1 以及Demo工程配置）

4. 上传用于调试的app-debug.apk（发布时再上传签名的apk）， 获取安全图片放在（res/drawable/yw_1222.jpg）。
5. AndroidManifest.xml:
	```
	<manifest ...
		xmlns:tools="http://schemas.android.com/tools"
			...
		<application
			...
		android:allowBackup="true"
		tools:replace="android:allowBackup">
		...
	</manifest>
	```
6. build.gradle:
	```
	...
	allprojects {
		repositories {
			...
			maven {
				url "http://repo.baichuan-android.taobao.com/content/groups/BaichuanRepositories/"
			}
			...
		}
	}
	...
	```
# API文档（TODO）

	参考Demo。
