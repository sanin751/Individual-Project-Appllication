package com.example.bicyclerentalservice.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bicyclerentalservice.databinding.ActivityRegisterBinding
import com.example.bicyclerentalservice.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        binding.signUp.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.registerEmail.text.toString().trim()
        val password = binding.registerPassword.text.toString().trim()
        val fName = binding.registerFname.text.toString().trim()
        val lName = binding.registerLName.text.toString().trim()
        val address = binding.registerAddress.text.toString().trim()
        val contact = binding.registerContact.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || fName.isEmpty() || lName.isEmpty() || address.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: ""
                val user = UserModel(userId, email, fName, lName, address, contact)

                // Store user in Firebase Database
                database.child(userId).setValue(user).addOnCompleteListener { dbTask ->
                    if (dbTask.isSuccessful) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
