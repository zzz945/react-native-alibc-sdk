//
//  AlibcWebView.h
//  RNAlibcSdk
//
//  Created by et on 17/4/19.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
#import "AlibcSdkBridge.h"

@interface AlibcWebView : UIWebView
@property (nonatomic, copy) RCTDirectEventBlock onTradeResult;
@property (nonatomic, copy) RCTDirectEventBlock onStateChange;
- (void)setParam:(NSDictionary *)param;
@end
