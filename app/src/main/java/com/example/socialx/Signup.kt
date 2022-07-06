package com.example.socialx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var Name: EditText
    private lateinit var Signupbutton: Button
    private lateinit var mauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        mauth= FirebaseAuth.getInstance()
        Email=findViewById(R.id.emailedit)
        Password=findViewById(R.id.passwordedit)
        Name=findViewById(R.id.nameedit)
        Signupbutton=findViewById(R.id.SignupButton)
        Signupbutton.setOnClickListener {
            val name=Name.text.toString()
            val email=Email.text.toString()
            val password=Password.text.toString()
            signup(email,password)
        }
    }
    private fun signup(email: String, password: String) {
        mauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this,"Something went worng", Toast.LENGTH_LONG).show()
                }
            }
    }
}