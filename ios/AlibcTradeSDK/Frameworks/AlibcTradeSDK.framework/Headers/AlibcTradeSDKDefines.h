/*
 * AlibcTradeSDKDefines.h 
 *
 * 阿里百川电商
 * 项目名称：阿里巴巴电商 AlibcTradeSDK 
 * 版本号：3.1.1.96
 * 发布时间：2017-03-24
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1229144682(阿里旺旺)
 * Copyright (c) 2016-2019 阿里巴巴-移动事业群-百川. All rights reserved.
 */

#ifndef AlibcTradeSDKDefines_h
#define AlibcTradeSDKDefines_h

/*
 * trackParam 参数key说明
 */
#define track_scm @"scm"
#define track_pvid @"pvid"
#define track_isv_code @"isv_code"

typedef void (^AlibcTradeProcessSuccessCallback)(AlibcTradeResult *__nullable result);

typedef void (^AlibcTradeProcessFailedCallback)(NSError *__nullable error);

#endif /* AlibcTradeSDKDefines_h */
