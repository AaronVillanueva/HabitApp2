package mx.itesm.habitapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_configuracios_habito.*

class ConfiguraciosHabitoActiv : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracios_habito)
        val nombre = intent.getStringExtra("HABITO")
        tvHabitoNombre.text = nombre
    }
}
