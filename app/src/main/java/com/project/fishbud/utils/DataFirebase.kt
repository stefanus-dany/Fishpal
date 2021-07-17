package com.project.fishbud.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.fishbud.model.UserModel
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import java.util.concurrent.Executors

object DataFirebase {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var bgthread: Thread? = null

    fun getDataUser(): LiveData<MutableList<UserModel>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<UserModel>>()
        val data = mutableListOf<UserModel>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                Log.i("cek_data", "user.uid: ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(UserModel::class.java)
                        if (value != null) {
                            val email = value.email
                            val name = value.name
                            val id = value.id
                            Log.i("cek_data", "email: $email")
                            Log.i("cek_data", "name: $name")
                            Log.i("cek_data", "id: $id")
                            data.add(UserModel(email, name, id))
                        }
                        mutableData.value = data
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
                Log.i("cek_data", "getDataUser: $data")

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                // Update ui in main thread
                bgthread = Thread()
                bgthread?.start()
            }
        }

        return mutableData
    }

    fun getDataIkan(): LiveData<MutableList<IkanEntity>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<IkanEntity>>()
        val data = mutableListOf<IkanEntity>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("ListProduk")
                Log.i("cek_data", "user.uid: ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.getValue(IkanEntity::class.java)
                            if (value != null) {
                                data.add(value)
                            }
                        }
                        mutableData.value = data
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
                Log.i("cek_data", "getDataUser: $data")

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                // Update ui in main thread
                bgthread = Thread()
                bgthread?.start()
            }
        }

        return mutableData
    }

}