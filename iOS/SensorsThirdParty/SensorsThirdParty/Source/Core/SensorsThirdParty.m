//
// SensorsThirdParty.m
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

#import "SensorsThirdParty.h"
#import "SAThirdPartyManager.h"

static const NSString *kSDKVersion = @"0.0.5";

@implementation SensorsThirdParty

+ (void)shareData:(NSDictionary *)data toThirdParty:(SAThirdPartyName)thirdParty {
    [[SAThirdPartyManager defaultManager] shareData:data toThirdParty:thirdParty];
}

@end
