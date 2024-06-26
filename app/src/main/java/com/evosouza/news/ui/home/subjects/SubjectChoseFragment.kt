package com.evosouza.news.ui.home.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evosouza.news.R
import com.evosouza.news.core.Status
import com.evosouza.news.data.firebase.FirebaseDataSourceImpl
import com.evosouza.news.data.model.SubjectsModel
import com.evosouza.news.data.sharedpreference.SharedPreference
import com.evosouza.news.databinding.FragmentSubjectChoseBinding
import com.evosouza.news.ui.home.subjects.viewmodel.SubjectChoseViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers


class SubjectChoseFragment : Fragment() {

    private var _binding: FragmentSubjectChoseBinding? = null
    private val binding: FragmentSubjectChoseBinding get() = _binding!!
    lateinit var viewModel: SubjectChoseViewModel
    private lateinit var interests: SubjectsModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSubjectChoseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cache = SharedPreference(requireContext())
        val data = FirebaseDataSourceImpl()
        viewModel = SubjectChoseViewModel.SubjectChooseViewModelProviderFactory(Dispatchers.IO, data, cache)
            .create(SubjectChoseViewModel::class.java)

        observeViewModel()
        setButtonClick()
        viewModel.getSubjects()

    }

    private fun setButtonClick() {
        binding.btnNext.setOnClickListener {
            viewModel.saveInterestsList(getSelectedChips())
            findNavController().popBackStack()
        }
    }

    private fun getSelectedChips(): SubjectsModel {
        val list = mutableListOf<String>()
        binding.chipGroup.checkedChipIds.forEach { index ->
            val n = (index % (interests.subjects.count()+1)) -1
            this.binding.chipGroup.getChildAt(n)?.let { chip ->
                val text = ( chip as Chip).text
                list.add(text.toString())
            }
        }
        return SubjectsModel(list)
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
                    data.data?.let {
                        interests = it
                        populateChipsView(it)
                    }
                }
                Status.ERROR->{
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun populateChipsView(list: SubjectsModel) {
        val chipList = list.subjects
        chipList.let {
            for (item in chipList) {
                val chip = LayoutInflater.from(context)
                    .inflate(R.layout.chip_choice, binding.root, false) as Chip
                chip.text = item
                binding.chipGroup.addView(chip)
                chip.setOnClickListener { binding.btnNext.isEnabled = true }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}