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
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Admin;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.CircleImageView;
import butterknife.BindView;
import butterknife.OnClick;

public class InfoModifyActivity extends BaseActivity {

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
                ToastUtil.show(mContext, "这里保存具体信息");
                break;
            case R.id.btn_save:
                ToastUtil.show(mContext, "这里保存具体信息");
                break;
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
        civUserHead.setBorderWidth(2);
        civUserHead.setBorderColor(R.color.white);
        initData();
    }

    public void initData() {
        Admin admin = Application.getAdminInfo();
        if (null == admin) {
            ToastUtil.show(mContext, "数据错误！");
            finish();
        }
        etUserAccount.setText(admin.getUserAccount());
        etUserAccount.setSelection(admin.getUserAccount().length());
        etUserName.setText(admin.getUserName());
        etUserPhone.setText(admin.getPhone());
        etUserEmail.setText(admin.getEmail());

    }
}
