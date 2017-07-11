package bdkj.com.englishstu.base.baseView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.common.data.PermissionData;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.ButterKnife;
import icepick.Icepick;
import me.drakeet.materialdialog.MaterialDialog;

public abstract class BaseActivity extends FragmentActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        mContext = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(getViewId());
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        initViews(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void setStatusBar() {
        Window window = getWindow();
//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏颜色
        window.setStatusBarColor(getResources().getColor(R.color.black));
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }

    /**
     * 使得权限被允许
     *
     * @param permissionCode 请求码,两位数字,十位是单页面请求次序,个位是权限在数组下标
     * @return
     */
    public void checkPermissionEnable(int permissionCode) {
        String permissionName = PermissionData.PERMISSION_NAME[permissionCode % 10];
        String permission = PermissionData.PERMISSION[permissionCode % 10];
        PackageManager pm = mContext.getPackageManager();
        boolean enable = false;
        if (permission.contains(",")) {//同时申请两个权限,包括读写存储卡和电话监听
            String permissions[] = permission.split(",");
            enable = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(permissions[0], mContext.getPackageName())) &&
                    (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission(permissions[1], mContext.getPackageName()));
        } else {
            enable = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(permission, mContext.getPackageName()));
        }
        if (enable) {//拥有这个权限,就可以执行下一步
            getPermissionEnabled(permissionCode);
        } else {//没有这个权限,需要申请,申请成功就执行,否则不执行
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0系统的动态权限申请
                requestPermission(permissionCode, permission);
            } else {//6.0以下的权限,提示
                showConfirmDialog(permissionName, permissionCode);
            }
        }
    }

    /**
     * 6.0系统请求权限
     *
     * @param permissionCode
     * @param permission
     */
    public void requestPermission(int permissionCode, String permission) {
        if (permission.contains(",")) {//同时申请两个权限,包括读写存储卡和电话监听
            String permissions[] = permission.split(",");
            AndPermission.with(this).requestCode(permissionCode)
                    .permission(permissions[0], permissions[1])
                    .send();
        } else {
            AndPermission.with(this)
                    .requestCode(permissionCode)
                    .permission(permission)
                    .send();
        }

    }

    /**
     * 6.0动态权限的回调
     */
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            getPermissionEnabled(requestCode);
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission((Activity) mContext, deniedPermissions)) {
//                 第二种：用自定义的提示语。
                AndPermission.defaultSettingDialog((Activity) mContext, requestCode)
                        .setTitle("权限申请失败")
                        .setMessage("应用需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            } else {
                ToastUtil.show(mContext, "权限取消,操作终止!");
            }
        }
    };

    /**
     * 接收权限请求的回调,并且让上面的监听处理回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    public void showConfirmDialog(String permissionName, final int permissionCode) {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.setTitle("温馨提示").setMessage("敏感权限 " + permissionName +
                " 未被系统允许,需要移步应用管理设置此权限,当前继续运行可能有未知错误。");
        dialog.setPositiveButton("运行", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionEnabled(permissionCode);
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mContext, "操作已取消!");
                dialog.dismiss();
            }
        }).setCanceledOnTouchOutside(false).show();
    }

    /**
     * 权限检查的回调,根据权限code进行结果分发处理
     *
     * @param permissionCode
     */
    public void getPermissionEnabled(int permissionCode) {
    }

    /**
     * 以下两个方法用来控制自动收起键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    abstract protected int getViewId();

    abstract protected void initViews(Bundle savedInstanceState);
}
