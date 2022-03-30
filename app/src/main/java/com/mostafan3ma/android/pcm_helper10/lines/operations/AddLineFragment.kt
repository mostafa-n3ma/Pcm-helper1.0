package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentAddLineBinding

class AddLineFragment :Fragment(){
    val viewModel by viewModels<AddLineViewModel>() {
        AddLineViewModel.AddLineViewModelFactory((requireContext().applicationContext as PcmApp).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentAddLineBinding.inflate(inflater)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                findNavController()
                    .navigate(AddLineFragmentDirections.actionAddLineFragmentToDetailsFragment(it))
                viewModel.navigateToDetailsComplete()
            }
        })

        binding.button.setOnClickListener {
            viewModel.addLineToDatabaseAndNavigateToDetails()
        }

        return binding.root
    }




}