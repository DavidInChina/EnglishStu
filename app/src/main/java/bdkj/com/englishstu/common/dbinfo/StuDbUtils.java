package bdkj.com.englishstu.common.dbinfo;

import java.util.LinkedList;
import java.util.List;

import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.beans.ExamDao;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.beans.MarkDao;
import bdkj.com.englishstu.common.beans.Note;
import bdkj.com.englishstu.common.beans.NoteDao;
import bdkj.com.englishstu.common.beans.NoteRecord;
import bdkj.com.englishstu.common.beans.NoteRecordDao;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.beans.StudentDao;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.beans.TestDao;
import bdkj.com.englishstu.common.tool.MD5Util;

/**
 * Created by davidinchina on 2017/5/25.
 */

public class StuDbUtils {

    /**
     * 学生登录
     *
     * @param account
     * @param password
     * @return
     */
    public static JsonEntity stuLogin(String account, String password) {
        JsonEntity<Student> result = new JsonEntity<>();
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        List<Student> list = studentDao.queryBuilder().limit(1)
                .where(StudentDao.Properties.UserAccount.eq(account)).build().list();
        if (list.size() > 0 && list.get(0).getUserPassword().equals(MD5Util.md5Encode(password))) {
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
     * @param student
     * @return
     */
    public static JsonEntity updateSelf(Student student) {
        JsonEntity result = new JsonEntity<>();
        StudentDao noteDao = Application.getDaoSession().getStudentDao();
        noteDao.update(student);
        result.setCode(0);
        result.setMsg("修改个人信息成功！");
        return result;
    }

    /**
     * 公告列表
     *
     * @param studentId
     * @return
     */
    public static JsonEntity noteList(String studentId) {
        JsonEntity<List<Note>> result = new JsonEntity<>();
        NoteRecordDao noteRecordDao = Application.getDaoSession().getNoteRecordDao();
        List<Note> notes = new LinkedList<>();
        List<NoteRecord> list = noteRecordDao.queryBuilder().limit(1)
                .where(NoteRecordDao.Properties.StudentId.eq(studentId)).build().list();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        if (list.size() > 0) {
            for (NoteRecord record : list
                    ) {
                Note note = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(record.getNoteId())).build().unique();
                note.setStatus(record.getStatus());
                note.setRecordId(record.getId());
                notes.add(note);
            }
            result.setCode(0);
            result.setData(notes);
            result.setMsg("获取公告列表成功！");
        } else {
            result.setMsg("获取公告列表失败！");
        }
        return result;
    }

    /**
     * 公告阅读
     *
     * @param recordId
     * @return
     */
    public static JsonEntity noteRead(String recordId) {
        JsonEntity<List<Note>> result = new JsonEntity<>();
        NoteRecordDao noteRecordDao = Application.getDaoSession().getNoteRecordDao();
        NoteRecord record = noteRecordDao.queryBuilder()
                .where(NoteRecordDao.Properties.Id.eq(recordId)).build().unique();
        record.setStatus(1);
        noteRecordDao.update(record);
        result.setCode(0);
        result.setData(null);
        result.setMsg("阅读公告成功！");
        return result;
    }

    /**
     * 获取练习或考试列表
     *
     * @param classesId
     * @return
     */
    public static JsonEntity testList(String studentId, String classesId, int type) {
        JsonEntity<List<Test>> result = new JsonEntity<>();
        TestDao testDao = Application.getDaoSession().getTestDao();
        List<Test> list = testDao.queryBuilder()
                .where(TestDao.Properties.ClassId.eq(classesId), TestDao.Properties.Type.eq(type)).build().list();
        MarkDao markDao = Application.getDaoSession().getMarkDao();
        if (list.size() > 0) {
            for (Test test : list
                    ) {
                Mark mark = markDao.queryBuilder().where(MarkDao.Properties.StudentId.eq(studentId)).build().unique();
                if (null != mark) {
                    test.setStatus(1);
                } else {
                    test.setStatus(0);
                }
            }
            result.setCode(0);
            result.setData(list);
            result.setMsg("获取列表成功！");
        } else {
            result.setMsg("获取列表失败！");
        }
        return result;
    }

    /**
     * 获取试题详情
     *
     * @param examId
     * @return
     */
    public static JsonEntity getExamDetail(String examId) {
        JsonEntity<Exam> result = new JsonEntity<>();
        ExamDao examDao = Application.getDaoSession().getExamDao();
        Exam exam = examDao.queryBuilder()
                .where(ExamDao.Properties.Id.eq(examId)).build().unique();
        if (null != exam) {
            result.setCode(0);
            result.setData(exam);
            result.setMsg("获取试题成功！");
        } else {
            result.setMsg("获取试题失败！");
        }
        return result;
    }

    /**
     * 提交考试成绩
     *
     * @param mark
     * @return
     */
    public static JsonEntity addMark(Mark mark) {
        JsonEntity<Mark> result = new JsonEntity<>();
        MarkDao markDao = Application.getDaoSession().getMarkDao();
        markDao.insert(mark);
        result.setCode(0);
        result.setData(null);
        result.setMsg("获取试题成功！");
        return result;
    }

    /**
     * 获取成绩列表
     *
     * @param studentId
     * @return
     */
    public static JsonEntity markList(String studentId, int type) {
        JsonEntity<List<Mark>> result = new JsonEntity<>();
        MarkDao markDao = Application.getDaoSession().getMarkDao();
        List<Mark> list = markDao.queryBuilder().where(MarkDao.Properties.StudentId.eq(studentId), MarkDao.Properties.TestType.eq(type)).build().list();
        if (list.size() > 0) {
            result.setCode(0);
            result.setData(list);
            result.setMsg("获取列表成功！");
        } else {
            result.setMsg("获取列表失败！");
        }
        return result;
    }
}
