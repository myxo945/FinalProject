package com.example.futworld

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.security.AccessController.getContext
import java.util.*
import kotlin.collections.HashMap

class Register : AppCompatActivity() {

    // Declare all the components we create in the register activity
    lateinit var userName: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText

    lateinit var userTypes: RadioGroup
    lateinit var userType: String
    lateinit var registerBtn: Button
    lateinit var loginBtn: TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar

    lateinit var fStore: FirebaseFirestore
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userName = findViewById(R.id.userName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        userTypes = findViewById(R.id.RGroup)
        registerBtn = findViewById(R.id.registerBtn)
        loginBtn = findViewById(R.id.login)

        firebaseAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        progressBar = findViewById(R.id.progressBar)

        // Check whether the user is already login.
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Radio group
        userTypes.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{
            group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            userType = radio.text.toString()
            Toast.makeText(applicationContext,"User type:"+
                    " ${radio.text}",
                Toast.LENGTH_SHORT).show()
        })

        // Handle the register button
        registerBtn.setOnClickListener {
            onClick(it)
        }

        loginBtn.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }

    }

    private fun onClick(it: View?) {
        var str_userName: String = userName.text.toString().trim()
        var str_email: String = email.text.toString().trim()
        var str_password: String = password.text.toString().trim()
        var str_confirmPassword: String = confirmPassword.text.toString().trim()

        // Set up the error messages.
        if (TextUtils.isEmpty(str_userName)) {
            userName.setError("User name is required.")
            return
        }
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
        if (TextUtils.isEmpty(str_confirmPassword) || !TextUtils.equals(
                str_confirmPassword,
                str_password
            )
        ) {
            confirmPassword.setError("Password and confirm password does not match.")
            return
        }

        // check the user type.
        if(userTypes.checkedRadioButtonId == -1){
            Toast.makeText(applicationContext, "Please select a user type.", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        // Register the user in the firebase
        firebaseAuth.createUserWithEmailAndPassword(str_email, str_password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "User created.", Toast.LENGTH_SHORT).show()

                    userId = firebaseAuth.currentUser!!.uid
                    val documentReference: DocumentReference = fStore.collection("users").document(userId)

                    var user: HashMap<String, Any> = HashMap<String, Any>()
                    user.put("userName", str_userName)
                    user.put("email", str_email)
                    user.put("userType", userType)
                    documentReference.set(user).addOnSuccessListener(OnSuccessListener {
                        Log.d("Success","onSuccess: user profile is created for " + userId)
                    }).addOnFailureListener {
                        Log.d("Fail", "onFailure: " + it.toString())
                    }

                    when(userTypes.checkedRadioButtonId){
                        R.id.administrator -> startActivity(Intent(this, MainActivity::class.java))
                        R.id.clubManager -> startActivity(Intent(this, ClubManagerActivity::class.java))
                        R.id.player -> startActivity(Intent(this, PlayerActivity::class.java))
                    }
                }
                else{
                    Toast.makeText(this, "Error! " + it.exception?.message, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
    }
}

