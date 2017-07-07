package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.et_word_1)
    TextView etWord1;
    @BindView(R.id.et_word_2)
    TextView etWord2;
    @BindView(R.id.et_word_3)
    TextView etWord3;
    @BindView(R.id.et_sentence)
    TextView etSentence;
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
        etSentence.setText(exam.getSentence());
        etWord1.setText(exam.getWords().split(",")[0]);
        etWord2.setText(exam.getWords().split(",")[1]);
        etWord3.setText(exam.getWords().split(",")[2]);
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
