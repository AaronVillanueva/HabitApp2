package mx.itesm.habitapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_configuracios_habito.*

class ConfiguraciosHabitoActiv : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracios_habito)
        val nombre = intent.getStringExtra("HABITO")
        tvHabitoNombre.text = nombre
    }

    fun actualizarHabito(v: View){
        var db=databaseController()
        val pos = intent.getStringExtra("KEY")
        val puntaje = intent.getStringExtra("PUNTAJE")
        val fecha = intent.getStringExtra("FECHA")
        db.actualizarHabit(pos,Habit(etNuevoNombre.text.toString(),puntaje,fecha))
        val inten= Intent(this, MainActivity::class.java)
        startActivity(inten)
    }
}
