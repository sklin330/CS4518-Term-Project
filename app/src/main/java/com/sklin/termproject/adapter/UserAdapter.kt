package com.sklin.termproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.R
import com.sklin.termproject.dataclass.User

class UserAdapter (private val data: List<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var user: User

        private val name: TextView = view.findViewById(R.id.username_text)
        private val points: TextView = view.findViewById(R.id.points_text)
        private val rank: TextView = view.findViewById(R.id.rank_text)

        fun bind(user: User, position: Int) {
            this.user = user
            name.text = user.username
            points.text = user.totalPoints.toString() + " points"
            rank.text = "Rank ${position + 1}"
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_user, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = data[position]
        viewHolder.bind(user, position)
    }

    override fun getItemCount() = data.size
}