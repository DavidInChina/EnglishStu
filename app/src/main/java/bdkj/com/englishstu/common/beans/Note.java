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
public class Note {
    @Id
    @NotNull
    @Unique
    private String id;

    @NotNull
    private String authorId;//发布人id
    private String authorName;//发布人名字
    private String img;//图片标签
    private String title;
    private String content;
    private Date updateDate;
    private Date createDate;
    @Generated(hash = 1605190231)
    public Note(@NotNull String id, @NotNull String authorId, String authorName,
            String img, String title, String content, Date updateDate,
            Date createDate) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.img = img;
        this.title = title;
        this.content = content;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    public String getAuthorName() {
        return this.authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
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