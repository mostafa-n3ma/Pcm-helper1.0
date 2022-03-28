package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentAddLineBinding

class AddLineFragment :Fragment(){
    private lateinit var viewModel: AddLineViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentAddLineBinding.inflate(inflater)
        viewModel=ViewModelProvider(this).get(AddLineViewModel::class.java)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel


        binding.button.setOnClickListener {
            Toast.makeText(requireContext(),"${viewModel.name.value}" +
                    ":${viewModel.ogm.value}" +
                    ":${viewModel.type.value}" +
                    ":${viewModel.length.value}" +
                    ":${viewModel.work_date.value}"
                ,Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }




}