package com.example.futworld

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateTournamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTournamentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var storageReference: StorageReference
    lateinit var fAuth: FirebaseAuth
    lateinit var fStore: FirebaseFirestore
    lateinit var user: FirebaseUser
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_create_tournament, container, false)
        var tournamentTitle = v.findViewById<EditText>(R.id.tournamentTitle)
        val createTournamentBtn = v.findViewById<Button>(R.id.button)
        createTournamentBtn.setOnClickListener {

            storageReference = FirebaseStorage.getInstance().reference
            fAuth = FirebaseAuth.getInstance()
            fStore = FirebaseFirestore.getInstance()
            user = fAuth.currentUser!!

            val title: String = tournamentTitle.text.toString().trim()

            if(TextUtils.isEmpty(title)){
                tournamentTitle.setError("Tournament title is required.")
                return@setOnClickListener
            }

            userId = user.uid

            // Create tournament to firebase storage
            val documentReference = fStore.collection("tournaments").document(title)
            val userDocumentReference = fStore.collection("users").document(userId).collection("tournaments").document(title)

            var tournament: HashMap<String, Any> = HashMap<String, Any>()
            tournament.put("title", title)
            tournament.put("createrId", userId)
            tournament.put("size", 4)
            tournament.put("badge", "https://firebasestorage.googleapis.com/v0/b/futworld-6bc7f.appspot.com/o/download.jpg?alt=media&token=836936cc-cd58-4da0-9a89-ac5325637e57")
            documentReference.set(tournament).addOnSuccessListener {
                Log.d("Success", "onSuccess: A new tournament is created.")
                Toast.makeText(context, "A new tournament is created.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Log.d("Fail", "onFailure: " + it.toString())
            }
            userDocumentReference.set(tournament).addOnSuccessListener {
                Log.d("Success", "onSuccess: A new tournament is created.")
            }.addOnFailureListener {
                Log.d("Fail", "onFailure: " + it.toString())
            }

            // get back to the main activity
            startActivity(Intent(context, MainActivity::class.java))
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateTournamentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateTournamentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}