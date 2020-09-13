package com.example.akashscrapper.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.classesPanel.ClassPanel
import com.example.akashscrapper.dashboard.filesPanel.FilesPanel
import com.example.akashscrapper.dashboard.userPanel.UserPanel
import com.example.akashscrapper.utils.getPrefs
import com.example.akashscrapper.utils.observer
import com.example.akashscrapper.utils.replaceFragmentSafely
import com.example.akashscrapper.utils.showSnackbar
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

@ExperimentalPagingApi
class DashboardActivity : AppCompatActivity() {

    val vm: DashboardViewModel by stateViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (getPrefs().SP_JWT_TOKEN_KEY.isNullOrEmpty()) {
            vm.getToken().observer(this) {
                getPrefs().SP_JWT_TOKEN_KEY = it ?: ""
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