//
//  AlibcWebView.h
//  RNAlibcSdk
//
//  Created by et on 17/4/19.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "AlibcSdkBridge.h"

@interface AlibcWebView : UIWebView
- (void)setParam:(NSDictionary *)param;
@end
