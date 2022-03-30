package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.PointListener
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.PointsAdapter
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLineDetailsBinding
import kotlin.math.min

class LineDetailsFragment : Fragment() {
    private lateinit var pointsAdapter: PointsAdapter
    private lateinit var binding: FragmentLineDetailsBinding
    private lateinit var pointBottomSheet: BottomSheetBehavior<LinearLayout>
    val viewModel by viewModels<LineDetailsViewModel>() {
        LineDetailsViewModel.LineDetailsViewModelFactory((requireContext().applicationContext as PcmApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLineDetailsBinding.inflate(inflater)
        var args = LineDetailsFragmentArgs.fromBundle(requireArguments())
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.selectedLine = args.selectedLine
        viewModel.getSelectedLine(args.selectedLine)
        pointsAdapter = PointsAdapter((PointListener {
            Toast.makeText(requireContext(), "${it.db}", Toast.LENGTH_SHORT).show()
        }))
        pointsAdapter.submitList(args.selectedLine.points)
        binding.pointsRecycler.adapter = pointsAdapter
        pointBottomSheet = setUpBottomSheet()
        viewModel.closeBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it){
                pointBottomSheet.state=BottomSheetBehavior.STATE_COLLAPSED
                viewModel.closeBottomSheetCompleted()
            }
        })

        viewModel.openBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it){
                pointBottomSheet.state=BottomSheetBehavior.STATE_EXPANDED
                viewModel.openBottomSheetCompleted()
            }
        })



        setUpNumberPickers()



        binding.fabAddPoint.setOnClickListener {
            initBottomSheetBehavior(pointBottomSheet)
        }



        return binding.root
    }

    private fun initBottomSheetBehavior(pointBottomSheet: BottomSheetBehavior<LinearLayout>) {
        binding.fabAddPoint.visibility = View.GONE
        viewModel.openBottomSheet()
        binding.bottomCancelBtn.setOnClickListener {
            viewModel.closeBottomSheet()
            binding.fabAddPoint.visibility = View.VISIBLE
        }
    }

    private fun setUpBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val pointBottomSheet = BottomSheetBehavior.from(binding.pointBottomSheetLayout).apply {
            isDraggable = false
        }
        return pointBottomSheet
    }


    private fun setUpNumberPickers() {
        binding.dpPicker1.apply {
            maxValue = 8
            minValue = 2
        }
        binding.dpPicker2.apply {
            maxValue = 9
            minValue = 0
        }
        binding.depthPicker1.apply {
            maxValue = 3
            minValue = 0
        }
        binding.depthPicker2.apply {
            maxValue = 9
            minValue = 0
        }
        binding.depthPicker3.apply {
            maxValue = 9
            minValue = 0
        }
        binding.current1NumberPicker.apply {
            maxValue = 1000
            minValue = 50
        }
        binding.current2NumberPicker.apply {
            maxValue = 1000
            minValue = 50
        }
    }


}