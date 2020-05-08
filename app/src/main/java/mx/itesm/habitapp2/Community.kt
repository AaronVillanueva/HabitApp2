package mx.itesm.habitapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class Community : AppCompatActivity() {
    var adaptadorHabito: adaptadorHabito? = null
    private lateinit var baseDatos: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        arrAlumnos= mutableListOf()
        baseDatos= FirebaseDatabase.getInstance()
        configurarRecycler()
    }
    private fun configurarRecycler(){
        val layout= LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        recyclerHabit.layoutManager=layout

        adaptadorHabito= adaptadorHabito(this,Habit.arrHabit)
        //adaptadorHabito?.listener = this
        recyclerHabit.adapter=adaptadorHabito

        val divisor= DividerItemDecoration(this,layout.orientation)
        recyclerHabit.addItemDecoration(divisor)
    }
    private fun leerDatos(){
        val baseDatos= FirebaseDatabase.getInstance()
        val referencia=baseDatos.getReference("/Community")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                arrAlumnos.clear()
                for (registro in snapshot.children){
                    val alumno=registro.getValue(Alumno:: class.java)
                    arrAlumnos.add("${alumno?.nombre} - ${alumno?.matricula}")
                }
                val adaptador= ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrAlumnos)
                listAdapter=adaptador
            }

        })

    }
}
