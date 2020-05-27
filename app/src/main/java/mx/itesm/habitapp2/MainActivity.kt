package mx.itesm.habitapp2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListenerRecycler
{
    var adaptadorHabito: adaptadorHabito? = null

    private lateinit var arrHabitos: MutableList<Habit>
    var db=databaseController()
    private lateinit var mUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    var registerData = RegisterActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrHabitos=db.leerArrHabitUsuario()
        arrHabitos=db.arrHabitos
        leerDatos()


        createNotificationChannel()
        val intentNot = Intent(this, Community::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intentNot, 0)
        var builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.common_full_open_on_phone)
            .setContentTitle(getString(R.string.NotificacionDiarioTitle))
            .setContentText("Hola! Es importante que actualices tu progreso todos los días")
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.NotificacionDiarioText)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(this)) { notify(1, builder.build()) }

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
        val intConfigHabito = Intent(this, ConfiguraciosHabitoActiv::class.java)
        intConfigHabito.putExtra("HABITO", nombreHabito)
        intConfigHabito.putExtra("PUNTAJE", puntajeHabito)
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