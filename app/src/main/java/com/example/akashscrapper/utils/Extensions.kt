package com.example.akashscrapper.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.akashscrapper.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

fun SharedPreferences.save(key: String, value: Any) {
    with(this.edit()) {
        when (value) {
            is Float -> putFloat(key, value).apply()
            is Boolean -> putBoolean(key, value).apply()
            is Int -> putInt(key, value).apply()
            is String -> putString(key, value).apply()
            else -> UnsupportedOperationException("Unsupported Type to save")
        }
    }
}

fun AppCompatActivity.setToolbar(
    toolbar: Toolbar,
    hasUpEnabled: Boolean = true,
    homeButtonEnabled: Boolean = true,
    title: String = "",
    show: Boolean = true,
    @DrawableRes indicator: Int = 0
) {
    setSupportActionBar(toolbar)
    if (show) {
        if (title.isNotEmpty())
            supportActionBar?.title = title
        if (indicator != 0)
            supportActionBar?.setHomeAsUpIndicator(indicator)
        supportActionBar?.setDisplayHomeAsUpEnabled(hasUpEnabled)
        supportActionBar?.setHomeButtonEnabled(homeButtonEnabled)
        supportActionBar?.show()
    } else supportActionBar?.hide()
}


fun AppCompatActivity.replaceFragmentSafely(
    fragment: Fragment,
    tag: String = "",
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    addToStack: Boolean = false
) {
    val ft = supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        .replace(containerViewId, fragment, tag)
    if (addToStack) {
        ft.addToBackStack(tag)
    }
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun <F : Fragment> F.replaceFragmentSafely(
    fragment: Fragment,
    tag: String = "",
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int,
    @AnimatorRes enterAnimation: Int = R.animator.slide_in_left,
    @AnimatorRes exitAnimation: Int = R.animator.slide_out_left,
    @AnimatorRes popEnterAnimation: Int = R.animator.slide_out_right,
    @AnimatorRes popExitAnimation: Int = R.animator.slide_in_right,
    addToStack: Boolean = true
) {
    val ft = fragmentManager!!
        .beginTransaction()
        .setCustomAnimations(
            enterAnimation, exitAnimation,
            popEnterAnimation, popExitAnimation
        )
        .replace(containerViewId, fragment, tag)
    if (addToStack) {
        ft.addToBackStack(tag)
    }
    if (!fragmentManager!!.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun ViewModel.runIO(function: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.IO) { function() }
}

fun View.showSnackbar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    anchorView: BottomNavigationView? = null,
    action: Boolean = true,
    actionText: String = "Retry",
    callback: () -> Unit = { }
): Snackbar {
    val snackBarView = Snackbar.make(this, message, length)
    val params = snackBarView.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        params.leftMargin,
        params.topMargin,
        params.rightMargin,
        params.bottomMargin + 100
    )
    snackBarView.animationMode = Snackbar.ANIMATION_MODE_SLIDE

    snackBarView.view.layoutParams = params
    if (anchorView != null) {
        snackBarView.anchorView = anchorView
    }
    if (action)
        snackBarView.setAction(actionText) {
            callback()
        }
    snackBarView.show()
    return snackBarView
}

@Keep
fun <F : Fragment> F.getPrefs(): PreferenceHelper? {
    return context?.let { PreferenceHelper.getPrefs(it) }
}

@Keep
fun <A : Context> A.getPrefs(): PreferenceHelper {
    return PreferenceHelper.getPrefs(this)
}


fun <T> LiveData<T>.observer(owner: LifecycleOwner, onEmission: (T) -> Unit) {
    return observe(owner, Observer<T> {
        if (it != null) {
            onEmission(it)
        }
    })
}

fun Context.checkPermission(): Boolean {

    val readExternal = ContextCompat.checkSelfPermission(
        this.applicationContext,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val writeExternal = ContextCompat.checkSelfPermission(
        this.applicationContext,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    return readExternal == PackageManager.PERMISSION_GRANTED && writeExternal == PackageManager.PERMISSION_GRANTED
}

fun Context.isStoragePermissionGranted(): Boolean {
    return if (Build.VERSION.SDK_INT >= 23) {
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {

            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            false
        }
    } else { // permission is automatically granted on sdk<23 upon installation
        true
    }
}

fun Context.getKeyAlias(): String {
    return MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
}

fun File.encryptFile(context: Context) {
    try {
        val encryptedFile = context.getEncryptedFile("${this.name}_encrypted")
        encryptedFile.openFileOutput().use { output ->
            output.write(this.readBytes())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun Context.getEncryptedFile(name: String): EncryptedFile {
    return EncryptedFile.Builder(
        File("${this.getExternalFilesDir(Environment.getDataDirectory().absolutePath)}/${Environment.DIRECTORY_DOCUMENTS}/$name"),
        this,
        this.getKeyAlias(),
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()
}

fun Context.decryptFile(title: String): String {
    val encryptedFile = getEncryptedFile("${title}_encrypted")

    try {
        encryptedFile.openFileInput().use { input ->
            return String(input.readBytes(), Charsets.UTF_8)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}
