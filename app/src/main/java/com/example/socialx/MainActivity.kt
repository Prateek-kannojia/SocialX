package com.example.socialx

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var mauth:FirebaseAuth
    private lateinit var recyclerview:RecyclerView
    private lateinit var modelList: ArrayList<Model>
    private lateinit var list:ArrayList<Model>
    private lateinit var adapter: Recyclerview_Adapter
    private lateinit var searchView:SearchView
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.blue))
        changeStatusBarColor(
            ContextCompat.getColor(
                this,
                R.color.blue
            )
        )
        mauth = FirebaseAuth.getInstance()
        recyclerview=findViewById(R.id.recycler_view)
        searchView=findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterlist(newText)
                return true
            }
        })
        modelList=ArrayList()
        fetchnews()
        adapter=Recyclerview_Adapter(this)
        recyclerview.layoutManager=LinearLayoutManager(this)
        recyclerview.adapter=adapter
        adapter.notifyDataSetChanged()
    }
    private fun fetchnews(){
        val token="ab2f6c7ffa2acaccf5e00d56365d3326"
        val url="https://gnews.io/api/v4/top-headlines?token=$token&lang=en&topic=world"
        val jsonObjectRequest=JsonObjectRequest(url, {
            val newsjsonarray=it.getJSONArray("articles")
            for (i in 0 until newsjsonarray.length()){
                val newsjsonobject=newsjsonarray.getJSONObject(i)
                val news =Model(
                    newsjsonobject.getString("title"),
                    newsjsonobject.getString("description"),
                    newsjsonobject.getString("image"),
                    newsjsonobject.getString("publishedAt"),
                )
                modelList.add(news)
            }
            adapter.updateNews(modelList)
        }, {

        })
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }
    private fun filterlist(newText: String?) {
        list=modelList
        for(model:Model in modelList){
            if(model.title!!.lowercase().contains(newText!!.lowercase(Locale.getDefault()))){
                list.add(model)
            }
        }
        if(list.isEmpty()){
            Toast.makeText(this,"Data not found",Toast.LENGTH_SHORT).show()
        }else{
            adapter.updateNews(list)
        }
    }

    fun Activity.changeStatusBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.logout){
            mauth.signOut()
            val intent= Intent(this, login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
    //for closing app
    override fun onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true)
    }
}