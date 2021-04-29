package space.yizhu.bean;/* Created by yi on 11/30/2020.*/

/**
 * <p>LogModel class.</p>
 *
 * @author yi
 * @version $Id: $Id
 */
public class LogModel extends BaseModel<LogModel> {
    /** Constant <code>me</code> */
    public static LogModel me = new LogModel().dao();
    public  String code;
    public  int type ;


    /**
     * Getter for property 'params'.
     *
     * @return Value for property 'params'.
     */
    public String getHeads() {
        return get("heads");
    }

    /**
     * Setter for property 'params'.
     *
     * @param params Value to set for property 'params'.
     * @return a {@link space.yizhu.bean.LogModel} object.
     */
    public LogModel setHeads(String params) {
        set("heads", params);
        return this;
    }

    /**
     * Getter for property 'params'.
     *
     * @return Value for property 'params'.
     */
    public int getType() {
        return get("type");
    }

    /**
     * Setter for property 'params'.
     *
     * @param params Value to set for property 'params'.
     * @return a {@link space.yizhu.bean.LogModel} object.
     */
    public LogModel setType(int params) {
        set("type", params);
        return this;
    }


    /**
     * Getter for property 'params'.
     *
     * @return Value for property 'params'.
     */
    public String getParams() {
        return get("params");
    }

    /**
     * Setter for property 'params'.
     *
     * @param params Value to set for property 'params'.
     * @return a {@link space.yizhu.bean.LogModel} object.
     */
    public LogModel setParams(String params) {
        set("params", params);
        return this;
    }

    /**
     * Getter for property 'returned'.
     *
     * @return Value for property 'returned'.
     */
    public String getReturned() {
        return get("returned");
    }

    /**
     * Setter for property 'returned'.
     *
     * @param returned Value to set for property 'returned'.
     * @return a {@link space.yizhu.bean.LogModel} object.
     */
    public LogModel setReturned(String returned) {
        set("returned", returned);
        return this;
    }

    /**
     * Getter for property 'fromAddr'.
     *
     * @return Value for property 'fromAddr'.
     */
    public String getFromAddr() {
        return get("from_addr");
    }

    /**
     * Setter for property 'fromAddr'.
     *
     * @param fromAddr Value to set for property 'fromAddr'.
     * @return a {@link space.yizhu.bean.LogModel} object.
     */
    public LogModel setFromAddr(String fromAddr) {
        set("from_addr", fromAddr);
        return this;
    }

    /**
     * Getter for property 'costTime'.
     *
     * @return Value for property 'costTime'.
     */
    public int getCostTime() {
        return getInt("cost_time");
    }

    /**
     * Setter for property 'costTime'.
     *
     * @param costTime Value to set for property 'costTime'.
     * @return a {@link space.yizhu.bean.LogModel} object.
     */
    public LogModel setCostTime(long costTime) {
        set("cost_time", costTime);
        return this;
    }

}
