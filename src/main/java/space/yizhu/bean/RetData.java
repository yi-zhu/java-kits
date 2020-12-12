package space.yizhu.bean;/* Created by yi on 12/1/2020.*/

import space.yizhu.kits.ModelKit;
import space.yizhu.kits.ToolKit;
import space.yizhu.record.plugin.activerecord.Page;

import java.util.List;
import java.util.StringJoiner;

public class RetData {
    private boolean succ;
    private int code = 200;
    private String msg;
    private Object data;
    private long pageNum;
    private long pageSize;
    private long totalPage;
    private long totalRow;

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

    public RetData() {
        this.succ = true;
        this.msg = "执行成功";

    }

    public RetData(boolean succ) {
        this.succ = succ;
        if (succ)
            this.msg = "执行成功";
        else {
            this.msg = "执行失败";
            this.code = -100;
        }
    }

    public RetData(boolean succ, String msg) {
        this.succ = succ;
        this.msg = msg;
        this.data = msg;
    }

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

    public RetData(int code, String msg) {
            this.code = code;
        this.msg = msg;
    }

    public RetData(int code, String msg, Object data) {
        this(code, msg);
        this.data = data;

    }

    public RetData(boolean succ, String msg, Object data) {
        this(succ, msg);
        this.data = data;
    }

    public RetData(int code, String msg, List<Object> datas) {
        this(code, msg);
        this.data = datas;
        this.pageNum = 1;
        this.pageSize = datas.size();
        this.totalPage = 1;
        this.totalRow = datas.size();
    }

    public RetData(int code, String msg, Page page) {
        this(code, msg);
        this.pageNum = page.getPageNumber();
        this.pageSize = page.getPageSize();
        this.totalPage = page.getTotalPage();
        this.totalRow = page.getTotalRow();
        this.data = page.getList();
    }
    public RetData(boolean succ, String msg, List<Object> datas) {
        this(succ, msg);
        this.data = datas;
        this.pageNum = 1;
        this.pageSize = datas.size();
        this.totalPage = 1;
        this.totalRow = datas.size();
    }

    public RetData(boolean succ, String msg, Page page) {
        this(succ, msg);
        this.pageNum = page.getPageNumber();
        this.pageSize = page.getPageSize();
        this.totalPage = page.getTotalPage();
        this.totalRow = page.getTotalRow();
        this.data = page.getList();
    }

    public RetData setCM(int code, String mesg) {
        setCode(code);
        setMsg(mesg);
        return this;
    }
    public RetData setCMD(int code, String mesg,Object data) {
        setCode(code);
        setMsg(mesg);
        setData(data);
        return this;
    }
    public RetData setCMD(int code, String mesg,Page page) {
        return setCMD(code, mesg, page, false);
    }
    public RetData setCMD(int code, String mesg,Page page,boolean changeToHump) {
        setCode(code);
        setMsg(mesg);
        setPage(page, changeToHump);
        return this;
    }
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


    public boolean isSucc() {
        return succ;
    }

    public RetData setSucc(boolean succ) {
        this.succ = succ;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RetData setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RetData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RetData setData(Object data) {
        this.data = data;
        return this;
    }

    public long getPageNum() {
        return pageNum;
    }

    public RetData setPageNum(long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public RetData setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public RetData setTotalPage(long totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public long getTotalRow() {
        return totalRow;
    }

    public RetData setTotalRow(long totalRow) {
        this.totalRow = totalRow;
        return this;
    }

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
