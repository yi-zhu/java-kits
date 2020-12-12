package space.yizhu.kits;/* Created by xiuxi on 2018/11/5.*/

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisKit {

    private static RedisTemplate<String, Object> redisTemplate;

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisKit. redisTemplate = redisTemplate;
    }
    //=============================common============================
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return value
     */
    public static boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public static void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                CollectionUtils.arrayToList(key).forEach(item->{
                    redisTemplate.delete(String.valueOf(item));

                } );
            }
        }
    }

    //============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public static Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }

    }
    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean setIfAbsent(String key,Object value) {
        try {
            redisTemplate.opsForValue().setIfAbsent(key, value);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
    * @return value
     */
    public static long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @return value
     */
    public static long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String key,String item){
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hmset(String key, Map<String,Object> map){
        for (Map.Entry<String, Object> st : map.entrySet()) {
            if (null==st.getKey()||null==st.getValue()){
                map.remove(st.getKey());
            }
        }
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }



    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmset(String key, Map<String,Object> map, long time){
        try {
            for (Map.Entry<String, Object> st : map.entrySet()) {
                if (null==st.getKey()||null==st.getValue()){
                    map.remove(st.getKey());
                }
            }
            redisTemplate.opsForHash().putAll(key, map);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hset(String key,String item,Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hset(String key,String item,Object value,long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return 值
     */
    public static double hincr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return 值
     */
    public static double hdecr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key, item,-by);
    }

    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return 值
     */
    public static Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
             SysKit.print(e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String key, Object...values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
             SysKit.print(e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String key,long time,Object...values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if(time>0) expire(key, time);
            return count;
        } catch (Exception e) {
             SysKit.print(e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return 值
     */
    public static long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
             SysKit.print(e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
             SysKit.print(e);
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return 值
     */
    public static List<Object> lGet(String key, long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
             SysKit.print(e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return 值
     */
    public static long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
             SysKit.print(e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index大于等于0时， 0表头，1第二个元素，依次类推；index小于0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 值
     */
    public static Object lGetIndex(String key,long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
             SysKit.print(e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return 结果
     */
    public static boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }



    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return 结果
     */
    public static boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return 结果
     */
    public static boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return 结果
     */
    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return 值
     */
    public static boolean lUpdateIndex(String key, long index,Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
             SysKit.print(e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return  remove 移除的个数
     */
    public static long lRemove(String key,long count,Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
             SysKit.print(e);
            return 0;
        }
    }

    /**
     * 序列化
     */
    public static byte[] ser(Object o)
    {

        ObjectOutput oos=null;
        ByteArrayOutputStream baos=null;
        try {
            //创建一个byte的数组的流
            baos=new ByteArrayOutputStream();
            //创建对象流将对象写入到byte数组里
            oos=new ObjectOutputStream(baos);
            //进行写入操作
            oos.writeObject(o);
            //将byte数组的对象进行转换为byte的数组
            return baos.toByteArray();
        } catch (IOException e) {
             SysKit.print(e);
        }



        return null;


    }
    /**
     * 反序列化
     */
    public static Object unser(byte[]bs)
    {
        ByteArrayInputStream bais=null;
        //创建一个byte的数组的读入流对其byte数组
        bais =new ByteArrayInputStream(bs);
        try {
            //对象的输入流，用于读取对象
            ObjectInputStream objectInputStream=new ObjectInputStream(bais);
            //将byte格式的转化为对象
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {

             SysKit.print(e);
            return null;
        }

    }/**
     * 序列化的集合
     */
    public static byte[] serlist(List<?>list)
    {
        ObjectOutputStream oos= null;
        //
        ByteArrayOutputStream byteArrayOutputStream=null;
        byte[] bytes =null;
        byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            oos=new ObjectOutputStream(byteArrayOutputStream);
            for (Object o : list) {
                oos.writeObject(o);

            }
            bytes =byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
             SysKit.print(e);
        }

        return bytes;
    }
    /**
     * 反序列化的集合
     */
    public  static List<?> unserList(byte[] bytes)
    {
        List<Object>list=new ArrayList<>();
        ByteArrayInputStream bais =null;
        ObjectInputStream ois=null;
        bais=new ByteArrayInputStream(bytes);
        try {
            ois =new ObjectInputStream(bais);
            while (bais.available()>0)
            {
                Object object =ois.readObject();
                if (object==null) {
                    break;

                }
                list.add(object);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
             SysKit.print(e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
             SysKit.print(e);
        }

        return list;
    }

/**
 * @param key 传入key
 * @return T 返回bean
 * */
    public  static  <T> T getBean(String key){
        return (T)RedisKit.unser(Base64Kit.decode(String.valueOf(RedisKit.get(String.valueOf(key)))));
    }

}
