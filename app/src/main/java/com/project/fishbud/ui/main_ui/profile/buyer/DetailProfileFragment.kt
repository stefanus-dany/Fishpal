package com.project.fishbud.ui.main_ui.profile.buyer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentDetailProfileBinding
import java.util.concurrent.Executors

class DetailProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDetailProfileBinding
    private lateinit var storage: StorageReference
    private lateinit var user: FirebaseUser
    private var uriImage: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storage = FirebaseStorage.getInstance().reference
        user = FirebaseAuth.getInstance().currentUser as FirebaseUser

        val sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            binding.etFullname.setText(sharedPreferences.getString(Constants.USER_NAME, ""))
            binding.etCity.setText(sharedPreferences.getString(Constants.CITY_USER, ""))
            binding.etBirthday.setText(sharedPreferences.getString(Constants.BIRTHDAY_USER, ""))
            binding.etAddress.setText(sharedPreferences.getString(Constants.ADDRESS_USER, ""))
            val url = sharedPreferences.getString(Constants.URL_PROFILE_IMAGE_USER, "")
            if (url != "") {
                context?.let {
                    Glide.with(it)
                        .load(url)
                        .into(binding.profileImage)
                }
            }
        }
        binding.btnUpdateProfile.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriImage = data?.data
        if (uriImage !== null) {
            binding.profileImage.setImageURI(uriImage)
        }
    }

    private fun uploadPicture(imageURI: Uri?) {
        var storePictureToDatabase = false
        var urlProfileImage = ""
        val tmp: StorageReference =
            storage.child("ProfilePictureUser/${user.uid}.jpg")
        if (imageURI != null) {
            storePictureToDatabase = true
            tmp.putFile(imageURI).addOnSuccessListener {
                //getImageUrl
                tmp.downloadUrl.addOnSuccessListener {
                    urlProfileImage = it.toString()
                    //store to database
                    storeToDatabase(urlProfileImage, storePictureToDatabase)
                }

            }.addOnFailureListener {
                Toast.makeText(context, "Error upload image", Toast.LENGTH_SHORT).show()
            }
        } else {
            storeToDatabase(urlProfileImage, storePictureToDatabase)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.profile_image -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
            R.id.btn_updateProfile -> {
                binding.progressBar.visibility = View.VISIBLE
                uploadPicture(uriImage)
            }
        }
    }

    private fun storeToDatabase(urlProfileImage: String, storePictureToDatabase: Boolean) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val name =
                    FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                        .child("name")
                name.setValue(binding.etFullname.text.toString())

                val city =
                    FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                        .child("city")
                city.setValue(binding.etCity.text.toString())

                val birthday =
                    FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                        .child("birthday")
                birthday.setValue(binding.etBirthday.text.toString())

                if (storePictureToDatabase) {
                    val urlProfileImage2 =
                        FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                            .child("urlProfileImage")
                    urlProfileImage2.setValue(urlProfileImage)
                }

                val address =
                    FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                        .child("address")
                address.setValue(binding.etAddress.text.toString())

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {

            }
        }
        binding.progressBar.visibility = View.GONE
        Toast.makeText(context, "Profil berhasil diupdate!", Toast.LENGTH_SHORT)
            .show()
    }


}