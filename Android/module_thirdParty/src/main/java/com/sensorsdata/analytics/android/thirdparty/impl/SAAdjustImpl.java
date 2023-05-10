/*
 * Created by chenru on 2023/3/10 下午17:03.
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

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * adjust 数据转发实现类
 * 注：文件位置不要随便修改，反射实例化
 */
public class SAAdjustImpl implements ISAThirdPartyShare {
    private boolean isSupport = true;

    public SAAdjustImpl() {
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
        SALog.d(TAG, "Adjust start addSessionCallbackParameter");
        try {
            Class<?> mAdjustClazz = Class.forName("com.adjust.sdk.Adjust");
            Method addSessionCallbackParameterMethod = mAdjustClazz.getMethod("addSessionCallbackParameter", String.class, String.class);
            if (data != null && !data.isEmpty()) {
                for (String key : data.keySet()) {
                    Object value = data.get(key);
                    if (value instanceof String) {
                        addSessionCallbackParameterMethod.invoke(mAdjustClazz, key, value);
                    }
                }
            }
            addSessionCallbackParameterMethod.invoke(mAdjustClazz, ThirdPartyConstants.KEY_DISTINCT_ID, SensorsDataAPI.sharedInstance().getDistinctId());
            addSessionCallbackParameterMethod.invoke(mAdjustClazz, ThirdPartyConstants.KEY_IS_LOGIN, Boolean.toString(!TextUtils.isEmpty(SensorsDataAPI.sharedInstance().getLoginId())));
            SALog.d(TAG, "Adjust start addSessionCallbackParameter succeed");
        } catch (NoSuchMethodException| SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            SALog.d(TAG, "Adjust reflection exception:" + e.getMessage());
            isSupport = false;
        } catch (Exception e) {
            SALog.d(TAG, "Adjust start addSessionCallbackParameter exception:" + e.getMessage());
        }
    }
}
