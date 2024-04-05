package com.example.task3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val tag = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        // Assuming you have a button with the id 'register'
        val registerButton = findViewById<Button>(R.id.register)

        registerButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val qualification = findViewById<EditText>(R.id.qualification).text.toString()
            val studentNumber = findViewById<EditText>(R.id.student_number).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()

            if (password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(tag, "createUserWithEmail:success")
                            val user = auth.currentUser
                            updateUI()

                            // Add user information to Firebase
                            val db = FirebaseFirestore.getInstance()
                            val userRef = db.collection("users")
                            val userData = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "qualification" to qualification,
                                "studentNumber" to studentNumber
                            )
                            userRef.document(user!!.uid).set(userData)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(tag, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI()
                        }
                    }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        // Handle user update
    }
}