/*
 * AlibcAlipayBridge.h 
 *
 * 阿里百川电商
 * 项目名称：阿里巴巴电商 AlibcTradeBiz 
 * 版本号：3.1.1.96
 * 发布时间：2017-03-24
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1229144682(阿里旺旺)
 * Copyright (c) 2016-2019 阿里巴巴-移动事业群-百川. All rights reserved.
 */

#import <Foundation/Foundation.h>
#import "UIKit/UIKit.h"

#ifndef AlibcAlipayBridge_h
#define AlibcAlipayBridge_h


/** 支付结果 */
@interface AlibcTradePayResult : NSObject
/** 支付成功订单 */
@property(nonatomic, copy, nullable, readonly) NSArray *paySuccessOrders;
/** 支付失败订单 */
@property(nonatomic, copy, nullable, readonly) NSArray *payFailedOrders;

@end

@interface AlibcAlipayBridge : NSObject

@property(nonatomic, copy, nullable) NSArray<NSString *> *orderIds;


+ (nonnull instancetype)sharedInstance;

+ (BOOL)isAlipayAvaleable;

+ (BOOL)isPaymentSuccess:(nullable NSDictionary *)payment;


/** 支付订单 */
- (void)payOrder:(nonnull NSString *)order scheme:(nonnull NSString *)scheme callback:(nullable void (^)(NSDictionary *__nullable result))callback;

/** 支付结果 */
- (void)receiptURL:(nullable NSURL *)url callback:(nullable void (^)(NSDictionary *__nullable result))callback;

/** 解析支付结果 */
- (void)processPayment:(NSDictionary *__nullable)payment callback:(nullable void (^)(AlibcTradePayResult *__nullable result, NSError *__nullable error))callback;


@end

#endif //AlibcAlipayBridge_h
