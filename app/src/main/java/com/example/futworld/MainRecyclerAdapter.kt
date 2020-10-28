package com.example.futworld

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.league_item.view.*

class MainRecyclerAdapter(
    private val context: Context,
    private val leagues: List<League>
) :
    RecyclerView.Adapter<MainRecyclerAdapter.LeagueViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LeagueViewHolder =
        LeagueViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.league_item, p0, false))


    override fun getItemCount(): Int = leagues.size

    override fun onBindViewHolder(p0: LeagueViewHolder, p1: Int) {
        p0.bindItem(leagues[p1])
    }


    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(league: League) {
            itemView.leagueName.text = league.name
        }
    }
}