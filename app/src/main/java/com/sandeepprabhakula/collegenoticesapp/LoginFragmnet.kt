package com.sandeepprabhakula.collegenoticesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sandeepprabhakula.collegenoticesapp.daos.UserDao
import com.sandeepprabhakula.collegenoticesapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginFragmnet : Fragment() {
    private val RC_SIGN_IN = 200;
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_login_fragmnet, container, false)
        val googleSignIn :SignInButton
        googleSignIn = view.findViewById(R.id.googleSignIn);
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.ADMIN_UID)
            .requestEmail()
            .build()
        mAuth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignIn.setOnClickListener {
            signIn()
        }
        return view
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w("TAG", "Google sign in failed", e)
        }
    }
    private fun firebaseAuthWithGoogle(idToken: Any) {
        val credential = GoogleAuthProvider.getCredential(idToken.toString(),null)

        GlobalScope.launch(Dispatchers.IO){
            val auth = mAuth.signInWithCredential(credential).await()
            val user = auth.user
            withContext(Dispatchers.Main){
                updateUI(user)
            }
        }
    }
    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser!=null){
            val user = User(firebaseUser.uid,firebaseUser.displayName,firebaseUser.photoUrl.toString())
            val userDao = UserDao()
            userDao.addUsers(user)
            findNavController().navigate(R.id.action_loginFragmnet_to_noticesFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        googleSignInClient.signOut()
        updateUI(mAuth.currentUser)
    }
}