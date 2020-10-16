package com.example.core.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.example.core.R
import com.example.core.dashboard.classesPanel.ClassPanel
import com.example.core.dashboard.filesPanel.FilesPanel
import com.example.core.dashboard.userPanel.UserPanel
import com.example.core.utils.getPrefs
import com.example.core.utils.observer
import com.example.core.utils.replaceFragmentSafely
import com.example.core.utils.showSnackbar
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.stateViewModel

@ExperimentalPagingApi
class DashboardActivity : AppCompatActivity() {

    val vm: DashboardViewModel by stateViewModel()

    @ExperimentalCoroutinesApi
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

        vm.errorLiveData.observer(this) {
            overlapping_panels.showSnackbar(it)
        }
    }
}