//
//  AlibcSdkBridge.m
//  RNAlibcSdk
//
//  Created by et on 17/4/18.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "AlibcSdkBridge.h"
#import "AlibcWebView.h"
#import <React/RCTLog.h>

#define NOT_LOGIN (@"not login")

@implementation AlibcSdkBridge {
    AlibcTradeTaokeParams *taokeParams;
    AlibcTradeShowParams *showParams;
}

+ (instancetype) sharedInstance
{
    static AlibcSdkBridge *instance = nil;
    if (!instance) {
        instance = [[AlibcSdkBridge alloc] init];
    }
    return instance;
}

- (void)init: (NSString *)pid forceH5:(BOOL)forceH5 callback:(RCTResponseSenderBlock)callback
{
    // 百川平台基础SDK初始化，加载并初始化各个业务能力插件
    [[AlibcTradeSDK sharedInstance] asyncInitWithSuccess:^{
        callback(@[[NSNull null]]);
    } failure:^(NSError *error) {
        NSDictionary *ret = @{@"code": @(error.code), @"msg":error.description};
        callback(@[ret]);
    }];
    
    // 初始化AlibabaAuthSDK
    [[ALBBSDK sharedInstance] ALBBSDKInit];
    
    // 开发阶段打开日志开关，方便排查错误信息
    //默认调试模式打开日志,release关闭,可以不调用下面的函数
    [[AlibcTradeSDK sharedInstance] setDebugLogOpen:YES];
    
    // 配置全局的淘客参数
    //如果没有阿里妈妈的淘客账号,setTaokeParams函数需要调用
    taokeParams = [[AlibcTradeTaokeParams alloc] init];
    taokeParams.pid = pid;
    [[AlibcTradeSDK sharedInstance] setTaokeParams:taokeParams];
    
    showParams = [[AlibcTradeShowParams alloc] init];
    showParams.openType = AlibcOpenTypeAuto;
    
    //设置全局的app标识，在电商模块里等同于isv_code
    //没有申请过isv_code的接入方,默认不需要调用该函数
    //[[AlibcTradeSDK sharedInstance] setISVCode:@"your_isv_code"];
    
    // 设置全局配置，是否强制使用h5
    [[AlibcTradeSDK sharedInstance] setIsForceH5:forceH5];
}

- (void)login: (RCTResponseSenderBlock)callback
{
    [[ALBBSDK sharedInstance] auth:[UIApplication sharedApplication].delegate.window.rootViewController
                   successCallback:^(ALBBSession *session) {
                       ALBBUser *s = [session getUser];
                       NSDictionary *ret = @{@"nick": s.nick, @"avatarUrl":s.avatarUrl, @"openId":s.openId, @"openSid":s.openSid};
                       callback(@[[NSNull null], ret]);
                   }
                   failureCallback:^(ALBBSession *session, NSError *error) {
                       NSDictionary *ret = @{@"code": @(error.code), @"msg":error.description};
                       callback(@[ret]);
                   }
     ];
}

- (void)isLogin: (RCTResponseSenderBlock)callback
{
    bool isLogin = [[ALBBSession sharedInstance] isLogin];
    callback(@[[NSNull null], [NSNumber numberWithBool: isLogin]]);
}

- (void)getUser: (RCTResponseSenderBlock)callback
{
    if([[ALBBSession sharedInstance] isLogin]){
        ALBBUser *s = [[ALBBSession sharedInstance] getUser];
        NSDictionary *ret = @{@"nick": s.nick, @"avatarUrl":s.avatarUrl, @"openId":s.openId, @"openSid":s.openSid};
        callback(@[[NSNull null], ret]);
    } else {
        callback(@[NOT_LOGIN]);
    }
}

- (void)logout: (RCTResponseSenderBlock)callback
{
    [[ALBBSDK sharedInstance] logout];
    callback(@[[NSNull null]]);
}

- (void)show: (NSDictionary *)param callback: (RCTResponseSenderBlock)callback
{
    NSString *type = param[@"type"];
    id<AlibcTradePage> page;
    if ([type isEqualToString:@"detail"]) {
        page = [AlibcTradePageFactory itemDetailPage:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"url"]) {
        page = [AlibcTradePageFactory page:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"shop"]) {
        page = [AlibcTradePageFactory shopPage:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"orders"]) {
        NSDictionary *payload = (NSDictionary *)param[@"payload"];
        page = [AlibcTradePageFactory myOrdersPage:[payload[@"orderType"] integerValue] isAllOrder:[payload[@"isAllOrder"] boolValue]];
    } else if ([type isEqualToString:@"addCard"]) {
        page = [AlibcTradePageFactory addCartPage:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"mycard"]) {
        page = [AlibcTradePageFactory myCartsPage];
    } else {
        RCTLog(@"not implement");
        return;
    }
    
    [self _show:page callback:callback];
}

- (void)_show: (id<AlibcTradePage>)page callback: (RCTResponseSenderBlock)callback
{
    id<AlibcTradeService> service = [AlibcTradeSDK sharedInstance].tradeService;
    
    [service
     show:[UIApplication sharedApplication].delegate.window.rootViewController
     page:page
     showParams:showParams
     taoKeParams:taokeParams
     trackParam:nil
     tradeProcessSuccessCallback:^(AlibcTradeResult * _Nullable result) {
         if (result.result == AlibcTradeResultTypeAddCard) {
             NSDictionary *ret = @{@"type": @"card"};
             callback(@[[NSNull null], ret]);
         } else if (result.result == AlibcTradeResultTypePaySuccess) {
             NSDictionary *ret = @{@"type": @"pay", @"orders": result.payResult.paySuccessOrders};
             callback(@[[NSNull null], ret]);
         }
     } tradeProcessFailedCallback:^(NSError * _Nullable error) {
         NSDictionary *ret = @{@"type": @"error", @"code": @(error.code), @"msg":error.description};
         callback(@[ret]);
     }];
}

- (void)showInWebView: (AlibcWebView *)webView param:(NSDictionary *)param
{
    NSString *type = param[@"type"];
    id<AlibcTradePage> page;
    if ([type isEqualToString:@"detail"]) {
        page = [AlibcTradePageFactory itemDetailPage:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"url"]) {
        page = [AlibcTradePageFactory page:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"shop"]) {
        page = [AlibcTradePageFactory shopPage:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"orders"]) {
        NSDictionary *payload = (NSDictionary *)param[@"payload"];
        page = [AlibcTradePageFactory myOrdersPage:[payload[@"orderType"] integerValue] isAllOrder:[payload[@"isAllOrder"] boolValue]];
    } else if ([type isEqualToString:@"addCard"]) {
        page = [AlibcTradePageFactory addCartPage:(NSString *)param[@"payload"]];
    } else if ([type isEqualToString:@"mycard"]) {
        page = [AlibcTradePageFactory myCartsPage];
    } else {
        RCTLog(@"not implement");
        return;
    }
    
    [self _showInWebView:webView page:page];
}

- (void)_showInWebView: (UIWebView *)webView page:(id<AlibcTradePage>)page
{
    id<AlibcTradeService> service = [AlibcTradeSDK sharedInstance].tradeService;
    
    [service
     show:[UIApplication sharedApplication].delegate.window.rootViewController
     webView:webView
     page:page
     showParams:showParams
     taoKeParams:taokeParams
     trackParam:nil
     tradeProcessSuccessCallback:^(AlibcTradeResult * _Nullable result) {
         if (result.result == AlibcTradeResultTypeAddCard) {
             ((AlibcWebView *)webView).onTradeResult(@{
                                     @"type": @"card",
                                     });
         } else if (result.result == AlibcTradeResultTypePaySuccess) {
             ((AlibcWebView *)webView).onTradeResult(@{
                                     @"type": @"pay",
                                     @"orders": result.payResult.paySuccessOrders,
                                     });
         }
     } tradeProcessFailedCallback:^(NSError * _Nullable error) {
         ((AlibcWebView *)webView).onTradeResult(@{
                                 @"type": @"error",
                                 @"code": @(error.code),
                                 @"msg": error.description,
                                 });
     }];
}


@end
