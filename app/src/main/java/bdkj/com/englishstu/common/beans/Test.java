package bdkj.com.englishstu.common.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidinchina on 2017/5/25.
 * 练习/考试
 */


@Entity(indexes = {@Index(value = "updateDate DESC", unique = true)})
public class Test implements Serializable{
    static final long serialVersionUID = 55L;
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String examId;
    private String examName;
    private String classId;
    private String type;//考试或者练习
    private String name;
    private String teacherId;
    private String teacherName;
    private String img;
    private Date updateDate;
    private Date createDate;
    private String beginTime;
    private String endTime;
    @Transient
    private int status;//状态0未做，1已做

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Generated(hash = 248357007)
    public Test(@NotNull String id, @NotNull String examId, String examName,
            String classId, String type, String name, String teacherId,
            String teacherName, String img, Date updateDate, Date createDate,
            String beginTime, String endTime) {
        this.id = id;
        this.examId = examId;
        this.examName = examName;
        this.classId = classId;
        this.type = type;
        this.name = name;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.img = img;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
    @Generated(hash = 372557997)
    public Test() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExamId() {
        return this.examId;
    }
    public void setExamId(String examId) {
        this.examId = examId;
    }
    public String getExamName() {
        return this.examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }
    public String getClassId() {
        return this.classId;
    }
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTeacherId() {
        return this.teacherId;
    }
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    public String getTeacherName() {
        return this.teacherName;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
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
    public String getBeginTime() {
        return this.beginTime;
    }
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
