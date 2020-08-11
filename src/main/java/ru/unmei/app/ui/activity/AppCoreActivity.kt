package ru.unmei.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_app_core.*
import ru.unmei.app.R
import ru.unmei.app.api.API
import ru.unmei.app.ui.fragment.AccountFragment
import ru.unmei.app.ui.fragment.NewsFragment
import ru.unmei.app.ui.fragment.NovelsFragment
import ru.unmei.app.ui.fragment.SettingsFragment

class AppCoreActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_core)
        API.initApi()

        supportFragmentManager.beginTransaction().replace(R.id.app_fragment_container, NewsFragment()).commit()
        bottom_navigation.setOnNavigationItemSelectedListener {menuItem ->
            var fg: Fragment? = null
            when(menuItem.itemId) {
                R.id.item_main -> fg = NewsFragment()
                R.id.item_novel -> fg = NovelsFragment()
                R.id.item_account -> fg = AccountFragment()
                R.id.item_settings -> fg = SettingsFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.app_fragment_container, fg!!).commit()
            return@setOnNavigationItemSelectedListener true
        }
    }
}