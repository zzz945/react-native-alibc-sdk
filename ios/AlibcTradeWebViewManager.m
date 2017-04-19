
#import <React/RCTViewManager.h>
#import "AlibcWebView.h"

@interface AlibcTradeWebViewManager : RCTViewManager

@end

@implementation AlibcTradeWebViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    AlibcWebView *webView = [[AlibcWebView alloc] initWithFrame:CGRectZero];
    return webView;
}

RCT_EXPORT_VIEW_PROPERTY(param, NSDictionary)

@end
