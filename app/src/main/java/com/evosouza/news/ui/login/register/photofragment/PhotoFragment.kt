package com.evosouza.news.ui.login.register.photofragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.evosouza.news.core.Status
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.model.User
import com.evosouza.news.databinding.FragmentPhotoBinding
import com.evosouza.news.ui.login.register.photofragment.photoviewmodel.PhotoViewModel

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var user: User
    private var isImageUri: Uri? = null
    private val RESULT_LOAD_IMAGE: Int = 999

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("user") as User
        Log.e("TAG", "onViewCreated: $user" )

        val userDB = UserRepositoryImpl(NewsDB(requireContext()))
        viewModel = PhotoViewModel.PhotoViewModelProviderFactory(userDB).create(PhotoViewModel::class.java)

        buttonClickListeners()
        setViewModelListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE){
            val uri = data?.data
            viewModel.setImageUri(uri)
        }else{
            viewModel.setImageUri(null)
        }
    }

    private fun setViewModelListeners() {
        viewModel.photo.observe(viewLifecycleOwner){ image ->
            when(image.status){
                Status.SUCCESS ->{
                    image.data?.let { imageUri ->
                        binding.profileImage.setImageURI(imageUri)
                        isImageUri = imageUri
                    }
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(), "Error getting image", Toast.LENGTH_SHORT).show()
                    isImageUri = null
                }
            }
        }
    }

    private fun setUserImage(imageResId: Uri) {
        //            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        //            binding.profileImage.setImageBitmap(bitmap)
    }

    private fun saveUser() {
        if(isImageUri != null) user.photo = isImageUri!!.path
        viewModel.saveUser(user)
    }

    private fun buttonClickListeners() {
        binding.profileImage.setOnClickListener {
            Intent(Intent.ACTION_PICK).run {
                type = "image/"
                startActivityForResult(this, RESULT_LOAD_IMAGE)
            }
        }

        binding.buttonNext.setOnClickListener {
            if (isImageUri != null){
                saveUser()
            }else{
                showDialog()
            }
        }

        binding.buttonPrevious.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setMessage("You have not choose a profile picture")
            setPositiveButton("Yes", nextScreen)
            setNegativeButton("Cancel", cancelDialog)
            setTitle("Do you want to continue?")
        }
        builder.create()
        builder.show()
    }

    private val nextScreen: DialogInterface.OnClickListener? =
        DialogInterface.OnClickListener{ _, _ ->
            Log.e("TAG", "dialog yes", )
        }

    private val cancelDialog: DialogInterface.OnClickListener? =
        DialogInterface.OnClickListener{ dialog, _ ->
            Log.e("TAG", "dialog cancel", )
            dialog.cancel()
        }

}