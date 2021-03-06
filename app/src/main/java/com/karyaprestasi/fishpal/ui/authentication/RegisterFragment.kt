package com.karyaprestasi.fishpal.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.karyaprestasi.fishpal.Constants
import com.karyaprestasi.fishpal.R
import com.karyaprestasi.fishpal.databinding.FragmentRegisterBinding
import com.karyaprestasi.fishpal.model.UserModel
import com.karyaprestasi.fishpal.ui.main_ui.MainActivity


class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener(this)
        binding.login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.registerBtn -> {
                binding.progressBar.visibility = View.VISIBLE

                if (binding.etName.text.toString().trim().isEmpty()) {
                    binding.etName.error = "Please enter name"
                    binding.etName.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etEmail.text.toString().trim().isEmpty()) {
                    binding.etEmail.error = "Please enter email"
                    binding.etEmail.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim())
                        .matches()
                ) {
                    binding.etEmail.error = "Please enter a valid email"
                    binding.etEmail.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etPassword.text.toString().trim().isEmpty()) {
                    binding.etPassword.error = "Please enter password"
                    binding.etPassword.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etPassword.text.toString().trim().length < 6) {
                    binding.etPassword.error = "Password less than 6 character"
                    binding.etPassword.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (!binding.term.isChecked) {
                    Toast.makeText(
                        context,
                        resources.getText(R.string.agreeTerm),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                cekEmailRegistered()


            }

            R.id.login -> {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, LoginFragment())
                    commit()
                }
            }


        }
    }

    private fun cekEmailRegistered() {
        //cek email has been registered?
        auth.fetchSignInMethodsForEmail(binding.etEmail.text.toString().trim())
            .addOnCompleteListener {
                Log.i("cekEmail", "cekEmailRegistered: ${it.result?.signInMethods?.size}")
                //cek email terdafar
                if (it.result?.signInMethods?.size!=0) {
                    Toast.makeText(context, "Email telah terdaftar!", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                } else {
                    createAccount()
                }
            }
    }

    private fun createAccount() {
        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString().trim(),
            binding.etPassword.text.toString().trim()
        )
            .addOnCompleteListener(AuthenticationActivity()) { task ->
                if (task.isSuccessful) {
                    //get user
                    user = auth.currentUser as FirebaseUser
                    user.sendEmailVerification().addOnSuccessListener {

                        fragmentManager?.beginTransaction()?.apply {
                            replace(
                                R.id.fl_wrapper,
                                VerifiedEmailFragment(),
                                VerifiedEmailFragment::class.java.simpleName
                            )
                            commit()
                        }

                    }.addOnFailureListener {
                        Log.d(Constants.TAG, "onFailure: Email not sent" + it.message)
                    }

                    //simpan di database Users
                    val reference = FirebaseDatabase.getInstance().reference.child("Users")
                        .child(user.uid)

                    val value = UserModel(
                        binding.etEmail.text.toString().trim(),
                        binding.etName.text.toString().trim(),
                        user.uid,
                        "",
                        "",
                        "",
                        "",
                        "false"
                    )

                    reference.setValue(value).addOnCompleteListener {
                        if (task.isSuccessful) {
                            binding.progressBar.visibility = View.INVISIBLE
                            fragmentManager?.beginTransaction()?.apply {
                                replace(R.id.fl_wrapper, VerifiedEmailFragment())
                                commit()
                            }
                        } else {
                            Toast.makeText(context, "Error from database", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }

                } else {
//                    Toast.makeText(
//                        context, "Email has been registered.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
    }
}