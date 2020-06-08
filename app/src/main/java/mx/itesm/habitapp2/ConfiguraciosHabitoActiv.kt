package mx.itesm.habitapp2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_configuracios_habito.*


class ConfiguraciosHabitoActiv : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracios_habito)
        val nombre = intent.getStringExtra("HABITO")
        tvHabitoNombre.text = nombre
        etNuevoNombre.setText(intent.getStringExtra("HABITO"))



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

    fun botonBorrarHabito(v: View){
        val builder1 =
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this Habit?")
                .setCancelable(true)

        builder1.setPositiveButton("Yes") { dialog, id -> borrarHabito(intent.getStringExtra("KEY")) }
        builder1.setNegativeButton("No") { dialog, id -> dialog.cancel() }

        val alert11 = builder1.create()
        alert11.show()


    }

    fun borrarHabito(key : String){
        var db=databaseController()
        db.eliminarHabit(key)
        val inten= Intent(this, MainActivity::class.java)
        startActivity(inten)
    }
}
