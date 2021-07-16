package com.project.fishbud.ui.main_ui.profile.tambahProduk

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
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


class TambahProdukViewModel : ViewModel() {

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var urlImage : String = ""
    @SuppressLint("StaticFieldLeak")
    lateinit var mContext : Context
    //implementasi alfabet acak untuk id
    val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val idProduk = List(20) { alphabet.random() }.joinToString("")

    fun uploadPicture(namaUser : String, namaIkan : String, hargaIkan : Int, imageURI : Uri) {
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val tmp: StorageReference =
            storageReference.child("TambahProduk/$idProduk.jpg")
        tmp.putFile(imageURI).addOnSuccessListener {
            //getImageUrl
            tmp.downloadUrl.addOnSuccessListener {
                urlImage = it.toString()
                //store to database
                storeToDatabase(namaUser, namaIkan, hargaIkan, urlImage)
            }
            Log.i("cek_cek", "uploadPicture: $urlImage")
        }.addOnFailureListener{
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

    fun storeToDatabase(namaUser : String, namaIkan : String, hargaIkan : Int, urlImage : String){
        val reference = FirebaseDatabase.getInstance().reference.child("ListProduk").child(idProduk)
        Log.i("cek_cek", "storeToDatabase: $urlImage")
        val value = TambahProdukEntity(
            idProduk,
            namaIkan,
            hargaIkan,
            "Toko Ikan $namaUser",
            urlImage
        )
        reference.setValue(value).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(mContext, "Produk telah ditambahkan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}