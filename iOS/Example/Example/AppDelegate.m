//
//  AppDelegate.m
//  Example
//
//  Created by 陈玉国 on 2023/2/16.
//

#import "AppDelegate.h"
#import <SensorsThirdParty/SensorsThirdParty.h>
#import <SensorsAnalyticsSDK/SensorsAnalyticsSDK.h>
#import <AppsFlyerLib/AppsFlyerLib.h>
#import <AppTrackingTransparency/AppTrackingTransparency.h>

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.

    //init Appsflyer SDK, set app key and app id
    AppsFlyerLib.shared.appsFlyerDevKey = @"appsFlyerDevKey";
    AppsFlyerLib.shared.appleAppID = @"appleAppID";
    //init SensorsAnalyticsSDK SDK
    SAConfigOptions *options = [[SAConfigOptions alloc] initWithServerURL:@"serverURL" launchOptions:launchOptions];
    options.enableLog = YES;
    [SensorsAnalyticsSDK startWithConfigOptions:options];
    //share data
    [SensorsThirdParty shareData:nil toThirdParty:SAThirdPartyNameAppsFlyer];
    //start Appsflyer SDK
    [NSNotificationCenter.defaultCenter addObserver:self selector:@selector(didBecomeActive) name:UIApplicationDidBecomeActiveNotification object:nil];
    [AppsFlyerLib.shared waitForATTUserAuthorizationWithTimeoutInterval:10];
    return YES;
}


#pragma mark - UISceneSession lifecycle


- (UISceneConfiguration *)application:(UIApplication *)application configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession options:(UISceneConnectionOptions *)options {
    // Called when a new scene session is being created.
    // Use this method to select a configuration to create the new scene with.
    return [[UISceneConfiguration alloc] initWithName:@"Default Configuration" sessionRole:connectingSceneSession.role];
}


- (void)application:(UIApplication *)application didDiscardSceneSessions:(NSSet<UISceneSession *> *)sceneSessions {
    // Called when the user discards a scene session.
    // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
    // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
}


- (void)didBecomeActive {
    //request for tracking usage
    [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
            //
        [AppsFlyerLib.shared start];
    }];
}

@end
