package com.sklin.termproject

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.adapter.AchievementAdapter
import com.sklin.termproject.adapter.UserAdapter
import com.sklin.termproject.databinding.FragmentAchievementBinding
import com.sklin.termproject.viewmodel.achievement.AchievementSource
import com.sklin.termproject.viewmodel.achievement.AchievementViewModel

class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AchievementViewModel
    private lateinit var leaderboardRecyclerView: RecyclerView
    private lateinit var achievementRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var achievementAdapter: AchievementAdapter

    private lateinit var achievementSource: AchievementSource

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(AchievementViewModel::class.java)

        achievementSource = AchievementSource.getDataSource()

        //Set up recycler view for leaderboard
        leaderboardRecyclerView = binding.leaderboardRecyclerView
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(activity)
        userAdapter = UserAdapter(viewModel.getUserList())
        leaderboardRecyclerView.adapter = userAdapter

        val userLiveData = viewModel.getLiveUserList()
        userLiveData.observe(viewLifecycleOwner) {
            it?.let {
                userAdapter = UserAdapter(it)
                leaderboardRecyclerView.adapter = userAdapter
            }
        }

        //Set up recycler view for achievements
        achievementRecyclerView = binding.achievementRecyclerView
        achievementRecyclerView.layoutManager = GridLayoutManager(activity, 3)

        val achievementLiveData = achievementSource.getLiveAchievementMap()

        achievementLiveData.observe(viewLifecycleOwner) {
            it?.let {
                achievementAdapter = AchievementAdapter(achievementSource.getAchievementList(), it)
                achievementRecyclerView.adapter = achievementAdapter
            }
        }

        var userData = achievementSource.getLiveUserData()

        userData.observe(viewLifecycleOwner) {
            it?.let {
//                achievementSource.updateStatsAndAchievements()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}