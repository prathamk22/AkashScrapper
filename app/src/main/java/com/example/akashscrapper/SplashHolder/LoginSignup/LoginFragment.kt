package com.example.akashscrapper.SplashHolder.LoginSignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.akashscrapper.R
import com.example.akashscrapper.utils.showSnackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    val vm by sharedViewModel<LoginSignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStarted.setOnClickListener {
            when {
                email.text.isNullOrBlank() -> getStarted.showSnackbar("Enter correct email")
                password.text.isNullOrBlank() -> getStarted.showSnackbar("Enter correct password")
                else -> {
                    vm.login(email.text.toString(), password.text.toString())
                }
            }
        }
    }
}