package com.evosouza.news.ui.home.subjects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evosouza.news.R
import com.evosouza.news.databinding.FragmentSubjectChoseBinding
import com.evosouza.news.ui.home.subjects.viewmodel.SubjectChoseViewModel
import com.google.android.material.chip.Chip

class SubjectChoseFragment : Fragment() {

    lateinit var binding: FragmentSubjectChoseBinding
    lateinit var viewModel: SubjectChoseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSubjectChoseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = SubjectChoseViewModel.SubjectChooseViewModelProviderFactory().create(SubjectChoseViewModel::class.java)
        populateChipsView()
    }

    private fun populateChipsView(){
        val chipList = viewModel.getSubjects()
        for (item in chipList) {
            val chip = LayoutInflater.from(context).inflate(R.layout.chip_choice, binding.root, false) as Chip
            chip.text = item
            binding.chipGroup.addView(chip)
            chip.setOnClickListener { binding.btnNext.isEnabled = true }
        }
    }
}