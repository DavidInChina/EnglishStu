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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.eventbus.ClassChoose;
import bdkj.com.englishstu.common.eventbus.RefreshTeacher;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.selector.ChooseData;
import bdkj.com.englishstu.selector.SelectPopWindow;
import bdkj.com.englishstu.view.Fragment.TeExamFragment;
import bdkj.com.englishstu.view.Fragment.TeNoticeFragment;
import bdkj.com.englishstu.view.Fragment.TeSettingFragment;
import bdkj.com.englishstu.view.Fragment.TeaTestFragment;
import bdkj.com.englishstu.view.Fragment.TeaMarkFragment;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class TeaMainActivity extends BaseActivity {

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
    @BindView(R.id.mBottom)
    View mButton;
    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private Teacher teacher;

    private static final String TYPE_NOTICE = "notice";
    private static final String TYPE_TIKU = "tiku";
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
    private List<ChooseData> classList;

    @Override
    protected int getViewId() {
        return R.layout.activity_tea_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initLeftMenu();
        initData();
    }

    public String getClassId() {
        return currentClassId;
    }

      public void onEventMainThread(RefreshTeacher refreshTeacher) {
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("SetTextI18n")
    public void initData() {
        teacher = Application.getTeacherInfo();
        if (null == teacher) {
            ToastUtil.show(mContext, "获取用户信息失败！");
            finish();
        }
        Logger.d(teacher.getClassIds());
        if (!"".equals(teacher.getClassIds())) {
            String[] classes = teacher.getClassIds().split(",");
            currentClassId = classes[0].split(";")[0];
            tvTopTitle.setText(classes[0].split(";")[1]);
//            handler.sendEmptyMessageDelayed(1, 1000);//延时刷新数据，等待fragment加载完成
            Logger.d(currentClassId);
        }
        Glide.with(mContext).load(teacher.getUserHead()).into(ivTopHead);
        tvUserName.setText("姓名：" + teacher.getUserName());
        tvUserNumber.setText("编号：" + teacher.getNumber());
    }

    /**
     * 初始化左侧菜单
     */
    public void initLeftMenu() {
        rb1.setText("公告管理");
        rb2.setText("题库管理");
        rb3.setText("考试练习");
        rb4.setText("成绩管理");
        rb5.setText("个人设置");
        rgMenuLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        toggleFragment(TYPE_NOTICE);
                        break;
                    case R.id.rb_2:
                        toggleFragment(TYPE_TIKU);
                        break;
                    case R.id.rb_3:
                        toggleFragment(TYPE_EXAM);
                        break;
                    case R.id.rb_4:
                        toggleFragment(TYPE_MARK);
                        break;
                    case R.id.rb_5:
                        toggleFragment(TYPE_SETTING);
                        break;
                    default:
                }
            }
        });
        rb1.setChecked(true);//默认初始化内容
        toggleFragment(TYPE_NOTICE);
    }

    public void toggleFragment(String tag) {
        if (!currentType.equals(tag)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragmentMap.containsKey(tag)) {
                switch (tag) {
                    case TYPE_NOTICE:
                        TeNoticeFragment fragment = new TeNoticeFragment();
                        fragmentMap.put(TYPE_NOTICE, fragment);
                        break;
                    case TYPE_TIKU:
                        fragmentMap.put(TYPE_TIKU, new TeExamFragment());
                        break;
                    case TYPE_EXAM:
                        fragmentMap.put(TYPE_EXAM, new TeaTestFragment());
                        break;
                    case TYPE_MARK:
                        fragmentMap.put(TYPE_MARK, new TeaMarkFragment());
                        break;
                    case TYPE_SETTING:
                        fragmentMap.put(TYPE_SETTING, new TeSettingFragment());
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

    @OnClick({R.id.iv_search_notice, R.id.iv_edit_notice, R.id.tv_top_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search_notice:
                switch (currentType) {
                    case TYPE_NOTICE:
                        break;
                    case TYPE_TIKU:
                        break;
                    case TYPE_EXAM:
                        break;
                    case TYPE_MARK:
                        break;
                }
                break;
            case R.id.iv_edit_notice:
                Bundle bundle = new Bundle();
                switch (currentType) {
                    case TYPE_NOTICE:
                        bundle.putString("classId", currentClassId);
                        IntentUtil.launcher(mContext, TeaEditNoticeActivity.class, bundle);
                        break;
                    case TYPE_TIKU:
                        bundle.putString("classId", currentClassId);
                        IntentUtil.launcher(mContext, EditExamActivity.class, bundle);
                        break;
                    case TYPE_EXAM:
                        bundle.putString("classId", currentClassId);
                        IntentUtil.launcher(mContext, EditTestActivity.class, bundle);
                        break;
                    case TYPE_MARK:
                        break;
                }
                break;
            case R.id.tv_top_title:
                choseClass();
                break;
        }
    }

    public void choseClass() {
        if (null == classList) {
            classList = new ArrayList<>();//这里从后台取出老师所在班级信息
            String classIds[] = teacher.getClassIds().split(",");
            for (int i = 0; i < classIds.length; i++) {
                classList.add(new ChooseData(classIds[i].split(";")[1], classIds[i], false));
            }
        }

        new SelectPopWindow.Builder(this)
                .setNameArray((ArrayList<ChooseData>) classList)
                .setSIngleChoose(true)
                .setConfirmListener(new SelectPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onClick(ArrayList<ChooseData> selectedList) {
                        if (selectedList.size() > 0) {
                            tvTopTitle.setText(selectedList.get(0).getShowText());
                            currentClassId = selectedList.get(0).getChooseDate().split(";")[0];//获取的班级id
                            EventBus.getDefault().post(new ClassChoose(currentClassId));//更新fragment数据
                        }
                    }
                })
                .setCancel("取消")
                .setConfirm("完成")
                .setTitle("所在班级")
                .build()
                .show(mButton);
    }
}
