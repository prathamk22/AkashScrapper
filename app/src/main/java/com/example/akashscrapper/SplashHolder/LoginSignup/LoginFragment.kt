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
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class LoginFragment : Fragment() {

    val vm by stateViewModel<LoginSignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener {
            when {
                email.text.isNullOrBlank() -> login.showSnackbar("Enter correct email")
                password.text.isNullOrBlank() -> login.showSnackbar("Enter correct password")
                else -> {
                    login.showSnackbar("Logging in")
                    login.isActivated = false
                    vm.login(email.text.toString(), password.text.toString())
                }
            }
        }

        vm.errorLiveData.observer(this) {
            login.showSnackbar(it)
        }

        vm.token.observer(this) {
            if (it.isNotEmpty()) {
                getPrefs()?.SP_JWT_TOKEN_KEY = it
            }
        }
    }
}