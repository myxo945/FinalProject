package com.example.futworld

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainFirebaseRepo {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    // Auth
    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun createUser(): Task<AuthResult> {
        return firebaseAuth.signInAnonymously()
    }

    // firestore
    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("tournaments").get()
    }
}