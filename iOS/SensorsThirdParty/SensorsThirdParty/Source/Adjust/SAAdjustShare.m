//
// SAAdjustShare.m
// SensorsThirdParty SDK
//
// Created by 陈玉国 on 2023/3/10.
// Copyright © 2015-2023 Sensors Data Co., Ltd. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

#if ! __has_feature(objc_arc)
#error This file must be compiled with ARC. Either turn on ARC for the project or use -fobjc-arc flag on this file.
#endif

#import "SAAdjustShare.h"
#import <Adjust/Adjust.h>
#import "SensorsThirdPartyConstant.h"

#if __has_include(<SensorsAnalyticsSDK/SensorsAnalyticsSDK.h>)
#import <SensorsAnalyticsSDK/SensorsAnalyticsSDK.h>
#elif __has_include("SensorsAnalyticsSDK.h")
#import "SensorsAnalyticsSDK.h"
#endif

@implementation SAAdjustShare

- (void)shareData:(NSDictionary *)data {
    if (data && ![data isKindOfClass:[NSDictionary class]]) {
        return;
    }
    [data enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        if ([key isKindOfClass:[NSString class]] && [obj isKindOfClass:[NSString class]]) {
            [Adjust addSessionCallbackParameter:key value:obj];
        }
    }];
    [Adjust addSessionCallbackParameter:kSAThirdPartyShareDataKeyDistinctId value:SensorsAnalyticsSDK.sharedInstance.distinctId];
    [Adjust addSessionCallbackParameter:kSAThirdPartyShareDataKeyIsLogin value:(SensorsAnalyticsSDK.sharedInstance.loginId ? @"true" : @"false")];
}

@end
