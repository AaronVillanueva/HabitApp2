package mx.itesm.habitapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_main.*

class Community : AppCompatActivity(), ListenerRecycler {
    var adaptadorHabito: adaptadorHabito? = null
    private lateinit var baseDatos: FirebaseDatabase
    private lateinit var arrHabitos: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        //arrHabitos= mutableListOf()
        //baseDatos= FirebaseDatabase.getInstance()
        //configurarRecycler()

    }
    private fun configurarRecycler(){
        val layout= LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        //recyclerCommunity.layoutManager=layout

        //adaptadorHabito= adaptadorHabito(this,Habit.arrHabit)
        adaptadorHabito?.listener = this
        //recyclerCommunity.adapter=adaptadorHabito

        val divisor= DividerItemDecoration(this,layout.orientation)
        //recyclerCommunity.addItemDecoration(divisor)
    }
    private fun leerDatos(){
        val baseDatos= FirebaseDatabase.getInstance()
        val referencia=baseDatos.getReference("/Community")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                arrHabitos.clear()
                for (registro in snapshot.children){
                    val alumno=registro.getValue(Habit:: class.java)
                        arrHabitos.add("${alumno?.nombre} - ${alumno?.puntaje}")
                }
                val adaptador= ArrayAdapter<String>(this@Community,android.R.layout.simple_list_item_1, arrHabitos)

            }

        })

    }
    fun ClickMain(v: View){
        val intDatosPais= Intent(this, MainActivity::class.java)
        startActivity(intDatosPais)
    }
}
