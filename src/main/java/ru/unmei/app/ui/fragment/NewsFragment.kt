package ru.unmei.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.unmei.app.R
import ru.unmei.app.api.API
import ru.unmei.app.api.News
import ru.unmei.app.api.NewsResponse
import ru.unmei.app.api.RecyclerAdapterNews
import ru.unmei.app.setProgressBar
import ru.unmei.app.textToastShort

class NewsFragment : Fragment() {

    private lateinit var recView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        getNews(view)
        return view
    }

    private fun getNews(view: View) {
        recView = view.findViewById(R.id.rec_view)

        val prg = setProgressBar(view)
        prg.show()
        API.responseAllData(NewsResponse::class.java, API.newsService.getAllNews()) { news ->
            if (news.error) textToastShort("Ошибка при загрузке новостей", view.context)
            else {
                prg.dismiss()
                showData(news.data, view)
            }
        }
    }

    private fun showData(items: ArrayList<News>, view: View) {
        recView.layoutManager = LinearLayoutManager(view.context)
        recView.adapter = RecyclerAdapterNews(items, view.context)
    }
}