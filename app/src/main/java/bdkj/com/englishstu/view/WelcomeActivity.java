package bdkj.com.englishstu.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.orhanobut.logger.Logger;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.IntentUtil;

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    IntentUtil.launcher(mContext, LoginActivity.class);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int getViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("count",MODE_PRIVATE);
        int count = sharedPreferences.getInt("count",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count",++count);//将使用次数添加到本地文件
        Logger.d(count);
        editor.commit();
        if (1==count){
            //是第一次使用应用
            AdmDbUtils.adminInsert();
            Logger.d("添加使用次数");
        }
        beginLogin();
    }

    public  void beginLogin(){
        handler.sendEmptyMessageDelayed(1, 1000);
    }

}
