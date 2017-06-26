package bdkj.com.englishstu.view;

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
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.selector.ChooseData;
import bdkj.com.englishstu.selector.SelectPopWindow;
import butterknife.BindView;
import butterknife.OnClick;

public class EditExamActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.civ_exam_logo)
    ImageView civExamLogo;
    @BindView(R.id.et_exam_name)
    EditText etExamName;
    @BindView(R.id.tv_exam_hard)
    TextView tvExamHard;
    @BindView(R.id.tv_exam_status)
    TextView tvExamStatus;
    @BindView(R.id.tv_exam_about)
    TextView tvExamAbout;
    @BindView(R.id.et_word_1)
    EditText etWord1;
    @BindView(R.id.et_word_2)
    EditText etWord2;
    @BindView(R.id.et_word_3)
    EditText etWord3;
    @BindView(R.id.et_sentence)
    EditText etSentence;
    @BindView(R.id.mBottom)
    View mBottom;


    private String examLogo = "";
    private String level = "简单";//试题难度
    private String type = "测试和练习";//练习或者考试
    private String about = "校园生活";//试题内容类别
    private String words = "";//三个单词
    private String sentence = "";//句子
    private List<ChooseData> typeList;
    private List<ChooseData> levelList;
    private List<ChooseData> aboutList;
    private String classId = "";
    private Teacher teacher;

    @Override
    protected int getViewId() {
        return R.layout.activity_edit_exam;
    }

    public void initWeight() {
        tvTopTitle.setText("添加试题");
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

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.civ_exam_logo, R.id.btn_save, R.id.tv_exam_status, R.id.tv_exam_hard, R.id.tv_exam_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exam_status://测试和练习
                chooseType();
                break;
            case R.id.tv_exam_hard:
                chooseHard();
                break;
            case R.id.tv_exam_about:
                chooseAbout();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                saveExam();
                break;
            case R.id.civ_exam_logo:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_save:
                saveExam();
                break;
        }
    }

    /**
     * 选择类别
     */
    public void chooseType() {
        if (null == typeList) {
            typeList = new ArrayList<>();
            String types[] = {"练习", "测试", "练习和测试"};
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
                            type = selectedList.get(0).getChooseDate();
                            tvExamStatus.setText(type);
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("试题类别")
                .build()
                .show(mBottom);
    }

    /**
     * 选择难度level
     */
    public void chooseHard() {
        if (null == levelList) {
            levelList = new ArrayList<>();
            String types[] = {"简单", "中等", "困难"};
            for (int i = 0; i < types.length; i++) {
                levelList.add(new ChooseData(types[i], types[i], false));
            }
        }

        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) levelList)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            level = selectedList.get(0).getChooseDate();
                            tvExamHard.setText(level);
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("试题难度")
                .build()
                .show(mBottom);
    }

    /**
     * 选择内容
     */
    public void chooseAbout() {
        if (null == aboutList) {
            aboutList = new ArrayList<>();
            String types[] = {"校园生活", "人物风景", "科学技术", "历史典故"};
            for (int i = 0; i < types.length; i++) {
                aboutList.add(new ChooseData(types[i], types[i], false));
            }
        }

        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) aboutList)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            about = selectedList.get(0).getChooseDate();
                            tvExamAbout.setText(about);
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("试题内容")
                .build()
                .show(mBottom);
    }

    public void saveExam() {
        Exam exam = new Exam();
        if (!"".equals(examLogo)) {
            exam.setLogo(examLogo);
        } else {
            ToastUtil.show(mContext, "试题logo不为空！");
            return;
        }
        if (!"".equals(etExamName.getText().toString())) {
            exam.setName(etExamName.getText().toString());
        } else {
            ToastUtil.show(mContext, "试题名称不为空！");
            return;
        }

        if (!"".equals(etWord1.getText().toString())) {
            words += etWord1.getText().toString();
        } else {
            ToastUtil.show(mContext, "第一个单词不为空！");
            return;
        }
        if (!"".equals(etWord2.getText().toString())) {
            words += "," + etWord2.getText().toString();
        } else {
            ToastUtil.show(mContext, "第二个单词不为空！");
            return;
        }
        if (!"".equals(etWord3.getText().toString())) {
            words += "," + etWord3.getText().toString();
        } else {
            ToastUtil.show(mContext, "第三个单词不为空！");
            return;
        }
        exam.setWords(words);
        if (!"".equals(etSentence.getText().toString())) {
            sentence = etSentence.getText().toString();
        } else {
            ToastUtil.show(mContext, "句子朗读不为空！");
            return;
        }
        exam.setSentence(sentence);
        exam.setType(type);
        exam.setAbout(about);
        exam.setLevel(level);
        exam.setTeacherId(teacher.getId());
        exam.setClassId(classId);
        JsonEntity entity = TeaDbUtils.addExam(exam);
        if (entity.getCode() == 0) {
            ToastUtil.show(mContext, "添加试题成功！");
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
                examLogo = images.get(0).path;
                Glide.with(mContext).load(examLogo).into(civExamLogo);
            }
        }
    }


}
