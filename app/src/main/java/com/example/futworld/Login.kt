package com.example.futworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Declare all the components we create in the register activity
    lateinit var email: EditText
    lateinit var password: EditText

    lateinit var userTypes: RadioGroup
    lateinit var userType: String
    lateinit var loginBtn: Button
    lateinit var registerBtn: TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        userTypes = findViewById(R.id.RGroup)
        loginBtn = findViewById(R.id.registerBtn)
        registerBtn = findViewById(R.id.login)
        firebaseAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBar)

        // Radio group
        userTypes.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{
                group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            userType = radio.text.toString()
            Toast.makeText(applicationContext,"User type:"+
                    " ${radio.text}",
                Toast.LENGTH_SHORT).show()
        })

        loginBtn.setOnClickListener {
            onClick(it)
        }

        registerBtn.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun onClick(it: View?) {
        var str_email: String = email.text.toString().trim()
        var str_password: String = password.text.toString().trim()

        // Set up the error messages.
        if (TextUtils.isEmpty(str_email)) {
            email.setError("Email is required.")
            return
        }
        if (TextUtils.isEmpty(str_password)) {
            password.setError("Password is required.")
            return
        }
        if (str_password.length < 6) {
            password.setError("Password must be >= 6 characters.")
            return
        }

        // check the user type.
        if(userTypes.checkedRadioButtonId == -1){
            Toast.makeText(applicationContext, "Please select a user type.", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        // Authenticate the users.
        firebaseAuth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener {
            onComplete(it)
        }

    }

    private fun onComplete(it: Task<AuthResult>) {
        if(it.isSuccessful){
            Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show()
            when(userTypes.checkedRadioButtonId){
                R.id.administrator -> startActivity(Intent(this, MainActivity::class.java))
                R.id.clubManager -> startActivity(Intent(this, MainActivity::class.java))
                R.id.player -> startActivity(Intent(this, MainActivity::class.java))
            }
        }
        else{
            Toast.makeText(this, "Error! " + it.exception?.message, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.INVISIBLE
        }
    }
}