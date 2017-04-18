
#import "RNAlibcSdk.h"
#import "AlibcSdkBridge.h"

#import <AlibcTradeSDK/AlibcTradeSDK.h>
#import <AlibabaAuthSDK/ALBBSDK.h>
#import <React/RCTLog.h>


#define NOT_LOGIN (@"not login")

@implementation RNAlibcSdk

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init: (NSString *)pid forceH5:(BOOL)forceH5 callback:(RCTResponseSenderBlock)callback)
{
    [[AlibcSdkBridge sharedInstance] init:pid forceH5:forceH5 callback:callback];
}

RCT_EXPORT_METHOD(login: (RCTResponseSenderBlock)callback)
{
    [[AlibcSdkBridge sharedInstance] login:callback];
}
RCT_EXPORT_METHOD(isLogin: (RCTResponseSenderBlock)callback)
{
    [[AlibcSdkBridge sharedInstance] isLogin:callback];
}

RCT_EXPORT_METHOD(getUser: (RCTResponseSenderBlock)callback)
{
    [[AlibcSdkBridge sharedInstance] getUser:callback];
}

RCT_EXPORT_METHOD(logout: (RCTResponseSenderBlock)callback)
{
    [[AlibcSdkBridge sharedInstance] logout:callback];
}

RCT_EXPORT_METHOD(show: (NSDictionary *)param callback: (RCTResponseSenderBlock)callback){
    [[AlibcSdkBridge sharedInstance] show:param callback:callback];
}


@end
  
