package bdkj.com.englishstu.base;


import android.content.Context;

import com.movebeans.lib.base.BaseApplication;
import com.movebeans.lib.net.ApiService;

import org.greenrobot.greendao.database.Database;

import bdkj.com.englishstu.common.beans.DaoMaster;
import bdkj.com.englishstu.common.beans.DaoSession;

public class Application
        extends BaseApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        initApi();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;

    private DaoSession daoSession;


    public DaoSession getDaoSession() {
        return daoSession;
    }
    public static Context getmContext() {
        return mContext;
    }

    public void initApi() {
        ApiService.init(Constants.URL);
    }

}
