package ru.unmei.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ru.unmei.app.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            run {
                val intent = Intent(this, AppCoreActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}