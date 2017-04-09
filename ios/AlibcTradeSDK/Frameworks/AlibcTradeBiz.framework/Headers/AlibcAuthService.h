/*
 * AlibcAuthService.h 
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
#import "AlibcHintProtocol.h"

#ifndef ALiAuthService_h
#define ALiAuthService_h

@interface AlibcAuthService : NSObject
+ (nonnull instancetype)sharedInstantce;

- (void)installHintService:(nonnull id <AlibcHintProtocol>)hint;

@end

#endif //ALiAuthService_h
