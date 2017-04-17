#import <MapKit/MapKit.h>

#import <React/RCTViewManager.h>

@interface AlibcTradeWebViewManager : RCTViewManager
@end

@implementation AlibcTradeWebViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  return [[MKMapView alloc] init];
}

@end
