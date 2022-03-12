package com.sandeepprabhakula.collegenoticesapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserProfile : Fragment() {
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        val logout: TextView = view.findViewById(R.id.logout)
        val deleteAccount: Button = view.findViewById(R.id.deleteAccount)
        val dp:ImageView = view.findViewById(R.id.displayPicture)
        val name:TextView = view.findViewById(R.id.userProfileName)
        val email:TextView = view.findViewById(R.id.userProfileEmail)
        Glide.with(dp).load(mAuth.currentUser?.photoUrl).circleCrop().into(dp)
        name.text = mAuth.currentUser?.displayName
        email.text = mAuth.currentUser?.email
        logout.setOnClickListener {
            if (mAuth.currentUser != null) {
                mAuth.signOut()
                findNavController().navigate(R.id.action_userProfile_to_loginFragmnet2)
            }
        }
        deleteAccount.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Delete Account")
                .setMessage("All data will be lost and account will be deleted on confirmation.")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        mAuth.currentUser?.delete()?.await()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_userProfile_to_loginFragmnet2)
                        }
                    }
                }
                .setNegativeButton("No") { _, _ ->

                }
            alertDialog.show()
        }

        return view
    }
}