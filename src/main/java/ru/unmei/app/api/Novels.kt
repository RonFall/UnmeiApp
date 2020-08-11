package ru.unmei.app.api

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.http.GET
import ru.unmei.app.R

//Novels
data class Novels(var original_name: String,
                  var description: String,
                  var image: String,
                  var rating: String,
                  var release_date: String)

data class NovelsResponse(var error: Boolean, var data: ArrayList<Novels>, @SerializedName("error_data") var errorData: Error?)

interface NovelsService {
    @GET("novels")
    fun getAllNovels(): Call<NovelsResponse>
}

class RecyclerAdapterNovels(private val items: ArrayList<Novels>, private val context: Context) : RecyclerView.Adapter<RecyclerHolderNovels>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolderNovels {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.app_novel_item, parent, false)
        return RecyclerHolderNovels(view)
    }

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holderNovels: RecyclerHolderNovels, position: Int) {
        val item = items[position]
        holderNovels.bind(item)
    }
}

class RecyclerHolderNovels(view: View) : RecyclerView.ViewHolder(view) {

    private val novelsImage = view.findViewById<ImageView>(R.id.novel_image)
    private val novelsTitle = view.findViewById<TextView>(R.id.novel_title)
    private val novelsRating = view.findViewById<TextView>(R.id.novel_rating)

    fun bind(novels: Novels) {
        Picasso.get().load(novels.image).into(novelsImage)
        novelsTitle.append(novels.original_name)
        novelsRating.append(novels.rating)
    }
}