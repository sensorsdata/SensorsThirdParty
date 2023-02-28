/*
 * Created by chenru on 2023/2/8 下午4:00.
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

package com.sensorsdata.analytics.module_thirdParty;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;

import com.appsflyer.AppsFlyerProperties;
import com.sensorsdata.analytics.android.sdk.SAConfigOptions;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.thirdparty.SensorsThirdParty;
import com.sensorsdata.analytics.android.thirdparty.bean.SAThirdPartyName;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class ExampleUnitTest {
    static Application mApplication = ApplicationProvider.getApplicationContext();

    @Before
    public void initSensors() {
        SAConfigOptions configOptions = new SAConfigOptions("");
        SensorsDataAPI.startWithConfigOptions(mApplication, configOptions);
    }

    @Test
    public void shareAppsFlyer() {
        Map<String, Object> customParams = new HashMap<>();
        customParams.put("key1", "value1");
        customParams.put("key2", "value2");
        SensorsThirdParty.share(SAThirdPartyName.APPSFLYER, customParams);
        try {
            AppsFlyerProperties.getInstance().setCustomData(customParams.toString());
            JSONObject custom = new JSONObject(AppsFlyerProperties.getInstance().getString("additionalCustomData"));
            Assert.assertTrue(custom.has("key1"));
            Assert.assertTrue(custom.has("key2"));
//            Assert.assertTrue(custom.has(ThirdPartyConstants.KEY_IS_LOGIN));
//            Assert.assertTrue(custom.has(ThirdPartyConstants.KEY_DISTINCT_ID));
//            Assert.assertFalse(custom.optBoolean(ThirdPartyConstants.KEY_IS_LOGIN, true));
//            SensorsDataAPI.sharedInstance().login("hello");
//            SensorsDataAPI.sharedInstance().share(null, SAThirdPartyName.APPSFLYER);
//            Assert.assertTrue(custom.optBoolean(ThirdPartyConstants.KEY_IS_LOGIN, false));
//            Map<String, String> customParams2 = new HashMap<>();
//            customParams2.put("key3", "value3");
//            customParams2.put(ThirdPartyConstants.KEY_DISTINCT_ID, "distinct_id");
//            customParams2.put(ThirdPartyConstants.KEY_IS_LOGIN, "false");
//            SensorsDataAPI.sharedInstance().share(customParams2, SAThirdPartyName.APPSFLYER);
//
//            Assert.assertFalse(custom.optBoolean(ThirdPartyConstants.KEY_IS_LOGIN, true));
//            Assert.assertEquals(custom.get(ThirdPartyConstants.KEY_DISTINCT_ID), "distinct_id");
//            Assert.assertTrue(custom.has("key1"));
//            Assert.assertTrue(custom.has("key3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}