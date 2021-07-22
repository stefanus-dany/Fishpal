package com.project.fishbud.ui.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.fishbud.Companion
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentVerifiedEmailBinding
import java.util.concurrent.Executors

class VerifiedEmailFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentVerifiedEmailBinding
    private var user: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private var bgthread: Thread? = null
    private var resendCodeTime = 59

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifiedEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        var moveFromLoginToVerifyEmail = false
        val bundle = arguments
        if (bundle!=null){
            moveFromLoginToVerifyEmail = bundle.getBoolean(Companion.MOVE_FROM_LOGIN_TO_VERIFIED_EMAIL, false)
        }

        if (moveFromLoginToVerifyEmail) {
            binding.email.visibility = View.INVISIBLE
            binding.textTimer.visibility = View.GONE
            binding.timer.visibility = View.GONE
            binding.btnResendCode.visibility = View.VISIBLE
        } else startTimer()
        binding.email.text = resources.getString(R.string.verification_email_has_been_sent)
        binding.btnGoToLogin.setOnClickListener(this)
        binding.btnResendCode.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnGoToLogin -> {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_wrapper, LoginFragment())
                    commit()
                }
            }
            R.id.btnResendCode -> {

                user?.sendEmailVerification()?.addOnSuccessListener {
                    binding.textTimer.visibility = View.VISIBLE
                    binding.timer.visibility = View.VISIBLE
                    binding.btnResendCode.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Resend verification email has been sent.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startTimer()

                }?.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Resend verification failed. Please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(Companion.TAG, "onFailure: Resend email not sent " + it.message)
                }
            }
        }
    }

    private fun startTimer() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                while (resendCodeTime != 0) {
                    // Simulating something timeconsuming
                    Thread.sleep(1000) // in milisecond
                    binding.timer.post {
                        binding.timer.text = resendCodeTime.toString()
                    }
                    resendCodeTime--
                }
                resendCodeTime = 59


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                bgthread = Thread()
                bgthread?.start()
                binding.textTimer.visibility = View.GONE
                binding.timer.visibility = View.GONE
                binding.btnResendCode.visibility = View.VISIBLE
            }
        }
    }
}