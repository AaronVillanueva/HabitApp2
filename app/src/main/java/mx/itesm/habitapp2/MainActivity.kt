package mx.itesm.habitapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    var adaptadorPais: adaptadorPais? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configurarRecycler()


    }
    private fun configurarRecycler(){
        val layout=LinearLayoutManager(this)
        layout.orientation=LinearLayoutManager.VERTICAL
        recyclerPaises.layoutManager=layout

        adaptadorPais= adaptadorPais(this,pais.arrPaises)
        adaptadorPais?.listener=this
        recyclerPaises.adapter=adaptadorPais

        val divisor=DividerItemDecoration(this,layout.orientation)
        recyclerPaises.addItemDecoration(divisor)
    }


}