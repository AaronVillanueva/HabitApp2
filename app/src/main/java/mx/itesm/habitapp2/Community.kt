package mx.itesm.habitapp2

import android.app.ActivityManager
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
        configurarRecycler()

    }

    fun ClickMain(v: View){
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun configurarRecycler(){
        val layout= LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL

        adaptadorHabito?.listener = this


        val divisor= DividerItemDecoration(this,layout.orientation)
        //recyclerCommunity.addItemDecoration(divisor)
    }

}
