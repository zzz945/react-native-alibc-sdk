
#import <React/RCTViewManager.h>
#import <WebKit/WebKit.h>

@interface AlibcTradeWebViewManager : RCTViewManager
@end

@implementation AlibcTradeWebViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    WKWebView *webView = [[WKWebView alloc] initWithFrame:CGRectZero];
    NSURL *url = [NSURL URLWithString:@"http://www.baidu.com"];
    NSURLRequest *request =[NSURLRequest requestWithURL:url];
    [webView loadRequest:request];
    return webView;
}

@end
