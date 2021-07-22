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
import com.project.fishbud.ui.main_ui.community.CommunityEntity
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import com.project.fishbud.ui.main_ui.marketplace.checkout.PaymentEntity
import com.project.fishbud.ui.main_ui.profile.OrderFishermanEntity
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

    fun getMyProduct(): LiveData<MutableList<IkanEntity>> {
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
                val reference = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(user.uid)
                    .child("myProduk")
                Log.i("cekIkan", "uid : ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.getValue(IkanEntity::class.java)
                            if (value != null) {
                                data.add(value)
                                Log.i("cekIkan", "data.add : $data")
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

    fun getDataThread(): LiveData<MutableList<CommunityEntity>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        val mutableData = MutableLiveData<MutableList<CommunityEntity>>()
        val data = mutableListOf<CommunityEntity>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference
                    .child("Thread")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.getValue(CommunityEntity::class.java)
                            if (value != null) {
                                data.add(value)
                                Log.i("cekIkan", "data.add : $data")
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

    fun getDataPayment(): LiveData<MutableList<PaymentEntity>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<PaymentEntity>>()
        val data = mutableListOf<PaymentEntity>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                    .child("waitingPayment")
                Log.i("cek_data", "user.uid: ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.getValue(PaymentEntity::class.java)
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

    fun getItemOrdered(idPembayaran: String): LiveData<MutableList<OrderFishermanEntity>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<OrderFishermanEntity>>()
        val data = mutableListOf<OrderFishermanEntity>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                    .child("waitingPayment")
                    .child(idPembayaran)
                    .child("itemOrdered")
                Log.i("cek_data", "user.uid: ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.getValue(OrderFishermanEntity::class.java)
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

    fun getIdOrderedFromFisherman(): LiveData<MutableList<String>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<String>>()
        val data = mutableListOf<String>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                    .child("itemOrdered")
                Log.i("cek_data", "user.uid: ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.key.toString()
                            Log.i("cekit", "cek value : $value")
                            data.add(value)
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

    fun getItemOrderedFromFisherman(idOrdered: List<String>): LiveData<MutableList<OrderFishermanEntity>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        Log.i("cekit", "cek id di database : $idOrdered")
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<OrderFishermanEntity>>()
        val data = mutableListOf<OrderFishermanEntity>()

        for (i in 0 until (idOrdered.size)) {
            val reference = FirebaseDatabase.getInstance().reference.child("Users")
                .child(user.uid)
                .child("itemOrdered")
                .child(idOrdered[i])
            Log.i("cek_data", "user.uid: ${user.uid}")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value = dataSnapshot.getValue(OrderFishermanEntity::class.java)
//                        Log.i("cekit", "cek value di database : $value")
                        if (value != null) {
                            data.add(value)
                            Log.i("cekit", "data di dalam value : $data")
                        }
                    }
                    Log.i("cekit", "data setelah for : $data")
                    mutableData.value = data
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        Log.i("cekit", "cek nilai data di akhir method : $data")

        return mutableData
    }

    fun getIdShipping(): LiveData<MutableList<String>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<String>>()
        val data = mutableListOf<String>()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                val reference = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(user.uid)
                    .child("shipping")
                Log.i("cek_data", "user.uid: ${user.uid}")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        data.clear()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            val value = dataSnapshot.key.toString()
                            Log.i("cekit", "cek value : $value")
                            data.add(value)
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

    fun getItemShipping(idOrdered: List<String>): LiveData<MutableList<OrderFishermanEntity>> {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser as FirebaseUser
        Log.i("cekit", "cek id di database : $idOrdered")
//        Log.i("cek_error", "masuk getDataUser: ")
        val mutableData = MutableLiveData<MutableList<OrderFishermanEntity>>()
        val data = mutableListOf<OrderFishermanEntity>()

        for (i in 0 until (idOrdered.size)) {
            val reference = FirebaseDatabase.getInstance().reference.child("Users")
                .child(user.uid)
                .child("shipping")
                .child(idOrdered[i])
            Log.i("cek_data", "user.uid: ${user.uid}")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val value = dataSnapshot.getValue(OrderFishermanEntity::class.java)
//                        Log.i("cekit", "cek value di database : $value")
                        if (value != null) {
                            data.add(value)
                            Log.i("cekit", "data di dalam value : $data")
                        }
                    }
                    Log.i("cekit", "data setelah for : $data")
                    mutableData.value = data
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        Log.i("cekit", "cek nilai data di akhir method : $data")

        return mutableData
    }

//    fun getThreadCommunity(): LiveData<MutableList<String>> {
//        auth = FirebaseAuth.getInstance()
//        user = auth.currentUser as FirebaseUser
////        Log.i("cek_error", "masuk getDataUser: ")
//        val mutableData = MutableLiveData<MutableList<String>>()
//        val data = mutableListOf<String>()
//
//        val executor = Executors.newSingleThreadExecutor()
//        val handler = Handler(Looper.getMainLooper())
//        executor.execute {
//            // Simulate process in background thread
//            try {
//                val reference = FirebaseDatabase.getInstance().reference.child("Users")
//                    .child(user.uid)
//                    .child("shipping")
//                Log.i("cek_data", "user.uid: ${user.uid}")
//                reference.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        data.clear()
//                        for (dataSnapshot: DataSnapshot in snapshot.children) {
//                            val value = dataSnapshot.key.toString()
//                            Log.i("cekit", "cek value : $value")
//                            data.add(value)
//                        }
//                        mutableData.value = data
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                    }
//                })
//                Log.i("cek_data", "getDataUser: $data")
//
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//            handler.post {
//                // Update ui in main thread
//                bgthread = Thread()
//                bgthread?.start()
//            }
//        }
//
//        return mutableData
//    }


//    fun getIdOrderedFromFisherman(idOrdered: List<String>): LiveData<MutableList<String>> {
//        auth = FirebaseAuth.getInstance()
//        user = auth.currentUser as FirebaseUser
//        Log.i("cekit", "cek id di database : $idOrdered")
//        val mutableData = MutableLiveData<MutableList<String>>()
//        val idPesanan = mutableListOf<String>()
//
//        for (i in 0 until (idOrdered.size)) {
//            val reference = FirebaseDatabase.getInstance().reference.child("Users")
//                .child(user.uid)
//                .child("itemOrdered")
//                .child(idOrdered[i])
//            Log.i("cek_data", "user.uid: ${user.uid}")
//            reference.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
////                    for (dataSnapshot: DataSnapshot in snapshot.children) {
////                    }
//                    val value = snapshot.childrenCount
////                        Log.i("cekit", "cek value di database : $value")
//                    for (j in 0 until (value)) {
//                        idPesanan.add(idOrdered[i])
//                    }
//                    mutableData.value = idPesanan
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })
//        }
////        Log.i("cekit", "cek nilai data di akhir method : $data")
//
//        return mutableData
//    }

}