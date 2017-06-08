package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class EditNoticeActivity extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.btnLogin)
    Button btnConfirm;

    @Override
    protected int getViewId() {
        return R.layout.activity_edit_notice;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWeight();
    }

    public void initWeight() {
        tvTopTitle.setText("发布通知");
        tvConfirm.setText("保存");
        btnConfirm.setText("保存");
        tvConfirm.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.btnLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                break;
            case R.id.btnLogin:
                break;
        }
    }
}
