
#import "RNAlibcSdk.h"

@implementation RNAlibcSdk

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(login: (RCTResponseSenderBlock)callback)
{
    callback(@[[NSNull null]]);
}

@end
  