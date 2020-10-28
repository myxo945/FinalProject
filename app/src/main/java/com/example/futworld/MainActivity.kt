package com.example.futworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    lateinit var userName: TextView
    lateinit var userImage: ImageView
    lateinit var fAuth: FirebaseAuth
    lateinit var fStore: FirebaseFirestore
    lateinit var storageReference: StorageReference

    lateinit var userId: String

    lateinit var arrayList: ArrayList<League>
    lateinit var myAdapter: MainRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the navigation menu
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.personalInfo -> {
                    Toast.makeText(applicationContext, "Personal Information", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PersonalInformationActivity::class.java))
                }
                R.id.tournament -> Toast.makeText(applicationContext, "Tournament", Toast.LENGTH_SHORT).show()
                R.id.club -> Toast.makeText(applicationContext, "Club", Toast.LENGTH_SHORT).show()
                R.id.player -> Toast.makeText(applicationContext, "Player", Toast.LENGTH_SHORT).show()
                R.id.logoutOption -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            }
            true
        }


        // Get the information from firebase
        val nav_header = findViewById<NavigationView>(R.id.navView)
        userName = nav_header.getHeaderView(0).findViewById(R.id.userName)
        userImage = nav_header.getHeaderView(0).findViewById(R.id.userImage)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference()

        userId = fAuth.currentUser!!.uid

        val documentReference: DocumentReference = fStore.collection("users").document(userId)
        documentReference.addSnapshotListener{
                snapshots, e ->
            if (snapshots != null) {

                var profileRef: StorageReference = storageReference.child("users/" + fAuth.currentUser!!.uid + "/profile.jpg")
                profileRef.downloadUrl.addOnSuccessListener {
                    Picasso.get().load(it).into(userImage)
                }
                userName.setText(snapshots.getString("userName"))
            }
        }

        // Load the league information
        arrayList = ArrayList<League>()
        arrayList.add(
            League("0", "Tournament A", "0")
        )
        arrayList.add(
            League("1", "Tournament B", "1")
        )
        arrayList.add(
            League("2", "Tournament C", "2")
        )

        myAdapter = MainRecyclerAdapter(this,arrayList)
        mainRecycler.layoutManager = LinearLayoutManager(this)
        mainRecycler.adapter = myAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}