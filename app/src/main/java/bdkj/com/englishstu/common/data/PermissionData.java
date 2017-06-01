package bdkj.com.englishstu.common.data;

/**
 * Created by davidinchina on 2017/1/16.
 * 敏感权限列表
 */

public class PermissionData {
    public static final String PERMISSION[] =
            {"android.permission.WRITE_EXTERNAL_STORAGE,android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO", "android.permission.CAMERA", "android.permission.READ_CONTACTS",
                    "android.permission.PROCESS_OUTGOING_CALLS,android.permission.READ_PHONE_STATE","android.permission.CAPTURE_AUDIO_OUTPUT"};
    //下标分别是0,1,2等,用来区分请求权限的不同,
    public static final String PERMISSION_NAME[] = {"存储卡读写", "音频录制", "系统相机", "通讯录读取", "通话状态监听","音频输出"};
}
