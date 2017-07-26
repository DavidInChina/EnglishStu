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
import bdkj.com.englishstu.common.beans.NoteRecord;
import bdkj.com.englishstu.common.beans.NoteRecordDao;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.beans.StudentDao;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.beans.TeacherDao;
import bdkj.com.englishstu.common.beans.TestDao;
import bdkj.com.englishstu.common.tool.NumberFactory;
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
            admin.setUserPassword("superadmin");
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
        if (list.size() > 0 && list.get(0).getUserPassword().equals(password)) {
            result.setCode(0);
            result.setData(list.get(0));
            result.setMsg("登录成功！");
        } else {
            result.setMsg("登录失败！");
        }
        return result;
    }

    /**
     * 修改个人信息
     *
     * @param admin
     * @return
     */
    public static JsonEntity updateSelf(Admin admin) {
        JsonEntity result = new JsonEntity<>();
        AdminDao noteDao = Application.getDaoSession().getAdminDao();
        noteDao.update(admin);
        result.setCode(0);
        result.setMsg("修改个人信息成功！");
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
        List<Note> list = noteDao.queryBuilder().orderDesc(NoteDao.Properties.CreateDate).build().list();
//                .where(NoteDao.Properties.AuthorId.eq(adminId)).build().list();
        Logger.d(list.size() + "");
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 根据关键字获取公告列表
     *
     * @param keWord
     * @return
     */
    public static JsonEntity searchNoteList(String keWord) {
        JsonEntity<List<Note>> result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        List<Note> list = noteDao.queryBuilder()
                .where(NoteDao.Properties.Title.like(keWord)).build().list();
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
    public static JsonEntity addNote(Note note) {
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        NoteRecordDao recordDao = Application.getDaoSession().getNoteRecordDao();
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        ClassesDao classesDao = Application.getDaoSession().getClassesDao();
        List<Classes> classes = classesDao.queryBuilder().build().list();
        if (classes.size() > 0) {
            for (Classes classe : classes) {
                List<Student> list = studentDao.queryBuilder().where(StudentDao.Properties.ClassIds.eq(classe.getId())).list();
                for (Student student : list) {
                    NoteRecord noteRecord = new NoteRecord();
                    noteRecord.setId(UUID.randomUUID().toString());
                    noteRecord.setStatus(0);
                    noteRecord.setNoteId(note.getId());
                    noteRecord.setStudentId(student.getId());
                    noteRecord.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
                    noteRecord.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));
                    recordDao.insert(noteRecord);
                }
                note.setId(UUID.randomUUID().toString());
                note.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
                note.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));//设置基本字段
                note.setClassesId(classe.getId());
                noteDao.insert(note);
            }
            result.setCode(0);
            result.setMsg("添加公告成功！");
        } else {
            result.setMsg("请先添加班级！");
        }
        return result;
    }

    /**
     * 修改公告
     *
     * @param note
     * @return
     */
    public static JsonEntity updateNote(Note note) {
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
     *
     * @param noteId
     * @return
     */
    public static JsonEntity deleteNote(String noteId) {
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        Note noteItem = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(noteId)).build().unique();
        if (null != noteItem) {
            NoteRecordDao noteRecordDao = Application.getDaoSession().getNoteRecordDao();
            List<NoteRecord> recordList = noteRecordDao.queryBuilder().where(NoteRecordDao.Properties.NoteId.eq(noteItem.getId()))
                    .build().list();
            for (NoteRecord reoc : recordList) {
                noteRecordDao.delete(reoc);//删除此公告记录
            }
            noteDao.delete(noteItem);
            result.setCode(0);
            result.setMsg("删除公告成功！");
        } else {
            result.setMsg("删除公告失败！");
        }
        return result;
    }

    /**
     * 添加班级
     *
     * @param classes
     * @return
     */
    public static JsonEntity addClasses(Classes classes) {
        classes.setId(UUID.randomUUID().toString());
        classes.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
        classes.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));
        JsonEntity result = new JsonEntity<>();
        ClassesDao classDao = Application.getDaoSession().getClassesDao();
        classDao.insert(classes);
        result.setCode(0);
        result.setMsg("添加班级成功！");
        return result;
    }

    /**
     * 根据管理员id获取班级列表
     *
     * @return
     */
    public static JsonEntity classList() {
        JsonEntity<List<Classes>> result = new JsonEntity<>();
        ClassesDao classDao = Application.getDaoSession().getClassesDao();
        TeacherDao teacherDao = Application.getDaoSession().getTeacherDao();
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        TestDao testDao = Application.getDaoSession().getTestDao();
        List<Classes> list = classDao.queryBuilder().orderDesc(ClassesDao.Properties.UpdateDate).build().list();
        for (Classes c : list) {
            c.setTeacherNumber(teacherDao.queryBuilder().where(TeacherDao.Properties.ClassIds.like("%"+c.getId()+"%")).build().list().size());//教师所在班级包括当前班级
            c.setClassNumber(studentDao.queryBuilder().where(StudentDao.Properties.ClassIds.like("%"+c.getId()+"%")).build().list().size());
            c.setTestNumber(testDao.queryBuilder().where(TestDao.Properties.ClassId.like("%"+c.getId()+"%")).build().list().size());
        }
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 根据关键字获取班级列表
     *
     * @param keWord
     * @return
     */
    public static JsonEntity searchClassList(String keWord) {
        JsonEntity<List<Classes>> result = new JsonEntity<>();
        ClassesDao classDao = Application.getDaoSession().getClassesDao();
        List<Classes> list = classDao.queryBuilder().where(ClassesDao.Properties.Name.like(keWord)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 修改班级信息
     *
     * @param classes
     * @return
     */
    public static JsonEntity updateClasses(String adminId, Classes classes) {
        JsonEntity result = new JsonEntity<>();
        ClassesDao noteDao = Application.getDaoSession().getClassesDao();
        noteDao.update(classes);
        result.setCode(0);
        result.setMsg("修改班级信息成功！");
        return result;
    }

    /**
     * 根据noteId 删除班级
     *
     * @param classId
     * @return
     */
    public static JsonEntity deleteClasses(String classId) {
        JsonEntity result = new JsonEntity<>();
        ClassesDao noteDao = Application.getDaoSession().getClassesDao();
        Classes noteItem = noteDao.queryBuilder().where(ClassesDao.Properties.Id.eq(classId)).build().unique();
        if (null != noteItem) {
            StudentDao studentDao = Application.getDaoSession().getStudentDao();
            List<Student> recordList = studentDao.queryBuilder().where(StudentDao.Properties.ClassIds.eq(noteItem.getId()))
                    .build().list();
            if (recordList.size() > 0) {//班级还有学生
                result.setMsg("班级还有学生，不可删除！");
            } else {
                noteDao.delete(noteItem);
                result.setCode(0);
                result.setMsg("删除班级成功！");
            }
        } else {
            result.setMsg("删除班级失败！");
        }
        return result;
    }

    /**
     * 添加教师
     *
     * @param teacher
     * @return
     */
    public static JsonEntity addTeacher(Teacher teacher) {
        teacher.setId(UUID.randomUUID().toString());
        teacher.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
        teacher.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));
        TeacherDao teacherDao = Application.getDaoSession().getTeacherDao();
        JsonEntity result = new JsonEntity<>();
        teacherDao.insert(teacher);
        result.setCode(0);
        result.setMsg("添加教师成功！");
        return result;
    }

    public static JsonEntity getMaxNumber() {
        String number = "";
        TeacherDao teacherDao = Application.getDaoSession().getTeacherDao();
        List<Teacher> teachers = teacherDao.queryBuilder().orderDesc(TeacherDao.Properties.CreateDate).build().list();
        if (null == teachers || teachers.size() == 0) {
            number = NumberFactory.getNumber(null);
        } else {
            number = NumberFactory.getNumber(teachers.get(0).getNumber());//第一位教师
        }
        JsonEntity result = new JsonEntity<>();
        result.setData(number);
        result.setCode(0);
        result.setMsg("获取最大教师工号成功！");
        return result;
    }

    public static JsonEntity getMaxStuNumber() {
        String number = "";
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        List<Student> students = studentDao.queryBuilder().orderDesc(StudentDao.Properties.CreateDate).build().list();
        if (null == students || students.size() == 0) {
            number = NumberFactory.getStuNumber(null);
        } else {
            number = NumberFactory.getStuNumber(students.get(0).getNumber());
        }
        JsonEntity result = new JsonEntity<>();
        result.setData(number);
        result.setCode(0);
        result.setMsg("获取最大学生工号成功！");
        return result;
    }

    /**
     * 根据管理员id获取教师列表
     *
     * @return
     */
    public static JsonEntity teacherList() {
        JsonEntity<List<Teacher>> result = new JsonEntity<>();
        TeacherDao classDao = Application.getDaoSession().getTeacherDao();
        List<Teacher> list = classDao.queryBuilder().orderDesc(TeacherDao.Properties.CreateDate).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 根据关键字获取教师列表
     *
     * @param keWord
     * @return
     */
    public static JsonEntity searchTeacherList(String keWord) {
        JsonEntity<List<Teacher>> result = new JsonEntity<>();
        TeacherDao classDao = Application.getDaoSession().getTeacherDao();
        List<Teacher> list = classDao.queryBuilder().where(TeacherDao.Properties.UserName.like(keWord)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 修改教师信息
     *
     * @param teacher
     * @return
     */
    public static JsonEntity updateTeacher(String adminId, Teacher teacher) {
        JsonEntity result = new JsonEntity<>();
        TeacherDao noteDao = Application.getDaoSession().getTeacherDao();
        noteDao.update(teacher);
        result.setCode(0);
        result.setMsg("修改教师信息成功！");
        return result;
    }

    /**
     * 根据noteId 删除教师
     *
     * @param teacherId
     * @return
     */
    public static JsonEntity deleteTeacher(String teacherId) {
        JsonEntity result = new JsonEntity<>();
        TeacherDao teacherDao = Application.getDaoSession().getTeacherDao();
        //这里应该还要添加教师删除内容的判定，如关于题库部分的教师信息等
        Teacher noteItem = teacherDao.queryBuilder().build().unique();
        if (null != noteItem) {
            teacherDao.delete(noteItem);
            result.setCode(0);
            result.setMsg("删除教师成功！");
        } else {
            result.setMsg("删除教师失败！");
        }
        return result;
    }


    /**
     * 添加学生
     *
     * @param student
     * @return
     */
    public static JsonEntity addStudent(Student student) {
        student.setId(UUID.randomUUID().toString());
        student.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
        student.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));
        JsonEntity result = new JsonEntity<>();
        StudentDao classDao = Application.getDaoSession().getStudentDao();
        classDao.insert(student);
        result.setCode(0);
        result.setMsg("添加学生成功！");
        return result;
    }

    /**
     * 根据管理员id获取学生列表
     *
     * @return
     */
    public static JsonEntity studentList() {
        JsonEntity<List<Student>> result = new JsonEntity<>();
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        List<Student> list = studentDao.queryBuilder().build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 根据关键字获取学生列表
     *
     * @param keWord
     * @return
     */
    public static JsonEntity searchStudentList(String keWord) {
        JsonEntity<List<Student>> result = new JsonEntity<>();
        StudentDao classDao = Application.getDaoSession().getStudentDao();
        List<Student> list = classDao.queryBuilder().where(StudentDao.Properties.UserName.like(keWord)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 修改学生信息
     *
     * @param student
     * @return
     */
    public static JsonEntity updateStudent(String adminId, Student student) {
        JsonEntity result = new JsonEntity<>();
        StudentDao noteDao = Application.getDaoSession().getStudentDao();
        noteDao.update(student);
        result.setCode(0);
        result.setMsg("修改学生信息成功！");
        return result;
    }

    /**
     * 根据noteId 删除学生
     *
     * @param studentId
     * @return
     */
    public static JsonEntity deleteStudent(String studentId) {
        JsonEntity result = new JsonEntity<>();
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        Student noteItem = studentDao.queryBuilder().build().unique();
        if (null != noteItem) {
            studentDao.delete(noteItem);
            result.setCode(0);
            result.setMsg("删除学生成功！");
        } else {
            result.setMsg("删除学生失败！");
        }
        return result;
    }


}
