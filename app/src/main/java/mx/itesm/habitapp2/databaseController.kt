package mx.itesm.habitapp2

import android.R
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class databaseController () {
    var arrHabitos= mutableListOf<Habit>()
    private var mAuth = FirebaseAuth.getInstance()
    private var baseDatos: FirebaseDatabase=FirebaseDatabase.getInstance()
    private val uid=mAuth.currentUser!!.uid
    private lateinit var cuentaLate: String

    public fun leerArrHabitUsuario(): MutableList<Habit>{
        val referencia=baseDatos.getReference("/Users/$uid")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                arrHabitos.clear()
                for (registro in snapshot.children){
                    if (registro.key !="Count"){
                        val habito=registro.getValue(Habit:: class.java)
                        arrHabitos.add(Habit ("${habito?.nombre}","${habito?.puntaje}","${habito?.fecha}")) } }} })
        return arrHabitos
    }

    public fun actualizarHabit(key:String,nuevoHabito:Habit){
        var referencia=baseDatos.getReference("/Users/$uid/$key")
        referencia.setValue(nuevoHabito) }
    public fun actualizarHabitString(key:String,nombre:String,puntaje:String,fecha:String){
        actualizarHabit(key,Habit(nombre,puntaje,fecha))
    }

    public fun agregarPuntaje(anterior:Habit,key:String):Int{
        var fecha=anterior.fecha.split(".")
        var fechaActual=getDate().split(".")
        var nuevoPuntaje=anterior.puntaje.toInt()

        if(fecha[2]<fechaActual[2]){nuevoPuntaje+=1}
        if(fecha[2]==fechaActual[2]){
            if(fecha[1]<fechaActual[1]){nuevoPuntaje+=1}
            if(fecha[1]==fechaActual[1]){
                if(fecha[0]<fechaActual[0]){ nuevoPuntaje+=1 }
            }
        }
        var res=0
        if(anterior.puntaje!=nuevoPuntaje.toString()){
            res=1
        }
        actualizarHabitString(key,anterior.nombre,nuevoPuntaje.toString(),getDate())
        return res
    }

    //metodos para escribir un habito nuevo en la BD
    public fun escribirHabitUsuario(habit:Habit,pos:Int){
        var next=pos+1
        var referencia=baseDatos.getReference("/Users/$uid/$next")
        referencia.setValue(habit) }

    public fun escribirHabitUsuarioStrings(nombre:String,puntaje:String,fecha:String,pos: Int){
        escribirHabitUsuario(Habit(nombre,puntaje,fecha),pos)}

    public fun getDate():String{
        var sdf = SimpleDateFormat("dd.MM.yyyy")
        var date= Date()
        var today=sdf.format(date)
        return today
    }

    private fun numeroHabitosUsuarioSNAP():String{
        val referencia=baseDatos.getReference("/Users/$uid")
        var cuenta=0
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                cuenta=snapshot.childrenCount.toInt()
                cuentaLate=snapshot.childrenCount.toString()
                Log.d("CUENTAinterior", "$cuenta")

            }})
        Log.d("CUENTAexterior", "$cuenta")
        return cuentaLate
    }

    //auxiliar para manejar habitos unicos depreciado
    private fun numeroHabitosUsuarioDep(): Int{
        val referencia=baseDatos.getReference("/Users/$uid/Count")
        var respuesta=referencia.key as Int
        return respuesta
    }

}