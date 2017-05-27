package bdkj.com.englishstu.mvp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

import bdkj.com.englishstu.R

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login = findViewById(R.id.btnLogin) as Button
        login.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
