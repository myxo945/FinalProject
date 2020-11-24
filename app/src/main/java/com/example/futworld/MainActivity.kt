package com.example.futworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
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


    private val firebaseRepo = MainFirebaseRepo()
    lateinit var arrayList: List<League>
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
                R.id.player -> {
                    Toast.makeText(applicationContext, "Player", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PlayerActivity::class.java))
                }
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
        storageReference = FirebaseStorage.getInstance().reference

        userId = fAuth.currentUser!!.uid

        // Load the league information
        arrayList = ArrayList<League>()

        val documentReference: DocumentReference = fStore.collection("users").document(userId)

        var userType: String = ""
        documentReference.addSnapshotListener{
                snapshots, e ->
            if (snapshots != null) {

                var profileRef: StorageReference = storageReference.child("users/$userId/profile.jpg")
                profileRef.downloadUrl.addOnSuccessListener {
                    Picasso.get().load(it).into(userImage)
                }
                userName.text = snapshots.getString("userName")
                userType = snapshots.getString("userType").toString()
            }
        }


        myAdapter = MainRecyclerAdapter(arrayList)

        firebaseRepo.getPostList().addOnCompleteListener {
            if (it.isSuccessful){
                arrayList = it.result!!.toObjects(League::class.java)
                myAdapter.leagues = arrayList
                myAdapter.notifyDataSetChanged()
            }
            else{
                Log.d("Error", "Error: ${it.exception!!.message}")
            }
        }
        mainRecycler.layoutManager = LinearLayoutManager(this)
        mainRecycler.adapter = myAdapter

        // Set a click listener on floating action button
//        if(userType == ""){
            create_tournament_btn.visibility = View.VISIBLE
//        }
//        else{
//            create_tournament_btn.visibility = View.INVISIBLE
//        }

        // Set up a floating action button to create new tournament
        create_tournament_btn.setOnClickListener {
            Toast.makeText(this, "Create a new tournament.", Toast.LENGTH_SHORT).show()
            val fm: FragmentManager = supportFragmentManager
            val createTournamentFragment: CreateTournamentFragment = CreateTournamentFragment()
            fm.beginTransaction().replace(R.id.container, createTournamentFragment).commit()
        }
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