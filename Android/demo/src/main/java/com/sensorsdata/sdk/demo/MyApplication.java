/*
 * Created by dengshiwei on 2022/06/28.
 * Copyright 2015－2021 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sensorsdata.sdk.demo;

import android.app.Application;
import android.util.Log;

import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;
import com.adjust.sdk.Util;
import com.sensorsdata.analytics.android.sdk.SAConfigOptions;
import com.sensorsdata.analytics.android.sdk.SensorsAnalyticsAutoTrackEventType;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.thirdparty.SensorsThirdParty;
import com.sensorsdata.analytics.android.thirdparty.bean.SAThirdPartyName;

import java.lang.reflect.Method;

public class MyApplication extends Application {
    /**
     * Sensors Analytics 采集数据的地址
     */
    private final static String SA_SERVER_URL = "https://sdkdebugtest.datasink.sensorsdata.cn/sa?project=default&token=cfb8b60e42e0ae9b";

    @Override
    public void onCreate() {
        super.onCreate();
        initSensorsDataAPI();
        SensorsThirdParty.share(SAThirdPartyName.ADJUST, null);
        initAdjust();
    }

    /**
     * 初始化 Sensors Analytics SDK
     */
    private void initSensorsDataAPI() {
        SAConfigOptions configOptions = new SAConfigOptions(SA_SERVER_URL);
        // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
        configOptions.setAutoTrackEventType(SensorsAnalyticsAutoTrackEventType.APP_START |
                SensorsAnalyticsAutoTrackEventType.APP_END |
                SensorsAnalyticsAutoTrackEventType.APP_VIEW_SCREEN |
                SensorsAnalyticsAutoTrackEventType.APP_CLICK);
        // 打开 crash 信息采集
        configOptions.enableTrackAppCrash();
        //传入 SAConfigOptions 对象，初始化神策 SDK
        SensorsDataAPI.startWithConfigOptions(this, configOptions);
    }

    private void initAppFlys() {
//        AppsFlyerLib.getInstance().init("<YOUR_DEV_KEY>", null, this);
//        String additionalCustomData = AppsFlyerProperties.getInstance().getString("additionalCustomData");
//        Map<String, Object> hashMap = new HashMap<String, Object>();
//        AppsFlyerLib.getInstance().setAdditionalData(hashMap);
    }

    private void initAdjust() {
        String appToken = "{YourAppToken}";
        String environment = AdjustConfig.ENVIRONMENT_SANDBOX;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
        config.setLogLevel(LogLevel.VERBOSE);
//        Adjust.initSdk(config);
        Log.d("SA.Adjust", "version = " + Util.getSdkVersion());
        Log.d("SA.Adjust", "version = " + getSDKVersion());
        // 4.37.0 使用一下方式；
//        Adjust.onCreate(config);
//        Adjust.addSessionCallbackParameter("gloabkey","global_value");
    }

    public String getSDKVersion() {
        try {
            // 获取 Utils 类的 Class 对象
            Class<?> utilsClass = Class.forName("com.adjust.sdk.Util");

            // 获取 getSdkVersion 方法
            Method getSdkVersionMethod = utilsClass.getDeclaredMethod("getSdkVersion");

            // 调用方法并获取返回值
            String sdkVersion = (String) getSdkVersionMethod.invoke(null); // 静态方法，第一个参数传 null
            return sdkVersion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
