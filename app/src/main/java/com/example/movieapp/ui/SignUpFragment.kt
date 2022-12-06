package com.example.movieapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSignUpBinding
import com.example.movieapp.viewmodel.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val REQUEST_CODE_SIGN_IN_WITH_GOOGLE_From_Sign_Up = 1
class SignUpFragment : Fragment() {
    private val viewModel : SignUpViewModel by viewModels()
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.activity = requireActivity()
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.showError.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(),viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
                viewModel.showErrorDone()
            }
        }

        viewModel.emptyPasswordListener.observe(viewLifecycleOwner){
            if(it){
                binding.passwordEdt.error = "You must enter your password"
                viewModel.emptyPasswordListenerDone()
            }
        }
        viewModel.emptyEmailListener.observe(viewLifecycleOwner){
            if(it){
                binding.emailEdt.error = "You must enter your email"
                viewModel.emptyEmailListenerDone()
            }
        }
        viewModel.emptyConfirmPasswordListener.observe(viewLifecycleOwner){
            if(it){
                binding.confirmPasswordEdt.error = "You must Confirm your password"
                viewModel.emptyConfirmPasswordListenerDone()
            }
        }
        viewModel.unEqualPasswordListener.observe(viewLifecycleOwner){
            if(it){
                binding.confirmPasswordEdt.error = "Not equal password"
                viewModel.unEqualPasswordDone()
            }
        }
        viewModel.navigateToLogIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                viewModel.navigateToLogInDone()
            }
        }
        viewModel.verificationDialog.observe(viewLifecycleOwner){
            if(it){
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Verification")
                    .setMessage("You have to verify your email")
                    .setPositiveButton("OK"){ _,_ ->
                        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                    }.show()
                dialog.setCancelable(false)

                viewModel.showDialogDone()
            }
        }

        viewModel.signInWithGoogleListener.observe(viewLifecycleOwner){
            if(it){
                viewModel.googleSignInClient.signInIntent.also { intent ->
                    startActivityForResult(intent, REQUEST_CODE_SIGN_IN_WITH_GOOGLE_From_Sign_Up)
                    viewModel.signInWithGoogleDone()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN_WITH_GOOGLE_From_Sign_Up){
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
        CoroutineScope(Dispatchers.Main).launch{
            try {
                withContext(Dispatchers.IO) {
                    auth.signInWithCredential(credentials).await()
                }
                if(auth.currentUser != null)
                    findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
            }catch (exe:Exception){
                Toast.makeText(requireContext(),exe.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
}