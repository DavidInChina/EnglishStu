package bdkj.com.englishstu.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.eventbus.ClassChoose;
import bdkj.com.englishstu.common.eventbus.RefreshStudent;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.selector.ChooseData;
import bdkj.com.englishstu.view.Fragment.StuExamFragment;
import bdkj.com.englishstu.view.Fragment.StuMarkFragment;
import bdkj.com.englishstu.view.Fragment.StuNoticeFragment;
import bdkj.com.englishstu.view.Fragment.StuSettingFragment;
import bdkj.com.englishstu.view.Fragment.StuTestFragment;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class StuMainActivity extends BaseActivity {
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
    @BindView(R.id.rb_test)
    RadioButton rbTest;
    @BindView(R.id.rb_menu_left)
    RadioGroup rgMenuLeft;
    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.iv_search_notice)
    ImageView ivSearchNotice;
    @BindView(R.id.mBottom)
    View mButton;
    @BindView(R.id.rb_menu_top)
    RadioGroup rbTop;
    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private Student student;

    private static final String TYPE_NOTICE = "notice";
    private static final String TYPE_TEST = "test";
    private static final String TYPE_EXAM = "exam";
    private static final String TYPE_MARK = "mark";
    private static final String TYPE_SETTING = "setting";
    private String currentType = TYPE_SETTING;//设置为不同第一选项，可以首次切换
    private long lastTime = 0;
    private String currentClassId = "";//从数据库获取到的教师所在班级的id
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    EventBus.getDefault().post(new ClassChoose(currentClassId));//更新fragment数据
                    break;
                default:
                    break;
            }
        }
    };

    public interface ChangeMarkTypeListener {
        void changeType(String type);
    }

    private ChangeMarkTypeListener listener;
    private List<ChooseData> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_stu_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initData();
        initLeftMenu();
        initTopMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void initTopMenu() {
        rbTop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_test:
                        Logger.d("练习");
                        if (null != listener) {
                            listener.changeType("练习");
                        }
                        break;
                    case R.id.rb_exam:
                        Logger.d("考试");
                        if (null != listener) {
                            listener.changeType("考试");
                        }
                        break;
                }
            }
        });
    }

    public void onEventMainThread(RefreshStudent refreshStudent) {
        initData();//更新信息
    }

    @SuppressLint("SetTextI18n")
    public void initData() {
        student = Application.getStudentInfo();
        if (null == student) {
            ToastUtil.show(mContext, "获取用户信息失败！");
            finish();
        }
        Glide.with(mContext).load(student.getUserHead()).into(ivTopHead);
        tvUserName.setText("姓名：" + student.getUserName());
        tvUserNumber.setText("编号：" + student.getNumber());
    }

    /**
     * 初始化左侧菜单
     */
    public void initLeftMenu() {
        rb1.setText("班级公告");
        rb2.setText("练 习");
        rb3.setText("考 试");
        rb4.setText("成绩查看");
        rb5.setText("个人设置");
        rgMenuLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        toggleFragment(TYPE_NOTICE);
                        tvTopTitle.setText("班级公告");
                        tvTopTitle.setVisibility(View.VISIBLE);
                        rbTop.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_2:
                        toggleFragment(TYPE_TEST);
                        tvTopTitle.setText("练习");
                        tvTopTitle.setVisibility(View.VISIBLE);
                        rbTop.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_3:
                        toggleFragment(TYPE_EXAM);
                        tvTopTitle.setText("考试");
                        tvTopTitle.setVisibility(View.VISIBLE);
                        rbTop.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_4:
                        toggleFragment(TYPE_MARK);
                        tvTopTitle.setVisibility(View.INVISIBLE);
                        rbTop.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_5:
                        toggleFragment(TYPE_SETTING);
                        tvTopTitle.setText("个人设置");
                        tvTopTitle.setVisibility(View.VISIBLE);
                        rbTop.setVisibility(View.INVISIBLE);
                        break;
                    default:
                }
            }
        });
        rb1.setChecked(true);//默认初始化内容
//        rbTest.setChecked(true);//默认选项
        toggleFragment(TYPE_NOTICE);
    }

    public void toggleFragment(String tag) {
        if (!currentType.equals(tag)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragmentMap.containsKey(tag)) {
                switch (tag) {
                    case TYPE_NOTICE:
                        fragmentMap.put(TYPE_NOTICE, new StuNoticeFragment());
                        break;
                    case TYPE_TEST:
                        fragmentMap.put(TYPE_TEST, new StuTestFragment());
                        break;
                    case TYPE_EXAM:
                        fragmentMap.put(TYPE_EXAM, new StuExamFragment());
                        break;
                    case TYPE_MARK:
                        StuMarkFragment fragment = new StuMarkFragment();
                        listener = fragment;
                        fragmentMap.put(TYPE_MARK, fragment);
                        break;
                    case TYPE_SETTING:
                        fragmentMap.put(TYPE_SETTING, new StuSettingFragment());
                        break;
                }
            }
            transaction.replace(R.id.fragmentView, fragmentMap.get(tag));
            currentType = tag;
            if (currentType.equals(TYPE_SETTING)) {
                ivSearchNotice.setVisibility(View.INVISIBLE);
            } else {
                ivSearchNotice.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.iv_search_notice, R.id.tv_top_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search_notice:
                switch (currentType) {
                    case TYPE_NOTICE:
                        break;
                    case TYPE_TEST:
                        break;
                    case TYPE_EXAM:
                        break;
                    case TYPE_MARK:
                        break;
                }
                break;
        }
    }
}
