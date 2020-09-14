package space.yizhu.kits;/* Created by xiuxi on 2018/11/16.*/

import space.yizhu.bean.BaseModel;

import java.util.concurrent.ConcurrentHashMap;

public class BeanKit {
    //    public static Map<String, Class<? extends Model<?>>> beanMap = new HashMap<>();
    public static ConcurrentHashMap<String, BaseModel<?>> beanMap = new ConcurrentHashMap<>();


    public static void add(String beanName) {
        if (beanName.length() < 2)
            return;
        if (CharKit.isSpecialString(beanName, "_"))
            return;
        String keyName = new String(beanName.getBytes());
        if (beanName.startsWith("_"))
            beanName = beanName.substring(1);
        if (beanName.startsWith("_"))
            return;
        try {

            BeanKit.beanMap.put(keyName, ClassKit.createBean(keyName));
        } catch (Exception e) {
            SysKit.print(e, "BeanKit-add");
        }
    }


    public static void set(String table_name, BaseModel<?> classT) {

        String keyName = table_name.toLowerCase();
        BeanKit.beanMap.put(keyName, classT);
    }


    public static BaseModel<?> get(String beanName) {
        return beanMap.get(beanName);
    }







}
