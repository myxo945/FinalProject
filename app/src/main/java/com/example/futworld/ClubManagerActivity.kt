package com.example.futworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class ClubManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_manager)
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}