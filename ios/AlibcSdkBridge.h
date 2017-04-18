//
//  AlibcSdkBridge.h
//  RNAlibcSdk
//
//  Created by et on 17/4/18.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <Foundation/Foundation.h>
#import <AlibabaAuthSDK/ALBBSDK.h>
#import <AlibcTradeSDK/AlibcTradeSDK.h>

@interface AlibcSdkBridge : NSObject
+ (instancetype)sharedInstance;
- (void)init: (NSString *)pid forceH5:(BOOL)forceH5 callback:(RCTResponseSenderBlock)callback;
- (void)login: (RCTResponseSenderBlock)callback;
- (void)isLogin: (RCTResponseSenderBlock)callback;
- (void)getUser: (RCTResponseSenderBlock)callback;
- (void)logout: (RCTResponseSenderBlock)callback;
- (void)show: (NSDictionary *)param callback: (RCTResponseSenderBlock)callback;
- (void)_show: (id<AlibcTradePage>)page callback: (RCTResponseSenderBlock)callback;
@end
