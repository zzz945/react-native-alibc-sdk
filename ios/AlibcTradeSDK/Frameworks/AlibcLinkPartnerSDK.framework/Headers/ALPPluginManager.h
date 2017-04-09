//
//  ALPPluginManager.h
//  ALPLinkPartnerSDK
//
//  Created by czp on 16/9/26.
//  Copyright © 2016年 czp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ALPBasePlugin.h"

@interface ALPPluginManager : NSObject


+ (nonnull instancetype)shareInstance;

/**
 *  添加插件，同一类型的插件只能添加一次
 *
 *  @param plugin 插件
 *
 *  @return 添加插件是否成功
 */
- (BOOL)addPlugin:(nonnull ALPBasePlugin *)plugin;

/**
 *  根据插件类型移除插件
 *
 *  @param key 插件类对应的key
 *
 *  @return 是否移除插件成功
 */
- (BOOL)removePluginWithKey:(nonnull NSString *)key;

/**
 *  根据插件类型查找插件
 *
 *  @param key 插件类对应的key
 *
 *  @return 对应插件
 */
- (nullable ALPBasePlugin *)searchPluginWithKey:(nonnull NSString *)key;

/**
 *  删除所有插件
 *
 */
- (void)clearPlugins;

/**
 *  所有插件
 *
 *  @return 所有插件
 */
- (nullable NSArray *)plugins;


@end
