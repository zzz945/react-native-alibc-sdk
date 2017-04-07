
# react-native-alibc-sdk

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

### 环境配置

进入阿里百川开发者控制台 -> 创建应用 -> 在我的产品后台开通百川电商SDK -> 在API申请开通初级电商能力。

#### Android
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
### 示例代码

Demo：https://github.com/zzz945/RNAlibcSdkDemo

### 淘宝登录授权

1. 引入sdk
	```
		import RNAlibcSdk from 'react-native-alibc-sdk';
	```
2. 初始化sdk(此方法为异步， 确保回调执行后再进行下一步操作)
	```
		AlibcSdk.init(
			() => console.log("init success"),
			(code, msg) => console.log(code + ":" + msg)
    	);
	```
3. 唤起手淘app进行授权登录， 获取用户个人信息。 
	```
		AlibcSdk.login(
			(userInfo) => console.log(userInfo),
			(code, msg) => console.log(code + ":" + msg)
		);
	```
3. 务必调用logout，否则app处在login状态将不会再次唤起手淘app进行授权登录。
	```
		AlibcSdk.logout(
			() => console.log("logout success"),
			(code, msg) => console.log(code + ":" + msg)
		);
	```
  