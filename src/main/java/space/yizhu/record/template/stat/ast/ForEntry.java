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

package space.yizhu.record.template.stat.ast;

import java.util.Map.Entry;

/**
 * ForEntry 包装 HashMap、LinkedHashMap 等 Map 类型的 Entry 对象
 */
public class ForEntry implements Entry<Object, Object> {

    private Entry<Object, Object> entry;

    public void init(Entry<Object, Object> entry) {
        this.entry = entry;
    }

    public Object getKey() {
        return entry.getKey();
    }

    public Object getValue() {
        return entry.getValue();
    }

    public Object setValue(Object value) {
        return entry.setValue(value);
    }
}



