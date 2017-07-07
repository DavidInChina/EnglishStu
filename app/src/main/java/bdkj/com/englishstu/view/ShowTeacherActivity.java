package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.CircleImageView;
import butterknife.BindView;
import butterknife.OnClick;

public class ShowTeacherActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.civ_teacher_head)
    CircleImageView civTeacherHead;
    @BindView(R.id.et_teacher_name)
    TextView etTeacherName;
    @BindView(R.id.et_teacher_account)
    TextView etTeacherAccount;
    @BindView(R.id.et_teacher_classes)
    TextView etTeacherClasses;
    @BindView(R.id.et_teacher_phone)
    TextView etTeacherPhone;
    @BindView(R.id.et_teacher_email)
    TextView etTeacherEmail;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.mBottom)
    View mButton;

    private Teacher teacher;

    @Override
    protected int getViewId() {
        return R.layout.activity_show_teacher;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("教师详情");
        btnSave.setText("确认添加");
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        if (null == teacher) {
            ToastUtil.show(mContext, "获取数据失败！");
            finish();
        }
        initData();
    }

    public void initData() {
        Glide.with(mContext).load(teacher.getUserHead()).into(civTeacherHead);
        etTeacherAccount.setText(teacher.getUserAccount());
        String classes[] = teacher.getClassIds().split(",");
        StringBuilder classes2 = new StringBuilder();
        for (String lc : classes
                ) {
            classes2.append(lc.split(";")[1] + "  ");
        }

        etTeacherClasses.setText(classes2.toString());
        etTeacherEmail.setText(teacher.getEmail());
        etTeacherName.setText(teacher.getUserName());
        etTeacherPhone.setText(teacher.getPhone());
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
