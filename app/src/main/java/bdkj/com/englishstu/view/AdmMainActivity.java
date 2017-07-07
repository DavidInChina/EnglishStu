package bdkj.com.englishstu.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Admin;
import bdkj.com.englishstu.common.eventbus.RefreshAdmin;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.Fragment.AdClassFragment;
import bdkj.com.englishstu.view.Fragment.AdNoticeFragment;
import bdkj.com.englishstu.view.Fragment.AdSettingFragment;
import bdkj.com.englishstu.view.Fragment.AdStudentFragment;
import bdkj.com.englishstu.view.Fragment.AdTeacherFragment;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AdmMainActivity extends BaseActivity {
    @BindView(R.id.tv_text_top)
    TextView tvTextTop;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_head)
    ImageView ivTopHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;
    @BindView(R.id.rb_5)
    RadioButton rb5;
    @BindView(R.id.rb_menu_left)
    RadioGroup rgMenuLeft;
    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.iv_search_notice)
    ImageView ivSearchNotice;
    @BindView(R.id.iv_edit_notice)
    ImageView ivEditNotice;
    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private Admin admin;

    private static final String TYPE_NOTICE = "notice";
    private static final String TYPE_CLASS = "class";
    private static final String TYPE_TEACHER = "teacher";
    private static final String TYPE_STUDENT = "student";
    private static final String TYPE_SETTING = "setting";
    private String currentType = TYPE_SETTING;//设置为不同第一选项，可以首次切换
    private long lastTime = 0;

    @Override
    protected int getViewId() {
        return R.layout.activity_adm_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initLeftMenu();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RefreshAdmin refreshAdmin) {
        initData();//更新管理员信息
    }

    @SuppressLint("SetTextI18n")
    public void initData() {
        admin = Application.getAdminInfo();
        if (null == admin) {
            ToastUtil.show(mContext, "获取用户信息失败！");
            finish();
        }
        Glide.with(mContext).load(admin.getUserHead()).into(ivTopHead);
        tvUserName.setText("姓名：" + admin.getUserName());
        tvUserNumber.setText("编号：" + admin.getUserAccount());
    }

    /**
     * 初始化左侧菜单
     */
    public void initLeftMenu() {
        rb1.setText("公告管理");
        rb2.setText("班级管理");
        rb3.setText("教师管理");
        rb4.setText("学生管理");
        rb5.setText("个人设置");
        rgMenuLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        toggleFragment(TYPE_NOTICE);
                        tvTopTitle.setText("公告管理");
                        break;
                    case R.id.rb_2:
                        toggleFragment(TYPE_CLASS);
                        tvTopTitle.setText("班级管理");
                        break;
                    case R.id.rb_3:
                        toggleFragment(TYPE_TEACHER);
                        tvTopTitle.setText("教师管理");
                        break;
                    case R.id.rb_4:
                        toggleFragment(TYPE_STUDENT);
                        tvTopTitle.setText("学生管理");
                        break;
                    case R.id.rb_5:
                        toggleFragment(TYPE_SETTING);
                        tvTopTitle.setText("个人设置");
                        break;
                    default:
                }
            }
        });
        rb1.setChecked(true);//默认初始化内容
        toggleFragment(TYPE_NOTICE);
        tvTopTitle.setText("公告管理");
    }

    public void toggleFragment(String tag) {
        if (!currentType.equals(tag)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragmentMap.containsKey(tag)) {
                switch (tag) {
                    case TYPE_NOTICE:
                        fragmentMap.put(TYPE_NOTICE, new AdNoticeFragment());
                        break;
                    case TYPE_CLASS:
                        fragmentMap.put(TYPE_CLASS, new AdClassFragment());
                        break;
                    case TYPE_TEACHER:
                        fragmentMap.put(TYPE_TEACHER, new AdTeacherFragment());
                        break;
                    case TYPE_STUDENT:
                        fragmentMap.put(TYPE_STUDENT, new AdStudentFragment());
                        break;
                    case TYPE_SETTING:
                        fragmentMap.put(TYPE_SETTING, new AdSettingFragment());
                        break;
                }
            }
            transaction.replace(R.id.fragmentView, fragmentMap.get(tag));
            currentType = tag;
            if (currentType.equals(TYPE_SETTING)) {
                ivSearchNotice.setVisibility(View.INVISIBLE);
                ivEditNotice.setVisibility(View.INVISIBLE);
            } else {
                ivSearchNotice.setVisibility(View.VISIBLE);
                ivEditNotice.setVisibility(View.VISIBLE);
            }
            transaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTime > 2000) {
            ToastUtil.show(mContext, "再按一次退出应用");
            lastTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.iv_search_notice, R.id.iv_edit_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search_notice:
                switch (currentType) {
                    case TYPE_NOTICE:
                        break;
                    case TYPE_CLASS:
                        break;
                    case TYPE_TEACHER:
                        break;
                    case TYPE_STUDENT:
                        break;
                }
                break;
            case R.id.iv_edit_notice:
                switch (currentType) {
                    case TYPE_NOTICE:
                        IntentUtil.launcher(mContext, EditNoticeActivity.class);
                        break;
                    case TYPE_CLASS:
                        IntentUtil.launcher(mContext, EditClassActivity.class);
                        break;
                    case TYPE_TEACHER:
                        IntentUtil.launcher(mContext, EditTeacherActivity.class);
                        break;
                    case TYPE_STUDENT:
                        IntentUtil.launcher(mContext, EditStudentActivity.class);
                        break;
                }
                break;
        }
    }
}
