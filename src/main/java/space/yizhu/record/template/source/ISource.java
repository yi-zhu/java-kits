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

package space.yizhu.record.template.source;

/**
 * ISource 用于表示模板内容的来源
 */
public interface ISource {

    /**
     * reload template if modified on devMode
     */
    boolean isModified();

    /**
     * cache key used to cache, return null if do not cache the template
     *
     * 注意：如果不希望缓存从该 ISource 解析出来的 Template 对象
     *      让 getCacheKey() 返回 null 值即可
     */
    String getCacheKey();

    /**
     * content of ISource
     */
    StringBuilder getContent();

    /**
     * encoding of content
     */
    String getEncoding();
}


