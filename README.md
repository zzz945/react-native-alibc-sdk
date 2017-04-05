
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
```javascript
import RNAlibcSdk from 'react-native-alibc-sdk';

// TODO: What to do with the module?
RNAlibcSdk;
```
  