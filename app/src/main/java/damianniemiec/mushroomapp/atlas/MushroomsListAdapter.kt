package damianniemiec.mushroomapp.atlas

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import damianniemiec.mushroomapp.R
import kotlinx.android.synthetic.main.row.view.*

class MushroomsListAdapter(private val context: Context?) : RecyclerView.Adapter<CustomViewHolder>() {

    companion object {
        private val LABEL_COLORS = hashMapOf(
                R.string.edible.toString() to Color.parseColor("#006600"),
                R.string.poisonous.toString() to Color.parseColor("#660000")
        )
        private val IS_EDIBLE = hashMapOf(
                true to R.string.edible.toString(),
                false to R.string.poisonous.toString()
        )
    }

    private val polishNames = context?.resources?.getStringArray(R.array.polish_names)
    private val latinNames = context?.resources?.getStringArray(R.array.latin_names)
    private val descriptions = context?.resources?.getStringArray(R.array.mushroom_descriptions)
    private val edibles = listOf(true, true, true, true, true, false, true, true, true, true, false, false)
    private val pictures = listOf(R.drawable.borowik,
                                R.drawable.kania,
                                R.drawable.kozlarz,
                                R.drawable.maslak,
                                R.drawable.mleczaj_rydz,
                                R.drawable.muchomor,
                                R.drawable.opienka,
                                R.drawable.pieczarka,
                                R.drawable.pieprznik,
                                R.drawable.podbrzybek,
                                R.drawable.sromotnikowy,
                                R.drawable.szatan)

    override fun getItemCount(): Int {
        return polishNames?.size ?: -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val plName = polishNames?.get(position) ?: R.string.unknown_text.toString()
        val latName = latinNames?.get(position) ?: R.string.unknown_text.toString()
        holder.itemView.tv_name.text = plName
        holder.itemView.tv_latin.text = latName
        holder.itemView.tv_edible.text = IS_EDIBLE[edibles[position]]
        holder.itemView.tv_edible.setTextColor(LABEL_COLORS[IS_EDIBLE[edibles[position]]] ?: 0)
        holder.namePL = plName
        holder.nameLAT = latName
        holder.imgRes = pictures[position]
        holder.desc = descriptions?.get(position) ?: R.string.unknown_text.toString()
        if(context != null)
            holder.itemView.iv_shroom.setImageDrawable(ContextCompat.getDrawable(context, pictures[position]))
        if(position %2 == 1 && context != null)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.background))
    }
}

