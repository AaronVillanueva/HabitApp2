package mx.itesm.habitapp2

import android.R
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class databaseController (val uid: String) {
    var arrHabitos= mutableListOf<Habit>()
    var mAuth = FirebaseAuth.getInstance()
    var baseDatos: FirebaseDatabase=FirebaseDatabase.getInstance()


    public fun leerArrHabitUsuario(): MutableList<Habit>{
        val referencia=baseDatos.getReference("/Users/$uid")
        referencia.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                arrHabitos.clear()
                for (registro in snapshot.children){
                    if (registro.key !="Count"){
                        val habito=registro.getValue(Habit:: class.java)
                        arrHabitos.add(Habit ("${habito?.nombre}","${habito?.puntaje}")) } }} })
        return arrHabitos
    }

    //metodos para escribir un habito nuevo en la BD
    public fun escribirHabitUsuario(habit:Habit){
        var currentUser = mAuth.currentUser!!
        val next=numeroHabitosUsuario()+1
        var referencia=baseDatos.getReference("/Users/$uid/$next")
        referencia.setValue(habit) }
    public fun escribirHabitUsuarioStrings(nombre:String,puntaje:String){
        var currentUser = mAuth.currentUser!!
        val nuevo=Habit(nombre,puntaje)
        val next=numeroHabitosUsuario()+1
        var referencia=baseDatos.getReference("/Users/$uid/$next")
        referencia.setValue(nuevo) }

    //auxiliar para manejar habitos unicos
    private fun numeroHabitosUsuario(): Int{
        val referencia=baseDatos.getReference("/Users/$uid/Count")
        var respuesta=referencia.key as Int
        return respuesta
    }

}