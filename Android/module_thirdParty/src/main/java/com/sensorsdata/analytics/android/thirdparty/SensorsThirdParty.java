/*
 * Created by chenru on 2023/2/8 下午5:38.
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

package com.sensorsdata.analytics.android.thirdparty;

import com.sensorsdata.analytics.android.sdk.SALog;
import com.sensorsdata.analytics.android.thirdparty.bean.SAThirdPartyName;
import com.sensorsdata.analytics.android.thirdparty.listener.ISAThirdPartyShare;

import java.util.HashMap;
import java.util.Map;

public class SensorsThirdParty {
    public final static String TAG = "SA.ThirdParty";

    private final static Map<String, ISAThirdPartyShare> thirdPartyCacheMaps = new HashMap();


    public static void share(SAThirdPartyName thirdPartyName) {
        share(thirdPartyName, null);
    }

    public static void share(SAThirdPartyName thirdPartyName, Map<String, Object> data) {
        try {
            ISAThirdPartyShare thirdPartyShare;
            if (thirdPartyCacheMaps.containsKey(thirdPartyName.getName())) {
                thirdPartyShare = thirdPartyCacheMaps.get(thirdPartyName.getName());
            } else {
                Class<?> shareClass = Class.forName(thirdPartyName.getClassName());
                thirdPartyShare = (ISAThirdPartyShare) shareClass.newInstance();
                thirdPartyCacheMaps.put(thirdPartyName.getName(), thirdPartyShare);
            }
            thirdPartyShare.share(data);
        } catch (Exception e) {
            SALog.d(TAG, "instantiation ThirdParty Class exception:" + e.getMessage());
        }
    }
}
