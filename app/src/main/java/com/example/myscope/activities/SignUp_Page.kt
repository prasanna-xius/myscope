package com.example.myscope.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import com.example.myscope.R
import kotlinx.android.synthetic.main.signuppage_main.*

class SignUp_Page : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signuppage_main)
        btn_SignUp!!.setOnClickListener(this)
        btn_login!!.setOnClickListener(this)
        hideKeyBoard()
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_SignUp -> signup()
            R.id.btn_login -> {
                navigateToActivity(Intent(applicationContext,Login_Page::class.java))
            }
        }
    }
    private fun signup() {
        Log.d(TAG, "SignUp_Page")
        val firstName = edt_firstname!!.text.toString().trim { it <= ' ' }
        val lastName = edt_lastname!!.text.toString().trim { it <= ' ' }
        val mobileNumber = edt_mobile!!.text.toString().trim { it <= ' ' }
        val emailId = edt_email!!.text.toString().trim { it <= ' ' }
        if (validate() == false) {
            onSignupFailed()
            return
        }
        navigateToActivity(Intent(applicationContext,Login_Page::class.java))
        showLongToast("Registration Successfully Completed")
    }
    private fun onSignupFailed() {
        showLongToast("Please Enter all fields")
        btn_SignUp!!.isEnabled = true
    }
    private fun validate(): Boolean {
        var valid = true
        val firstName = edt_firstname!!.text.toString().trim { it <= ' ' }
        val lastName = edt_lastname!!.text.toString().trim { it <= ' ' }
        val mobileNumber = edt_mobile!!.text.toString().trim { it <= ' ' }
        val emailId = edt_email!!.text.toString().trim { it <= ' ' }
        firstname_layout!!.error = null
        lastname_layout!!.error = null
        mobile_layout!!.error = null
        email_layout!!.error = null
        if (firstName.isEmpty() || firstName.length < 3) {
            firstname_layout!!.error = "at least 3 characters"
            valid = false
        }
        if (lastName.isEmpty() || lastName.length < 3) {
            lastname_layout!!.error = "at least 3 characters"
            valid = false
        }
        if (mobileNumber.isEmpty() || mobileNumber.length < 10) {
            mobile_layout!!.error = "enter mobile number"
            valid = false
        }
        if (TextUtils.isEmpty(emailId)) {
            email_layout!!.error = "Email is required"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            email_layout!!.error = "Enter a valid email"
            return false
        }
        return valid
    }
    companion object {
        private const val TAG = "SignUp_Page"
    }
}