package com.samsung.android.notepad

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import com.samsung.android.notepad.databinding.FragmentEditorBinding

class EditorFragment : Fragment() {
    private lateinit var viewModel: EditorViewModel
    private val args: EditorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)
        viewModel.setNoteId(args.noteId)

        if (args.noteId == 0)
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "New Note"
        else
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Edit Note"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> saveNote()
                    else -> menuItem.onNavDestinationSelected(findNavController())
                }
            }
        }, viewLifecycleOwner)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveNote()
                }
            }
        )
/*
        val binding: FragmentEditorBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_editor, container, false)

 */
        val binding = FragmentEditorBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    private fun saveNote(): Boolean {
        //Toast.makeText(context, "saveNote", Toast.LENGTH_SHORT).show()
        viewModel.saveNote()
        findNavController().navigateUp()
        return true
    }
}