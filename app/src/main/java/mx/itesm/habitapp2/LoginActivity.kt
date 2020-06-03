package mx.itesm.habitapp2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    //global variables
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etName: EditText
    private lateinit var etLastName: EditText


    //private lateinit var mProgressBar: ProgressBar

    //Creamos nuestra variable de autenticación firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialise()
    }

    override fun onResume() {
        super.onResume()
        nextPage()
    }

    fun nextPage(){
        var currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /*Creamos un método para inicializar nuestros elementos del diseño y la autenticación de firebase*/
    private fun initialise() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        //mProgressBar = ProgressBar(this)
        mAuth = FirebaseAuth.getInstance()
    }

//Ahora vamos a Iniciar sesión con firebase es muy sencillo

    private fun loginUser() {
        //Obtenemos usuario y contraseña
        email = etEmail.text.toString()
        password = etPassword.text.toString()
        //Verificamos que los campos no este vacios
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            //Mostramos el progressdialog
            //mProgressBar.setMessage("Registering User...")
            //mProgressBar.show()

            //Iniciamos sesión con el método signIn y enviamos usuario y contraseña
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {

                    //Verificamos que la tarea se ejecutó correctamente
                        task ->
                    if (task.isSuccessful) {
                        // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                        startAlarm(true, true)
                        goHome()

                        // Creamos nuestro método en la parte de abajo
                    } else {
                        // sino le avisamos el usuairo que orcurrio un problema
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }


    private fun goHome() {
//Ocultamos el progress
        //mProgressBar.hide()
//Nos vamos a Home
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

/* Tenemos que crear nuestros métodos con el mismo nombre que le agregamos a nuestros botones en el atributo onclick y forzosamente tenemos que recibir un parámetro view para que sea reconocido como click de nuestro button ya que en view podemos modificar los atributos*/

    /*Primero creamos nuestro evento login dentro de este llamamos nuestro método loginUser al dar click en el botón se iniciara sesión */
    fun login(view: View) {
        loginUser()
    }

    private fun startAlarm(
        isNotification: Boolean,
        isRepeat: Boolean
    ) {
        val manager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent: Intent
        val pendingIntent: PendingIntent
        // SET TIME HERE
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 5)
        myIntent = Intent(this, Notification_receiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        if (!isRepeat) manager[AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000] =
            pendingIntent else manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

/*Si se olvido de la contraseña lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/

    fun forgotPassword(view: View) {
        startActivity(Intent(this,
            ForgotPaswordActivity::class.java))
    }

/*Si quiere registrarse lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/

    fun register(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
