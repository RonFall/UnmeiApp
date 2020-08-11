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
import ru.unmei.app.api.Novels
import ru.unmei.app.api.NovelsResponse
import ru.unmei.app.api.RecyclerAdapterNovels
import ru.unmei.app.setProgressBar
import ru.unmei.app.textToastShort

class NovelsFragment : Fragment() {

    private lateinit var recView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_novels, container, false)
        getNovels(view)
        return view
    }

    private fun getNovels(view: View) {
        recView = view.findViewById(R.id.rec_view)

        val prg = setProgressBar(view)
        prg.show()
        API.responseAllData(NovelsResponse::class.java, API.novelsService.getAllNovels()) { novels ->
            if (novels.error) textToastShort("Ошибка при загрузке новелл", view.context)
            else {
                prg.dismiss()
                showData(novels.data, view)
            }
        }
    }

    private fun showData(items: ArrayList<Novels>, view: View) {
        recView.layoutManager = LinearLayoutManager(view.context)
        recView.adapter = RecyclerAdapterNovels(items, view.context)
    }
}