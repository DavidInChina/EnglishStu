package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.view.Fragment.AdClassFragment;
import bdkj.com.englishstu.view.Fragment.AdNoticeFragment;
import bdkj.com.englishstu.view.Fragment.AdSettingFragment;
import bdkj.com.englishstu.view.Fragment.AdStudentFragment;
import bdkj.com.englishstu.view.Fragment.AdTeacherFragment;
import butterknife.BindView;
import butterknife.OnClick;

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
    private Map<String, Fragment> fragmentMap = new HashMap<>();

    private static final String TYPE_NOTICE = "notice";
    private static final String TYPE_CLASS = "class";
    private static final String TYPE_TEACHER = "teacher";
    private static final String TYPE_STUDENT = "student";
    private static final String TYPE_SETTING = "setting";
    private String currentType = TYPE_SETTING;//设置为不同第一选项，可以首次切换

    @Override
    protected int getViewId() {
        return R.layout.activity_adm_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initLeftMenu();
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
            transaction.commit();
        }

    }

    @OnClick({R.id.iv_search_notice, R.id.iv_edit_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search_notice:
                break;
            case R.id.iv_edit_notice:
                IntentUtil.launcher(mContext, EditNoticeActivity.class);
                break;
        }
    }
}
