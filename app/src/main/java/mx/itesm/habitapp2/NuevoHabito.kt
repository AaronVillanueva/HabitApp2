package mx.itesm.habitapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_nuevo_habito.*

class NuevoHabito : AppCompatActivity() {
    private var db=databaseController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_habito)
        val size = intent.getStringExtra("arrSize").toInt()
        Log.d("arrSize",size.toString())
    }

    fun nuevoHabito(v: View){
        var db=databaseController()
        val size = intent.getStringExtra("arrSize").toInt()
        val texto=entrada.text.toString()
        db.escribirHabitUsuarioStrings("$texto","0",db.getDate(),size)
        val inten= Intent(this, MainActivity::class.java)
        startActivity(inten)
    }
}
