package mx.itesm.habitapp2
import com.google.firebase.database.Exclude

data class User (val name: String = "", val lastName: String = "", val email: String = "", @Exclude val uid: String = "")