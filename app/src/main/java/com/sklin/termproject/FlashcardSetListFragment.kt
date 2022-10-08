package com.sklin.termproject

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.adapter.FlashcardSetAdapter
import com.sklin.termproject.databinding.FragmentFlashcardSetListBinding
import com.sklin.termproject.viewmodel.flashcard.FlashcardSetListViewModel

private const val TAG = "FlashcardSetListFragment"

class FlashcardSetListFragment : Fragment() {

    private var _binding: FragmentFlashcardSetListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FlashcardSetListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var flashcardSetAdapter: FlashcardSetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(FlashcardSetListViewModel::class.java)

        _binding = FragmentFlashcardSetListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.flashcardSetRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        flashcardSetAdapter = FlashcardSetAdapter(viewModel.getFlashcardSetList(), context)
        recyclerView.adapter = flashcardSetAdapter

        val flashcardLiveData = viewModel.getLiveFlashcardSetList()

        flashcardLiveData.observe(viewLifecycleOwner) {
            it?.let {
                flashcardSetAdapter = FlashcardSetAdapter(it, context)
                recyclerView.adapter = flashcardSetAdapter
            }
        }
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.flashcard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add -> context?.let {
                var addFlashcardSetDialog =  AddFlashcardSetDialog()
                addFlashcardSetDialog.showDialog(it)
                addFlashcardSetDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.white_card_background)
            };
        }
        return NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}