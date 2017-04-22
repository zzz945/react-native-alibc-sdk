
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import <UIKit/UIKit.h>
#import "AlibcWebView.h"

#import <React/RCTLog.h>

@interface AlibcTradeWebViewManager : RCTViewManager<UIWebViewDelegate>

@end

@implementation AlibcTradeWebViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    AlibcWebView *webView = [[AlibcWebView alloc] initWithFrame:CGRectZero];
    webView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    webView.scrollView.scrollEnabled = YES;
    webView.delegate = self;
    return webView;
}

RCT_EXPORT_VIEW_PROPERTY(param, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(onTradeResult, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStateChange, RCTDirectEventBlock)

RCT_EXPORT_METHOD(goBack:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, AlibcWebView *> *viewRegistry) {
        AlibcWebView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[AlibcWebView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting AlibcWebView, got: %@", view);
        } else {
            [view goBack];
        }
    }];
}

RCT_EXPORT_METHOD(goForward:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, AlibcWebView *> *viewRegistry) {
        AlibcWebView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[AlibcWebView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting AlibcWebView, got: %@", view);
        } else {
            [view goForward];
        }
    }];
}

RCT_EXPORT_METHOD(reload:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, AlibcWebView *> *viewRegistry) {
        AlibcWebView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[AlibcWebView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting AlibcWebView, got: %@", view);
        } else {
            [view reload];
        }
    }];
}
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    RCTLog(@"Loading URL :%@",request.URL.absoluteString);
    NSString* url = request.URL.absoluteString;
    if ([url hasPrefix:@"http://"]  ||
            [url hasPrefix:@"https://"] ||
            [url hasPrefix:@"file://"]) {
        return YES;
    } else {
        return FALSE; //to stop loading
    }
}

- (void)webViewDidStartLoad:(AlibcWebView *)webView
{
    webView.onStateChange(@{
                            @"loading": @(true),
                            @"canGoBack": @([webView canGoBack]),
                            });
}

- (void)webViewDidFinishLoad:(AlibcWebView *)webView
{
    webView.onStateChange(@{
                            @"loading": @(false),
                            @"canGoBack": @([webView canGoBack]),
                            @"title": [webView stringByEvaluatingJavaScriptFromString:@"document.title"],
                            });
}

- (void)webView:(AlibcWebView *)webView didFailLoadWithError:(NSError *)error
{
    /*webView.onStateChange(@{
                            @"loading": @(false),
                            @"error": @(true),
                            @"canGoBack": @([webView canGoBack]),
                            });
    RCTLog(@"Failed to load with error :%@",[error debugDescription]);*/
}

@end
