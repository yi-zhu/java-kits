package space.yizhu.bean;/* Created by yi on 9/18/2020.*/

/**
 * @author yi
 */
public class Test  extends BaseModel<Test>{
    private String id ,code,name,creator,updator,remark,create_date, update_date;

    public String getId() {
        return id;
    }

    public Test setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Test setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Test setName(String name) {
        this.name = name;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public Test setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public String getUpdator() {
        return updator;
    }

    public Test setUpdator(String updator) {
        this.updator = updator;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public Test setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getCreate_date() {
        return create_date;
    }

    public Test setCreate_date(String create_date) {
        this.create_date = create_date;
        return this;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public Test setUpdate_date(String update_date) {
        this.update_date = update_date;
        return this;
    }
}
