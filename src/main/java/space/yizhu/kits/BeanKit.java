package space.yizhu.kits;/* Created by xiuxi on 2018/11/16.*/

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import space.yizhu.bean.BaseModel;
import space.yizhu.bean.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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


    /**
     * 把java对象序列化成byte数组
     * @param object
     * @return byte[]
     */
    public static byte[] serialize(Object object) {
        if(object==null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        Output output = null;
        try {
            Kryo kryo = new Kryo();
            baos = new ByteArrayOutputStream();
            output = new Output(baos);
            kryo.writeObject(output, object);
            output.flush();
            return baos.toByteArray();
        }  finally {
            try {
                if(baos!=null) baos.close();
            } catch (IOException e) {
                 SysKit.print(e);
            }
            output.close();
        }
    }

    /**
     * 把byte数组反序列化得到java对象
     * @param bytes
     * @param clazz
     * @return
     */
    public static <T> T unserialize(byte[] bytes, Class<T> clazz) {
        if(bytes==null || bytes.length==0) {
            return null;
        }
        Kryo kryo = new Kryo();
        Input input = new Input(bytes);
        T obj = kryo.readObject(input, clazz);
        input.close();
        return obj;
    }


    public static void main(String[] args) {


    }




}
