package mx.itesm.habitapp2

class Habit (val nombre:String, val casos:Int): Comparable<Habit>{
    override fun compareTo(other: Habit): Int {
        return nombre.compareTo(other.nombre)
    }
    companion object{
        val arrPaises= arrayOf(
            Habit("test",500)
        )
    }
}