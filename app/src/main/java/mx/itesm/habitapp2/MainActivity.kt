package mx.itesm.habitapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.Map as Map1

class MainActivity : AppCompatActivity(), ListenerRecycler
{
    var adaptadorHabito: adaptadorHabito? = null

    private lateinit var arrHabitos: MutableList<Habit>

    private lateinit var mUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    var registerData = RegisterActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        arrHabitos = mutableListOf()
        leerDatos()

        configurarRecycler()

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var currentUser = mAuth.currentUser!!
        var uid = currentUser.uid


        var UserData = FirebaseDatabase.getInstance().getReference("Users")
        val key = UserData.push().key
        //UserData.child(uid).push().setValue(PrevHab("Correr"))
        //UserData.child("firstName").setValue(registerData.firstName)
        //UserData.child("lastName").setValue(registerData.lastName)

    }

    fun ClickCommunity(v: View){
        val intDatosPais= Intent(this, Community::class.java)
        startActivity(intDatosPais)
    }

    fun ClickLogout(v: View){
        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun leerDatos(){
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var currentUser = mAuth.currentUser!!
        var uid = currentUser.uid
        val baseDatos= FirebaseDatabase.getInstance()
        val referencia=baseDatos.getReference("/Users/$uid")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                arrHabitos.clear()
                for (registro in snapshot.children) {
                    val alumno = registro.getValue(Habit::class.java)
                    arrHabitos.add(Habit("${alumno?.nombre}", "${alumno?.puntaje}"))
                }
                configurarRecycler()
            }
        })
    }

    private fun configurarRecycler(){
        val layout= LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        recyclerHabit.layoutManager=layout

        //adaptadorHabito= adaptadorHabito(this,Habit.arrHabit)
        adaptadorHabito= adaptadorHabito(this,arrHabitos)
        adaptadorHabito?.listener = this
        recyclerHabit.adapter=adaptadorHabito

        val divisor= DividerItemDecoration(this,layout.orientation)
        recyclerHabit.addItemDecoration(divisor)
    }

    override fun itemClicked(position: Int) {
        val nombreHabito = adaptadorHabito?.arrHabitos?.get(position)?.nombre
        val intConfigHabito = Intent(this, ConfiguraciosHabitoActiv::class.java)
        intConfigHabito.putExtra("HABITO", nombreHabito)
        startActivity(intConfigHabito)

    }
}