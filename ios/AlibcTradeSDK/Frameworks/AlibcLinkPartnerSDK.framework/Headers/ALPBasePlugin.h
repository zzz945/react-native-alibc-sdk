//
//  ALPBasePlugin.h
//  ALPLinkPartnerSDK
//
//  Created by czp on 16/9/26.
//  Copyright © 2016年 czp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ALPBasePluginParam.h"

@protocol ALPBasePluginProtocol <NSObject>

@required
/**
 *  定义对应的匹配规则，与跳入或跳出的URL进行匹配，判断插件是否执行
 *
 *  @return 匹配规则
 */
- (NSArray<NSString*> *)pluginRules;


/**
 *  插件通用协议
 *
 *  @param param 通用参数
 *  @param block 插件处理完回调
 */
- (void)pluginService:(ALPBasePluginParam *)param;

@end

typedef NS_ENUM(NSInteger, ALPLinkPartnerPrior) {
    ALPLinkPartnerPriorLow     = -2,
    ALPLinkPartnerPriorDefault = 0,
    ALPLinkPartnerPriorHigh    = 2,
};

typedef NS_ENUM(NSInteger, ALPPluginEvent) {
    //触发FC中页面分发插件
    ALPPluginNav,
    //触发FC中授权登录插件
    ALPPluginAuth
};

//基础插件，即非自定义插件
//根据这个key可以从插件容器取出Auth 插件
FOUNDATION_EXTERN NSString *const kAuthPluginKey;


@interface ALPBasePlugin : NSObject<ALPBasePluginProtocol>

/**
 *  是否是打断型插件(该插件执行，后面的插件不执行)
 */
@property (nonatomic, assign) BOOL isBreak;

/**
 *  插件调用优先级，自定义插件可设置，非自定义（Auth 插件）插件已设置，无需再设置
 */
@property (nonatomic, assign) ALPLinkPartnerPrior prior;

/**
 *  用于指定触发FC中页面分发还是授权登录时机的对应插件，非自定义（Auth 插件）插件已设置，无需再设置
 */
@property (nonatomic, assign) ALPPluginEvent event;

/**
 *  插件对应的key,自定义插件可设置，非自定义（Auth 插件）插件已设置，无需再设置
 */
@property (nonatomic, copy) NSString *pluginKey;

/**
 *  创建插件对象
 *
 *  @param pluginKey 插件对应的key
 *
 *  @return 插件对象
 */
+ (instancetype)initPluginWitKey:(NSString *)key;

/**
 *  初始化插件，供提供的非自定义（Auth 插件）插件调用，key已经设定
 *
 *  @return 插件
 */
+ (instancetype)initPlugin;

/**
 *  根据APP被唤起的URL，判断是否匹配到插件来执行
 *
 *  @param url APP被唤起的URL
 *
 *  @return 是否匹配
 */
- (BOOL)isMatchInPluginWithURL:(NSURL *)url;

@end
