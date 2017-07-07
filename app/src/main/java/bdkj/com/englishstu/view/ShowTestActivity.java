package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class ShowTestActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.mBottom)
    View mBottom;
    @BindView(R.id.civ_test_logo)
    ImageView civTestLogo;
    @BindView(R.id.et_test_name)
    TextView etTestName;
    @BindView(R.id.tv_test_type)
    TextView tvTestType;
    @BindView(R.id.tv_exam_name)
    TextView tvExamName;
    @BindView(R.id.tv_test_begin)
    TextView tvTestBegin;
    @BindView(R.id.tv_test_end)
    TextView tvTestEnd;

    private Test test;

    @Override
    protected int getViewId() {
        return R.layout.activity_show_test;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("考试练习详情");
        test = (Test) getIntent().getSerializableExtra("test");
        if (null == test) {
            ToastUtil.show(mContext, "数据获取失败！");
            finish();
        }
        initData();
    }

    public void initData() {
        Glide.with(mContext).load(test.getImg()).into(civTestLogo);
        etTestName.setText(test.getName());
        tvTestType.setText(test.getType());
        tvExamName.setText(test.getExamName());
        tvTestBegin.setText(test.getBeginTime());
        tvTestEnd.setText(test.getEndTime());
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
