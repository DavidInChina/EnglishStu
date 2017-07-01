package bdkj.com.englishstu.view;

import android.os.Bundle;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.tool.ToastUtil;

public class AnswerExamActivity extends BaseActivity {

    private Student student;
    private Test test;

    @Override
    protected int getViewId() {
        return R.layout.activity_answer_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        student = Application.getStudentInfo();
        test = (Test) getIntent().getExtras().getSerializable("test");
        if (null == student || null == test) {
            ToastUtil.show(mContext, "数据获取有误！");
            finish();
        }
        initWeight();
    }

    public void initWeight() {

    }


}
