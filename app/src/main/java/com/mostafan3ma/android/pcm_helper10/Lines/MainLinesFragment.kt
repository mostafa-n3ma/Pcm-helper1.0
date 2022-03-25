package com.mostafan3ma.android.pcm_helper10.Lines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.data.source.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLine
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLinesMainBinding

class MainLinesFragment : Fragment() {

    private lateinit var linAdapter:LinesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLinesMainBinding.inflate(inflater)
        binding.lifecycleOwner=this

        val list= listOf(
            PipeLine(1,"ADM2-7H","OGM7","1000m","OIL","900mA","500mA","568724","3593445","2/5/2022", mutableListOf(DamagePoint("45","1.3","500mA","400mA","568774","3592225"))),
            PipeLine(1,"AD1-9H","OGM7","1000m","OIL","900mA","500mA","568724","3593445","2/5/2022", mutableListOf(DamagePoint("45","1.3","500mA","400mA","568774","3592225"))),
            PipeLine(1,"ADM1-2-9H","OGM7","1000m","OIL","900mA","500mA","568724","3593445","2/5/2022", mutableListOf(DamagePoint("45","1.3","500mA","400mA","568774","3592225"))),
            PipeLine(1,"ADM4-12H","OGM7","1000m","OIL","900mA","500mA","568724","3593445","2/5/2022", mutableListOf(DamagePoint("45","1.3","500mA","400mA","568774","3592225"))),
            PipeLine(1,"AD-13H","OGM7","1000m","OIL","900mA","500mA","568724","3593445","2/5/2022", mutableListOf(DamagePoint("45","1.3","500mA","400mA","568774","3592225")))
        )
        linAdapter=LinesAdapter(LineListener {
            Toast.makeText(this.context,"clicking on  : ${it.name}",Toast.LENGTH_SHORT).show()
        })
        linAdapter.submitList(list)

        binding.lineRecycler.adapter=linAdapter


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}