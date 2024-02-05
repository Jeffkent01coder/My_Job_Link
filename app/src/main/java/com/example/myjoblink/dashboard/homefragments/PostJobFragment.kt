package com.example.myjoblink.dashboard.homefragments

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myjoblink.databinding.FragmentPostJobBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class PostJobFragment : Fragment() {
    private var _binding: FragmentPostJobBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var jobUri: Uri? = null
    private val TAG = "JOB ADD TAG"

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        jobUri = it
        binding.selectImage.setImageURI(jobUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostJobBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.selectImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnPostJob.setOnClickListener {
            validateData()
        }
    }

    private var jobTitle = ""
    private var jobLocation = ""
    private var jobNature = ""
    private var jobPay = ""
    private var jobDescription = ""

    private fun validateData() {
        Log.d(TAG, "Validating Data")
        jobTitle = binding.jobTitle.text.toString().trim()
        jobLocation = binding.jobLocation.text.toString().trim()
        jobNature = binding.jobNature.text.toString().trim()
        jobPay = binding.jobPay.text.toString().trim()
        jobDescription = binding.jobDescription.text.toString().trim()

        if (jobTitle.isEmpty()){
            Toast.makeText(requireActivity(), "Enter Job Title", Toast.LENGTH_SHORT).show()
        } else if (jobLocation.isEmpty()){
            Toast.makeText(requireActivity(), "Enter Job Location", Toast.LENGTH_SHORT).show()
        }else if (jobNature.isEmpty()){
            Toast.makeText(requireActivity(), "Enter Job Nature", Toast.LENGTH_SHORT).show()
        }else if (jobPay.isEmpty()){
            Toast.makeText(requireActivity(), "Enter Job Pay", Toast.LENGTH_SHORT).show()
        }else if (jobDescription.isEmpty()){
            Toast.makeText(requireActivity(), "Enter Job Description", Toast.LENGTH_SHORT).show()
        } else{
            uploadJobToStorage()
            binding.jobTitle.text?.clear()
            binding.jobLocation.text?.clear()
            binding.jobNature.text?.clear()
            binding.jobPay.text?.clear()
            binding.jobDescription.text?.clear()
        }

    }

    private fun uploadJobToStorage() {
        Log.d(TAG, "Uploading")
        progressDialog.setMessage("Uploading Job")
        progressDialog.show()

        val timeStamp = System.currentTimeMillis()
        val filePathAndName = "job/$timeStamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(jobUri!!)
            .addOnSuccessListener { taskSnapShot ->
                val uriask: Task<Uri> = taskSnapShot.storage.downloadUrl
                while (!uriask.isSuccessful);
                val uploadedImageUrl = "${uriask.result}"

                uploadJobInfoToDb(uploadedImageUrl, timeStamp)

                progressDialog.dismiss()
            }
    }

    private fun uploadJobInfoToDb(uploadedImageUrl: String, timeStamp: Long) {
        val progressDialog = ProgressDialog(requireActivity())
        progressDialog.setMessage("Uploading data")
        val uid = FirebaseAuth.getInstance().uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timeStamp"
        hashMap["uid"] = "$uid"
        hashMap["jobTitle"] = "$jobTitle"
        hashMap["jobLocation"] = "$jobLocation"
        hashMap["jobNature"] = "$jobNature"
        hashMap["jobPay"] = "$jobPay"
        hashMap["jobDescription"] = "$jobDescription"
        hashMap["imageUrl"] = "$uploadedImageUrl"



    }
}