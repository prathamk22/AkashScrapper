package com.example.akashscrapper.SplashHolder.LoginSignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.akashscrapper.R
import com.example.akashscrapper.utils.getPrefs
import com.example.akashscrapper.utils.observer
import com.example.akashscrapper.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_signup.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class SignupFragment : Fragment() {

    val vm by stateViewModel<LoginSignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStarted.setOnClickListener {
            when {
                fullName.text.isNullOrBlank() -> getStarted.showSnackbar("Enter Proper Fullname")
                email.text.isNullOrEmpty() or (email.text?.indexOf('@') != -1) ->
                    getStarted.showSnackbar("Enter Proper Email Address")
                password.text.isNullOrEmpty() or (password.text?.length!! < 6) -> getStarted.showSnackbar(
                    "Password length should be more than 6 characters"
                )
                password.text.toString() != confirmPassword.text.toString() -> getStarted.showSnackbar(
                    "Password doesn't matches"
                )
                else -> vm.signUp(
                    email.text.toString(),
                    password.text.toString(),
                    fullName.text.toString()
                )
            }
        }

        vm.token.observer(this) {
            if (it.isNotEmpty()) {
                getPrefs()?.SP_JWT_TOKEN_KEY = it
                getPrefs()?.NAME = fullName.text.toString()
            }
        }
    }
}