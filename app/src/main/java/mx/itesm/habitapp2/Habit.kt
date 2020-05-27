package mx.itesm.habitapp2

class Habit (val nombre:String="", val puntaje:String="",val fecha:String=""): Comparable<Habit>{

    override fun compareTo(other: Habit): Int {
        return nombre.compareTo(other.nombre)
    }
    companion object{
        val arrHabit= arrayOf(
            Habit("Leer","5","5.5.5"),
            Habit("Escribir","5","5.5.5")
        )
    }
}