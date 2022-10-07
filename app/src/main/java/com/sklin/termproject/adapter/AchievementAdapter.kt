package com.sklin.termproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.R
import com.sklin.termproject.dataclass.Achievement
import java.util.HashMap

class AchievementAdapter (private val data: List<Achievement>, private val achievementMap: HashMap<String, Boolean>) :
    RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var achievement: Achievement

        private val name: TextView = view.findViewById(R.id.achievment_name_text)
        private val image: ImageView = view.findViewById(R.id.achievement_image)

        fun bind(achievement: Achievement, isAchieved: Boolean) {
            this.achievement = achievement
            name.text = achievement.name

            image.alpha = if (isAchieved) 1.0f else 0.3f
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_achievement, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val achievement = data[position]
        val isAchieved: Boolean = achievementMap.getOrDefault(achievement.id, false)
        viewHolder.bind(achievement, isAchieved)
    }

    override fun getItemCount() = data.size
}