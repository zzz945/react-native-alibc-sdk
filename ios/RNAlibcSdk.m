
#import "RNAlibcSdk.h"

#import <AlibcTradeSDK/AlibcTradeSDK.h>
#import <AlibabaAuthSDK/ALBBSDK.h>
#import <React/RCTLog.h>


@implementation RNAlibcSdk

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init: (RCTResponseSenderBlock)callback)
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
    //ALiTradeTaokeParams *taokeParams = [[ALiTradeTaokeParams alloc] init];
    //taokeParams.pid = @"mm_XXXXX"; //mm_XXXXX为你自己申请的阿里妈妈淘客pid
    //[[AlibcTradeSDK sharedInstance] setTaokeParams:taokeParams];
     
    //设置全局的app标识，在电商模块里等同于isv_code
    //没有申请过isv_code的接入方,默认不需要调用该函数
    //[[AlibcTradeSDK sharedInstance] setISVCode:@"your_isv_code"];
     
    // 设置全局配置，是否强制使用h5
    [[AlibcTradeSDK sharedInstance] setIsForceH5:NO];
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

RCT_EXPORT_METHOD(logout: (RCTResponseSenderBlock)callback)
{
    [[ALBBSDK sharedInstance] logout];
    callback(@[[NSNull null]]);
}

@end
  