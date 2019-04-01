package damianniemiec.mushroomapp.atlas

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import damianniemiec.mushroomapp.R

class Atlas : Fragment() {

    companion object {
        fun newInstance(): Atlas = Atlas()
    }

    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.atlas_layout, container, false)
        recyclerView = view.findViewById(R.id.atlas_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MushroomsListAdapter(context)
        return view
    }

    fun getRecyclerView(): RecyclerView {
        return recyclerView
    }
}
