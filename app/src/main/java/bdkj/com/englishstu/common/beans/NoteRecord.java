package bdkj.com.englishstu.common.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by davidinchina on 2017/6/1.
 */

@Entity(indexes = {@Index(value = "updateDate DESC", unique = true)})
public class NoteRecord {
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String noteId;
    private String studentId;
    private int status;//记录状态，0未读，1已读
    private Date updateDate;
    private Date createDate;
    @Generated(hash = 1558592743)
    public NoteRecord(@NotNull String id, @NotNull String noteId, String studentId,
            int status, Date updateDate, Date createDate) {
        this.id = id;
        this.noteId = noteId;
        this.studentId = studentId;
        this.status = status;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }
    @Generated(hash = 38732380)
    public NoteRecord() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNoteId() {
        return this.noteId;
    }
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
    public String getStudentId() {
        return this.studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
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