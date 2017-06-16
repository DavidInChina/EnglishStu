package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.beans.Admin;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.CircleImageView;
import bdkj.com.englishstu.view.InfoModifyActivity;
import bdkj.com.englishstu.view.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdSettingFragment extends BaseFragment {
    @BindView(R.id.civ_user_head)
    CircleImageView civUserHead;
    @BindView(R.id.tv_setting_name)
    TextView tvSettingName;
    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.tv_user_account)
    TextView tvUserAccount;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    Unbinder unbinder;

    @OnClick({R.id.btn_quit, R.id.tv_modify_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_quit:
                IntentUtil.launcher(mContext, LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.tv_modify_info:
                IntentUtil.launcher(mContext, InfoModifyActivity.class);
                break;
        }
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_setting;
    }

    @Override
    public void initView(ViewGroup parent) {
        initData();
    }

    public void initData() {
        Admin admin = Application.getAdminInfo();
        if (null == admin) {
            ToastUtil.show(mContext, "数据错误！");
            getActivity().finish();
        }
        civUserHead.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
        tvSettingName.setText("姓名：" + admin.getUserName());
        tvUserNumber.setText("编号：" + admin.getUserAccount());
        tvUserAccount.setText(admin.getUserAccount());
        tvUserPhone.setText(admin.getPhone());
        tvUserEmail.setText(admin.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
