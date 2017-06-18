package bdkj.com.englishstu.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import bdkj.com.englishstu.R
import bdkj.com.englishstu.R.layout.activity_login
import bdkj.com.englishstu.base.Application
import bdkj.com.englishstu.common.beans.Admin
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils
import bdkj.com.englishstu.common.tool.IntentUtil
import bdkj.com.englishstu.common.tool.ToastUtil
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import icepick.Icepick

class LoginActivity : FragmentActivity() {
    @BindView(R.id.btnLogin)
    lateinit var btnLogin: Button
    @BindView(R.id.et_login_account)
    lateinit var etLoginAccount: EditText
    @BindView(R.id.et_login_password)
    lateinit var etLoginPassword: EditText
    @BindView(R.id.rl_left)
    lateinit var reLeft: RelativeLayout
    @BindView(R.id.tv_forget_ps)
    lateinit var tvForgetPs: TextView
    lateinit var mContext: Context

    lateinit var account: String
    lateinit var password: String
    var identify: Int = 0//0管理员，1老师，2学生，默认管理员

    @OnClick(R.id.btnLogin)
    internal fun login() {
        account = etLoginAccount.text.toString()
        password = etLoginPassword.text.toString()
        if ("" != account) {
            if (password.length in 6..10) {
                when (identify) {
                    0 -> {
                        val result = AdmDbUtils.adminLogin(account, password)
                        ToastUtil.show(mContext, result.msg)
                        if (result.code == 0) {
                            if (Application.setAdminInfo(mContext, (result.data as Admin?))) {
                                IntentUtil.launcher(mContext, AdmMainActivity::class.java)
                                finish()
                            }
                        }
                    }
                    1 -> {
                        val result = AdmDbUtils.adminLogin(account, password)
                        ToastUtil.show(mContext, result.msg)
                        if (result.code == 0) {
                            IntentUtil.launcher(mContext, TeaMainActivity::class.java)
                            finish()
                        }
                    }
                    2 -> {
                        val result = AdmDbUtils.adminLogin(account, password)
                        ToastUtil.show(mContext, result.msg)
                        if (result.code == 0) {
                            IntentUtil.launcher(mContext, StuMainActivity::class.java)
                            finish()
                        }
                    }
                }

            } else {
                ToastUtil.show(mContext, "密码长度不合法！")
            }
        } else {
            ToastUtil.show(mContext, "账号长度不合法！")
        }
    }

    @OnClick(R.id.rl_left)
    internal fun chooseIdentify() {
        ToastUtil.show(mContext, "选择身份")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)
        ButterKnife.bind(this)
        mContext = this
        initViews()
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    fun initViews() {
        var admin = Application.getAdminInfo()
        if (null != admin) {
            ToastUtil.show(mContext, admin.userAccount)
            etLoginAccount.setText(admin.userAccount)
            etLoginPassword.setText(admin.userPassword)//这里关于密码加密的问题
            etLoginAccount.setSelection(admin.userAccount.length)
        }
        AdmDbUtils.adminInsert()
    }

    /**
     * 以下两个方法用来控制自动收起键盘

     * @param ev
     * *
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (window.superDispatchTouchEvent(ev)) {
            return true
        }
        return onTouchEvent(ev)
    }

    fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

}

