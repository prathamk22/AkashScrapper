package com.example.akashscrapper.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.classesPanel.ClassPanel
import com.example.akashscrapper.dashboard.filesPanel.FilesPanel
import com.example.akashscrapper.dashboard.userPanel.UserPanel
import com.example.akashscrapper.network.APICommunicator
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.utils.getPrefs
import com.example.akashscrapper.utils.replaceFragmentSafely
import com.example.akashscrapper.utils.showSnackbar
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class DashboardActivity : AppCompatActivity() {

    val vm: DashboardViewModel by stateViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (getPrefs().SP_JWT_TOKEN_KEY.isNullOrEmpty()) {
            vm.getToken().observe(this) {
                getPrefs().SP_JWT_TOKEN_KEY = it ?: ""
                AkashOnlineLib.initialize(object : APICommunicator {
                    override var authJwt: String
                        get() = getPrefs().SP_JWT_TOKEN_KEY
                        set(value) {
                            getPrefs().SP_JWT_TOKEN_KEY = value
                        }
                    override var refreshToken: String
                        get() = getPrefs().SP_JWT_REFRESH_TOKEN
                        set(value) {
                            getPrefs().SP_JWT_REFRESH_TOKEN = value
                        }

                })
            }
        }

        replaceFragmentSafely(ClassPanel(), containerViewId = R.id.classesPanel)
        replaceFragmentSafely(FilesPanel(), containerViewId = R.id.filesPanel)
        replaceFragmentSafely(UserPanel(), containerViewId = R.id.userPanel)

        vm.errorLiveData.observe(this) {
            overlapping_panels.showSnackbar(it)
        }
    }
}