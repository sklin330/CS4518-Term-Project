package com.sklin.termproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.adapter.EXTRA_SET_ID
import com.sklin.termproject.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Firebase.auth.signOut()
        val intent = Intent(context, LoginActivity::class.java)
        context?.startActivity(intent)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}