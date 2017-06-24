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
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Classes;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.tool.VerificationUtils;
import bdkj.com.englishstu.common.widget.CircleImageView;
import bdkj.com.englishstu.selector.ChooseData;
import bdkj.com.englishstu.selector.SelectPopWindow;
import butterknife.BindView;
import butterknife.OnClick;

public class EditTeacherActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.civ_teacher_head)
    CircleImageView civTeacherHead;
    @BindView(R.id.et_teacher_name)
    EditText etTeacherName;
    @BindView(R.id.et_teacher_account)
    EditText etTeacherAccount;
    @BindView(R.id.et_teacher_password)
    EditText etTeacherPassword;
    @BindView(R.id.et_teacher_classes)
    TextView etTeacherClasses;
    @BindView(R.id.et_teacher_phone)
    EditText etTeacherPhone;
    @BindView(R.id.et_teacher_email)
    EditText etTeacherEmail;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.mBottom)
    View mButton;

    private String teacherHead = "";
    private String teacherNumber = "";
    private String teacherClasses = "";

    @Override
    protected int getViewId() {
        return R.layout.activity_edit_teacher;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("添加教师");
        tvConfirm.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
        btnSave.setText("确认添加");
        initFirst();
    }

    /**
     * 设置默认账号密码编号
     */
    public void initFirst() {
        JsonEntity entity = AdmDbUtils.getMaxNumber();
        if (entity.getCode() == 0) {
            String number = (String) entity.getData();
            etTeacherAccount.setText(number);
            etTeacherPassword.setText(number);
            teacherNumber = number;
        } else {
            ToastUtil.show(mContext, "初始化默认账号密码失败！");
            finish();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.btn_save, R.id.civ_teacher_head, R.id.et_teacher_classes})
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
            case R.id.civ_teacher_head:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.et_teacher_classes:
                chooseClasses();
                break;
        }
    }

    public void chooseClasses() {
        JsonEntity entity = AdmDbUtils.classList();
        List<ChooseData> list = new ArrayList<>();//这里从后台取出老师所在班级信息
        if (entity.getCode() == 0) {
            for (Classes classes : (List<Classes>) entity.getData()) {
                Logger.d(teacherClasses.contains(classes.getId()));
                list.add(new ChooseData(classes.getName(), classes.getId() + ";" + classes.getName(), teacherClasses.contains(classes.getId())));//是否已选
            }
        } else {
            ToastUtil.show(mContext, entity.getMsg());
            finish();
        }
        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) list)
                .setSIngleChoose(false)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        etTeacherClasses.setText(selectedList.size() + "个");
                        if (selectedList.size() > 0) {
                            teacherClasses = "";
                            for (ChooseData data : selectedList) {
                                teacherClasses = teacherClasses + data.getChooseDate() + ",";
                            }
                            teacherClasses =  teacherClasses.substring(0, teacherClasses.length() - 1);//去掉最后的逗号
                            Logger.d(teacherClasses);
                        } else {
                            teacherClasses = "";//置空
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
        Teacher teacher = new Teacher();
        teacher.setNumber(teacherNumber);
        if (!"".equals(teacherHead)) {
            teacher.setUserHead(teacherHead);
        } else {
            ToastUtil.show(mContext, "教师照片不为空！");
            return;
        }
        if (VerificationUtils.matcherRealName(etTeacherName.getText().toString())) {
            teacher.setUserName(etTeacherName.getText().toString());
        } else {
            ToastUtil.show(mContext, "教师姓名不合法！");
            return;
        }
        if (VerificationUtils.matcherAccount(etTeacherAccount.getText().toString())) {
            teacher.setUserAccount(etTeacherAccount.getText().toString());
        } else {
            ToastUtil.show(mContext, "教师登录账号不合法！");
            return;
        }
        if (VerificationUtils.matcherPassword(etTeacherPassword.getText().toString())) {
            teacher.setUserPassword(etTeacherPassword.getText().toString());
        } else {
            ToastUtil.show(mContext, "教师登录密码不合法！");
            return;
        }
        //这里先省去所在班级
        if (!"".equals(teacherClasses)) {
            teacher.setClassIds(teacherClasses);
        } else {
            ToastUtil.show(mContext, "请选择教师所在班级！");
            return;
        }

        if (VerificationUtils.matcherPhoneNum(etTeacherPhone.getText().toString())) {
            teacher.setPhone(etTeacherPhone.getText().toString());
        } else {
            ToastUtil.show(mContext, "联系方式不合法！");
            return;
        }
        if (VerificationUtils.matcherEmail(etTeacherEmail.getText().toString())) {
            teacher.setEmail(etTeacherEmail.getText().toString());
        } else {
            ToastUtil.show(mContext, "邮箱不合法！");
            return;
        }
        JsonEntity result = AdmDbUtils.addTeacher(teacher);
        if (result.getCode() == 0) {
            ToastUtil.show(mContext, "添加教师成功！");
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
                teacherHead = images.get(0).path;
                Glide.with(mContext).load(teacherHead).into(civTeacherHead);
            }
        }
    }
}
