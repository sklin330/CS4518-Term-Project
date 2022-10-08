package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.dataclass.Achievement
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.viewmodel.achievement.AchievementSource

class AchievementDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog(context: Context, achievement: Achievement, isAchieved: Boolean) {
        dialog = Dialog(context)
        mContext = context

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_achievement)
        dialog.setCanceledOnTouchOutside(true)

        var achievementTitle: TextView = dialog.findViewById(R.id.achievement_title_text)
        var achievementImage: ImageView = dialog.findViewById(R.id.achievement_image)
        var achievementPoints: TextView = dialog.findViewById(R.id.achievment_points_text)

        achievementTitle.text = "Achievement: ${achievement.description}"
        achievementPoints.text = "+${achievement.totalPoints} points"
        achievementImage.alpha = if (isAchieved) 1.0f else 0.3f

        dialog.show()
    }

    fun getWindow(): Window? {
        return dialog.window
    }


}