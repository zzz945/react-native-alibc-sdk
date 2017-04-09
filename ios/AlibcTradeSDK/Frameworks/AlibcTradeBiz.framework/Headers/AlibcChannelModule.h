/*
 * AlibcChannelModule.h 
 *
 * 阿里百川电商
 * 项目名称：阿里巴巴电商 AlibcTradeBiz 
 * 版本号：3.1.1.96
 * 发布时间：2017-03-24
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1229144682(阿里旺旺)
 * Copyright (c) 2016-2019 阿里巴巴-移动事业群-百川. All rights reserved.
 */

#ifndef ALiChannelModule_h
#define ALiChannelModule_h

#import <Foundation/Foundation.h>

//添加umpChannel u_channel isvCode,ybhpss,ttid参数
@interface AlibcChannelModule : NSObject

+ (void)addChannelParam:(NSMutableDictionary *)param;

@end

#endif /* ALiChannelModule_h */
