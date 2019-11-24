package com.abhikr.abhikr.projects

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhikr.abhikr.ABHIWeb
import com.abhikr.abhikr.R
import com.abhikr.abhikr.firepush.GlideApp
import com.abhikr.abhikr.firepush.SharedPrefManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_work_station.*
import kotlinx.android.synthetic.main.content_work_station.*
import kotlinx.android.synthetic.main.workdesign.view.*


class WorkStation : AppCompatActivity(),View.OnClickListener {
    private val TAG = WorkStation::class.java.getSimpleName()
    private lateinit var firebaseDB:FirebaseFirestore
    private var manager: LinearLayoutManager?=null
    private var adaptor: FirestoreRecyclerAdapter<WorkModal, ViewHolder>?=null
    private var appBarExpanded = true
    private var collapsedMenu: Menu? = null
    override fun onStart() {
        super.onStart()
        adaptor!!.startListening()
    }
    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.workfab->
            {
                startActivity(Intent(this@WorkStation,WorkStore::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this@WorkStation).toBundle())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_station)
        setSupportActionBar(worktoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        workfab.setOnClickListener(this@WorkStation)
        // Access a Cloud Firestore instance from your Activity


        /*firebaseDB.collection("Projects")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("WorkStation", "${document.id} => ${document.data}")
                        // Successfully received data. List in querySnapshot.documents
                        //val taskList: List<RemoteTask> = querySnapshot.toObjects(RemoteTask::class.java)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("WorkStation", "Error getting documents.", exception)
                }*/
         manager= LinearLayoutManager(this@WorkStation,LinearLayoutManager.VERTICAL,false)
        workrecycler!!.layoutManager=manager
        firebaseDB = FirebaseFirestore.getInstance()
        try {
            GetWork()
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Workstation fetch error :"+e.message)
        }
        workapp_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            override fun onOffsetChanged(appBarLayout: AppBarLayout,      verticalOffset: Int) {
                //Initialize the size of the scroll
            /*  internal var scrollRange = -1
            if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }


                val scale = 1 + verticalOffset / scrollRange.toFloat()


                if (scale <= 0) {
                    workapp_bar.elevation = 4f
                } else {
                    workapp_bar.elevation = 0f
                }*/
                //Log.d(AbhiCompleteMain.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);

                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 156) {
                    appBarExpanded = false
                    invalidateOptionsMenu()
                    //supportInvalidateOptionsMenu()
                } else {
                    appBarExpanded = true
                    invalidateOptionsMenu();
                    //supportInvalidateOptionsMenu()
                }
            }
        })
        Log.d(TAG,"abhikr token: "+SharedPrefManager.getInstance(this@WorkStation).deviceToken)

    }
    private fun GetWork()
    {
        var lastPosition = -1
        val query:Query=firebaseDB.collection("Projects")
        val response = FirestoreRecyclerOptions.Builder<WorkModal>()
                .setQuery(query, WorkModal::class.java)
                .build()
        adaptor = object : FirestoreRecyclerAdapter<WorkModal, ViewHolder>(response) {
            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: WorkModal) {

                holder.itemView.worktitle.text = model.Title
                holder.itemView.workdesc.text = model.Description
                holder.itemView.workclient.text = model.Client
                holder.itemView.workduration.text = model.Duration
                //holder.itemView.worklogo.setImageURI(Uri.parse(model.Logo))
                GlideApp.with(this@WorkStation)
                        .load(model.Logo)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.favicon)
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        //.bitmapTransform(new RoundedCornersTransformation(this,25,25, RoundedCornersTransformation.CornerType.ALL))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Cache all size of images and loads according imageview
                        .into(holder.itemView.worklogo);
                holder.itemView.workplaystore.setOnClickListener { v ->
                    try {
                        val playintent = Intent("android.intent.action.VIEW",
                                Uri.parse(model.PlayStore))
                        startActivity(playintent)
                    } catch (e: Exception) {
                        Snackbar.make(v,"Unable to Connect With play store...",
                                Snackbar.LENGTH_SHORT).setAction("Dismiss",null).show()
                        e.printStackTrace()
                    }
                }
                holder.itemView.workwebsite.setOnClickListener { v ->
                    try {
                        /*val webintent = Intent(Intent.ACTION_VIEW, Uri.parse(model.Website))
                        startActivity(webintent)*/
                        val abhiweb=ABHIWeb(this@WorkStation)
                        abhiweb.redirectUsingCustomTab(model.Website)
                    }
                    catch (e:Exception)
                    {
                        Snackbar.make(v,"Unable to Connect with web browser...",
                                Snackbar.LENGTH_SHORT).setAction("Dismiss",null).show()
                    }

                }
                //model.TimeStamp!!.toDate()
                holder.itemView.workcardview.setOnClickListener { v ->
                    val snapshot:DocumentSnapshot=snapshots.getSnapshot(holder.adapterPosition)
                    val intent=Intent(this@WorkStation,WorkStore::class.java)
                    intent.putExtra("projects",snapshot.id)
                    intent.putExtra("projects_item", model)
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this@WorkStation).toBundle())
                    /*Snackbar.make(v, model.Title+", "+model.Description+" at "+model.Client+" id : "+snapshot.id, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()*/
                }
                val animation = AnimationUtils.loadAnimation(this@WorkStation, if (position > lastPosition) R.anim.up_from_bottom else R.anim.down_from_top)
                holder.itemView.startAnimation(animation)
                lastPosition = position
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workdesign, parent, false))
            }

            override fun onViewDetachedFromWindow(holder: ViewHolder) {
                super.onViewDetachedFromWindow(holder)
                holder.itemView.clearAnimation()
            }

            override fun onDataChanged() {
                super.onDataChanged()
            }

            override fun updateOptions(options: FirestoreRecyclerOptions<WorkModal>) {
                super.updateOptions(options)
            }
            override fun onError(e: FirebaseFirestoreException) {
                Log.e(TAG, e.message)
            }
        }
        adaptor!!.notifyDataSetChanged()
        workrecycler.adapter = adaptor
    }
   class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (collapsedMenu != null && (!appBarExpanded || collapsedMenu!!.size() != 1)) {
            //collapsed
            collapsedMenu!!.add(getString(R.string.title_activity_work_station))
                    .setIcon(R.drawable.ic_project_management)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } /*else {
            //expanded
            //Log.d(TAG,"not collasped");
        }*/
        return super.onPrepareOptionsMenu(collapsedMenu)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_work_station,menu)
        collapsedMenu = menu
        //return super.onCreateOptionsMenu(menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home)
        {
            supportFinishAfterTransition()
        }
        else if(item.itemId==R.id.action_settings)
        {
            OpenSetting()

        }
        if (item.title === getString(R.string.title_activity_work_station)) {
            Snackbar.make(findViewById(android.R.id.content), "Working on it..(--", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            /*Intent ik=new Intent(getApplicationContext(),Home.class);
            //ik.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity below code will clear all stack activity
            ik.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ik, ActivityOptions.makeSceneTransitionAnimation(AbhiCompleteMain.this).toBundle());*/
        }
        return super.onOptionsItemSelected(item)
    }
    fun OpenSetting()
    {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }

    override fun onStop() {
        super.onStop()
        adaptor!!.stopListening()
    }
}
