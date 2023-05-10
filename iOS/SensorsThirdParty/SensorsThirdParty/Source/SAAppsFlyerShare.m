//
// SAAppsFlyerShare.m
// SensorsThirdParty SDK
//
// Created by 陈玉国 on 2023/2/13.
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

#import "SAAppsFlyerShare.h"
#import <AppsFlyerLib/AppsFlyerLib.h>

#if __has_include(<SensorsAnalyticsSDK/SensorsAnalyticsSDK.h>)
#import <SensorsAnalyticsSDK/SensorsAnalyticsSDK.h>
#elif __has_include("SensorsAnalyticsSDK.h")
#import "SensorsAnalyticsSDK.h"
#endif

static NSString *const kSAAppsFlyerCustomDataKeyDistinctId = @"sensors_distinct_id";
static NSString *const kSAAppsFlyerCustomDataKeyIsLogin = @"sensors_is_login";

@interface SAAppsFlyerShare ()

@property (nonatomic, copy) NSDictionary *customData;

@end

@implementation SAAppsFlyerShare

- (void)shareData:(NSDictionary *)data {
    if (data && ![data isKindOfClass:[NSDictionary class]]) {
        return;
    }
    NSDictionary *customData = AppsFlyerLib.shared.customData;
    if (customData && ![customData isKindOfClass:[NSDictionary class]]) {
        return;
    }
    NSMutableDictionary *tempCustomData = [NSMutableDictionary dictionary];
    if (customData) {
        [tempCustomData addEntriesFromDictionary:customData];
    }
    if (self.customData) {
        [tempCustomData addEntriesFromDictionary:self.customData];
    }
    if (data) {
        [tempCustomData addEntriesFromDictionary:data];
    }
    tempCustomData[kSAAppsFlyerCustomDataKeyDistinctId] = SensorsAnalyticsSDK.sharedInstance.distinctId;
    tempCustomData[kSAAppsFlyerCustomDataKeyIsLogin] = (SensorsAnalyticsSDK.sharedInstance.loginId ? @YES : @NO);
    self.customData = tempCustomData;
    AppsFlyerLib.shared.customData = tempCustomData;
}

@end
