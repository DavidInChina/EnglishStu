package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.view.Fragment.AdNoticeFragment;

public class AdmMainActivity extends BaseActivity {
    private Map<String, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_adm_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        fragmentMap.put("note", new AdNoticeFragment());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentView, fragmentMap.get("note"));
        transaction.commit();
    }
}
