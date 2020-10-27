package com.example.futworld

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class PersonalInformationActivity : AppCompatActivity() {

    lateinit var userName: TextView
    lateinit var fAuth: FirebaseAuth
    lateinit var fStore: FirebaseFirestore
    lateinit var userId: String
    lateinit var user: FirebaseUser
    lateinit var saveBtn: Button
    lateinit var profileImage: ImageView
    lateinit var storageReference: StorageReference

    lateinit var newUserName: EditText
    lateinit var newEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        profileImage = findViewById(R.id.profileImage)
        saveBtn = findViewById(R.id.saveBtn)

        // Get the information from firebase
        userName = findViewById(R.id.userName)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference()

        var profileRef: StorageReference = storageReference.child("users/" + fAuth.currentUser!!.uid + "/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(profileImage)
        }

        user = fAuth.currentUser!!

        userId = fAuth.currentUser!!.uid

        val documentReference: DocumentReference = fStore.collection("users").document(userId)
        documentReference.addSnapshotListener{
                snapshots, e ->
            if (snapshots != null) {
                userName.setText(snapshots.getString("userName"))
            }
        }

        // Change the profile image
        profileImage.setOnClickListener {
            // open gallery
            var openGalleryIntent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent, 1000)
        }

        val data = intent
        var editUserName: String = data.getStringExtra("editUserName").toString()
        // Change the user name
        newUserName = findViewById(R.id.editUserName)
        newEmail = findViewById(R.id.editEmail)

        saveBtn.setOnClickListener {
            if(newUserName.text.toString().isEmpty() || newEmail.text.toString().isEmpty()){
                Toast.makeText(this, "One or more fields are empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editedEmail: String = newEmail.text.toString()
            user.updateEmail(editedEmail).addOnSuccessListener {
                var docRef: DocumentReference = fStore.collection("users").document(user.uid)
                var edited: HashMap<String, Any> = HashMap<String, Any>()
                edited.put("email", editedEmail)
                edited.put("userName", newUserName.text.toString())
                docRef.update(edited).addOnSuccessListener {
                    Toast.makeText(this, "Profile changed.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                var imageUri: Uri = data!!.data!!
                // profileImage.setImageURI(imageUri)

                uploadImageToFirebase(imageUri)


            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        // Upload image to firebase storage
        var fileRef: StorageReference = storageReference.child("users/" + fAuth.currentUser!!.uid + "/profile.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(profileImage)
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show()
        }
    }
}