package space.yizhu.bean;/* Created by yi on 12/1/2020.*/

import space.yizhu.kits.ModelKit;
import space.yizhu.kits.ToolKit;
import space.yizhu.record.plugin.activerecord.Page;

import java.util.List;
import java.util.StringJoiner;

/**
 * <p>RetData class.</p>
 *
 * @author yi
 * @version $Id: $Id
 */
public class RetData {
    private boolean succ;
    private int code = 200;
    private String msg;
    private Object data;
    private long pageNum;
    private long pageSize;
    private long totalPage;
    private long totalRow;

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param data a {@link java.lang.Object} object.
     */
    public RetData(Object data) {
        if (data instanceof Boolean)
            this.succ = (boolean) data;
        else
            this.succ = true;

        if (succ)
            this.msg = "执行成功";
        else {
            this.msg = "执行失败";
            this.code = -100;
        }
    }

    /**
     * <p>Constructor for RetData.</p>
     */
    public RetData() {
        this.succ = true;
        this.msg = "执行成功";

    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param succ a boolean.
     */
    public RetData(boolean succ) {
        this.succ = succ;
        if (succ)
            this.msg = "执行成功";
        else {
            this.msg = "执行失败";
            this.code = -100;
        }
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param succ a boolean.
     * @param msg a {@link java.lang.String} object.
     */
    public RetData(boolean succ, String msg) {
        this.succ = succ;
        this.msg = msg;
        this.data = msg;
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param succ a boolean.
     * @param data a {@link java.lang.Object} object.
     */
    public RetData(boolean succ, Object data) {
        this.succ = succ;
        if (succ)
            this.msg = "执行成功";
        else {
            this.msg = "执行失败";
            this.code = -100;
        }
        this.data = data;
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param code a int.
     * @param msg a {@link java.lang.String} object.
     */
    public RetData(int code, String msg) {
            this.code = code;
        this.msg = msg;
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param code a int.
     * @param msg a {@link java.lang.String} object.
     * @param data a {@link java.lang.Object} object.
     */
    public RetData(int code, String msg, Object data) {
        this(code, msg);
        this.data = data;

    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param succ a boolean.
     * @param msg a {@link java.lang.String} object.
     * @param data a {@link java.lang.Object} object.
     */
    public RetData(boolean succ, String msg, Object data) {
        this(succ, msg);
        this.data = data;
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param code a int.
     * @param msg a {@link java.lang.String} object.
     * @param datas a {@link java.util.List} object.
     */
    public RetData(int code, String msg, List<Object> datas) {
        this(code, msg);
        this.data = datas;
        this.pageNum = 1;
        this.pageSize = datas.size();
        this.totalPage = 1;
        this.totalRow = datas.size();
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param code a int.
     * @param msg a {@link java.lang.String} object.
     * @param page a {@link space.yizhu.record.plugin.activerecord.Page} object.
     */
    public RetData(int code, String msg, Page page) {
        this(code, msg);
        this.pageNum = page.getPageNumber();
        this.pageSize = page.getPageSize();
        this.totalPage = page.getTotalPage();
        this.totalRow = page.getTotalRow();
        this.data = page.getList();
    }
    /**
     * <p>Constructor for RetData.</p>
     *
     * @param succ a boolean.
     * @param msg a {@link java.lang.String} object.
     * @param datas a {@link java.util.List} object.
     */
    public RetData(boolean succ, String msg, List<Object> datas) {
        this(succ, msg);
        this.data = datas;
        this.pageNum = 1;
        this.pageSize = datas.size();
        this.totalPage = 1;
        this.totalRow = datas.size();
    }

    /**
     * <p>Constructor for RetData.</p>
     *
     * @param succ a boolean.
     * @param msg a {@link java.lang.String} object.
     * @param page a {@link space.yizhu.record.plugin.activerecord.Page} object.
     */
    public RetData(boolean succ, String msg, Page page) {
        this(succ, msg);
        this.pageNum = page.getPageNumber();
        this.pageSize = page.getPageSize();
        this.totalPage = page.getTotalPage();
        this.totalRow = page.getTotalRow();
        this.data = page.getList();
    }

    /**
     * <p>setCM.</p>
     *
     * @param code a int.
     * @param mesg a {@link java.lang.String} object.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setCM(int code, String mesg) {
        setCode(code);
        setMsg(mesg);
        return this;
    }
    /**
     * <p>setCMD.</p>
     *
     * @param code a int.
     * @param mesg a {@link java.lang.String} object.
     * @param data a {@link java.lang.Object} object.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setCMD(int code, String mesg,Object data) {
        setCode(code);
        setMsg(mesg);
        setData(data);
        return this;
    }
    /**
     * <p>setCMD.</p>
     *
     * @param code a int.
     * @param mesg a {@link java.lang.String} object.
     * @param page a {@link space.yizhu.record.plugin.activerecord.Page} object.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setCMD(int code, String mesg,Page page) {
        return setCMD(code, mesg, page, false);
    }
    /**
     * <p>setCMD.</p>
     *
     * @param code a int.
     * @param mesg a {@link java.lang.String} object.
     * @param page a {@link space.yizhu.record.plugin.activerecord.Page} object.
     * @param changeToHump a boolean.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setCMD(int code, String mesg,Page page,boolean changeToHump) {
        setCode(code);
        setMsg(mesg);
        setPage(page, changeToHump);
        return this;
    }
    /**
     * <p>setPage.</p>
     *
     * @param page a {@link space.yizhu.record.plugin.activerecord.Page} object.
     * @param changeToHump a boolean.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setPage(Page page,boolean changeToHump) {
        if (null!=page.getList()&&page.getList().size()>0&&page.getList().get(0) instanceof BaseModel){
            setData(ModelKit.toMaps(page.getList(),changeToHump));
        }else {
            setData(page.getList());

        }

        this.pageNum = page.getPageNumber();
        this.pageSize = page.getPageSize();
        this.totalPage = page.getTotalPage();
        this.totalRow = page.getTotalRow();
        return this;
    }


    /**
     * <p>isSucc.</p>
     *
     * @return a boolean.
     */
    public boolean isSucc() {
        return succ;
    }

    /**
     * <p>Setter for the field <code>succ</code>.</p>
     *
     * @param succ a boolean.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setSucc(boolean succ) {
        this.succ = succ;
        return this;
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a int.
     */
    public int getCode() {
        return code;
    }

    /**
     * <p>Setter for the field <code>code</code>.</p>
     *
     * @param code a int.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * <p>Getter for the field <code>msg</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * <p>Setter for the field <code>msg</code>.</p>
     *
     * @param msg a {@link java.lang.String} object.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * <p>Getter for the field <code>data</code>.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object getData() {
        return data;
    }

    /**
     * <p>Setter for the field <code>data</code>.</p>
     *
     * @param data a {@link java.lang.Object} object.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * <p>Getter for the field <code>pageNum</code>.</p>
     *
     * @return a long.
     */
    public long getPageNum() {
        return pageNum;
    }

    /**
     * <p>Setter for the field <code>pageNum</code>.</p>
     *
     * @param pageNum a long.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setPageNum(long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    /**
     * <p>Getter for the field <code>pageSize</code>.</p>
     *
     * @return a long.
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * <p>Setter for the field <code>pageSize</code>.</p>
     *
     * @param pageSize a long.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * <p>Getter for the field <code>totalPage</code>.</p>
     *
     * @return a long.
     */
    public long getTotalPage() {
        return totalPage;
    }

    /**
     * <p>Setter for the field <code>totalPage</code>.</p>
     *
     * @param totalPage a long.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setTotalPage(long totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    /**
     * <p>Getter for the field <code>totalRow</code>.</p>
     *
     * @return a long.
     */
    public long getTotalRow() {
        return totalRow;
    }

    /**
     * <p>Setter for the field <code>totalRow</code>.</p>
     *
     * @param totalRow a long.
     * @return a {@link space.yizhu.bean.RetData} object.
     */
    public RetData setTotalRow(long totalRow) {
        this.totalRow = totalRow;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {

        return new StringJoiner(", ", RetData.class.getSimpleName() + "[", "]")
                .add("succ=" + succ)
                .add("code=" + code)
                .add("mesg='" + msg + "'")
                .add("data=" + ToolKit.toJson(data))
                .add("pageNum=" + pageNum)
                .add("pageSize=" + pageSize)
                .add("totalPage=" + totalPage)
                .add("totalRow=" + totalRow)
                .toString();
    }
}
