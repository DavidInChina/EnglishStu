package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;

public class ShowExamActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.civ_exam_logo)
    ImageView civExamLogo;
    @BindView(R.id.et_exam_name)
    TextView etExamName;
    @BindView(R.id.tv_exam_hard)
    TextView tvExamHard;
    @BindView(R.id.tv_exam_status)
    TextView tvExamStatus;
    @BindView(R.id.tv_exam_about)
    TextView tvExamAbout;
    @BindView(R.id.tag_container_layout_word)
    TagContainerLayout tagContainerLayoutWord;
    @BindView(R.id.tag_container_layout_sentence)
    TagContainerLayout tagContainerLayoutSentence;

    @BindView(R.id.mBottom)
    View mBottom;

    private Exam exam;

    @Override
    protected int getViewId() {
        return R.layout.activity_show_exam;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("试题详情");
        exam = (Exam) getIntent().getSerializableExtra("exam");
        if (null == exam) {
            ToastUtil.show(mContext, "数据获取失败！");
            finish();
        }
        initData();
    }

    public void initData() {
        Glide.with(mContext).load(exam.getLogo()).into(civExamLogo);
        etExamName.setText(exam.getName());
        String words[] =  exam.getWords().split(",");
        List<String> wordList = new ArrayList<String>();
        Collections.addAll(wordList, words);
        String sentences[] =  exam.getSentence().split(",");
        Collections.addAll(wordList, sentences);
        tagContainerLayoutWord.setTags(wordList);
//        tagContainerLayoutSentence.setTags(sentenceList);
        tvExamAbout.setText(exam.getAbout());
        tvExamHard.setText(exam.getLevel());
        tvExamStatus.setText(exam.getType());
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
