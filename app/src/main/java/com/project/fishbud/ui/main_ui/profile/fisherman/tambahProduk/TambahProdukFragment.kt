package com.project.fishbud.ui.main_ui.profile.fisherman.tambahProduk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.fishbud.R
import com.project.fishbud.databinding.FragmentTambahProdukBinding
import java.util.concurrent.Executors

class TambahProdukFragment : Fragment(), View.OnClickListener, TambahProdukViewModel.goBack {

    private lateinit var binding: FragmentTambahProdukBinding
    private lateinit var viewModel: TambahProdukViewModel
    private var namaUser: String = ""
    private var userId: String = ""
    private var uriImage: Uri? = null
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTambahProdukBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[TambahProdukViewModel::class.java]
        viewModel.mContext = requireContext()
        viewModel.mCallback = this
        binding.ivTambahProduk.setOnClickListener(this)
        binding.btnTambahProduk.setOnClickListener(this)
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_tambahProduk -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }

            R.id.btn_tambahProduk -> {
                binding.progressBar.visibility = View.VISIBLE

                if (binding.etNamaIkan.text.toString().trim().isEmpty()) {
                    binding.etNamaIkan.error = "Masukkan nama ikan!"
                    binding.etNamaIkan.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etHargaIkan.text.toString().trim().isEmpty()) {
                    binding.etHargaIkan.error = "Masukkan harga ikan!"
                    binding.etHargaIkan.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (binding.etHargaIkan.text.toString().trim().toInt() > Int.MAX_VALUE) {
                    binding.etHargaIkan.error = "Harga terlalu besar!"
                    binding.etHargaIkan.requestFocus()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                if (uriImage == null) {
                    Toast.makeText(context, "Masukkan foto ikan!", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    return onClick(view)
                }

                val observe = viewModel.getDataUser()
                observe.observeOnce(this, {
                    if (it.isNotEmpty()) {
                        count++
                        Log.i("cek_count", "count tambahprodukfragment: $count")
                        namaUser = it[0].name
                        userId = it[0].id

                        //store data to database
                        val executor = Executors.newSingleThreadExecutor()
                        val handler = Handler(Looper.getMainLooper())
                        executor.execute {
                            // Simulate process in background thread
                            try {
                                viewModel.uploadPicture(
                                    namaUser,
                                    binding.etNamaIkan.text.toString().trim(),
                                    binding.etHargaIkan.text.toString().trim().toInt(),
                                    uriImage!!,
                                    userId
                                )
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                            handler.post {
                                // Update ui in main thread

                            }
                        }
                    }
                    else {
                        Toast.makeText(context, "Error tambah produk!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriImage = data?.data
        if (uriImage !== null) {
            binding.ivTambahProduk.scaleType = ImageView.ScaleType.FIT_XY
            binding.ivTambahProduk.setImageURI(uriImage)
        }
    }

    override fun buttonBack(data: Boolean) {
        if (data){
            fragmentManager?.popBackStack()
            binding.progressBar.visibility = View.GONE
        }
    }


}