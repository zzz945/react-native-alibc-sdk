//
//  ALPTBLinkPartnerSDK.h
//  ALPLinkPartnerSDK
//
//  Created by czp on 16/10/10.
//  Copyright © 2016年 czp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "ALPTBShopParam.h"
#import "ALPTBDetailParam.h"
#import "ALPTBAuthParam.h"
#import "ALPTBURIParam.h"
#import "ALPJumpFailedStrategy.h"

#define ALPSDKVersion @"1.1.0.9"

typedef NS_ENUM(NSInteger, ALPOpenType) {
    ALPOpenTypeNative,          //跳转native打开
    ALPOpenTypeH5,              //H5打开
    ALPOpenTypeBrowser          //浏览器打开
};

typedef NS_ENUM(NSInteger, ALPConfigEnv) {
    ALPConfigEnvNone    = -1,//未定义环境
    ALPConfigEnvRelease = 0,//线上环境
    ALPConfigEnvDaily,       //测试环境
    ALPConfigEnvPreRelease,  //预发环境
};

@interface ALPTBLinkPartnerSDK : NSObject

/**
 *  初始化SDK，必须初始化SDK不然后续跳转操作将报错
 *
 *  @param appKey 必传参数
 */
+ (void)initWithAppkey:(nonnull NSString *)appKey;

/**
 *  设置debug模式打开，会有log输出
 *
 *  @param on 是否打开
 */
+ (void)setDebugOn:(BOOL)on;

/**
 *  设置开发环境，默认线上环境
 *
 *  @param env 开发环境
 */
+ (void)setEnv:(ALPConfigEnv)env;

/**
 *  设置打开页面的类型
 *
 *  @param type 类型
 */
+ (void)setOpenType:(ALPOpenType)type;

/**
 *  设置业务追踪id，仅供二方使用，其他接入无需关心
 *
 *  @param ttid
 */
+ (void)setTTID:(nonnull NSString *)ttid;

/**
 *  授权登录时必传
 *
 *  @param authSecret 百川平台appSecret
 */
+ (void)setAuthSecret:(nonnull NSString *)authSecret;

/**
 *  跳转到店铺页
 *
 *  @param param    店铺的配置参数
 *  @param strategy 跳转失败时处理策略，不传，默认h5页面打开
 *
 *  @return 错误信息
 */
+ (nullable ALPError *)jumpShop:(nonnull ALPTBShopParam *)param
                 failedStrategy:(nullable ALPJumpFailedStrategy *)strategy;

/**
 *  跳转到商品详情页
 *
 *  @param param    商品详情的配置参数
 *  @param strategy 跳转失败时处理策略，不传，默认h5页面打开
 *
 *  @return 错误信息
 */
+ (nullable ALPError *)jumpDetail:(nonnull ALPTBDetailParam *)param
                   failedStrategy:(nullable ALPJumpFailedStrategy *)strategy;

/**
 *  根据URI跳转，以webView的形式打开
 *
 *  @param param    URI的配置参数
 *  @param strategy 跳转失败时处理策略，不传，默认h5页面打开
 *
 *  @return 错误信息
 */
+ (nullable ALPError *)jumpURI:(nonnull ALPTBURIParam *)param
                failedStrategy:(nullable ALPJumpFailedStrategy *)strategy;

/**
 *  授权
 *
 *  @param param 授权参数，只支持淘宝授权登录，linkkey为kTaobaoLinkKey
 *               天猫不支持授权登录
 *
 *  @return 错误信息
 */
+ (nullable ALPError *)doAuth:(nonnull ALPTBAuthParam *)param;

/**
 *  跳转失败，是否支持默认打开手淘
 *
 *  @param isSupport 是否支持
 */
+ (void)setSupportJumpFailedOpenTaobao:(BOOL)isSupport;

/**
 *  处理linkPartner返回结果，需要在AppDelegate的[application:(UIApplication)app handleOpenURL:(NSURL*)url]中添加
 *
 *  @param url               跳入打开APP的URL
 *  @param sourceApplication 来源APP sourceApplication
 *
 *  @return  是否处理该url
 */
+ (BOOL)handleOpenURL:(nonnull NSURL *)url sourceApplication:(nullable NSString *)sourceApplication;

/**
 *  检查是否能打开指定APP
 *
 *  @param linkKey 根据对应对应常量字符串来设定（kTaobaoLinkKey,kTmallLinkKey)
 *
 *  @return 是否能打开APP
 */
+ (BOOL)canOpenApp:(nullable NSString*)linkKey;


@end
