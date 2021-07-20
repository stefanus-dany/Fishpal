package com.project.fishbud.ui.main_ui.profile.tambahProduk

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.fishbud.model.UserModel
import com.project.fishbud.utils.DataFirebase
import java.util.concurrent.Executors

class TambahProdukViewModel : ViewModel() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context

    //implementasi alfabet acak untuk id
    private var alphabet: List<Char> = emptyList()
    private var idProduk: String
    private var bgthread: Thread? = null
    private var check: Boolean = false

    init {
        alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        idProduk = List(20) { alphabet.random() }.joinToString("")
    }

    fun uploadPicture(
        namaUser: String,
        namaIkan: String,
        hargaIkan: Int,
        imageURI: Uri,
        userId: String
    ) {
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        val tmp: StorageReference =
            storageReference.child("TambahProduk/$idProduk.jpg")
        tmp.putFile(imageURI).addOnSuccessListener {
            //getImageUrl
            if (!check) {
                check = true
                tmp.downloadUrl.addOnSuccessListener {
                    val urlImage = it.toString()
                    Log.i("cek_uri", "url: $urlImage")
//                Log.i("cek_uri", "image Uri: $imageURI")
                    //store to database
                    storeToDatabase(namaUser, namaIkan, hargaIkan, urlImage, userId)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(mContext, "Error upload image", Toast.LENGTH_SHORT).show()
        }
    }

    fun getDataUser(): LiveData<MutableList<UserModel>> {
        val mutableData = MutableLiveData<MutableList<UserModel>>()
        DataFirebase.getDataUser().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    private fun storeToDatabase(
        namaUser: String,
        namaIkan: String,
        hargaIkan: Int,
        urlImage: String,
        userId: String
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                Log.i("check", "sebelum masuk ")

                Log.i("check", "masuk ")
                val reference =
                    FirebaseDatabase.getInstance().reference.child("ListProduk").child(idProduk)
                Log.i("cek_cek", "storeToDatabase: $urlImage")
                val value = TambahProdukEntity(
                    idProduk,
                    namaIkan,
                    hargaIkan,
                    "Toko Ikan $namaUser",
                    urlImage,
                    userId
                )
                reference.setValue(value).addOnCompleteListener {
                    if (it.isSuccessful) {
//                            store to fisherman account
                        val reference2 =
                            FirebaseDatabase.getInstance().reference.child("Users")
                                .child(userId)
                                .child("myProduk").child(idProduk)
                        Log.i("user.id", "user.id: $userId")

                        reference2.setValue(value).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    mContext,
                                    "Produk telah ditambahkan!",
                                    Toast.LENGTH_SHORT
                                ).show()
//                                    check = true
                            } else {
                                Toast.makeText(
                                    mContext,
                                    "Error from database",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

//

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                // Update ui in main thread
                bgthread = Thread()
                bgthread?.start()
            }
        }

    }
}