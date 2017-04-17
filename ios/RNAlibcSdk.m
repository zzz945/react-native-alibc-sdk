
#import "RNAlibcSdk.h"

#import <AlibcTradeSDK/AlibcTradeSDK.h>
#import <AlibabaAuthSDK/ALBBSDK.h>
#import <React/RCTLog.h>

#define NOT_LOGIN (@"not login")

@implementation RNAlibcSdk
{
    AlibcTradeTaokeParams *taokeParams;
    AlibcTradeShowParams *showParams;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init: (NSString *)pid forceH5:(BOOL)forceH5 callback:(RCTResponseSenderBlock)callback)
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

RCT_EXPORT_METHOD(login: (RCTResponseSenderBlock)callback)
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

RCT_EXPORT_METHOD(isLogin: (RCTResponseSenderBlock)callback)
{
    bool isLogin = [[ALBBSession sharedInstance] isLogin];
    callback(@[[NSNull null], [NSNumber numberWithBool: isLogin]]);
}

RCT_EXPORT_METHOD(getUser: (RCTResponseSenderBlock)callback)
{
    if([[ALBBSession sharedInstance] isLogin]){
        ALBBUser *s = [[ALBBSession sharedInstance] getUser];
        NSDictionary *ret = @{@"nick": s.nick, @"avatarUrl":s.avatarUrl, @"openId":s.openId, @"openSid":s.openSid};
        callback(@[[NSNull null], ret]);
    } else {
        callback(@[NOT_LOGIN]);
    }
}

RCT_EXPORT_METHOD(logout: (RCTResponseSenderBlock)callback)
{
    [[ALBBSDK sharedInstance] logout];
    callback(@[[NSNull null]]);
}

RCT_EXPORT_METHOD(show: (NSDictionary *)param callback: (RCTResponseSenderBlock)callback){
    id<AlibcTradeService> service = [AlibcTradeSDK sharedInstance].tradeService;
    NSString *type = param[@"type"];
    if ([type isEqualToString:@"detail"]) {
        id<AlibcTradePage> page = [AlibcTradePageFactory itemDetailPage:(NSString *)param[@"payload"]];

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
            NSDictionary *ret = @{@"code": @(error.code), @"msg":error.description};
            callback(@[ret]);
        }];
    } else if ([type isEqualToString:@"url"]) {
        RCTLog(@"not implement");
    } else if ([type isEqualToString:@"shop"]) {
        RCTLog(@"not implement");
    } else if ([type isEqualToString:@"orders"]) {
        RCTLog(@"not implement");
    } else if ([type isEqualToString:@"addCard"]) {
        RCTLog(@"not implement");
    } else if ([type isEqualToString:@"mycard"]) {
        RCTLog(@"not implement");
    } else {
        RCTLog(@"not implement");
    }
}

@end
  