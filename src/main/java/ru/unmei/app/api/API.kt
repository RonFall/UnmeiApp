package ru.unmei.app.api

import com.google.gson.Gson
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpCookie
import java.net.URI

data class Error(var code: Int, var text: String)

object API {
    var apiUrl = URI("https://api.unmei.nix13.pw")
    lateinit var manager: CookieManager

    lateinit var loginService: Users.AuthService
    lateinit var newsService: NewsService
    lateinit var novelsService: NovelsService
    lateinit var accountService: Users.UsersService
    lateinit var accountServiceNovels: Users.UsersServiceNovels

    fun initApi() {
        manager = CookieManager()
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

        println("CookieManager initialized!")

        val client = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(manager))
            .build()

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.unmei.nix13.pw/v1/")
            .client(client)
            .build().also {
                loginService = it.create(Users.AuthService::class.java)
                newsService = it.create(NewsService::class.java)
                novelsService = it.create(NovelsService::class.java)
                accountService = it.create(Users.UsersService::class.java)
                accountServiceNovels = it.create(Users.UsersServiceNovels::class.java)
            }
    }

    fun <T> getBodyFromResponse(res: Response<T>, cls: Class<T>): T? {
        return if (res.isSuccessful) res.body()
        else Gson().fromJson(res.errorBody()!!.string(), cls)
    }

    fun getCookie(uri: URI, name: String): HttpCookie? {
        val uriStore = manager.cookieStore.get(uri)
        uriStore.forEach { cookie ->
            if (cookie.name == name) {
                return cookie
            }
        }
        return null
    }

    fun <T> responseAllData(responseType: Class<T>, dataFunction: Call<T>, onResponse: (body: T) -> Any) {
        dataFunction.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = getBodyFromResponse(response, responseType)
                onResponse(body!!)
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}