package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Note;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class TeaEditNoticeActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.et_note_title)
    EditText etNoteTitle;
    @BindView(R.id.et_note_content)
    EditText etNoteContent;

    private Teacher teacher;
    String mClassId = "";

    @Override
    protected int getViewId() {
        return R.layout.activity_edit_notice;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        teacher = Application.getTeacherInfo();
        String classId = getIntent().getExtras().getString("classId");
        if (null == teacher||null == classId){
            ToastUtil.show(mContext,"获取数据失败！");
            finish();
        }
        mClassId = classId;
        initWeight();
    }

    public void initWeight() {
        tvTopTitle.setText("发布通知");
        tvConfirm.setText("保存");
        btnSave.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                saveNotice();
                break;
            case R.id.btn_save:
                saveNotice();
                break;
        }
    }

    public void saveNotice() {
        Note note = new Note();
        note.setAuthorId(teacher.getId());
        note.setAuthorName(teacher.getUserName());
        note.setClassesId(mClassId);
        note.setImg(teacher.getUserHead());
        if (!"".equals(etNoteTitle.getText().toString())){
            note.setTitle(etNoteTitle.getText().toString());
        }else{
            ToastUtil.show(mContext,"标题不为空！");
            return;
        }
        if (!"".equals(etNoteContent.getText().toString())){
            note.setContent(etNoteContent.getText().toString());
        }else{
            ToastUtil.show(mContext,"内容不为空！");
            return;
        }
        JsonEntity result = TeaDbUtils.addNote(note);
        if (result.getCode() == 0){
            ToastUtil.show(mContext,"添加通知成功！");
            finish();
        }else{
            ToastUtil.show(mContext,result.getMsg());
        }
    }

}
