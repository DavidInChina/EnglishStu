package bdkj.com.englishstu.common.dbinfo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.common.beans.Classes;
import bdkj.com.englishstu.common.beans.ClassesDao;
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
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.beans.TeacherDao;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.beans.TestDao;
import bdkj.com.englishstu.common.tool.TimeUtil;

/**
 * Created by davidinchina on 2017/6/3.
 */

public class TeaDbUtils {

    /**
     * 教师登录
     *
     * @param account
     * @param password
     * @return
     */
    public static JsonEntity teaLogin(String account, String password) {
        JsonEntity<Teacher> result = new JsonEntity<>();
        TeacherDao studentDao = Application.getDaoSession().getTeacherDao();
        Teacher teacher = studentDao.queryBuilder()
                .where(TeacherDao.Properties.UserAccount.eq(account)).build().unique();
        if (null != teacher && teacher.getUserPassword().equals(password)) {
            result.setCode(0);
            result.setData(teacher);
            result.setMsg("登录成功！");
        } else {
            result.setMsg("登录失败！");
        }
        return result;
    }

    /**
     * 修改个人信息
     *
     * @param teacher
     * @return
     */
    public static JsonEntity updateSelf(Teacher teacher) {
        JsonEntity result = new JsonEntity<>();
        TeacherDao teacherDao = Application.getDaoSession().getTeacherDao();
        teacherDao.update(teacher);
        result.setCode(0);
        result.setMsg("修改个人信息成功！");
        return result;
    }

    /**
     * 根据班级id获取班级列表
     *
     * @param classesId
     * @return
     */
    public static JsonEntity classList(String classesId) {
        JsonEntity<List<Classes>> result = new JsonEntity<>();
        List<Classes> classes = new LinkedList<>();
        ClassesDao classesDao = Application.getDaoSession().getClassesDao();
        String ids[] = classesId.split(",");
        for (String id : ids
                ) {
            Classes classes2 = classesDao.queryBuilder()
                    .where(NoteDao.Properties.Id.eq(id)).build().unique();
            classes.add(classes2);
        }
        result.setCode(0);
        result.setData(classes);
        result.setMsg("获取列表成功！");
        return result;
    }


    /**
     * 根据教师id获取公告列表
     *
     * @param teacherId
     * @return
     */
    public static JsonEntity noteList(String teacherId, String classesId) {
        JsonEntity<List<Note>> result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        List<Note> list = noteDao.queryBuilder().orderDesc(NoteDao.Properties.CreateDate)
                .where(NoteDao.Properties.AuthorId.eq(teacherId), NoteDao.Properties.ClassesId.eq(classesId)).build().list();
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
    public static JsonEntity searchNoteList(String keWord, String teacherId, String classesId) {
        JsonEntity<List<Note>> result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        List<Note> list = noteDao.queryBuilder()
                .where(NoteDao.Properties.Title.like(keWord), NoteDao.Properties.AuthorId.eq(teacherId), NoteDao.Properties.ClassesId.eq(classesId)).build().list();
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
        note.setId(UUID.randomUUID().toString());
        note.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
        note.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));//设置基本字段
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        NoteRecordDao recordDao = Application.getDaoSession().getNoteRecordDao();
        StudentDao studentDao = Application.getDaoSession().getStudentDao();
        List<Student> list = studentDao.queryBuilder().list();
        for (Student student : list
                ) {
            NoteRecord noteRecord = new NoteRecord();
            noteRecord.setId(UUID.randomUUID().toString());
            noteRecord.setStatus(0);
            noteRecord.setNoteId(note.getId());
            noteRecord.setStudentId(student.getId());
            noteRecord.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
            noteRecord.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));
            recordDao.insert(noteRecord);
        }
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
    public static JsonEntity updateNode(Note note) {
        JsonEntity result = new JsonEntity<>();
        NoteDao noteDao = Application.getDaoSession().getNoteDao();
        noteDao.update(note);
        result.setCode(0);
        result.setMsg("修改公告成功！");
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
        Note noteItem = noteDao.queryBuilder().build().unique();
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
     * 根据教师id获取题库列表
     *
     * @param teacherId
     * @return
     */
    public static JsonEntity examList(String teacherId, String classesId) {
        JsonEntity<List<Exam>> result = new JsonEntity<>();
        ExamDao examDao = Application.getDaoSession().getExamDao();
        List<Exam> list = examDao.queryBuilder()
                .where(ExamDao.Properties.TeacherId.eq(teacherId), ExamDao.Properties.ClassId.eq(classesId)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 根据关键字获取题库列表
     *
     * @param keWord
     * @return
     */
    public static JsonEntity searchExamList(String keWord, String teacherId, String classesId) {
        JsonEntity<List<Exam>> result = new JsonEntity<>();
        ExamDao examDao = Application.getDaoSession().getExamDao();
        List<Exam> list = examDao.queryBuilder()
                .where(ExamDao.Properties.Name.like(keWord), ExamDao.Properties.TeacherId.eq(teacherId), ExamDao.Properties.ClassId.eq(classesId)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 添加题库
     *
     * @param exam
     * @return
     */
    public static JsonEntity addExam(Exam exam) {
        exam.setId(UUID.randomUUID().toString());
        exam.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
        exam.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));//设置基本字段
        exam.setImg("");//多余字段
        JsonEntity result = new JsonEntity<>();
        ExamDao examDao = Application.getDaoSession().getExamDao();
        examDao.insert(exam);
        result.setCode(0);
        result.setMsg("添加题库成功！");
        return result;
    }

    /**
     * 修改题库
     *
     * @param exam
     * @return
     */
    public static JsonEntity updateNode(Exam exam) {
        JsonEntity result = new JsonEntity<>();
        ExamDao examDao = Application.getDaoSession().getExamDao();
        examDao.update(exam);
        result.setCode(0);
        result.setMsg("修改题库成功！");
        return result;
    }

    /**
     * 根据examId 删除题库
     *
     * @param examId
     * @return
     */
    public static JsonEntity deleteExam(String examId) {
        JsonEntity result = new JsonEntity<>();
        ExamDao examDao = Application.getDaoSession().getExamDao();
        Exam examItem = examDao.queryBuilder().where(ExamDao.Properties.Id.eq(examId)).build().unique();
        if (null != examItem) {
            TestDao testDao = Application.getDaoSession().getTestDao();
            List<Test> recordList = testDao.queryBuilder().where(TestDao.Properties.ExamId.eq(examItem.getId()))
                    .build().list();
            if (recordList.size() > 0) {
                result.setCode(1);
                result.setMsg("该题库还有测试或练习，无法删除！");
            } else {
                examDao.delete(examItem);
                result.setCode(0);
                result.setMsg("删除题库成功！");
            }
        } else {
            result.setMsg("删除题库失败！");
        }
        return result;
    }


    /**
     * 根据教师id获取考试练习列表
     *
     * @param teacherId
     * @return
     */
    public static JsonEntity testList(String teacherId, String classesId) {
        JsonEntity<List<Test>> result = new JsonEntity<>();
        TestDao testDao = Application.getDaoSession().getTestDao();
        List<Test> list = testDao.queryBuilder()
                .where(TestDao.Properties.TeacherId.eq(teacherId), TestDao.Properties.ClassId.eq(classesId)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 根据关键字获取考试练习列表
     *
     * @param keWord
     * @return
     */
    public static JsonEntity searchTestList(String keWord, String teacherId, String classesId) {
        JsonEntity<List<Test>> result = new JsonEntity<>();
        TestDao testDao = Application.getDaoSession().getTestDao();
        List<Test> list = testDao.queryBuilder()
                .where(TestDao.Properties.Name.like(keWord), TestDao.Properties.TeacherId.eq(teacherId), TestDao.Properties.ClassId.eq(classesId)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }

    /**
     * 添加考试练习
     *
     * @param test
     * @return
     */
    public static JsonEntity addTest(Test test) {
        test.setId(UUID.randomUUID().toString());
        test.setCreateDate(new Date(TimeUtil.getCurrentMillis()));
        test.setUpdateDate(new Date(TimeUtil.getCurrentMillis()));//设置基本字段
        JsonEntity result = new JsonEntity<>();
        TestDao testDao = Application.getDaoSession().getTestDao();
        testDao.insert(test);
        result.setCode(0);
        result.setMsg("添加考试练习成功！");
        return result;
    }

    /**
     * 修改考试练习
     *
     * @param test
     * @return
     */
    public static JsonEntity updateNode(Test test) {
        JsonEntity result = new JsonEntity<>();
        TestDao testDao = Application.getDaoSession().getTestDao();
        testDao.update(test);
        result.setCode(0);
        result.setMsg("修改考试练习成功！");
        return result;
    }

    /**
     * 根据testId 删除考试练习
     *
     * @param testId
     * @return
     */
    public static JsonEntity deleteTest(String testId) {
        JsonEntity result = new JsonEntity<>();
        TestDao testDao = Application.getDaoSession().getTestDao();
        Test test = testDao.queryBuilder().where(TestDao.Properties.Id.eq(testId)).build().unique();
        if (null != test) {
            MarkDao markDao = Application.getDaoSession().getMarkDao();
            List<Mark> recordList = markDao.queryBuilder().where(MarkDao.Properties.TestId.eq(test.getId()))
                    .build().list();
            if (recordList.size() > 0) {
                result.setCode(1);
                result.setMsg("该考试练习已有学生参与测试，无法删除！");
            } else {
                testDao.delete(test);
                result.setCode(0);
                result.setMsg("删除考试练习成功！");
            }

        } else {
            result.setMsg("删除考试练习失败！");
        }
        return result;
    }


    /**
     * 根据测试或练习id获取成绩列表
     *
     * @param testId
     * @return
     */
    public static JsonEntity markList(String testId) {
        JsonEntity<List<Mark>> result = new JsonEntity<>();
        MarkDao markDao = Application.getDaoSession().getMarkDao();
        List<Mark> list = markDao.queryBuilder()
                .where(MarkDao.Properties.TestId.eq(testId)).build().list();
        result.setCode(0);
        result.setData(list);
        result.setMsg("获取列表成功！");
        return result;
    }
}
