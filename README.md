
# react-native-alibc-sdk

目前项目已经实现的功能有：淘宝登录授权、设置淘客参数、打开宝贝详情页， 其它功能正在积极开发中。

## Getting started

`$ npm install react-native-alibc-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-alibc-sdk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-alibc-sdk` and add `RNAlibcSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNAlibcSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.daiyan.RNAlibcSdkPackage;` to the imports at the top of the file
  - Add `new RNAlibcSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-alibc-sdk'
  	project(':react-native-alibc-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-alibc-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-alibc-sdk')
  	```


## Usage

Demo：https://github.com/zzz945/RNAlibcSdkDemo
<br>
如果已安装和登录手淘app， 会唤起app授权，无需输入用户名密码。

![demo_login](https://cloud.githubusercontent.com/assets/21496977/24909124/33949adc-1ef5-11e7-9a2f-a467a4d920d4.gif)

### 环境配置

进入阿里百川开发者控制台 -> 创建应用 -> 在我的产品后台开通百川电商SDK -> 在API申请开通初级电商能力和无线开放百川淘宝客。

#### Ios （参考 http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.VWjqPl&treeId=129&articleId=105648&docType=1 以及Demo工程配置）

1. 配置URL Types为tbopen{AppKey}， 比如tbopen123456。
2. 在info.plist中,增加LSApplicationQueriesSchemes字段,并添加tbopen,tmall。
3. 配置ATS, 允许HTTP请求。
4. 上传BundleID， 获取安全图片放到工程目录底下， 并将安全图片加入工程(Build phases -> 
Copy Bundle Resources）。
5. 将node_modules/react-native-alibc-sdk/ios/AlibcTradeSDK/Frameworks和Reaources全部加入工程。参考Demo的工程配置， 添加其它依赖库。
6. 参考Demo的工程配置， 配置Framework Search Paths和Header Search Paths。
7. Other Linker flags中添加-lc++和-lstdc++。
8. 关闭bitcode（build settings -> build options）
9. 参考DEMO添加下面代码到AppDelegate.m, 让sdk处理应用跳转结果。
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

#### Android （参考 http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.Qn05oE&treeId=129&articleId=105647&docType=1 以及Demo工程配置）
1. 上传用于调试的app-debug.apk（发布时再上传签名的apk）， 获取安全图片放在（res/drawable/yw_1222.jpg）。
2. AndroidManifest.xml:
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
3. build.gradle:
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
### API文档（TODO）

	参考Demo。
