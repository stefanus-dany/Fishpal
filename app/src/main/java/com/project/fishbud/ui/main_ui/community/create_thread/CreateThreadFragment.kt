package com.project.fishbud.ui.main_ui.community.create_thread

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentCreateThreadBinding
import com.project.fishbud.ui.main_ui.community.CommunityEntity

class CreateThreadFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentCreateThreadBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var sharedPreferences: SharedPreferences
    private var alphabet: List<Char> = emptyList()
    private var idThread: String
    private var nameUser = ""
    private var urlImage = ""

    init {
        alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        idThread = List(20) { alphabet.random() }.joinToString("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateThreadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE) as SharedPreferences
        nameUser = sharedPreferences.getString(Constants.USER_NAME, "").toString()
        binding.userName.text = nameUser
        urlImage = sharedPreferences.getString(Constants.URL_PROFILE_IMAGE_USER, "").toString()
        context?.let {
            Glide.with(it)
                .load(urlImage)
                .into(binding.civImage)
        }
        binding.share.setOnClickListener(this)
        binding.btnBack.setOnClickListener{
            fragmentManager?.popBackStack()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.share -> {
                binding.progressBar.visibility = View.VISIBLE
                if (binding.descCreateThread.text.toString().trim().isEmpty()) {
                    binding.descCreateThread.error = "Tuliskan utasan Anda!"
                    binding.descCreateThread.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                storeToDatabase()
            }
        }
    }

    private fun storeToDatabase() {
        val reference = FirebaseDatabase.getInstance().reference.child("Thread")
            .child(idThread)

        val value = CommunityEntity(
            user.uid,
            nameUser,
            idThread,
            binding.descCreateThread.text.toString(),
            urlImage
        )

        reference.setValue(value).addOnCompleteListener {
            if (it.isSuccessful) {
                binding.progressBar.visibility = View.INVISIBLE
                fragmentManager?.popBackStack()
            } else {
                Toast.makeText(context, "Error from database", Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }
}