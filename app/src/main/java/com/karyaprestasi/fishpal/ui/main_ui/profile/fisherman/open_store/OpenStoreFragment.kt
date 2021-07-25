package com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.open_store

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karyaprestasi.fishpal.Constants
import com.karyaprestasi.fishpal.R
import com.karyaprestasi.fishpal.databinding.FragmentOpenStoreBinding
import java.util.concurrent.Executors

class OpenStoreFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentOpenStoreBinding
    private var uriImageKtp: Uri? = null
    private var uriImageFotoDiri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private val user = FirebaseAuth.getInstance().currentUser as FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenStoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            binding.etStoreName.setText(sharedPreferences.getString(Constants.USER_NAME, ""))
        }
        binding.etStoreName.keyListener = null
        binding.btnDaftarSekarang.setOnClickListener(this)
        binding.btnUploadKtp.setOnClickListener(this)
        binding.btnUploadFotodiri.setOnClickListener(this)
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                uriImageKtp = data?.data
//                Log.i("cekUri", "uriKTP: $uriImageKtp")
                if (uriImageKtp !== null) {
                    binding.ivKtp.visibility = View.VISIBLE
                    binding.ivKtp.setImageURI(uriImageKtp)
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                uriImageFotoDiri = data?.data
//                Log.i("cekUri", "uriImage: $uriImageFotoDiri")
                if (uriImageFotoDiri !== null) {
                    binding.ivFotoDiri.visibility = View.VISIBLE
                    binding.ivFotoDiri.setImageURI(uriImageFotoDiri)
                }
            }
        }

    }

    private fun uploadPicture(uriImageKtp: Uri?, uriImageFotoDiri: Uri?) {
        storage = FirebaseStorage.getInstance()
        val tmp: StorageReference =
            storage.reference.child("DaftarToko/${user.uid}/ktp.jpg")
        //foto ktp
        if (uriImageKtp != null) {
            tmp.putFile(uriImageKtp).addOnSuccessListener {
                //getImageUrl
                tmp.downloadUrl.addOnSuccessListener {
                    val urlKtp = it.toString()
                    //foto diri
                    if (uriImageFotoDiri != null) {
                        val tmp2: StorageReference =
                            storage.reference.child("DaftarToko/${user.uid}/fotoDiri.jpg")
                        tmp2.putFile(uriImageFotoDiri).addOnSuccessListener {
                            //getImageUrl
                            tmp2.downloadUrl.addOnSuccessListener { er ->
                                val urlFotoDiri = er.toString()
                                //store to database
                                storeToDatabase(urlKtp, urlFotoDiri)
                            }

                        }.addOnFailureListener {
                            Toast.makeText(context, "Error upload image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(context, "Error upload image", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun storeToDatabase(urlKtp: String, urlFotoDiri: String) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("DaftarToko")
                    .child(user.uid)
                val value = DaftarTokoEntity(
                    binding.etStoreName.text.toString(),
                    binding.etCity.text.toString(),
                    binding.etAddress.text.toString(),
                    urlKtp,
                    urlFotoDiri
                )
                reference.setValue(value).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Data berhasil dikirim!\nJika pengajuan diterima, Akun Toko otomatis terbuka.",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
                        fragmentManager?.popBackStack()

                    } else {
                        Toast.makeText(context, "Error from database", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {

            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_upload_ktp -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            }

            R.id.btn_upload_fotodiri -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 2)
            }

            R.id.btn_daftarSekarang -> {
                binding.progressBar.visibility = View.VISIBLE

                if (binding.etCity.text.toString().trim().isEmpty()) {
                    binding.etCity.error = "Masukkan letak toko anda!"
                    binding.etCity.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etAddress.text.toString().trim().isEmpty()) {
                    binding.etAddress.error = "Masukkan alamat lengkap toko anda!"
                    binding.etAddress.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (uriImageFotoDiri == null) {
                    Toast.makeText(context, "Upload foto KTP", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (uriImageKtp == null) {
                    Toast.makeText(context, "Upload foto diri", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }
                Log.i("cekUri", "uriKTP: $uriImageKtp")
                Log.i("cekUri", "uriImage: $uriImageFotoDiri")
                uploadPicture(uriImageKtp, uriImageFotoDiri)
            }
        }
    }

    override fun onDestroy() {
        val navBar : BottomAppBar? = activity?.findViewById(R.id.bottomAppBar)
        navBar?.visibility = View.VISIBLE
        val scan : FloatingActionButton? = activity?.findViewById(R.id.detection)
        scan?.visibility = View.VISIBLE
        super.onDestroy()
    }

}