package bdkj.com.englishstu.view

import android.os.Bundle
import android.widget.Button
import bdkj.com.englishstu.R
import bdkj.com.englishstu.base.baseView.BaseActivity
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils
import bdkj.com.englishstu.common.tool.IntentUtil
import bdkj.com.englishstu.common.tool.ToastUtil

class LoginActivity : BaseActivity() {
    override fun getViewId(): Int {
        return R.layout.activity_login
    }

    override fun initViews(savedInstanceState: Bundle?) {
//        AdmDbUtils.adminInsert()
        var result = AdmDbUtils.adminLogin("superadmin", "superadmin")
        ToastUtil.show(mContext, result.msg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val login = findViewById(R.id.btnLogin) as Button
        login.setOnClickListener {
            IntentUtil.launcher(mContext, AdmMainActivity::class.java)
            finish()
        }

    }
}
