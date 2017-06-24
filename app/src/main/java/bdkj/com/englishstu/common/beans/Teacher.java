package bdkj.com.englishstu.common.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by davidinchina on 2017/5/25.
 */

@Entity(indexes = {@Index(value = "updateDate DESC", unique = true)})
public class Teacher implements Serializable{
    static final long serialVersionUID = 44L;
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String classIds;//多个值，格式是：  班级id;班级名称，班级id;班级名称
    private String userName;
    private String number;//工号
    private String userHead;
    private String userAccount;
    private String userPassword;
    private java.util.Date createDate;
    private java.util.Date updateDate;
    private String email;
    private String phone;
    @Generated(hash = 1625336758)
    public Teacher(@NotNull String id, @NotNull String classIds, String userName,
            String number, String userHead, String userAccount, String userPassword,
            java.util.Date createDate, java.util.Date updateDate, String email,
            String phone) {
        this.id = id;
        this.classIds = classIds;
        this.userName = userName;
        this.number = number;
        this.userHead = userHead;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.email = email;
        this.phone = phone;
    }
    @Generated(hash = 1630413260)
    public Teacher() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClassIds() {
        return this.classIds;
    }
    public void setClassIds(String classIds) {
        this.classIds = classIds;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getUserHead() {
        return this.userHead;
    }
    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
    public String getUserAccount() {
        return this.userAccount;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public String getUserPassword() {
        return this.userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

}