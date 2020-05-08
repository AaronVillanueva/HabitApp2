package mx.itesm.habitapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), ListenerRecycler
{
    var adaptadorHabito: adaptadorHabito? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configurarRecycler()


    }
    private fun configurarRecycler(){
        val layout= LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        recyclerHabit.layoutManager=layout

        adaptadorHabito= adaptadorHabito(this,Habit.arrHabit)
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