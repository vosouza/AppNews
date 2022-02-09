package com.evosouza.news.ui.home.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.evosouza.news.R
import com.evosouza.news.core.Status
import com.evosouza.news.databinding.FragmentSubjectChoseBinding
import com.evosouza.news.ui.home.subjects.viewmodel.SubjectChoseViewModel
import com.google.android.material.chip.Chip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SubjectChoseFragment : Fragment() {

    lateinit var binding: FragmentSubjectChoseBinding
    lateinit var viewModel: SubjectChoseViewModel
    lateinit var dataBase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSubjectChoseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBase = Firebase.database.reference
        viewModel = SubjectChoseViewModel.SubjectChooseViewModelProviderFactory(dataBase).create(SubjectChoseViewModel::class.java)

        observeViewModel()

        viewModel.getSubjects()
    }

    private fun observeViewModel(){
        viewModel.subjectList.observe(viewLifecycleOwner){ data ->
            when(data.status){
                Status.LOADING ->{
                    binding.scrollView2.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS->{
                    binding.progressBar.visibility = View.GONE
                    binding.scrollView2.visibility = View.VISIBLE
                    populateChipsView(data.data)
                }
                Status.ERROR->{
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun populateChipsView(chipList: List<String>?) {
        if (chipList != null) {
            for (item in chipList) {
                val chip = LayoutInflater.from(context).inflate(R.layout.chip_choice, binding.root, false) as Chip
                chip.text = item
                binding.chipGroup.addView(chip)
                chip.setOnClickListener { binding.btnNext.isEnabled = true }
            }
        }
    }
}