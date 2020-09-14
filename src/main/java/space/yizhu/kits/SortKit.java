package space.yizhu.kits;/* Created by xiuxi on 2018/11/16.*/

import java.util.HashMap;
import java.util.Map;

public class SortKit {
    public static Map<String, Integer> sortMap = new HashMap<>();
    private static int index = 99;


    static Integer getSort(String key) {
        if (sortMap.get(key.toLowerCase()) != null)
            return sortMap.get(key.toLowerCase());
        else
            return index++;
    }

}
