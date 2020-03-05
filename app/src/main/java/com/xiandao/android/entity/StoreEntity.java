package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:商店对象
 *
 * @author TanYong
 *         create at 2018/3/1 09:55
 *         <p>
 *         "id": 2,
 *         "name": "商家2",
 *         "address": "天心区",
 *         "projectids": "1,2,3",
 *         "remark": null,
 *         "createtime": "2018-02-27 14:43:39",
 *         "managername": "张四",
 *         "managerconeact": "13918832824"
 */
public class StoreEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String address;
    private String projectids;
    private String remark;
    private String createtime;
    private String managername;
    private String managerconeact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProjectids() {
        return projectids;
    }

    public void setProjectids(String projectids) {
        this.projectids = projectids;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getManagername() {
        return managername;
    }

    public void setManagername(String managername) {
        this.managername = managername;
    }

    public String getManagerconeact() {
        return managerconeact;
    }

    public void setManagerconeact(String managerconeact) {
        this.managerconeact = managerconeact;
    }
}
