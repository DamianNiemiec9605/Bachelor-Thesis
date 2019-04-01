package damianniemiec.mushroomapp.atlas

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View

class CustomViewHolder(v: View): RecyclerView.ViewHolder(v) {

    companion object {
        const val PL_NAME_TAG = "name_pl"
        const val DESCRIPTION_TAG = "desc"
        const val IMAGE_TAG = "img"
    }

    lateinit var namePL: String
    lateinit var nameLAT: String
    lateinit var desc: String
    var imgRes: Int = 0

    init {
        v.setOnClickListener {
            val intent = Intent(v.context, DetailActivity::class.java)
            intent.putExtra(PL_NAME_TAG, namePL)
            intent.putExtra(DESCRIPTION_TAG, desc)
            intent.putExtra(IMAGE_TAG, imgRes)
            v.context.startActivity(intent)
        }
    }
}