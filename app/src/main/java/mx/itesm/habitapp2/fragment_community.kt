package mx.itesm.habitapp2

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.ListFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class fragment_community : ListFragment() {
    private lateinit var arrAlumnos: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrAlumnos= mutableListOf()

    }

    override fun onStart() {
        super.onStart()
        leerDatos()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return TextView(activity).apply { setText(R.string.hello_blank_fragment) }
        return super.onCreateView(inflater,container,savedInstanceState)
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
                    val alumno=registro.getValue(Habit:: class.java)
                    arrAlumnos.add("${alumno?.nombre} - ${alumno?.puntaje}")
                }
                val adaptador= ArrayAdapter<String>(context!!, R.layout.simple_list_item_1, arrAlumnos)
                listAdapter=adaptador
            }

        })

    }

}