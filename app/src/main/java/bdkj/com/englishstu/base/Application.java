package bdkj.com.englishstu.base;


import android.content.Context;


import org.greenrobot.greendao.database.Database;

import bdkj.com.englishstu.common.beans.DaoMaster;
import bdkj.com.englishstu.common.beans.DaoSession;

public class Application
        extends android.app.Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "english-db-encrypted" : "english-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;

    private static DaoSession daoSession;


    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getmContext() {
        return mContext;
    }


}
