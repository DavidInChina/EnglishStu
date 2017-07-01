package bdkj.com.englishstu.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.tool.DialogUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.wheelpicker.DatePicker;
import bdkj.com.englishstu.selector.ChooseData;
import bdkj.com.englishstu.selector.SelectPopWindow;
import butterknife.BindView;
import butterknife.OnClick;

public class EditTestActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.mBottom)
    View mBottom;
    @BindView(R.id.civ_test_logo)
    ImageView civTestLogo;
    @BindView(R.id.et_test_name)
    EditText etTestName;
    @BindView(R.id.tv_test_type)
    TextView tvTestType;
    @BindView(R.id.tv_exam_name)
    TextView tvExamName;
    @BindView(R.id.tv_test_begin)
    TextView tvTestBegin;
    @BindView(R.id.tv_test_end)
    TextView tvTestEnd;


    private String testLogo = "";
    private List<ChooseData> examList;
    private List<ChooseData> typeList;
    private ChooseData chooseData;//已选的试题
    private String testType = "考试";//默认考试
    private String classId = "";
    private Teacher teacher;

    @Override
    protected int getViewId() {
        return R.layout.activity_edit_test;
    }

    public void initWeight() {
        tvTopTitle.setText("添加考试练习");
        tvConfirm.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        classId = getIntent().getExtras().getString("classId");
        teacher = Application.getTeacherInfo();
        if ("".equals(classId) || null == teacher) {
            ToastUtil.show(mContext, "数据获取失败！");
            finish();
        }
        initWeight();
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.tv_test_type, R.id.btn_save, R.id.tv_exam_name, R.id.tv_test_begin, R.id.tv_test_end, R.id.civ_test_logo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exam_name://测试和练习
                chooseExams();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                saveExam();
                break;
            case R.id.tv_test_type:
                chooseType();
                break;
            case R.id.tv_test_begin:
                ChooseBegin();
                break;
            case R.id.tv_test_end:
                ChooseEnd();
                break;
            case R.id.civ_test_logo:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_save:
                saveExam();
                break;
        }
    }

    public void ChooseBegin() {
        final DatePicker datePicker = new DatePicker(this);
        Dialog dialog = DialogUtil.getViewDialog(this, new String[]{"开始时间", null, "确定", "取消", null}, datePicker, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    tvTestBegin.setText(datePicker.getDate());
                }
            }
        });
        dialog.show();
    }

    public void ChooseEnd() {
        final DatePicker datePicker = new DatePicker(this);
        Dialog dialog = DialogUtil.getViewDialog(this, new String[]{"结束时间", null, "确定", "取消", null}, datePicker, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    tvTestEnd.setText(datePicker.getDate());
                }
            }
        });
        dialog.show();
    }

    /**
     * 选择类别
     */
    public void chooseType() {
        if (null == typeList) {
            typeList = new ArrayList<>();
            String types[] = {"考试", "练习"};
            for (int i = 0; i < types.length; i++) {
                typeList.add(new ChooseData(types[i], types[i], false));
            }
        }

        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) typeList)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            testType = selectedList.get(0).getChooseDate();
                            tvTestType.setText(testType);
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("试题内容")
                .build()
                .show(mBottom);
    }

    /**
     * 选择试题
     */
    public void chooseExams() {
        if (null == examList) {
            examList = new ArrayList<>();
            JsonEntity entity = TeaDbUtils.examList(teacher.getId(), classId);
            if (entity.getCode() == 0) {
                List<Exam> list = (List<Exam>) entity.getData();
                for (Exam exam : list) {
                    examList.add(new ChooseData(exam.getName(), exam.getId(), false));
                }
            } else {
                return;
            }
        }
        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) examList)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            chooseData = selectedList.get(0);
                            tvExamName.setText(chooseData.getShowText());
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("试题类别")
                .build()
                .show(mBottom);
    }


    public void saveExam() {
        Test test = new Test();
        if (!"".equals(testLogo)) {
            test.setImg(testLogo);
        } else {
            ToastUtil.show(mContext, "考试练习logo不为空！");
            return;
        }
        if (!"".equals(etTestName.getText().toString())) {
            test.setName(etTestName.getText().toString());
        } else {
            ToastUtil.show(mContext, "考试练习名称不为空！");
            return;
        }
        if (null != chooseData) {
            test.setExamId(chooseData.getChooseDate());
            test.setExamName(chooseData.getShowText());
        } else {
            ToastUtil.show(mContext, "练习试题不为空！");
            return;
        }
        if (!"".equals(tvTestBegin.getText().toString())) {
            test.setBeginTime(tvTestBegin.getText().toString());
        } else {
            ToastUtil.show(mContext, "考试练习开始时间不为空！");
            return;
        }
        if (!"".equals(tvTestEnd.getText().toString())) {
            test.setEndTime(tvTestEnd.getText().toString());
        } else {
            ToastUtil.show(mContext, "考试练习结束时间不为空！");
            return;
        }
        test.setType(testType);
        test.setTeacherId(teacher.getId());
        test.setTeacherName(teacher.getUserName());
        test.setClassId(classId);
        JsonEntity entity = TeaDbUtils.addTest(test);
        if (entity.getCode() == 0) {
            ToastUtil.show(mContext, "添加练习成功！");
            finish();
        } else {
            ToastUtil.show(mContext, entity.getMsg());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size() > 0) {
                testLogo = images.get(0).path;
                Glide.with(mContext).load(testLogo).into(civTestLogo);
            }
        }
    }



}
