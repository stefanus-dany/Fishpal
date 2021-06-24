package com.project.fishbud.ui.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.fishbud.Companion
import com.project.fishbud.ui.main_ui.MainActivity
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var sharedPreferences: SharedPreferences
    private var moveFromVerifiedEmailToLogin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = this.activity?.getSharedPreferences(
            "sharedPrefs",
            Context.MODE_PRIVATE
        ) as SharedPreferences
        val emailSaved = sharedPreferences.getString(Companion.CHECK_EMAIL, null)
        val rememberMe = sharedPreferences.getBoolean(Companion.REMEMBER_ME, false)
//        moveFromVerifiedEmailToLogin =
//            intent.getBooleanExtra(Companion.MOVE_FROM_VERIFIED_EMAIL_TO_LOGIN, false)

        if (rememberMe && emailSaved != null) {
            binding.etEmail.setText(emailSaved)
        }

        binding.rememberMe.isChecked = rememberMe
        binding.loginBtn.setOnClickListener(this)
        binding.registerNow.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginBtn -> {
                binding.progressBar.visibility = View.VISIBLE

                if (binding.etEmail.text.toString().trim().isEmpty()) {
                    binding.etEmail.error = "Please enter email"
                    binding.etEmail.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim())
                        .matches()
                ) {
                    binding.etEmail.error = "Please enter valid email"
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

                login()

            }

            R.id.registerNow -> {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, RegisterFragment())
                    commit()
                }
            }
        }
    }

    private fun login() {
        auth.signInWithEmailAndPassword(
            binding.etEmail.text.toString().trim(),
            binding.etPassword.text.toString().trim()
        )
            .addOnCompleteListener(AuthenticationActivity()) { task ->
                if (task.isSuccessful) {
                    //get user
                    user = auth.currentUser as FirebaseUser
                    if (user.isEmailVerified) {
                        val reference = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(user.uid)
                        reference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                binding.progressBar.visibility = View.INVISIBLE
                                val user = auth.currentUser
                                updateUI(user)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                        })
                    } else {
                        Toast.makeText(context, "Verified your account first!", Toast.LENGTH_SHORT)
                            .show()
                        val verifiedEmail = VerifiedEmailFragment()
                        val mBundle = Bundle()
                        mBundle.putBoolean(Companion.MOVE_FROM_LOGIN_TO_VERIFIED_EMAIL, true)
                        verifiedEmail.arguments = mBundle

                        fragmentManager?.beginTransaction()?.apply {
                            replace(
                                R.id.fl_wrapper,
                                verifiedEmail,
                                VerifiedEmailFragment::class.java.simpleName
                            )
                            commit()
                        }
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null)
                    Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null && !moveFromVerifiedEmailToLogin) {
            updateUI(auth.currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            if (currentUser.isEmailVerified){
                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        if (binding.rememberMe.isChecked) {
            editor.putString(Companion.CHECK_EMAIL, binding.etEmail.text.toString())
            editor.putBoolean(Companion.REMEMBER_ME, true)
            editor.apply()
        } else editor.clear().commit()
    }
}