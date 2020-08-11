package ru.unmei.app.api

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import ru.unmei.app.R
import ru.unmei.app.setTime

//News
data class News(var title: String, var short_post: String, var author: String, var date: String)
data class NewsResponse(var error: Boolean, var data: ArrayList<News>, @SerializedName("error_data") var errorData: Error?)

interface NewsService {
    @GET("news")
    fun getAllNews(): Call<NewsResponse>
}

class RecyclerAdapterNews(private val items: ArrayList<News>, private val context: Context) : RecyclerView.Adapter<RecyclerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.app_news_item, parent, false)
        return RecyclerHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holderNews: RecyclerHolder, position: Int) {
        val item = items[position]
        holderNews.bind(item)
    }
}

class RecyclerHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val newsTitle = view.findViewById<TextView>(R.id.news_title)
    private val newsDesc = view.findViewById<TextView>(R.id.news_desc)
    private val newsAuthor = view.findViewById<TextView>(R.id.news_author)
    private val newsDate = view.findViewById<TextView>(R.id.news_date)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(news: News) {
        newsTitle.append(news.title)
        newsDesc.append(news.short_post)
        newsAuthor.append("Автор: ${news.author}")
        newsDate.append("Дата: ${setTime(news.date)}")
    }
}