package bdkj.com.englishstu.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.eventbus.RefreshTeacher;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.tool.VerificationUtils;
import bdkj.com.englishstu.common.widget.CircleImageView;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class TeaInfoModifyActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.civ_user_head)
    CircleImageView civUserHead;
    @BindView(R.id.et_user_account)
    EditText etUserAccount;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_email)
    EditText etUserEmail;

    private String userHead = "";
    private Teacher teacher;

    @Override
    protected int getViewId() {
        return R.layout.activity_info_modify;
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.btn_save, R.id.civ_user_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.civ_user_head:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_confirm:
                saveModify();
                break;
            case R.id.btn_save:
                saveModify();
                break;
        }
    }
    public void saveModify(){
            if (!userHead.equals("")){
                teacher.setUserHead(userHead);
            }else {
                ToastUtil.show(mContext,"请选择用户头像！");
                return;
            }
        if (VerificationUtils.matcherAccount(etUserAccount.getText().toString())){
            teacher.setUserAccount(etUserAccount.getText().toString());
        }else{
            ToastUtil.show(mContext,"用户登陆账号不合法！");
            return;
        }
        if (VerificationUtils.matcherRealName(etUserName.getText().toString())){
            teacher.setUserName(etUserName.getText().toString());
        }else{
            ToastUtil.show(mContext,"用户名不合法！");
            return;
        }
        if (!"".equals(etUserPassword.getText().toString())){
            if (VerificationUtils.matcherPassword(etUserPassword.getText().toString())){
                teacher.setUserPassword(etUserPassword.getText().toString());
            }else{
                ToastUtil.show(mContext,"密码不合法！");
                return;
            }
        }
        if (VerificationUtils.matcherPhoneNum(etUserPhone.getText().toString())){
            teacher.setPhone(etUserPhone.getText().toString());
        }else{
            ToastUtil.show(mContext,"用户联系电话不合法！");
            return;
        }
            if (VerificationUtils.matcherEmail(etUserEmail.getText().toString())){
                teacher.setEmail(etUserEmail.getText().toString());
            }else{
                ToastUtil.show(mContext,"用户邮箱不合法！");
                return;
            }
        JsonEntity entity = TeaDbUtils.updateSelf(teacher);
        if (entity.getCode()==0){
            ToastUtil.show(mContext,"个人信息修改成功！");
            Application.setTeacherInfo(mContext,teacher);
            EventBus.getDefault().post(new RefreshTeacher());
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size() > 0) {
                userHead = images.get(0).path;
                Logger.d(userHead);
                Glide.with(mContext).load(userHead).into(civUserHead);
            }
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("修改个人信息");
        tvConfirm.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
        civUserHead.setBorderColor(R.color.white);
        civUserHead.setBorderWidth(2);
        initData();
    }

    public void initData() {
        teacher = Application.getTeacherInfo();
        if (null == teacher) {
            ToastUtil.show(mContext, "数据错误！");
            finish();
        }
        if (null!=teacher.getUserHead()&&!"".equals(teacher.getUserHead())){
            userHead = teacher.getUserHead();
            Glide.with(mContext).load(teacher.getUserHead()).into(civUserHead);
        }
        etUserAccount.setText(teacher.getUserAccount());
        etUserAccount.setSelection(teacher.getUserAccount().length());
        etUserName.setText(teacher.getUserName());
        etUserPhone.setText(teacher.getPhone());
        etUserEmail.setText(teacher.getEmail());

    }
}
