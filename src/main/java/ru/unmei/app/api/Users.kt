package ru.unmei.app.api

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.unmei.app.R

data class UserGroup(var name: String, var color: String)
data class User(var id: Int, var username: String, var reg_date: String, var avatar: String, var group: UserGroup)
data class UserResponse(var error: Boolean, var data: User, @SerializedName("error_data") var errorData: Error?)

// Requests bodies
data class Login(var login: String, var password: String)

// Getting novels
data class UserNovels(var novel_id: String, var mark: String, var status: String, var original_name: String, var image: String)
data class UserNovelsContainer(var data: ArrayList<UserNovels>)

object Users {
    interface AuthService {
        @POST("auth/login")
        fun login(@Body body: Login): Call<UserResponse>
    }

    interface UsersService {
        @GET("users/me")
        fun getMe(): Call<UserResponse>
    }

    interface UsersServiceNovels {
        @GET("users/{id}/novels")
        fun getUserNovels(@Path("id") id: Int): Call<UserNovelsContainer>
    }
}

class RecyclerAdapterUserNovels<T>(private val items: ArrayList<T>, private val context: Context,  private val arg: (body: T) -> Any) : RecyclerView.Adapter<RecyclerHolderUserNovels>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolderUserNovels {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.app_novel_item, parent, false)
        return RecyclerHolderUserNovels(view)
    }

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holderUserNovels: RecyclerHolderUserNovels, position: Int) {
        val item = items[position]
        holderUserNovels.bind(arg(item))
    }
}

class RecyclerHolderUserNovels(view: View) : RecyclerView.ViewHolder(view) {

    fun <T> bind(body: T): T {
        return body
    }
}