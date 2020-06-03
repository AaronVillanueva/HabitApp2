package mx.itesm.habitapp2

//import jdk.nashorn.internal.objects.NativeDate.getTime

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), ListenerRecycler
{

    val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"
    lateinit var btnDate: Button
    var NOTIFICATION_ID = "notification-id"
    var NOTIFICATION = "notification"


    val myCalendar = Calendar.getInstance()
    var adaptadorHabito: adaptadorHabito? = null

    lateinit var show: Button

    var MY_PREFS_NAME = "nameOfSharedPreferences"

    private lateinit var arrHabitos: MutableList<Habit>
    var db=databaseController()
    private lateinit var mUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    var registerData = RegisterActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //show = findViewById(R.id.btn_show)

        //show.setOnClickListener { startAlarm(true, true) }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                //Log.d(TAG, msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

        arrHabitos=db.leerArrHabitUsuario()
        arrHabitos=db.arrHabitos
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


    companion object {
        private const val TAG = "MainActivity"
    }

    fun ClickCommunity(v: View){
        val inten= Intent(this, Community::class.java)
        startActivity(inten)}

    fun ClickNuevo(v: View){
        val inten= Intent(this, NuevoHabito::class.java)
            inten.putExtra("arrSize", arrHabitos.size.toString())
        startActivity(inten)}

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
                    if (registro.key !="Count"){
                    val habitonuevo = registro.getValue(Habit::class.java)
                    arrHabitos.add(Habit("${habitonuevo?.nombre}", "${habitonuevo?.puntaje}","${habitonuevo?.fecha}"))}
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel) } }

    override fun itemClicked(position: Int) {
        val nombreHabito = adaptadorHabito?.arrHabitos?.get(position)?.nombre
        val puntajeHabito = adaptadorHabito?.arrHabitos?.get(position)?.puntaje
        val fechaHabito = adaptadorHabito?.arrHabitos?.get(position)?.fecha
        val intConfigHabito = Intent(this, ConfiguraciosHabitoActiv::class.java)
        intConfigHabito.putExtra("HABITO", nombreHabito)
        intConfigHabito.putExtra("PUNTAJE", puntajeHabito)
        intConfigHabito.putExtra("FECHA", fechaHabito)
        intConfigHabito.putExtra("KEY", (position+1).toString())
        startActivity(intConfigHabito)

    }
    fun checkmarked(v:View){
        var vv=v as Button
        var pos=vv.hint.toString().toInt()
        var modificado=arrHabitos[pos-1]
        var res=db.agregarPuntaje(modificado,pos.toString())
        if (res==1){
            val inten= Intent(this, MainActivity::class.java)
            startActivity(inten)}
    }
}