/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
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
package space.yizhu.kits;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.record.plugin.activerecord.Record;
import space.yizhu.bean.BaseModel;

import java.util.*;
import java.util.Map.Entry;

public class ModelKit {

    static Gson gson = new Gson();


    public static BaseModel<? extends BaseModel> toModel(Record record, BaseModel<? extends BaseModel> model) {
        String[] attrs = record.getColumnNames();
        for (String entry : attrs) {
            model.set(entry, record.get(entry));
        }
        return model;
    }

    public static List<? extends BaseModel> toModels(List<Record> records, BaseModel<? extends BaseModel> model) {
        List<BaseModel<? extends BaseModel>> models = new ArrayList<>();
        for (Record record : records) {
            models.add(toModel(record, model));
        }

        return models;
    }

    public static Map toJson(List<? extends BaseModel> models, BaseModel<?> model, String removal) {
        String[] keysT = new String[0];
        if (model != null)
            keysT = model.getField();

        List<String> names = new ArrayList<>(); //人看的
        List<String> keysN = new ArrayList<>(); //数据库的
        List<Integer> indexs = new ArrayList<>(); //排序的
        Map<Integer, String> sortMap = new HashMap<>();

        if (removal == null)
            removal = ",";
        removal = removal.toLowerCase();
        if (removal.endsWith(","))
            removal = removal.substring(0, removal.length() - 1);
        if (null == models) {
            Map<String, Object> map = new HashMap<>();
            map.put("datas", "[]");
            map.put("names", "[]");
            map.put("tableName", model.getTableName());
            map.put("keys", keysT == null ? "[]" : gson.toJson(keysT));
            if (keysT == null) {
                return map;
            }
            for (String keyT : keysT) {
                int index = SortKit.getSort(keyT);
                indexs.add(index);
                sortMap.put(index, keyT);
            }
            indexs.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if (o1 > o2)
                        return 1;
                    else return -1;
                }
            });
            for (int i : indexs) {
                keysN.add(sortMap.get(i));
                names.add(DictKit.getChinese(sortMap.get(i)));
            }
            map.put("names", gson.toJson(names));
            map.put("keys", gson.toJson(keysN));
            return map;
        }
        if (models.size() == 0) {

            Map<String, Object> map = new HashMap<>();
            map.put("datas", "[]");
            map.put("keys", keysT == null ? "[]" : gson.toJson(keysT));
            for (String keyT : keysT) {
                if (Arrays.asList(removal.split(",")).contains(keyT.toLowerCase()))
                    continue;
                names.add(DictKit.getChinese(keyT));
                keysN.add(keyT);
            }
            map.put("tableName", model.getTableName());
            map.put("names", gson.toJson(names));
            map.put("keys", gson.toJson(keysN));
            return map;
        }
        List<Map<String, Object>> maps = toMaps(models);
        String jsonArray = gson.toJson(maps);
        List<String> keys = new ArrayList<>();
        Map<String, Object> mapT = maps.get(0);
        for (Object keyT : mapT.keySet()) {
            String keyTS = keyT.toString();
            if (Arrays.asList(removal.split(",")).contains(keyTS.toLowerCase()))
                continue;
            if (!keys.equals(keyT.toString())) {
                int index = SortKit.getSort(keyT.toString());
                indexs.add(index);
                sortMap.put(index, keyT.toString());
            }
        }
        indexs.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2)
                    return 1;
                else return -1;
            }
        });
        for (int i : indexs) {
            String nameT = DictKit.getChinese(sortMap.get(i));
            keys.add(sortMap.get(i));
            names.add(nameT);
        }
        Map<String, String> map = new HashMap<>();
        map.put("datas", jsonArray);
        try {
            map.put("tableName", models.get(0).getTableName());
        } catch (Exception e) {
            SysKit.print(e);
        }
        map.put("keys", gson.toJson(keys));
        map.put("names", gson.toJson(names));
        return map;
    }

    public static Map toJson(BaseModel<?> model, String removal) {
        return toJson(null, model, removal);
    }

    public static Map toJson(BaseModel<?> model) {
        return toJson(null, model, "");
    }


    public static List<Map<String, Object>> toMaps(List<? extends BaseModel> models) {
        if (models == null)
            return null;
        else {
            List<Map<String, Object>> maps = new ArrayList<>();
            for (BaseModel model : models) {

                maps.add(toMap(model));
            }

            return maps;
        }
    }

    public static JsonArray toTree(List<? extends BaseModel> models) {
        if (models == null)
            return null;
        else {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            for (BaseModel model : models) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("id", model.getStr("id"));
                jsonObject.addProperty("name", model.getStr("name"));
                jsonObject.addProperty("code", model.getStr("code"));
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }
    }



    public static JsonArray toTrees(List<? extends BaseModel> listMap) {
        if (listMap == null || listMap.size() == 0)
            return null;

        //获得所有CODE
        List allCode = null;
        allCode = listMap.get(0).getFields();
        for (int i = 0; i < allCode.size(); i++) {
            if (!allCode.get(i).toString().endsWith("_code")) {
                allCode.remove(i);
                i--;
            }
        }
        allCode.remove("equiment_code");

        JsonObject jsonObject = new JsonObject();
        JsonObject childObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonArray childArray = new JsonArray();

        for (int i = 0; i < listMap.size(); i++) {
            BaseModel baseModel = listMap.get(i);
            jsonObject = new JsonObject();
            jsonObject.addProperty("id", baseModel.getStr("id"));
            if (baseModel.get("parent_code") == null || baseModel.get("parent_code").toString().length() < 2) {
                jsonObject.addProperty("name", baseModel.getStr("name"));
                jsonObject.addProperty("code", baseModel.getStr("code"));
                for (Object code : allCode)
                    jsonObject.addProperty(String.valueOf(code), baseModel.getStr(String.valueOf(code)));
                listMap.remove(i);
                i--;
            } else
                continue;

            List<JsonObject> pCodes = new ArrayList<>();
            List<JsonObject> pCodesT = new ArrayList<>();
            pCodes.add(jsonObject);
            boolean isEnd = false;
            while (!isEnd) {
                isEnd = true;
                if (pCodesT.size() > 0) {
                    pCodes.clear();
                    pCodes = (pCodesT);
                    pCodesT = new ArrayList<>();
                }
                for (JsonObject jsonObjectT : pCodes) {
                    childArray = new JsonArray();
                    for (int ii = 0; ii < listMap.size(); ii++) {
                        BaseModel model = listMap.get(ii);
                        if (model.getStr("parent_code").equals(jsonObjectT.get("code").getAsString())) {
                            childObject = new JsonObject();
                            childObject.addProperty("id", model.getStr("id"));
                            childObject.addProperty("name", model.getStr("name"));
                            childObject.addProperty("code", model.getStr("code"));
                            for (Object code : allCode)
                                childObject.addProperty(String.valueOf(code), model.getStr(String.valueOf(code)));
                            childArray.add(childObject);
                            listMap.remove(ii);
                            pCodesT.add(childObject);
                            ii--;
                            isEnd = false;
                        }
                    }
                    jsonObjectT.add("children", childArray);
                }
            }


            jsonArray.add(jsonObject);

        }

        return jsonArray;
    }

    public static Map<String, Object> toMap(Model model) {
        Map<String, Object> map = new HashMap<>();
        Set<Entry<String, Object>> attrs = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrs) {
            try {
                map.put(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                map.put(entry.getKey(), "");
            }
        }
        return map;
    }

    public static BaseModel<?> toModel1(BaseModel<?> model, Map<String, String[]> mapData) {
        model.clear();
        Map<String, Object> map = new HashMap<>();
        Set<String> attrs = mapData.keySet();
        for (String entry : attrs) {
            if (null != mapData.get(entry))
                try {
                    model.set(entry, mapData.get(entry)[0]);
                } catch (Exception e) {
                    model.put(entry, mapData.get(entry)[0]);
                }
        }
        try {
            model.set("update_date", new Date().toLocaleString());
        } catch (Exception e) {
            System.out.println("没有更新时间");
        }

        return model;
    }

    public static BaseModel<?> toModel(BaseModel<?> model, Map<String, Object> mapData) {
        model.clear();
        Set<String> attrs = mapData.keySet();
        List<String> fields = model.getFields();
        for (String entry : attrs) {
            if (null != mapData.get(entry))
                if (fields.contains(entry)) {
                    model.set(entry, mapData.get(entry));
                } else {
                    model.put(entry, mapData.get(entry));
                }
        }
        try {
            model.set("update_date", new Date().toLocaleString());
        } catch (Exception e) {
            System.out.println("没有更新时间");
        }

        return model;
    }

    public static void sort(List<? extends BaseModel> list, String sortKey, boolean isBig2Small) {
        removeDuplicate(list);
        try {
            list.sort(new Comparator<BaseModel>() {
                @Override
                public int compare(BaseModel o1, BaseModel o2) {
                    try {
                        Date dt1 = o1.getDate(sortKey);
                        Date dt2 = o2.getDate(sortKey);
                        if (dt1.getTime() < dt2.getTime()) {
                            if (isBig2Small) return 1;
                            else return -1;

                        } else if (dt1.getTime() > dt2.getTime()) {
                            if (isBig2Small) return -1;
                            else return 1;
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
        } catch (Exception e) {
             SysKit.print(e);
        }
    }

    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
