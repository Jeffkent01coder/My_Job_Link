package com.example.myjoblink.job

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myjoblink.databinding.ActivityJobApplicationBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class JobApplication : AppCompatActivity() {
    private lateinit var binding: ActivityJobApplicationBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var cvUri: Uri? = null
    private val TAG = "Application ADD TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityJobApplicationBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.applicantCV.setOnClickListener {
            pickPdfIntent()
        }

        binding.btnApplyJob.setOnClickListener {
            validateData()
        }


    }


    private var applicantName = ""
    private var applicantLocation = ""
    private var applicantPhone = ""
    private var applicantEmail = ""
    private var applicantAge = ""

    private fun validateData() {
        Log.d(TAG, "Validating Data")
        applicantName = binding.applicantName.text.toString().trim()
        applicantLocation = binding.applicantLocation.text.toString().trim()
        applicantPhone = binding.applicantPhone.text.toString().trim()
        applicantEmail = binding.applicantEmail.text.toString().trim()
        applicantAge = binding.applicantAge.text.toString().trim()

        if (applicantName.isEmpty()) {
            Toast.makeText(this, "Enter Job Title", Toast.LENGTH_SHORT).show()
        } else if (applicantLocation.isEmpty()) {
            Toast.makeText(this, "Enter Job Location", Toast.LENGTH_SHORT).show()
        } else if (applicantPhone.isEmpty()) {
            Toast.makeText(this, "Enter Job Nature", Toast.LENGTH_SHORT).show()
        } else if (applicantEmail.isEmpty()) {
            Toast.makeText(this, "Enter Job Pay", Toast.LENGTH_SHORT).show()
        } else if (applicantAge.isEmpty()) {
            Toast.makeText(this, "Enter Job Description", Toast.LENGTH_SHORT).show()
        } else {
            uploadApplicationToStorage()
            binding.applicantName.text?.clear()
            binding.applicantLocation.text?.clear()
            binding.applicantPhone.text?.clear()
            binding.applicantEmail.text?.clear()
            binding.applicantAge.text?.clear()
        }
    }

    private fun uploadApplicationToStorage() {
        Log.d(TAG, "Uploading to storage")

        progressDialog.setMessage("Uploading CV")
        progressDialog.show()

        val timeStamp = System.currentTimeMillis()
        val filePathAndName = "PDF/$timeStamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(cvUri!!)
            .addOnSuccessListener { taskSnapShot ->
                Log.d(TAG, "Uploading to Storage")
                val uriask: Task<Uri> = taskSnapShot.storage.downloadUrl
                while (!uriask.isSuccessful);
                val uploadedPdfUrl = "${uriask.result}"

                uploadPdfInfoToDb(uploadedPdfUrl, timeStamp)

            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Uploading to Storage Failed due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploading to Storage Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadPdfInfoToDb(uploadedPdfUrl: String, timeStamp: Long) {
        Log.d(TAG, "Uploading to Database")
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()

        hashMap["uid"] = "$uid"
        hashMap["applicantName"] = "$applicantName"
        hashMap["applicantLocation"] = "$applicantLocation"
        hashMap["applicantPhone"] = "$applicantPhone"
        hashMap["applicantEmail"] = "$applicantEmail"
        hashMap["applicantAge"] = "$applicantAge"
        hashMap["url"] = "$uploadedPdfUrl"

        val ref = FirebaseDatabase.getInstance().getReference("applications")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Uploaded")
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploaded",
                    Toast.LENGTH_SHORT
                ).show()
                cvUri = null

            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Uploading to Storage Failed due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Uploading to Storage Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }


    private fun pickPdfIntent() {
        Log.d(TAG, "Starting pdf picking Intent")
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)
    }

    val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "PDF Picked")
                cvUri = result.data!!.data
            } else {
                Log.d(TAG, "PDF Picking Cancelled")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    )

}
