/**
 * Copyright (c) 2011-2019, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.yizhu.record.template.io;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * DateFormats
 */
public class DateFormats {

    private Map<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();

    public SimpleDateFormat getDateFormat(String datePattern) {
        SimpleDateFormat ret = map.get(datePattern);
        if (ret == null) {
            ret = new SimpleDateFormat(datePattern);
            map.put(datePattern, ret);
        }
        return ret;
    }
}






