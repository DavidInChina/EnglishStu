package bdkj.com.englishstu.common.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidinchina on 2017/5/25.
 * 考试成绩
 */

@Entity(indexes = {@Index(value = "updateDate DESC", unique = true)})
public class Mark implements Serializable {
    static final long serialVersionUID = 77L;
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String studentId;
    private String stuHead;//学生头像
    private String stuName;
    private String stuNumber;
    private String classId;
    private String testId;
    private String testType;
    private String examId;
    private String score;
    private String wordXml;//单词阅读结果评析
    private String sentenceXml;//单词阅读结果评析
    private Date updateDate;
    private Date createDate;

    private String testName;
    private String examName;
    private String img;
    private String beginTime;
    private String endTime;
    @Generated(hash = 984011077)
    public Mark(@NotNull String id, @NotNull String studentId, String stuHead,
            String stuName, String stuNumber, String classId, String testId,
            String testType, String examId, String score, String wordXml,
            String sentenceXml, Date updateDate, Date createDate, String testName,
            String examName, String img, String beginTime, String endTime) {
        this.id = id;
        this.studentId = studentId;
        this.stuHead = stuHead;
        this.stuName = stuName;
        this.stuNumber = stuNumber;
        this.classId = classId;
        this.testId = testId;
        this.testType = testType;
        this.examId = examId;
        this.score = score;
        this.wordXml = wordXml;
        this.sentenceXml = sentenceXml;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.testName = testName;
        this.examName = examName;
        this.img = img;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
    @Generated(hash = 1181118646)
    public Mark() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStudentId() {
        return this.studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStuHead() {
        return this.stuHead;
    }
    public void setStuHead(String stuHead) {
        this.stuHead = stuHead;
    }
    public String getStuName() {
        return this.stuName;
    }
    public void setStuName(String stuName) {
        this.stuName = stuName;
    }
    public String getStuNumber() {
        return this.stuNumber;
    }
    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }
    public String getClassId() {
        return this.classId;
    }
    public void setClassId(String classId) {
        this.classId = classId;
    }
    public String getTestId() {
        return this.testId;
    }
    public void setTestId(String testId) {
        this.testId = testId;
    }
    public String getTestType() {
        return this.testType;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }
    public String getExamId() {
        return this.examId;
    }
    public void setExamId(String examId) {
        this.examId = examId;
    }
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getWordXml() {
        return this.wordXml;
    }
    public void setWordXml(String wordXml) {
        this.wordXml = wordXml;
    }
    public String getSentenceXml() {
        return this.sentenceXml;
    }
    public void setSentenceXml(String sentenceXml) {
        this.sentenceXml = sentenceXml;
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
    public String getTestName() {
        return this.testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getExamName() {
        return this.examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
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
