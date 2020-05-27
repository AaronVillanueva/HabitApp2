package mx.itesm.habitapp2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.renglon_habito.view.*

class adaptadorHabito (private val contexto: Context, var arrHabitos: MutableList<Habit> ):
     RecyclerView.Adapter<adaptadorHabito.renglonHabito>()
{
    var listener: ListenerRecycler? = null

    inner class renglonHabito (var vistaRenglon: View): RecyclerView.ViewHolder(vistaRenglon)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): renglonHabito {
        val vista= LayoutInflater.from(contexto).inflate(R.layout.renglon_habito,parent,false)
        return renglonHabito(vista)
    }

    override fun getItemCount(): Int {
        return arrHabitos.size
    }

    override fun onBindViewHolder(holder: renglonHabito, position: Int) {
        val habito = arrHabitos[position]
        holder.vistaRenglon.tvNombre.text=habito.nombre
        holder.vistaRenglon.tvPuntaje.text=habito.puntaje

        //no tocar
        val pos=(position+1).toString()
        holder.vistaRenglon.botonDone.setHint(pos)

        holder.vistaRenglon.setOnClickListener{
            listener?.itemClicked(position)
        }

    }
}