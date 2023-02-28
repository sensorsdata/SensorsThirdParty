/*
 * Created by chenru on 2023/2/20 下午4:07.
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

package com.sensorsdata.analytics.android.thirdparty.utils;

import com.sensorsdata.analytics.android.sdk.SALog;
import com.sensorsdata.analytics.android.thirdparty.SensorsThirdParty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SAThirdPartyJSONUtils {

    public static Map<String, String> json2Map(JSONObject json) {
        if (json != null && json.length() > 0) {
            Map<String, String> maps = new HashMap<>();
            Iterator<String> iterator = json.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                maps.put(key, json.optString(key));
            }
            return maps;
        }
        return null;
    }

    /**
     * 以"{"开头的jsonStr转map,map里面有list,list里面还装有map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> jsonStr2Map(String jsonStr) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!jsonStr.isEmpty()) {
            try {
                JSONObject json = new JSONObject(jsonStr);
                Iterator i = json.keys();
                while (i.hasNext()) {
                    String key = (String) i.next();
                    String value = json.getString(key);
                    if (value.indexOf("{") == 0) {
                        map.put(key.trim(), jsonStr2Map(value));
                    } else if (value.indexOf("[") == 0) {
                        map.put(key.trim(), jsonStr2List(value));
                    } else {
                        map.put(key.trim(), value.trim());
                    }
                }
            } catch (Exception e) {
                SALog.d(SensorsThirdParty.TAG, e.getMessage());
            }
        }
        return map;
    }

    /**
     * 以"["开头的String转list,list里装有map
     */
    public static List<Map<String, Object>> jsonStr2List(String jsonStr) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray ja = new JSONArray(jsonStr);
        for (int j = 0; j < ja.length(); j++) {
            String jm = ja.getString(j);
            if (jm.indexOf("{") == 0) {
                Map<String, Object> m = jsonStr2Map(jm);
                list.add(m);
            }
        }
        return list;
    }
}
