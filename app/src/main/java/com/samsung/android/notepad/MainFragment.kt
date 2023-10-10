package com.samsung.android.notepad

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.samsung.android.notepad.data.NoteEntity
import com.samsung.android.notepad.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment(), NoteListAdapter.ListItemListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (viewModel.hasSelectedNotes())
                    menuInflater.inflate(R.menu.menu_main_selected, menu)
                else
                    menuInflater.inflate(R.menu.menu_main, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_sample -> addSampleNotes()
                    R.id.action_delete_all -> deleteAllNotes()
                    R.id.action_delete -> deleteSelectedNotes()
                    else -> false
                }
            }
        }, viewLifecycleOwner)

/*
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)*/
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.mainFragment = this

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        adapter = NoteListAdapter(this)
        binding.recyclerView.adapter = adapter
        //viewModel.addSampleNotes()

        return binding.root
    }

    private fun deleteSelectedNotes(): Boolean {
        viewModel.deleteSelectedNotes()
        lifecycleScope.launch {
            while (true) {
                if (!viewModel.hasSelectedNotes()) {
                    requireActivity().invalidateOptionsMenu()
                    break
                }
            }
        }
        return true
    }

    private fun deleteAllNotes(): Boolean {
        viewModel.deleteAllNotes()
        return true
    }

    private fun addSampleNotes(): Boolean {
        viewModel.addSampleNotes()
        return true
    }

    override fun onItemClicked(noteId: Int) {
        //Toast.makeText(context, "$noteId clicked", Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditorFragment(noteId, "Edit Note")
        )
    }

    override fun onItemSelected(note: NoteEntity) {
        viewModel.selectNote(note)
        adapter.notifyDataSetChanged()
        requireActivity().invalidateOptionsMenu()
    }

    fun createNote() {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToEditorFragment(NEW_NOTE_ID, "New Note")
        )
    }
}