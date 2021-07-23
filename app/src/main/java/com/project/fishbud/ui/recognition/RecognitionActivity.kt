package com.project.fishbud.ui.recognition

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.project.fishbud.Constants
import com.project.fishbud.R
import com.project.fishbud.databinding.ActivityRecognitionBinding
import com.project.fishbud.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecognitionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRecognitionBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var bitmap: Bitmap
    private lateinit var outputDirectory: File
    private lateinit var labels: List<String>
    private var max: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecognitionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i(Constants.TAG, "onCreate")
        labels = application.assets.open("labelss.txt").bufferedReader().use { it.readText() }
            .split("\n")

        //getFile
        outputDirectory = getOutputDirectory()

        //custom camera
        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnTakePhoto.setOnClickListener(this)
        binding.btnFlashlight.setOnClickListener(this)
        binding.selectImage.setOnClickListener(this)
        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permission not granted by the user", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { mPreview ->

                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (e: Exception) {
                Log.d(Constants.TAG, "startCamera Fail ", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    //getCameraPermission
    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTakePhoto -> {
                Log.i(Constants.TAG, "onError: masuk cek ga")
                takePhoto()
            }
            R.id.selectImage -> {
                Log.d("mssg", "button pressed")
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        Log.e(Constants.TAG, "onError: masuk")
        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.i(Constants.TAG, "onError: masuk2")
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo Saved"

                    Toast.makeText(this@RecognitionActivity, "$msg $savedUri", Toast.LENGTH_SHORT)
                        .show()

                    // val imgFile = File("/sdcard/Images/test_image.jpg")

                    //menampilkan foto yang telah di capture dengan cara mengambil dari file storage
                    val myBitmap: Bitmap?
                    if (photoFile.exists()) {
                        myBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                        //mendapatkan hasil prediksi dari gambar yang telah discan
                        getPrediction(myBitmap)
                        //pindah ke info scan activity dengan intent
                        val move = Intent(this@RecognitionActivity, InfoScanActivity::class.java)
                        move.putExtra(Constants.RESULT_PREDICTION_TEXT, labels[max])
                        startActivity(move)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.i(Constants.TAG, "onError: ${exception.message}", exception)
                }

            }
        )
    }

    private fun getPrediction(myBitmap: Bitmap) {
        val resized = Bitmap.createScaledBitmap(myBitmap, 224, 224, true)
        val model = Model.newInstance(this)

        val tbuffer = TensorImage.fromBitmap(resized)
        val byteBuffer = tbuffer.buffer

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        max = getMax(outputFeature0.floatArray)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri? = data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        //mendapatkan hasil prediksi dari gambar yang telah discan
        getPrediction(bitmap)
        val move = Intent(this@RecognitionActivity, InfoScanActivity::class.java)
        move.putExtra(Constants.RESULT_PREDICTION_TEXT, labels[max])
        startActivity(move)
    }

    private fun getMax(arr: FloatArray): Int {
        var ind = 0
        var min = 0.0f

        for (i in 0..2) {
            if (arr[i] > min) {
                min = arr[i]
                ind = i

            }
        }
        Log.i("check_error", "${arr[0]} : Tuna")
        Log.i("check_error", "${arr[1]} : Gurame")
        Log.i("check_error", "${arr[2]} : Pari")
        return ind
    }

}