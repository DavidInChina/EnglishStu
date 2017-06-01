package bdkj.com.englishstu.common.tool;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

/**
 * The type Storage utils.
 * <p>
 * Description: 外置设备工具类
 *
 * @author: chenwei
 * @version: V1.0
 * Date: 16 /7/18 下午4:41
 */
public class StorageUtils
    {

    private static final int AVALIABLE_EXTERNAL_MEMORY_SIZE = 50 * 1024 * 1024; // 50MB

    private StorageUtils () {
        throw new AssertionError();
    }

    /**
     * 判断储存设备是否挂载
     *
     * @return boolean
     */
    public static boolean isMount() {

        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取储存设备路径
     *
     * @return storage directory
     */
    public static String getStorageDirectory() {

        File file = getStorageFile();
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取储存设备路径
     *
     * @return storage file
     */
    public static File getStorageFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取储存设备的总大小
     *
     * @return storage volume
     */
    @SuppressWarnings("deprecation")
    public static long getStorageVolume() {
        File file = getStorageFile();
        StatFs sFs = new StatFs(file.getPath());
        long blockSize = sFs.getBlockSize();
        int total = sFs.getBlockCount();
        long size = total * blockSize;
        return size;
    }

    /**
     * 获取储存器总大小并格式化输出
     *
     * @param context the context
     * @return the storage volume format
     */
    public static String getStorageVolumeFormat(Context context) {
        long size = getStorageVolume();
        return formatSize(context, size);
    }

    /**
     * 获取储存器的可用空间大小
     *
     * @return usable volumn
     */
    @SuppressWarnings("deprecation")
    public static long getUsableVolumn() {
        File file = getStorageFile();
        StatFs sFs = new StatFs(file.getPath());
        long blockSize = sFs.getBlockSize();
        int avialable_blocks = sFs.getAvailableBlocks();
        long avialable = avialable_blocks * blockSize;
        return avialable;
    }

    /**
     * 获取储存器可用空间并格式化输出
     *
     * @param context the context
     * @return the usable volumn format
     */
    public static String getUsableVolumnFormat(Context context) {
        long size = getUsableVolumn();
        return formatSize(context, size);
    }

    /**
     * 剩余空间能否能否保存文件
     *
     * @param dataSize the data size
     * @return boolean
     */
    public static boolean canSave(long dataSize) {
        File file = getStorageFile();
        StatFs sFs = new StatFs(file.getPath());
        long blockSize = sFs.getBlockSize();
        int total = sFs.getBlockCount();
        long size = total * blockSize;
        int avialable_blocks = sFs.getAvailableBlocks();
        long avialable = avialable_blocks * blockSize;
        return size - avialable - AVALIABLE_EXTERNAL_MEMORY_SIZE > dataSize;
    }

    /**
     * Judge whether external memory is full
     *
     * @return boolean
     */
    public static boolean isExternalMemoryFull() {
        return getUsableVolumn() - AVALIABLE_EXTERNAL_MEMORY_SIZE < 0;
    }

    /**
     * 将size格式化成储存格式输出，即M/G/kb等
     *
     * @param context the context
     * @param size    the size
     * @return the string
     */
    public static String formatSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }
}
