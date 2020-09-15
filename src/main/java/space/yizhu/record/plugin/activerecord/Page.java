

package space.yizhu.record.plugin.activerecord;

import java.io.Serializable;
import java.util.List;


public class Page<T> implements Serializable {

    private static final long serialVersionUID = -7102129155309986923L;

    private List<T> list;                
    private int pageNumber;                
    private int pageSize;                
    private int totalPage;                
    private int totalRow;                

    
    public Page(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
    }

    public Page() {

    }

    
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    
    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    
    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public boolean isFirstPage() {
        return pageNumber == 1;
    }

    public boolean isLastPage() {
        return pageNumber >= totalPage;
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("pageNumber : ").append(pageNumber);
        msg.append("\npageSize : ").append(pageSize);
        msg.append("\ntotalPage : ").append(totalPage);
        msg.append("\ntotalRow : ").append(totalRow);
        return msg.toString();
    }
}

