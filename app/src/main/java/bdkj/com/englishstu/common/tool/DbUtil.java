package bdkj.com.englishstu.common.tool;

import android.content.Context;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import bdkj.com.englishstu.base.Constants;

/**
 * author:davidinchina on 2017/7/21 09:34
 * email:davicdinchina@gmail.com
 * tip:
 */
public class DbUtil {
    public static void copyDb(Context mContext, String dbName, String copyName) {
        File dbFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/"
                + mContext.getPackageName() + "/databases/" + dbName + ".db");
        Logger.e(dbFile.length() + "数据库大小");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //文件复制到sd卡中
            fis = new FileInputStream(dbFile);
            String root = Environment.getExternalStorageDirectory().getPath() + "/"
                    + Constants.ROOT_DIRECTORY;
            FileUtil.makeDirs(root);//生成文件夹
            String DB_FILE_PATH = root + "/Dbs/" + copyName + ".db";
            File resultFile = new File(DB_FILE_PATH);
            fos = new FileOutputStream(resultFile);
            int len = 0;
            byte[] buffer = new byte[2048];
            while (-1 != (len = fis.read(buffer))) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            Logger.e("复制成功！");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("复制失败！");
        } finally {
            //关闭数据流
            try {
                if (fos != null) fos.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
