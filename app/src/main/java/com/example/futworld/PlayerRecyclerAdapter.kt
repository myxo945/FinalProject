package com.example.futworld

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.player_item.view.*
import org.w3c.dom.Text

class PlayerRecyclerAdapter(
    val modelClass: Class<Player>, var query: Query
):
    FirebaseRecyclerAdapter<Player, PlayerRecyclerAdapter.PlayerViewHolder>(
        FirebaseRecyclerOptions.Builder<Player>()
            .setQuery(query, modelClass)
            .build()
    ){

    var myListener: MyItemClickListener? = null

    interface MyItemClickListener {
        fun onItemClickedFromAdapter(position: Int)
        fun onItemLongClickedFromAdapter(position: Int)
    }

    fun setMyItemClickListener(listener: MyItemClickListener) {
        this.myListener = listener
    }

    inner class PlayerViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name: TextView = view.findViewById<TextView>(R.id.playerName)
        var age: TextView = view.findViewById(R.id.playerAge)
        var number: TextView = view.findViewById(R.id.playerNumber)
        var position: TextView = view.findViewById(R.id.playerPosition)
        var img: ImageView = view.findViewById(R.id.playerPhoto)

        init {
            view.setOnClickListener {
                if (myListener != null) {
                    if (adapterPosition !=
                        RecyclerView.NO_POSITION
                    ) {
                        myListener!!.onItemClickedFromAdapter(adapterPosition)
                    }
                }
            }
            view.setOnLongClickListener {
                if (myListener != null) {
                    if (adapterPosition !=
                        RecyclerView.NO_POSITION
                    ) {
                        myListener!!.onItemLongClickedFromAdapter(adapterPosition)
                    }
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int, player: Player) {
        holder.name.text = player.name
        holder.age.text = player.age
        holder.number.text = player.number
        holder.position.text = player.position
        holder.img.setImageResource(R.drawable.player_1)
    }


}