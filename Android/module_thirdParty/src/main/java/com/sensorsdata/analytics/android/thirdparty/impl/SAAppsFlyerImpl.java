/*
 * Created by chenru on 2023/2/20 下午4:03.
 * Copyright 2015－2023 Sensors Data Inc.
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

package com.sensorsdata.analytics.android.thirdparty.impl;

import static com.sensorsdata.analytics.android.thirdparty.SensorsThirdParty.TAG;

import android.text.TextUtils;

import com.sensorsdata.analytics.android.sdk.SALog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.listener.SAFunctionListener;
import com.sensorsdata.analytics.android.thirdparty.ThirdPartyConstants;
import com.sensorsdata.analytics.android.thirdparty.listener.ISAThirdPartyShare;
import com.sensorsdata.analytics.android.thirdparty.utils.SAThirdPartyJSONUtils;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * AppsFlyer 数据转发实现类
 * 注：文件位置不要随便修改，反射实例化
 */
public class SAAppsFlyerImpl implements ISAThirdPartyShare {
    private boolean isSupport = true;

    public SAAppsFlyerImpl() {
        registerListener();
    }

    private void registerListener() {
        SensorsDataAPI.sharedInstance().addFunctionListener(new SAFunctionListener() {
            @Override
            public void call(String function, JSONObject args) {
                switch (function) {
                    case ThirdPartyConstants.FUNCTION_LOGIN:
                    case ThirdPartyConstants.FUNCTION_LOGOUT:
                    case ThirdPartyConstants.FUNCTION_RESET_ANONYMOUS_ID:
                    case ThirdPartyConstants.FUNCTION_IDENTIFY:
                        share(null);
                }
            }
        });
    }

    @Override
    public void share(Map<String, Object> data) {
        //反射失败认为没有集成或版本不兼容，直接 return
        if (!isSupport) {
            return;
        }
        SALog.d(TAG, "Appsflyer start set additionalData");
        try {
            Class<?> mAppsFlyerPropertiesClazz = Class.forName("com.appsflyer.AppsFlyerProperties");
            Method getPropertiesInstanceMethod = mAppsFlyerPropertiesClazz.getMethod("getInstance");
            //反射获取 AppsFlyerProperties 实例
            Object afpObject = getPropertiesInstanceMethod.invoke(null);
            Method getCustom = afpObject.getClass().getMethod("getString", String.class);
            String customStr = (String) getCustom.invoke(afpObject, "additionalCustomData");
            Map<String, Object> customMap;
            if (!TextUtils.isEmpty(customStr)) {
                customMap = SAThirdPartyJSONUtils.jsonStr2Map(customStr);
            } else {
                customMap = new HashMap<>();
            }
            if (data != null && data.size() > 0) {
                customMap.putAll(data);
            }
            customMap.put(ThirdPartyConstants.KEY_DISTINCT_ID, SensorsDataAPI.sharedInstance().getDistinctId());
            customMap.put(ThirdPartyConstants.KEY_IS_LOGIN, !TextUtils.isEmpty(SensorsDataAPI.sharedInstance().getLoginId()));
            Class<?> mAppsFlyerClazz = Class.forName("com.appsflyer.AppsFlyerLib");
            Method getInstanceMethod = mAppsFlyerClazz.getMethod("getInstance");
            //反射获取 AppsFlyerLib 实例
            Object afObject = getInstanceMethod.invoke(null);
            //支持 6.0 以上版本，5.0 版本参数类型为 HashMap
            Method mSetAdditionalDataMethod
                    = mAppsFlyerClazz.getMethod("setAdditionalData", Map.class);
            mSetAdditionalDataMethod.invoke(afObject, customMap);
            SALog.d(TAG, "AppsFlyer set additionalData succeed");
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            SALog.d(TAG, "AppsFlyer set additionalData exception:" + e.getMessage());
            isSupport = false;
        } catch (Exception e) {
            SALog.d(TAG, "AppsFlyer set additionalData exception:" + e.getMessage());
        }
    }
}
