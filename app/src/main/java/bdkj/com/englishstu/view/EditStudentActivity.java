package bdkj.com.englishstu.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Classes;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.tool.VerificationUtils;
import bdkj.com.englishstu.common.widget.CircleImageView;
import bdkj.com.englishstu.selector.ChooseData;
import bdkj.com.englishstu.selector.SelectPopWindow;
import butterknife.BindView;
import butterknife.OnClick;

public class EditStudentActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.mBottom)
    View mButton;
    @BindView(R.id.civ_stu_head)
    CircleImageView civStuHead;
    @BindView(R.id.et_stu_name)
    EditText etStuName;
    @BindView(R.id.tv_stu_sex)
    TextView tvStuSex;
    @BindView(R.id.et_stu_account)
    EditText etStuAccount;
    @BindView(R.id.et_stu_password)
    EditText etStuPassword;
    @BindView(R.id.et_stu_classes)
    TextView etStuClasses;
    @BindView(R.id.et_stu_phone)
    EditText etStuPhone;
    @BindView(R.id.et_stu_email)
    EditText etStuEmail;

    private String stuHead = "";
    private String stuNumber = "";
    private String stuClasses = "";

    @Override
    protected int getViewId() {
        return R.layout.activity_edit_student;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("添加学生");
        tvConfirm.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
        btnSave.setText("确认添加");
        initFirst();
    }

    /**
     * 设置默认账号密码编号
     */
    public void initFirst() {
        JsonEntity entity = AdmDbUtils.getMaxStuNumber();
        if (entity.getCode() == 0) {
            String number = (String) entity.getData();
            etStuAccount.setText(number);
            etStuPassword.setText(number);
            stuNumber = number;
        } else {
            ToastUtil.show(mContext, "初始化默认账号密码失败！");
            finish();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.btn_save, R.id.civ_stu_head, R.id.et_stu_classes, R.id.tv_stu_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                saveTeacher();
                break;
            case R.id.btn_save:
                saveTeacher();
                break;
            case R.id.civ_stu_head:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_stu_sex:
                chooseSex();
                break;
            case R.id.et_stu_classes:
                chooseClasses();
                break;
        }
    }

    public void chooseSex() {
        List<ChooseData> list = new ArrayList<>();
        list.add(new ChooseData("男", "男", false));
        list.add(new ChooseData("女", "女", false));
        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) list)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            tvStuSex.setText(selectedList.get(0).getChooseDate());
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("所在班级")
                .build()
                .show(mButton);
    }

    public void chooseClasses() {
        JsonEntity entity = AdmDbUtils.classList();
        List<ChooseData> list = new ArrayList<>();//这里从后台取出老师所在班级信息
        if (entity.getCode() == 0) {
            for (Classes classes : (List<Classes>) entity.getData()) {
                list.add(new ChooseData(classes.getName(), classes.getName() + "," + classes.getId(), false));//是否已选
            }
        } else {
            ToastUtil.show(mContext, entity.getMsg());
            finish();
        }
        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) list)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            etStuClasses.setText(selectedList.get(0).getShowText());
                            stuClasses = selectedList.get(0).getChooseDate();
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("所在班级")
                .build()
                .show(mButton);
    }

    public void saveTeacher() {
        Student student = new Student();
        student.setNumber(stuNumber);
        student.setSex(tvStuSex.getText().toString());
        if (!"".equals(stuHead)) {
            student.setUserHead(stuHead);
        } else {
            ToastUtil.show(mContext, "学生照片不为空！");
            return;
        }
        if (VerificationUtils.matcherRealName(etStuName.getText().toString())) {
            student.setUserName(etStuName.getText().toString());
        } else {
            ToastUtil.show(mContext, "学生姓名不合法！");
            return;
        }

        if (VerificationUtils.matcherAccount(etStuAccount.getText().toString())) {
            student.setUserAccount(etStuAccount.getText().toString());
        } else {
            ToastUtil.show(mContext, "学生登录账号不合法！");
            return;
        }
        if (VerificationUtils.matcherPassword(etStuPassword.getText().toString())) {
            student.setUserPassword(etStuPassword.getText().toString());
        } else {
            ToastUtil.show(mContext, "学生登录密码不合法！");
            return;
        }
        if (!"".equals(stuClasses)) {
            student.setClassIds(stuClasses);
        } else {
            ToastUtil.show(mContext, "请选择学生所在班级！");
            return;
        }

        if (VerificationUtils.matcherPhoneNum(etStuPhone.getText().toString())) {
            student.setPhone(etStuPhone.getText().toString());
        } else {
            ToastUtil.show(mContext, "联系方式不合法！");
            return;
        }
        if (VerificationUtils.matcherEmail(etStuEmail.getText().toString())) {
            student.setEmail(etStuEmail.getText().toString());
        } else {
            ToastUtil.show(mContext, "邮箱不合法！");
            return;
        }
        JsonEntity result = AdmDbUtils.addStudent(student);
        if (result.getCode() == 0) {
            ToastUtil.show(mContext, "添加学生成功！");

            finish();
        } else {
            ToastUtil.show(mContext, result.getMsg());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size() > 0) {
                stuHead = images.get(0).path;
                Glide.with(mContext).load(stuHead).into(civStuHead);
            }
        }
    }


}
