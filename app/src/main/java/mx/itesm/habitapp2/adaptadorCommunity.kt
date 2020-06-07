package mx.itesm.habitapp2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.renglon_community.view.*
import kotlinx.android.synthetic.main.renglon_habito.view.*
import kotlinx.android.synthetic.main.renglon_habito.view.tvNombre

class adaptadorCommunity (private val contexto: Context, var arrHabitos: MutableList<Habit> ):
    RecyclerView.Adapter<adaptadorCommunity.renglonCommunity>() {
    var listener: ListenerRecycler? = null

    inner class renglonCommunity (var vistaRenglon: View): RecyclerView.ViewHolder(vistaRenglon)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): renglonCommunity {
        val vista= LayoutInflater.from(contexto).inflate(R.layout.renglon_community,parent,false)
        return renglonCommunity(vista)
    }

    override fun getItemCount(): Int {
        return arrHabitos.size
    }

    override fun onBindViewHolder(holder: renglonCommunity, position: Int) {
        val habito = arrHabitos[position]
        holder.vistaRenglon.tvNombre.text=habito.nombre


        holder.vistaRenglon.botonAdd.setHint(habito.nombre)
        holder.vistaRenglon.setOnClickListener{
            listener?.itemClicked(position)
        }

    }
}