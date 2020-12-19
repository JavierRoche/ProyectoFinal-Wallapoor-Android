package com.tushe.wallapoor.ui.filters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tushe.wallapoor.R

class FiltersActivity: AppCompatActivity() {
    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        // Metodo estatico que devuelve el Intent de la actividad
        fun getIntent(context: Context): Intent {
            return Intent(context, FiltersActivity::class.java)
        }
    }


    /**
     * LIFE CYCLE
     **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Indicamos cual es el fichero xml que manejara la actividad
        setContentView(R.layout.filters_activity)

    }
}