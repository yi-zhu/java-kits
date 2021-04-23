package space.yizhu.kits;/* Created by xiuxi on 2018/11/16.*/

import java.util.HashMap;
import java.util.Map;

public class SortKit {
    public static Map<String, Integer> sortMap = new HashMap<String, Integer>(){{
        put("code", 1);
        put("name", 2);
        put("parent_code", 11);
        put("type_code", 11);
        put("is_main", 12);
        put("url", 12);
        put("sort", 13);

        put("heads", 12);
        put("params", 13);
        put("returned", 14);
        put("from_addr", 11);
        put("cost_time", 10);

        put("result", 9);
        put("creator", 20);
        put("mender", 21);
        put("create_time", 22);
        put("modify_time", 23);
        put("is_del", 59);




        put("password", 9);
        put("gender", 11);
        put("birthday", 12);
        put("telephone", 13);
        put("email", 15);
        put("group_code", 16);
        put("organization_code", 17);
        put("competence", 18);
        put("identity", 14);




    }};
    private static int index = 99;


    static Integer getSort(String key) {
        if (sortMap.get(key.toLowerCase()) != null)
            return sortMap.get(key.toLowerCase());
        else
            return index++;
    }

}
