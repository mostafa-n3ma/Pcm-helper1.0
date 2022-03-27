package com.mostafan3ma.android.pcm_helper10.Lines.Operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLineDetailsBinding
class LineDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLineDetailsBinding.inflate(inflater)

        var args=LineDetailsFragmentArgs.fromBundle(requireArguments())

        binding.selectedLine=args.selectedLine

        return binding.root
    }


}