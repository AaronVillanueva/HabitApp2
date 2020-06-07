package mx.itesm.habitapp2

import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_main.*

class Community : AppCompatActivity(), ListenerRecycler {
    var adaptadorCommunity: adaptadorCommunity? = null
    private lateinit var baseDatos: FirebaseDatabase
    private lateinit var arrHabitos: MutableList<Habit>
    private lateinit var mAuth: FirebaseAuth
    var db=databaseController()
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        //arrHabitos= mutableListOf()
        //baseDatos= FirebaseDatabase.getInstance()
        arrHabitos=db.leerArrHabitUsuario()
        arrHabitos=db.arrHabitos
        leerDatos()

    }

    fun ClickMain(v: View){
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun leerDatos(){
        val baseDatos= FirebaseDatabase.getInstance()
        val referencia=baseDatos.getReference("/Community")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                arrHabitos.clear()
                for (registro in snapshot.children) {
                    val habitonuevo = registro.getValue(Habit::class.java)
                    arrHabitos.add(Habit("${habitonuevo?.nombre}", "0"))
                }
                configurarRecycler()
            }
        })
    }
    fun actualizarHabito(v: View){
        var db=databaseController()
        var vv=v as Button
        var pos = intent.getStringExtra("POS")
        db.escribirHabitUsuario( Habit(vv.hint.toString(),"0",db.getDate()),pos.toInt())
        val inten= Intent(this, MainActivity::class.java)
        startActivity(inten)
    }

    private fun configurarRecycler(){
        val layout= LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        recyclerCommunity.layoutManager=layout

        //adaptadorCommunity= adaptadorCommunity(this,Habit.arrHabit)
        adaptadorCommunity= adaptadorCommunity(this,arrHabitos)
        adaptadorCommunity?.listener = this
        recyclerCommunity.adapter=adaptadorCommunity

        val divisor= DividerItemDecoration(this,layout.orientation)
        recyclerCommunity.addItemDecoration(divisor)
    }

}
