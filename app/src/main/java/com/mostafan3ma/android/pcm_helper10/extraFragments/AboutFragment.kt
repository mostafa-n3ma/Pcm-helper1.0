package com.mostafan3ma.android.pcm_helper10.extraFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

     binding=FragmentAboutBinding.inflate(inflater)

        return binding.root
    }

    @VisibleForTesting
    fun visibleBottomTxt(isDisplayed:Boolean){
        when(isDisplayed){
            true-> binding.bottomTxt.visibility=View.VISIBLE
            false-> binding.bottomTxt.visibility=View.GONE
        }
    }

}