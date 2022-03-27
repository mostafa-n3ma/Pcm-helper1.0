package com.mostafan3ma.android.pcm_helper10.Lines.Operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mostafan3ma.android.pcm_helper10.Lines.Operations.Points.PointListener
import com.mostafan3ma.android.pcm_helper10.Lines.Operations.Points.PointsAdapter
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLineDetailsBinding
class LineDetailsFragment : Fragment() {
    private lateinit var pointsAdapter:PointsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLineDetailsBinding.inflate(inflater)
        var args=LineDetailsFragmentArgs.fromBundle(requireArguments())

        pointsAdapter= PointsAdapter((PointListener {
            Toast.makeText(requireContext(),"${it.db}",Toast.LENGTH_SHORT).show()
        }))
        pointsAdapter.submitList(args.selectedLine.points)
        binding.pointsRecycler.adapter=pointsAdapter





        binding.selectedLine=args.selectedLine

        return binding.root
    }


}