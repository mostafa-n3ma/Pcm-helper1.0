package com.mostafan3ma.android.pcm_helper10.lines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository

@Suppress("UNCHECKED_CAST")
class LinesViewModelFactory(
    private val repository: PipeLinesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (LinesViewModel(repository) as T)
}
