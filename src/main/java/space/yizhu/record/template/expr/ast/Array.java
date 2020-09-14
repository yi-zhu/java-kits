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

package space.yizhu.record.template.expr.ast;

import java.util.ArrayList;
import java.util.List;

import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;

/**
 * Array
 *
 * 用法：
 * 1：[1, 2, 3]
 * 2：["a", 1, "b", 2, false, 3.14]
 */
public class Array extends Expr {

    private Expr[] exprList;

    public Array(Expr[] exprList, Location location) {
        if (exprList == null) {
            throw new ParseException("exprList can not be null", location);
        }
        this.exprList = exprList;
    }

    public Object eval(Scope scope) {
        List<Object> array = new ArrayListExt(exprList.length);
        for (Expr expr : exprList) {
            array.add(expr.eval(scope));
        }
        return array;
    }

    /**
     * 支持 array.length 表达式
     */
    @SuppressWarnings("serial")
    public static class ArrayListExt extends ArrayList<Object> {

        public ArrayListExt(int initialCapacity) {
            super(initialCapacity);
        }

        public Integer getLength() {
            return size();
        }
    }
}



