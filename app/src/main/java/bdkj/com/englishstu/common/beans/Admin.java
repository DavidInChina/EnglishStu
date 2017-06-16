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
public class Admin implements Serializable{
   static final long serialVersionUID = 42L;
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String userName;
    private String userHead;
    private String userAccount;
    private String userPassword;
    private java.util.Date createDate;
    private java.util.Date updateDate;
    private String email;
    private String phone;
    @Generated(hash = 250375881)
    public Admin(@NotNull String id, @NotNull String userName, String userHead,
            String userAccount, String userPassword, java.util.Date createDate,
            java.util.Date updateDate, String email, String phone) {
        this.id = id;
        this.userName = userName;
        this.userHead = userHead;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.email = email;
        this.phone = phone;
    }
    @Generated(hash = 1708792177)
    public Admin() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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