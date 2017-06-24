package bdkj.com.englishstu.view.Fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.CircleImageView;
import bdkj.com.englishstu.view.LoginActivity;
import bdkj.com.englishstu.view.TeaInfoModifyActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeSettingFragment extends BaseFragment {
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

    @OnClick({R.id.btn_quit, R.id.tv_modify_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_quit:
                IntentUtil.launcher(mContext, LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.tv_modify_info:
                IntentUtil.launcher(mContext, TeaInfoModifyActivity.class);
                break;
        }
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_setting;
    }

    @Override
    public void initView(ViewGroup parent) {
        civUserHead.setBorderColor(R.color.white);
        civUserHead.setBorderWidth(2);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();//更新信息
    }

    public void initData() {
        Teacher teacher = Application.getTeacherInfo();
        if (null == teacher) {
            ToastUtil.show(mContext, "数据错误！");
            getActivity().finish();
        }
        if (null!=teacher.getUserHead()&&!"".equals(teacher.getUserHead())){
            Glide.with(mContext).load(teacher.getUserHead()).into(civUserHead);
        }
        tvSettingName.setText("姓名：" + teacher.getUserName());
        tvUserNumber.setText("编号：" + teacher.getNumber());
        tvUserAccount.setText(teacher.getUserAccount());
        tvUserPhone.setText(teacher.getPhone());
        tvUserEmail.setText(teacher.getEmail());
    }


}
