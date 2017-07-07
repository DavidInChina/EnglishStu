package bdkj.com.englishstu.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Classes;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class EditClassActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.et_note_title)
    EditText etClassName;
    @BindView(R.id.iv_class_logo)
    ImageView ivClassLogo;

    private String classLogo="";
    @Override
    protected int getViewId() {
        return R.layout.activity_edit_class;
    }

    public void initWeight() {
        tvTopTitle.setText("添加班级");
        tvConfirm.setText("保存");
        btnSave.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWeight();
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.iv_class_logo, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_class_logo:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_save:
                saveClass();
                break;
            case R.id.tv_confirm:
                saveClass();
                break;
        }
    }
    public void saveClass(){
        Classes classes = new Classes();
        if (!"".equals(classLogo)){
            classes.setLogo(classLogo);
        }else{
            ToastUtil.show(mContext,"班级logo不为空！");
            return;
        }
        if (!"".equals(etClassName.getText().toString())){
            classes.setName(etClassName.getText().toString());
        }else{
            ToastUtil.show(mContext,"班级名称不为空！");
            return;
        }
       JsonEntity entity =  AdmDbUtils.addClasses(classes);
        if (entity.getCode()==0){
            ToastUtil.show(mContext,"添加班级信息成功！");
            finish();
        }else{
            ToastUtil.show(mContext,entity.getMsg());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size() > 0) {
                classLogo =  images.get(0).path;
                Glide.with(mContext).load(classLogo).into(ivClassLogo);
            }
        }
    }
}
