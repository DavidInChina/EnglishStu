package bdkj.com.englishstu.common.dbinfo;

import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.common.beans.Admin;
import bdkj.com.englishstu.common.beans.AdminDao;
import bdkj.com.englishstu.common.beans.Classes;
import bdkj.com.englishstu.common.beans.ClassesDao;
import bdkj.com.englishstu.common.beans.Note;
import bdkj.com.englishstu.common.beans.NoteDao;
import bdkj.com.englishstu.common.tool.MD5Util;
import bdkj.com.englishstu.common.tool.TimeUtil;

/**
 * Created by davidinchina on 2017/5/25.
 */

public class AdmDbUtils {

    /**
     * 管理员账号初始化插入
     */
    public static void adminInsert() {
        JsonEntity<Admin> result = new JsonEntity<>();
        String account = "superadmin";
        AdminDao msgBeanDao = Application.getDaoSession().getAdminDao();
        List<Admin> list = msgBeanDao.queryBuilder().limit(1)
                .where(AdminDao.Properties.UserAccount.eq(account)).build().list();
        if (list.size() > 0) {
            Logger.d("管理员账户已添加！");
        } else {
            Admin admin = new Admin();
            admin.setId(UUID.randomUUID().toString());
            admin.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
            admin.setPhone("");
            admin.setEmail("admin@123.com");
            admin.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));
            admin.setUserHead("");
            admin.setUserPassword(MD5Util.md5Encode("superadmin"));
            admin.setUserName("Admin");
            admin.setUserAccount(account);
            msgBeanDao.insert(admin);
        }
    }

    /**
     * 管理员登录
     *
     * @param account
     * @param password
     * @return
     */
    public static JsonEntity adminLogin(String account, String password) {
        JsonEntity<Admin> result = new JsonEntity<>();
        AdminDao msgBeanDao = Application.getDaoSession().getAdminDao();
        List<Admin> list = msgBeanDao.queryBuilder().limit(1)
                .where(AdminDao.Properties.UserAccount.eq(account)).build().list();
        if (list.size() > 0 & list.get(0).getUserPassword().equals(MD5Util.md5Encode(password))) {
            result.setCode(0);
            result.setData(list.get(0));
            result.setMsg("登录成功！");
        } else {
            result.setMsg("登录失败！");
        }
        return result;
    }

    /**
     * 根据管理员id获取公告列表
     *
     * @param adminId
     * @return
     */
    public static JsonEntity noteList(String adminId) {
        JsonEntity<List<Note>> result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        List<Note> list = noteDao.queryBuilder()
                .where(NoteDao.Properties.AuthorId.eq(adminId)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 添加公告
     *
     * @param note
     * @return
     */
    public static JsonEntity addNode(Note note) {
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        noteDao.insert(note);
        result.setCode(0);
        result.setMsg("添加公告成功！");
        return result;
    }

    /**
     * 修改公告
     *
     * @param note
     * @return
     */
    public static JsonEntity updateNode(String adminId, Note note) {
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        noteDao.update(note);
        result.setCode(0);
        result.setMsg("修改公告成功！");
//        List<Note> list = noteDao.queryBuilder()
//                .where(NoteDao.Properties.AuthorId.eq(adminId)).build().list();
//        if (list.size() > 0)
//            for (Note noteItem : list) {
//                if (noteItem.getId().equals(note.getId())) {
//
//                    break;
//                }
//            }

        return result;
    }

    /**
     * 根据noteId 删除公告
     * @param noteId
     * @return
     */
    public static JsonEntity deleteNode(String noteId) {
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        List<Note> list = noteDao.queryBuilder().build().list();
        if (list.size() > 0) {
            for (Note noteItem : list) {
                if (noteItem.getId().equals(noteId)) {
                    noteDao.delete(noteItem);
                    result.setCode(0);
                    result.setMsg("删除公告成功！");
                    break;
                }
            }
        } else {
            result.setMsg("删除公告失败！");
        }
        return result;
    }

    /**
     * 添加班级
     * @param classes
     * @return
     */
    public static JsonEntity addClasses(Classes classes) {
        JsonEntity result = new JsonEntity<>();
        ClassesDao classDao = Application.getDaoSession().getClassesDao();
        classDao.insert(classes);
        result.setCode(0);
        result.setMsg("添加班级成功！");
        return result;
    }
}
