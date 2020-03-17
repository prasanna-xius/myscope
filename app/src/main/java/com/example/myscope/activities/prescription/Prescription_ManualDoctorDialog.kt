package com.example.myscope.activities.prescription

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myscope.R
import com.example.myscope.activities.BaseActivity
import com.example.myscope.activities.PrescriptionInterface
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.isprescribed_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.SSLContext


class Prescription_ManualDoctorDialog : BaseActivity() {

    var mobile_no: String? = null
    var prescription: Int = 0

    var isprescribed: Spinner? = null

    var doctorname: String? = null
    var prescribed_is: String? = null
    var hospitalname: String? = null
    var medicalcondition: String? = null

    var rv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescriptionmanual_recyclerview)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById<View>(R.id.fab_addprescribed) as FloatingActionButton
        mobile_no = "8142529582"
        fab.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {

        val d = Dialog(this)
        //NO TITLE
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //layout of dialog
        d.setContentView(R.layout.isprescribed_main)

        isprescribed = d.findViewById<Spinner>(R.id.is_prescribed1)

//        val bundle: Bundle? = intent.extras

//        position = intent.getIntExtra("position", 0)

        // prescribed spinner
        val isprescribedadapter = ArrayAdapter(this,
                R.layout.spinner_dropdown_item, resources.getStringArray(R.array.is_prescribed))
        isprescribed!!.adapter = isprescribedadapter

        isprescribed?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()

                if (selectedItem.equals("None")) {
                    d.doctor_layout1!!.setVisibility(View.GONE);
                    d.hosp_layout1!!.setVisibility(View.GONE);
                    // do your stuff
                } else if (selectedItem.equals("Over the counter (OTC)")) {
                    d.doctor_layout1!!.setVisibility(View.GONE);
                    d.hosp_layout1!!.setVisibility(View.GONE);
                    // do your stuff
                } else if (selectedItem.equals("Prescribed")) {

                    d.doctor_layout1!!.setVisibility(View.VISIBLE);
                    d.hosp_layout1!!.setVisibility(View.VISIBLE);

                    // do your stuff
                } else if (selectedItem.equals("Prescribed OTC")) {
                    d.doctor_layout1!!.setVisibility(View.GONE);
                    d.hosp_layout1!!.setVisibility(View.GONE);
                    // do your stuff
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                d.doctor_layout1!!.setVisibility(View.GONE);
                d.hosp_layout1!!.setVisibility(View.GONE);

            }

        }
        d.presDoctor_save_dialog.setOnClickListener {

            doctorname = d.et_doctor_name1.text.toString()
             prescribed_is = d.is_prescribed1!!.selectedItem.toString()
             hospitalname =d.et_hosp_name1.text.toString()
             medicalcondition = d.et_medical_condition1.text.toString()

            validateInput(d.et_doctor_name1, doctorname!!)
            validateInput(d.et_hosp_name1, hospitalname!!)
            validateInput(d.et_medical_condition1, medicalcondition!!)

            validateSpinner(d.is_prescribed1!!, prescribed_is!!)

            if ((doctorname != "") &&
                    (prescribed_is != "None")
                    && (hospitalname != "")
                    && (medicalcondition != "")) {
                showLongToast("save the details")
            } else {

                showLongSnackBar("Please fill the required fields")

            }
            validate(isprescribed!!)
            try {
                // Google Play will install latest OpenSSL
                ProviderInstaller.installIfNeeded(getApplicationContext());
                var sslContext: SSLContext
                sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                sslContext.createSSLEngine();
            } catch (e: Exception) {
                e.printStackTrace();
            }


            val newPrescription = PrescriptionDataClass()
            newPrescription.is_prescribed = d.is_prescribed1?.getSelectedItem().toString()
            newPrescription.doctor_name = d.et_doctor_name1!!.text.toString().trim()
            newPrescription.hospital_name = d.et_hosp_name1!!.text.toString().trim()
            newPrescription.medical_condition = d.et_medical_condition1!!.text.toString().trim()
            newPrescription.prescription_note = d.et_precsription_note1!!.text.toString().trim()
            newPrescription.mobile_no = mobile_no




            val diseaseService = ServiceBuilder1.buildService(PrescriptionInterface::class.java)


            val requestCall = diseaseService.addDoctor(newPrescription)

            requestCall.enqueue(object : Callback<PrescriptionDataClass> {
                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected
                 * exception occurred creating the request or processing the response.
                 */
                override fun onResponse(call: Call<PrescriptionDataClass>, resp: Response<PrescriptionDataClass>) {

                    if (resp.isSuccessful) {

                        var newbody = resp.body()
                        // Use it or ignore it
                        navigateToActivity(Intent(applicationContext, Prescription_manualDrugDialog::class.java))
                    } else {
                        Toast.makeText(applicationContext, "Failed at else part.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PrescriptionDataClass>, t: Throwable) {

                    Toast.makeText(applicationContext, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            })

        }
        d.show()
    }

    private fun loadDetails() {
        val prescription_data = ServiceBuilder1.buildService(PrescriptionInterface::class.java)
        val requestCall = prescription_data.getDoctorListbyId(mobile_no.toString())
        showLongToast(requestCall.toString())
        requestCall.enqueue(object : retrofit2.Callback<List<PrescriptionDataClass>> {
            override fun onResponse(call: Call<List<PrescriptionDataClass>>, response: Response<List<PrescriptionDataClass>>) {
                if (response.isSuccessful) {
                    val destination = response.body()
                    val size = destination!!.size
                    val prescription = destination?.get(size)

                    prescription?.let {
                       val prescriptionid = prescription.prescription_id
                        showLongToast(prescriptionid.toString())

                    }!!
                } else {
                }
            }

            override fun onFailure(call: Call<List<PrescriptionDataClass>>, t: Throwable) {
                
            }
        })
    }


    override fun onResume() {
        super.onResume()
        loadValues()

//                retrieve();
    }

    private fun loadValues() {

        val destinationService = ServiceBuilder1.buildService(PrescriptionInterface::class.java)
        rv = findViewById<View>(R.id.recyclerview_doctorlist) as RecyclerView

        val requestCall = destinationService.getDoctorListbyId(mobile_no!!)

        requestCall.enqueue(object : Callback<List<PrescriptionDataClass>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */


            // If you receive a HTTP Response, then this method is executed
            // Your STATUS Code will decide if your Http Response is a Success or Error
            override fun onResponse(call: Call<List<PrescriptionDataClass>>, response: Response<List<PrescriptionDataClass>>) {

                if (response.isSuccessful()) {
                    // Your status code is in the range of 200's
                    val prescriptionList = response.body()!!

                    val linearlayoutmanager = LinearLayoutManager(applicationContext)

                    linearlayoutmanager.orientation = LinearLayoutManager.VERTICAL


                    rv!!.setLayoutManager(linearlayoutmanager)
                    rv!!.adapter = Prscription_DoctorList_Adapter(prescriptionList)

                    rv!!.adapter!!.notifyDataSetChanged()


                    Log.e("errpr msg resp succ", response.message())

                } else if (response.code() == 401) {
                    Toast.makeText(this@Prescription_ManualDoctorDialog, "Your session has expired. Please Login again.", Toast.LENGTH_LONG).show()
                } else { // Application-level failure
                    // Your status code is in the range of 300's, 400's and 500's
                    Toast.makeText(this@Prescription_ManualDoctorDialog, "Failed to retrieve items123", Toast.LENGTH_LONG).show()
                }
            }

            // Invoked in case of Network Error or Establishing connection with Server
            // or Error Creating Http Request or Error Processing Http Response
            override fun onFailure(call: Call<List<PrescriptionDataClass>>, t: Throwable) {

                Toast.makeText(this@Prescription_ManualDoctorDialog, "Error Occurred" + t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}