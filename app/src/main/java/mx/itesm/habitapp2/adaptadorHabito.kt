package mx.itesm.habitapp2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.renglon_habito.view.*

class adaptadorHabito (private val contexto: Context, var arrPaises: Array<Habit> ):
    RecyclerView.Adapter<adaptadorHabito.renglonHabito>()
{
    var listener: listenerRecycler?=null
    inner class renglonHabito (var vistaRenglon: View): RecyclerView.ViewHolder(vistaRenglon)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): renglonHabito {
        val vista= LayoutInflater.from(contexto).inflate(R.layout.renglon_habito,parent,false)
        return renglonHabito(vista)
    }

    override fun getItemCount(): Int {
        return arrPaises.size
    }

    override fun onBindViewHolder(holder: renglonHabito, position: Int) {
        val pais=arrPaises[position]
        holder.vistaRenglon.tvNombre.text=pais.nombre
        holder.vistaRenglon.setOnClickListener{listener?.itemClicked(position)}
    }
}