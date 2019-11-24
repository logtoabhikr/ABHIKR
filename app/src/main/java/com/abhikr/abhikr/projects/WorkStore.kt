package com.abhikr.abhikr.projects

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abhikr.abhikr.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_work_store.*


class WorkStore : AppCompatActivity(),View.OnClickListener {
    private val TAG = WorkStore::class.java.simpleName
    private lateinit var firebaseDB: FirebaseFirestore
    private var storeUid:String?=null
    private var workModal:WorkModal?=null
    private var spotsdialog:AlertDialog?=null
    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.fab_workstore ->
            {
                when { storeUid.isNullOrEmpty()-> Snackbar.make(v, "Document id found empty or null $storeUid", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", null).show()
                    TextUtils.isEmpty(titleworkstore.text.toString()) -> textInputLayout.error="Project Title can't be empty"
                    TextUtils.isEmpty(clientworkstore.text.toString()) -> textInputLayout2.error="Project Client can't be empty"
                    TextUtils.isEmpty(descriptionworkstore.text.toString()) -> textInputLayout3.error="Project Desc can't be empty"
                    TextUtils.isEmpty(durationworkstore.text.toString()) -> textInputLayout4.error="Project Duration can't be empty"
                    TextUtils.isEmpty(logoworkstore.text.toString()) -> textInputLayout5.error="Project Logo can't be empty"
                    TextUtils.isEmpty(playstoreworkstore.text.toString()) -> textInputLayout6.error="Project app can't be empty"
                    TextUtils.isEmpty(websiteworkstore.text.toString()) -> textInputLayout7.error="Project Website can't be empty"
                else ->
                    try {
                        UpdateStore()
                    }
                    catch (e:Exception)
                    {
                        Log.d(TAG,"WorkStore update error: "+e.message)
                    }
                }
            }
            R.id.goupload->
            {
                when {
                    TextUtils.isEmpty(titleworkstore.text.toString()) -> textInputLayout.error="Project Title can't be empty"
                    TextUtils.isEmpty(clientworkstore.text.toString()) -> textInputLayout2.error="Project Client can't be empty"
                    TextUtils.isEmpty(descriptionworkstore.text.toString()) -> textInputLayout3.error="Project Desc can't be empty"
                    TextUtils.isEmpty(durationworkstore.text.toString()) -> textInputLayout4.error="Project Duration can't be empty"
                    TextUtils.isEmpty(logoworkstore.text.toString()) -> textInputLayout5.error="Project Logo can't be empty"
                    TextUtils.isEmpty(playstoreworkstore.text.toString()) -> textInputLayout6.error="Project app can't be empty"
                    TextUtils.isEmpty(websiteworkstore.text.toString()) -> textInputLayout7.error="Project Website can't be empty"
                    else ->  try {
                        UploadWork()
                    } catch (e:Exception) {
                        Log.d(TAG,"Upload work error :"+e.message)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_store)
        setSupportActionBar(toolbar_workstore)
        spotsdialog = SpotsDialog.Builder()
                .setContext(this@WorkStore)
                .setTheme(R.style.abhi)
                .build()
        fab_workstore.setOnClickListener(this@WorkStore)
        firebaseDB = FirebaseFirestore.getInstance()
        goupload.setOnClickListener(this@WorkStore)
        // Create a new user with a first and last name
        val bundle=intent.extras
        if(bundle!=null)
        {
            storeUid=bundle.getString("projects")
            workModal = intent.getParcelableExtra("projects_item")
// Do something with the item (example: set Item title and price)
            if(workModal!=null)
            {
                titleworkstore.setText(workModal?.Title)
                descriptionworkstore.setText(workModal?.Description)
                durationworkstore.setText(workModal?.Duration)
                playstoreworkstore.setText(workModal?.PlayStore)
                websiteworkstore.setText(workModal?.Website)
                clientworkstore.setText(workModal?.Client)
                logoworkstore.setText(workModal!!.Logo)
            }
            goupload.isEnabled=false
            fab_workstore.isEnabled=true
        }
        else
        {
            goupload.isEnabled=true
            fab_workstore.isEnabled=false
        }
    }
    private fun UploadWork()
    {
        spotsdialog?.show()
        spotsdialog?.setMessage("Uploading Projects..")
        val uploadModal:WorkModal= WorkModal(titleworkstore.text.toString(),
                clientworkstore.text.toString(),
                descriptionworkstore.text.toString(),
                durationworkstore.text.toString(),
                playstoreworkstore.text.toString(),
                websiteworkstore.text.toString(),
                logoworkstore.text.toString(),
                Timestamp.now())
// Adding a new document with a generated ID
       firebaseDB.collection("Projects")
               .add(uploadModal)
               .addOnSuccessListener { documentReference ->
                   spotsdialog?.dismiss()
                   Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                   supportFinishAfterTransition()
               }
               .addOnFailureListener { e ->
                   spotsdialog?.dismiss()
                   Log.w(TAG, "Error adding document", e)
               }
    }
    private fun UpdateStore()
    {
        spotsdialog?.show()
        spotsdialog?.setMessage("Updating Projects..")
    /*    val projects = hashMapOf(
                "Title" to "DealComeTrue Seller",
                "Description" to "Apppl Combine DCT Merchant App",
                "Duration" to "3 Months",
                "Play Store" to "https://play.google.com/store/apps/details?id=com.apppl.dealcometrue.seller&hl=en",
                "Website" to "https://www.seller.dealcometrue.com/",
                "Client" to "Apppl Combine, New Delhi",
                "Logo" to "https://lh3.googleusercontent.com/gvsqZRoRWfS_XXG6_IacZkHv7UEjTvgdQzP6WvEIMXU-8qDjBrRB5piwdP85JVyn4g=s180-rw",
                "TimeStamp" to  FieldValue.serverTimestamp())*/
        val updatemodel:WorkModal= WorkModal(titleworkstore.text.toString(),
                clientworkstore.text.toString(),
                descriptionworkstore.text.toString(),
                durationworkstore.text.toString(),
                playstoreworkstore.text.toString(),
                websiteworkstore.text.toString(),
                logoworkstore.text.toString(),
                Timestamp.now())
        firebaseDB.collection("Projects").document(storeUid!!).set(updatemodel).addOnCompleteListener(object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful) {
                    spotsdialog?.dismiss()
                    Snackbar.make(findViewById(android.R.id.content), "WorkStore projects update", Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", null).show()
                } else {
                    if(spotsdialog!!.isShowing)
                    {
                        spotsdialog?.dismiss()
                    }
                    Snackbar.make(findViewById(android.R.id.content), "Document update", Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", null).show()
                    //Toast.makeText(applicationContext, "WorkStore update error "+task.exception, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Error adding document", task.exception)
                }
            }
        })
                .addOnFailureListener(OnFailureListener { e ->
                    spotsdialog?.dismiss()
                    Log.w(TAG, "Error writing document", e) })

    }
}
