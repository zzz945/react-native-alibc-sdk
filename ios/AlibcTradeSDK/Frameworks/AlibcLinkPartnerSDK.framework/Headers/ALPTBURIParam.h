//
//  ALPTBURIParam.h
//  ALPLinkPartnerSDK
//
//  Created by czp on 16/9/22.
//  Copyright © 2016年 czp. All rights reserved.
//

#import "ALPTBJumpParam.h"

@interface ALPTBURIParam : ALPTBJumpParam

/**
 *  要跳转到的H5页面的url
 */
@property (nonatomic, strong) NSString *uri;

/**
 *  初始化,传入h5URL
 */
- (instancetype)initWithURI:(NSString *)URI;

@end
