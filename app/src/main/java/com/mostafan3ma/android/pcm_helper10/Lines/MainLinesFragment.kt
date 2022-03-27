package com.mostafan3ma.android.pcm_helper10.Lines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
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


        linAdapter=LinesAdapter(LineListener {selectedLine->
           viewModel.navigateToSelectedLine(selectedLine)
        })
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


        return binding.root

    }



}