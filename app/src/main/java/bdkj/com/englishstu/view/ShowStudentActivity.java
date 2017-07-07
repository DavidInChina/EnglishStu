package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.CircleImageView;
import butterknife.BindView;
import butterknife.OnClick;

public class ShowStudentActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    @BindView(R.id.mBottom)
    View mButton;
    @BindView(R.id.civ_stu_head)
    CircleImageView civStuHead;
    @BindView(R.id.et_stu_name)
    TextView etStuName;
    @BindView(R.id.tv_stu_sex)
    TextView tvStuSex;
    @BindView(R.id.et_stu_account)
    TextView etStuAccount;
    @BindView(R.id.et_stu_classes)
    TextView etStuClasses;
    @BindView(R.id.et_stu_phone)
    TextView etStuPhone;
    @BindView(R.id.et_stu_email)
    TextView etStuEmail;

    private Student student;

    @Override
    protected int getViewId() {
        return R.layout.activity_show_student;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("学生详情");
        student = (Student) getIntent().getSerializableExtra("student");
        if (null == student) {
            ToastUtil.show(mContext, "数据获取失败！");
            finish();
        }
        initData();
    }

    /**
     */
    public void initData() {
        Glide.with(mContext).load(student.getUserHead()).into(civStuHead);
        etStuAccount.setText(student.getUserAccount());
        etStuClasses.setText(student.getClassIds().split(",")[0]);
        etStuEmail.setText(student.getEmail());
        etStuPhone.setText(student.getPhone());
        tvStuSex.setText(student.getSex());
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }


}
