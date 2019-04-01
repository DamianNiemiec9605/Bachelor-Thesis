package damianniemiec.mushroomapp.atlas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import damianniemiec.mushroomapp.R
import kotlinx.android.synthetic.main.details_layout.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val PL_NAME_TAG = "name_pl"
        const val DESCRIPTION_TAG = "desc"
        const val IMAGE_TAG = "img"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_layout)
        tv_detail_name.text = intent.getStringExtra(PL_NAME_TAG)
        tv_detail_desc.text = intent.getStringExtra(DESCRIPTION_TAG)
        iv_detail.setImageResource(intent.getIntExtra(IMAGE_TAG, 0))
    }
}