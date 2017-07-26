package bdkj.com.englishstu.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.idescout.sql.SqlScoutServer;
import com.orhanobut.logger.Logger;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.DbUtil;
import bdkj.com.englishstu.common.tool.DialogUtil;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.NetUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;

public class WelcomeActivity extends BaseActivity {


    public static final int REQUEST_PERMISSION = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

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
        SharedPreferences sharedPreferences = getSharedPreferences("count", MODE_PRIVATE);
        int count = sharedPreferences.getInt("count", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", ++count);//将使用次数添加到本地文件
        Logger.d(count);
        editor.commit();
        if (1 == count) {
            //是第一次使用应用
            AdmDbUtils.adminInsert();
            Logger.d("添加使用次数");
        }
        if (NetUtil.isNetworkConnected(mContext)) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && checkPermission(Manifest.permission.CAMERA)
                        && checkPermission(Manifest.permission.READ_PHONE_STATE)
                        && checkPermission(Manifest.permission.RECORD_AUDIO)
                        && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        ) {
                    beginLogin();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    , Manifest.permission.CAMERA
                                    , Manifest.permission.READ_PHONE_STATE
                                    , Manifest.permission.RECORD_AUDIO
                                    , Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
                }
            } else {
                beginLogin();
            }
        } else {
            String arrays[] = {"提示", "网络未连接，应用不可用！", "退出", "", ""};
            DialogUtil.getTextDialog(mContext, arrays, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case  DialogInterface.BUTTON_POSITIVE:
                            finish();
                            break;
                    }
                }
            }).show();
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            boolean permission = true;
            for (int i : grantResults
                    ) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    permission = false;
                }
            }
            if (permission) {
                beginLogin();
            } else {
                ToastUtil.show(mContext, "权限被禁止，无法使用应用！");
//                finish();
            }
        }
    }

    public void beginLogin() {
        SqlScoutServer.create(this, getPackageName());
        DbUtil.copyDb(mContext, "english-db", "english-db");
        handler.sendEmptyMessageDelayed(1, 1000);
    }

}
