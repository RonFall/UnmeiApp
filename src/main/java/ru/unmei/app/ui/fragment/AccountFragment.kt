package ru.unmei.app.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.unmei.app.*
import ru.unmei.app.api.*
import java.net.HttpCookie
import java.net.URI

class AccountFragment : Fragment() {

    private lateinit var recView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        API.initApi()
        val view: View
        if (!getStringData(container!!.context).contains("token")) {
            view = inflater.inflate(R.layout.fragment_login, container, false)
            onLoginButtonClick(view)
            onWebButtonPress(view)
        } else {
            view = inflater.inflate(R.layout.fragment_account, container, false)
            getAccount(view)
            logoutBtn(view)
        }
        API.manager.cookieStore.add(
            URI("https://api.unmei.nix13.pw"),
            HttpCookie("token", getStringData(view.context).getString("token", ""))
        )
        return view
    }

    private fun onLoginButtonClick(view: View) {
        val login = view.findViewById<EditText>(R.id.login)
        val password = view.findViewById<EditText>(R.id.password)
        val btn = view.findViewById<Button>(R.id.loginButton)

        btn.setOnClickListener {
            API.loginService.login(Login(login.text.toString(), password.text.toString())).enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        val body = API.getBodyFromResponse(response, UserResponse::class.java)

                        if (body!!.error) textToastShort(R.string.log_screen_failure, view.context)
                        else {
                            val token = API.getCookie(API.apiUrl, "token")
                            if (token == null) {
                                textToastShort("Ошибка", view.context)
                                return
                            }
                            putStringData("token", token.value, view.context)
                            fragmentManager!!.beginTransaction().replace(R.id.app_fragment_container, AccountFragment()).commit()
                            println("User added in database!")
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        if (getStringData(view.context).contains("token")) {
            println("User logged in app!")
        }
    }

    private fun onWebButtonPress(view: View) {
        //REGISTER
        onWebButtonClick(R.id.registerButton, "https://unmei.nix13.pw/", view)

        //FORGOT PASSWORD
        onWebButtonClick(R.id.forgot_pass, "https://unmei.nix13.pw/restore", view)

        //SOCIAL
        onWebButtonClick(R.id.vk_link, "https://vk.com/unmei_site", view)
        onWebButtonClick(R.id.discord_link, "https://discord.gg/4CA8Cju", view)
        onWebButtonClick(R.id.telegram_link, "https://t.me/unmei_site", view)
    }

    private fun getAccount(view: View) {
        val prg = setProgressBar(view)
        prg.show()

        API.responseAllData(UserResponse::class.java, API.accountService.getMe()) { user ->
            if (user.error) {
                println(API.getCookie(API.apiUrl, "token"))
                textToastShort("Ошибка загрузки аккаунта", view.context)
            } else {
                prg.dismiss()
                val userAvatar = view.findViewById<ImageView>(R.id.user_avatar)
                val username = view.findViewById<TextView>(R.id.username)
                val userGroup = view.findViewById<TextView>(R.id.user_group)

                Picasso.get().load(user.data.avatar).into(userAvatar)
                username.append(user.data.username)

                putStringData("userID", "${user.data.id}", view.context)

                val color = user.data.group.color
                userGroup.append(user.data.group.name)
                userGroup.setTextColor(Color.parseColor(color))

                getUserNovels(view)
            }
        }
    }

    private fun getUserNovels(view: View) {
        val userID = getStringData(view.context).getString("userID", "")!!.toInt()
        API.responseAllData(UserNovelsContainer::class.java, API.accountServiceNovels.getUserNovels(userID)) { user ->
            getNovelsCount(view, R.id.planned, user, "planned")
            getNovelsCount(view, R.id.completed, user, "completed")
            getNovelsCount(view, R.id.inProgress, user, "in_progress")
            getNovelsCount(view, R.id.deferred, user, "deferred")
            getNovelsCount(view, R.id.dropped, user, "dropped")

            getUserNovels(view, user)
        }
    }

    private fun getNovelsCount(view: View, id: Int, user: UserNovelsContainer, str: String) {
        val stat = view.findViewById<TextView>(id)
        var countStat = 0
        for (item in user.data) {
            when (item.status) {
                str -> (++countStat).toString()
            }
        }
        stat.append(" $countStat")
    }

    private fun getUserNovels(view: View, user: UserNovelsContainer) {
        recView = view.findViewById(R.id.rec_view)
        recView.layoutManager = LinearLayoutManager(view.context)

        val novelsImage = view.findViewById<ImageView>(R.id.novel_image)
        val novelsTitle = view.findViewById<TextView>(R.id.novel_title)
        val novelsRating = view.findViewById<TextView>(R.id.novel_rating)
        recView.adapter = RecyclerAdapterUserNovels(user.data, view.context) {
            Picasso.get().load(it.image).into(novelsImage)
            novelsTitle.append(it.original_name)
            println(R.id.planned)
            when(it.status) {
                "planned" -> novelsTitle.setTextColor(view.resources.getColor(R.color.text_planned))
                "completed" -> novelsTitle.setTextColor(view.resources.getColor(R.color.text_completed))
                "in_progress" -> novelsTitle.setTextColor(view.resources.getColor(R.color.text_in_progress))
                "deferred" -> novelsTitle.setTextColor(view.resources.getColor(R.color.text_deferred))
                "dropped" -> novelsTitle.setTextColor(view.resources.getColor(R.color.text_dropped))
            }
            novelsRating.append(it.mark)
        }
    }

    private fun logoutBtn(view: View) {
        val exitBtn = view.findViewById<Button>(R.id.logout_btn)
        exitBtn.setOnClickListener {
            deleteStringData("token", view.context)
            if (!getStringData(view.context).contains("token")) {
                println("User successful exited!")
                fragmentManager!!.beginTransaction().replace(R.id.app_fragment_container, AccountFragment()).commit()
            }
        }
    }
}