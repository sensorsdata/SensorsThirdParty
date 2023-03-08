//
// SAThirdPartyManager.m
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

#import "SAThirdPartyManager.h"

#if __has_include("SAAppsFlyerShare.h")
#import "SAAppsFlyerShare.h"
#endif

static NSNotificationName const kTrackLoginNotification = @"SensorsAnalyticsTrackLoginNotification";
static NSNotificationName const kTrackLogoutNotification = @"SensorsAnalyticsTrackLogoutNotification";
static NSNotificationName const kTrackIdentifyNotification = @"SensorsAnalyticsTrackIdentifyNotification";
static NSNotificationName const kTrackResetAnonymousidNotification = @"SensorsAnalyticsTrackResetAnonymousIdNotification";

void sensors_third_party_dispatch_safe_sync(dispatch_queue_t queue,DISPATCH_NOESCAPE dispatch_block_t block) {
    if ((dispatch_queue_get_label(DISPATCH_CURRENT_QUEUE_LABEL)) == dispatch_queue_get_label(queue)) {
        block();
    } else {
        dispatch_sync(queue, block);
    }
}

@interface SAThirdPartyManager ()

@property (nonatomic, strong) NSMutableDictionary<NSString *, SAThirdPartyShare *> *thirdPartyShares;
//avoid mutiple thread access thirdPartyShares
@property (nonatomic, strong) dispatch_queue_t serialQueue;

@end

@implementation SAThirdPartyManager

+ (instancetype)defaultManager {
    static dispatch_once_t onceToken;
    static SAThirdPartyManager *manager = nil;
    dispatch_once(&onceToken, ^{
        manager = [[SAThirdPartyManager alloc] init];
        manager.serialQueue = dispatch_queue_create("cn.SensorsData.ThirdParty", DISPATCH_QUEUE_SERIAL);
        [manager addListeners];
    });
    return manager;
}

- (void)shareData:(NSDictionary *)data toThirdParty:(SAThirdPartyName)thirdParty {
    sensors_third_party_dispatch_safe_sync(self.serialQueue, ^{
        [[self shareWithName:thirdParty] shareData:data];
    });
}

- (SAThirdPartyShare *)shareWithName:(SAThirdPartyName)name {
    NSString *rawNameValue = [self rawNameValueFrom:name];
    SAThirdPartyShare *share = self.thirdPartyShares[rawNameValue];
    if (share) {
        return share;
    }
    switch (name) {
        case SAThirdPartyNameAppsFlyer:
#if __has_include("SAAppsFlyerShare.h")
            share = [[SAAppsFlyerShare alloc] init];
#endif
            break;
        default:
            share = [[SAThirdPartyShare alloc] init];
            break;
    }
    self.thirdPartyShares[rawNameValue] = share;
    return share;
}

- (NSString *)rawNameValueFrom:(SAThirdPartyName)thirdPartyName {
    NSString *rawNameValue = nil;
    switch (thirdPartyName) {
        case SAThirdPartyNameAppsFlyer:
            rawNameValue = @"SAAppsFlyer";
            break;
        default:
            rawNameValue = @"Unknown";
            break;
    }
    return rawNameValue;
}

- (void)addListeners {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateUser) name:kTrackLoginNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateUser) name:kTrackLogoutNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateUser) name:kTrackIdentifyNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateUser) name:kTrackResetAnonymousidNotification object:nil];
}

- (void)updateUser {
    sensors_third_party_dispatch_safe_sync(self.serialQueue, ^{
        for (SAThirdPartyShare *share in self.thirdPartyShares.allValues) {
            [share shareData:nil];
        }
    });
}

- (NSMutableDictionary *)thirdPartyShares {
    if (!_thirdPartyShares) {
        _thirdPartyShares = [NSMutableDictionary dictionary];
    }
    return _thirdPartyShares;
}

@end

@implementation SAThirdPartyShare

- (void)shareData:(NSDictionary *)data {
}

@end
