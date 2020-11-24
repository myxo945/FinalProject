package com.example.futworld

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.league_item.view.*

class MainRecyclerAdapter(
    var leagues: List<League>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LeagueViewHolder =
        LeagueViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.league_item, p0, false))


    override fun getItemCount(): Int = leagues.size


    class LeagueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(league: League) {
            itemView.leagueName.text = league.title

            Glide.with(itemView.context).load(league.badge).into(itemView.leagueBadge)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LeagueViewHolder).bindItem(leagues[position])
    }
}