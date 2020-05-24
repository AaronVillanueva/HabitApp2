package mx.itesm.habitapp2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListenerRecycler
{

    var adaptadorHabito: adaptadorHabito? = null
    private lateinit var arrHabitos: MutableList<Habit>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrHabitos=mutableListOf()
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


    }
    fun ClickCommunity(v: View){
        val intDatosPais= Intent(this, Community::class.java)
        startActivity(intDatosPais)
    }

    private fun leerDatos(){
        val baseDatos= FirebaseDatabase.getInstance()
        val referencia=baseDatos.getReference("/test")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                arrHabitos.clear()
                for (registro in snapshot.children){
                    val habitoRegistro=registro.getValue(Habit:: class.java)
                    arrHabitos.add( Habit("${habitoRegistro?.nombre}", "${habitoRegistro?.puntaje}"))
                }
                configurarRecycler()
            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel) } }


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