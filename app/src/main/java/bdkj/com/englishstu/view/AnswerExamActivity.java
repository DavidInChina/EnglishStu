package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.StuDbUtils;
import bdkj.com.englishstu.common.tool.TimeUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.xml.Result;
import bdkj.com.englishstu.common.xml.XmlResultParser;
import bdkj.com.englishstu.view.Fragment.TestNoteFragment;
import bdkj.com.englishstu.view.Fragment.TestSentenceFragment;
import bdkj.com.englishstu.view.Fragment.TestSubFragment;
import bdkj.com.englishstu.view.Fragment.TestWordFragment;
import butterknife.BindView;
import butterknife.OnClick;

public class AnswerExamActivity extends BaseActivity {

    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;
    @BindView(R.id.rb_menu_left)
    RadioGroup rbMenuLeft;
    @BindView(R.id.fragmentView)
    FrameLayout fragmentView;
    @BindView(R.id.mBottom)
    View mBottom;
    @BindView(R.id.view_line)
    View viewLine;
    private Student student;
    private Test test;//这个是做的题
    private Mark mark;//这是最终要完成的答题成绩
    private int discount;
    private String type = "0";//默认考试
    private Map<String, BaseFragment> fragmentMap = new HashMap<>();
    private static final String ANS_NOTE = "note";
    private static final String ANS_WORD = "word";
    private static final String ANS_SENTENCE = "sentence";
    private static final String ANS_SUBMIT = "submit";
    private String currentType = ANS_SUBMIT;
    private long lastTime = 0;
    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    tvDiscount.setText(TimeUtil.time2Minutes(message.arg1));
                    break;
                case 1:
                    //这里倒计时结束，弹出自动交卷提示
                    ToastUtil.show(mContext, "倒计时结束");
                    break;
            }
        }
    };

    @Override
    protected int getViewId() {
        return R.layout.activity_answer_layout;
    }

    public Test getCurrentTest() {
        return test;
    }

    public String getCurrentType() {
        return type;
    }

    public void setWordResult(String wordResult) {
        mark.setWordXml(wordResult);
    }

    public void setSentenceResult(String sentenceResult) {
        mark.setSentenceXml(sentenceResult);
    }

    public void beginSaveMark() {
        if (null == mark || null == mark.getSentenceXml() || "".equals(mark.getSentenceXml()) ||
                null == mark.getWordXml() || "".equals(mark.getWordXml())) {
            ToastUtil.show(mContext, "答题结果有误，考试结束！");
        }
        mark.setImg(test.getImg());
        mark.setBeginTime(test.getBeginTime());
        mark.setEndTime(test.getEndTime());
        mark.setTestName(test.getName());
        mark.setTestId(test.getId());
        mark.setTestType(test.getType());
        JsonEntity entity1 = StuDbUtils.getExamDetail(test.getExamId());
        if (entity1.getCode() == 0) {
            Exam exam = (Exam) entity1.getData();
            if (null != exam) {
                mark.setExamId(exam.getId());
                mark.setExamName(exam.getName());
            } else {
                ToastUtil.show(mContext, "获取试题详情失败！");
                finish();
            }
        } else {
            ToastUtil.show(mContext, entity1.getMsg());
        }
        mark.setStudentId(student.getId());
        mark.setStuNumber(student.getNumber());
        mark.setClassId(student.getClassIds().split(",")[1]);
        mark.setStuHead(student.getUserHead());
        mark.setStuName(student.getUserName());
        float score = 0;//总分
        int size = 0;//单词加语句个数
        if (!"".equals(mark.getWordXml())) {
            XmlResultParser resultParser = new XmlResultParser();
            String word[] = mark.getWordXml().split(",");
            for (String wordXml : word
                    ) {
                size++;
                Result result = resultParser.parse(wordXml);
                score += result.total_score;
            }
        }
        if (!"".equals(mark.getSentenceXml())) {
            XmlResultParser resultParser = new XmlResultParser();
            String sentences[] = mark.getSentenceXml().split(",");
            for (String sentenceXml : sentences
                    ) {
                size++;
                Result result = resultParser.parse(sentenceXml);
                score += result.total_score;
            }
        }
        int lastScore = (int) ((100 * score) / (size * 5));
        Logger.d(lastScore + "/" + score + "/" + size);
        mark.setScore(lastScore + "");
        JsonEntity entity = StuDbUtils.addMark(mark);
        if (entity.getCode() == 0) {
            ToastUtil.show(mContext, "答题成功，请到成绩查看查询答题结果。");
        } else {
            ToastUtil.show(mContext, entity.getMsg());
        }
        finish();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        student = Application.getStudentInfo();
        test = (Test) getIntent().getExtras().getSerializable("test");
        type = getIntent().getStringExtra("type");
        if (null == student || null == test) {
            ToastUtil.show(mContext, "数据获取有误！");
            finish();
        }
        initMenu();
        initWeight();
        beginDiscount();
    }

    @Override
    public void onBackPressed() {
        if (type.equals("0")) {
            if (System.currentTimeMillis() - lastTime > 2000) {
                ToastUtil.show(mContext, "再按一次取消当前考试");
                lastTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }


    public void initMenu() {
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        rb4.setClickable(false);
        rbMenuLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        toggleFragment(ANS_NOTE);
                        break;
                    case R.id.rb_2:
                        toggleFragment(ANS_WORD);
                        break;
                    case R.id.rb_3:
                        toggleFragment(ANS_SENTENCE);
                        break;
                    case R.id.rb_4:
                        toggleFragment(ANS_SUBMIT);
                        break;
                    default:
                }
            }
        });
        rb1.setChecked(true);//默认初始化内容
        toggleFragment(ANS_NOTE);
    }

    public void toggleFragment(String type) {
        if (!type.equals(currentType)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//获取管理类对象
            if (!fragmentMap.containsKey(type)) {//进行初始化添加
                switch (type) {
                    case ANS_NOTE:
                        fragmentMap.put(ANS_NOTE, new TestNoteFragment());
                        break;
                    case ANS_WORD:
                        fragmentMap.put(ANS_WORD, new TestWordFragment());
                        break;
                    case ANS_SENTENCE:
                        fragmentMap.put(ANS_SENTENCE, new TestSentenceFragment());
                        break;
                    case ANS_SUBMIT:
                        fragmentMap.put(ANS_SUBMIT, new TestSubFragment());
                        break;
                }
            }
            transaction.replace(R.id.fragmentView, fragmentMap.get(type));//替换到布局页面
            currentType = type;
            transaction.commit();
        }
    }

    public void initWeight() {

    }

    /**
     * 开始倒数
     */
    public void beginDiscount() {
        discount = (int) TimeUtil.minutes2Time(tvDiscount.getText().toString());//获得秒数
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (discount-- > 0) {
                    try {
                        Thread.sleep(1000);//暂停一秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = discount;
                    handler.sendMessage(message);
                }
                handler.sendEmptyMessage(1);//结束倒计时

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    @OnClick(R.id.tv_confirm)
    public void onViewClicked() {
        switch (currentType) {
            case ANS_NOTE:
                toggleFragment(ANS_WORD);
                rb2.setChecked(true);
                tvConfirm.setText("下一项");
                tvTopTitle.setText("单词朗读");
                if (null == mark) {
                    mark = new Mark();
                }
                break;
            case ANS_WORD:
                if (null != mark.getWordXml() && !"".equals(mark.getWordXml())) {
                    toggleFragment(ANS_SENTENCE);
                    rb3.setChecked(true);
                    tvTopTitle.setText("语句朗读");
                } else {
                    ToastUtil.show(mContext, "请完成单词朗读!");
                }

                break;
            case ANS_SENTENCE:
                if (null != mark.getSentenceXml() && !"".equals(mark.getSentenceXml())) {
                    toggleFragment(ANS_SUBMIT);
                    tvConfirm.setText("提交");
                    rb4.setChecked(true);
                    tvTopTitle.setText("提交答案");
                } else {
                    ToastUtil.show(mContext, "请完成语句朗读!");
                }
                break;
            case ANS_SUBMIT:
                beginSaveMark();
                break;
        }
    }
}
