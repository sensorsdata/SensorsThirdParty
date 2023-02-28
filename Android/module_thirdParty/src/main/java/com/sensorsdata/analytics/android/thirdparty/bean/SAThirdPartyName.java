/*
 * Created by chenru on 2023/2/9 下午3:37.
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

package com.sensorsdata.analytics.android.thirdparty.bean;

public enum SAThirdPartyName {
    APPSFLYER("AppsFlyer", "com.sensorsdata.analytics.android.thirdparty.impl.SAAppsFlyerImpl");

    private final String mName;
    private final String mClassName;

    SAThirdPartyName(String name, String className) {
        this.mName = name;
        this.mClassName = className;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getName() {
        return mName;
    }
}
