package com.example.futworld

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_player.*

var query = FirebaseDatabase.getInstance()
    .reference
    .child("players")
    .limitToLast(50)

class PlayerFragment : Fragment(), PlayerRecyclerAdapter.MyItemClickListener {

    var idx: Int = 0
    private val playersList: ArrayList<Player>? = null
    lateinit var myAdapter: PlayerRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null) {
            idx = savedInstanceState.getInt("index")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(view.context)
        rv.hasFixedSize()
        rv.layoutManager = layoutManager
        myAdapter = PlayerRecyclerAdapter(Player::class.java,query)
        myAdapter.setMyItemClickListener(this)
        rv.adapter = myAdapter
    }

    override fun onItemClickedFromAdapter(position: Int) {
        idx = position
        var playerDetailFragment = PlayerDetailFragment()
        val bundle = Bundle()
        bundle.putInt("index", idx)
        playerDetailFragment.setArguments(bundle)
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()
        transaction?.replace(R.id.fg, playerDetailFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun onItemLongClickedFromAdapter(position: Int) {
        Toast.makeText(activity, "You Click the $position player!", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", idx)
    }

    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }
}