package bdkj.com.englishstu.common.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by davidinchina on 2017/5/25.
 */

@Entity(indexes = {@Index(value = "updateDate DESC", unique = true)})
public class Classes {
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String name;
    private String logo;
    private Date updateDate;
    private Date createDate;
    @Transient
    private int classNumber;//班级人数
    @Transient
    private int teacherNumber;//教师人数
    @Transient
    private int testNumber;//近期考试数目

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public int getTeacherNumber() {
        return teacherNumber;
    }

    public void setTeacherNumber(int teacherNumber) {
        this.teacherNumber = teacherNumber;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    @Generated(hash = 1746054923)
    public Classes(@NotNull String id, @NotNull String name, String logo,
            Date updateDate, Date createDate) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }

    @Generated(hash = 11797154)
    public Classes() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogo() {
        return this.logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
