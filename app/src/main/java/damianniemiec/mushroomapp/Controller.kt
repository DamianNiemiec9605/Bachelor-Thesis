package damianniemiec.mushroomapp

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import damianniemiec.mushroomapp.atlas.Atlas
import damianniemiec.mushroomapp.identification.Identification

class Controller : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_identify -> {
                val identificationFragment = Identification.newInstance()
                openFragment(identificationFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_atlas -> {
                val atlasFragment = Atlas.newInstance()
                openFragment(atlasFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                val aboutFragment = About.newInstance()
                openFragment(aboutFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        askPermissions()
        openFragment(Atlas.newInstance())
    }

    private fun askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 50)
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
