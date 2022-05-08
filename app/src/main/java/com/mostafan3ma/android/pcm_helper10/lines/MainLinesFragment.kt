package com.mostafan3ma.android.pcm_helper10.lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLinesMainBinding


class MainLinesFragment : Fragment() {

    private  val  viewModel by viewModels<LinesViewModel>() {
        LinesViewModelFactory((requireContext().applicationContext as PcmApp).repository)
    }

    private lateinit var linAdapter:LinesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLinesMainBinding.inflate(inflater)
        binding.lifecycleOwner=this


        linAdapter=LinesAdapter(
            LineListener {selectedLine->
           viewModel.navigateToSelectedLine(selectedLine)
        },
            LongClickListener{line,view->
               viewModel.getLongClickedLine(line)
                showPopupMenu(view)
            }
        )
        viewModel.navigateToSelectedLine.observe(viewLifecycleOwner, Observer { selectedLine->
            if (selectedLine!=null){
                findNavController().navigate(MainLinesFragmentDirections.actionLinesMainFragmentToDetailsFragment(selectedLine))
                viewModel.navigateToSelectedLineComplete()
            }
        })

        viewModel.linesList.observe(viewLifecycleOwner, Observer {
            linAdapter.submitList(it)
        })

        binding.lineRecycler.adapter=linAdapter

        binding.fabAddLine.setOnClickListener {
            viewModel.navigateToAddLineFragment()
        }
        viewModel.navigateToAddLineFragment.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(MainLinesFragmentDirections.actionLinesMainFragmentToAddLineFragment())
                viewModel.navigateToAddLineFragmentCompleted()
            }
        })

        viewModel.undo.observe(viewLifecycleOwner, Observer {
            if (it){
                showUndoSnackBar()
                viewModel.undoCompleted()
            }
        })
        return binding.root

    }

    private fun showUndoSnackBar() {
        Snackbar.make(requireView(),"Deleted Successfully",Snackbar.LENGTH_SHORT)
            .setAction("UNDO") {
                viewModel.undoDeleting()
            }.show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu=PopupMenu(requireContext(),view)
        popupMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.delete_popup_option->{
                        viewModel.deleteLongClickedLine()
                        return true
                    }
                    R.id.edit_popup_option->{
                        Toast.makeText(requireContext(),"edit Popup option",Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }
        })
        popupMenu.inflate(R.menu.long_clicked_menu)
        try {
            val popup=PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible=true
            val menu=popup.get(popupMenu)
            menu.javaClass
                .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }catch (e:Exception){
                e.printStackTrace()
        }finally {
            popupMenu.show()
        }
        popupMenu.show()
    }



}