package com.example.myscope.activities.medical_history

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myscope.R
import com.example.myscope.helpers.AllergyAdapter
import com.example.myscope.helpers.ServiceBuilder
import com.example.myscope.models.Allergy
import com.example.myscope.services.AllergyService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_allergy_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllergyItemListActivity : AppCompatActivity(){

    //lateinit var adapter: AllergyAdapter

    var fab : FloatingActionButton?=null

    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergy_list)

        fab= findViewById(R.id.fab)
        //fab?.setBackgroundColor(Color.parseColor("#2196F3"));
        //setSupportActionBar(toolbar as Toolbar?)
        //toolbar.title = title

        fab?.setOnClickListener {

            val intent = Intent(this@AllergyItemListActivity, AllergyUpdate_Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadDestinations()
    }

    private fun loadDestinations() {

        val destinationService = ServiceBuilder.buildService(AllergyService::class.java)

        val filter = HashMap<String, String>()
//        filter["country"] = "India"
//        filter["count"] = "1"

        val requestCall = destinationService.getAllergyList(filter)          ///service file method called (binding)

        //val requestCall = destinationService.getAllergy(filter)

        requestCall.enqueue(object: Callback<List<Allergy>> {

            // If you receive a HTTP Response, then this method is executed
            // Your STATUS Code will decide if your Http Response is a Success or Error
            override fun onResponse(call: Call<List<Allergy>>, response: Response<List<Allergy>>) {
                if (response.isSuccessful()) {
                    // Your status code is in the range of 200's
                    val allergyList = response.body()!!
                    //linearLayoutManager = LinearLayoutManager(this@AllergyItemListActivity,
                    // LinearLayoutManager.VERTICAL, false)
                    //destiny_recycler_view.adapter = AllergyAdapter(allergyList)

                    val llm = LinearLayoutManager(applicationContext)
                    llm.orientation = LinearLayoutManager.VERTICAL
                    destiny_recycler_view.setLayoutManager(llm)
                    destiny_recycler_view.adapter = AllergyAdapter(allergyList)



                } else if(response.code() == 401) {
                    Toast.makeText(this@AllergyItemListActivity,
                            "Your session has expired. Please Login again.", Toast.LENGTH_LONG).show()
                } else { // Application-level failure
                    // Your status code is in the range of 300's, 400's and 500's
                    Toast.makeText(this@AllergyItemListActivity, "Failed to retrieve items", Toast.LENGTH_LONG).show()
                }
            }

            // Invoked in case of Network Error or Establishing connection with Server
            // or Error Creating Http Request or Error Processing Http Response
            override fun onFailure(call: Call<List<Allergy>>, t: Throwable) {

                Toast.makeText(this@AllergyItemListActivity, "Error Occurred" + t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}

