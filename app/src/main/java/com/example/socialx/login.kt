package com.example.socialx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import org.w3c.dom.Text
import kotlin.math.log

class login : AppCompatActivity() {
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var Loginbutton: Button
    private lateinit var register: TextView
    private lateinit var mauth: FirebaseAuth
    private lateinit var client:GoogleSignInClient
    private lateinit var signingoogle:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mauth= FirebaseAuth.getInstance()
        Email=findViewById(R.id.emailedit)
        Password=findViewById(R.id.passwordedit)
        Loginbutton=findViewById(R.id.loginbutton)
        register=findViewById(R.id.registerbutton)
        signingoogle=findViewById(R.id.signingoogle)
        val options:GoogleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
        requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail().build()
        client=GoogleSignIn.getClient(this,options)
        signingoogle.setOnClickListener {
            val intent=client.signInIntent
            startActivityForResult(intent,1234)
        }
        register.setOnClickListener {
            val intent=Intent(this,Signup::class.java)
            startActivity(intent)
        }
        Loginbutton.setOnClickListener {
            val email=Email.text.toString()
            val password=Password.text.toString()
            login(email,password)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1234){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            val account:GoogleSignInAccount=task.result
            val credential=GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if(task.isSuccessful){
                        val intent=Intent(application,MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"task failed",Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    override fun onStart() {
        super.onStart()
        val user=FirebaseAuth.getInstance().currentUser
        if(user!=null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

    }
    private fun login(email: String, password: String) {
        mauth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"user not found", Toast.LENGTH_SHORT).show()
                }
            }
    }
}