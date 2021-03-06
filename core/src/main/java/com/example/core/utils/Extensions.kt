package com.example.core.utils

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.core.R
import com.google.android.gms.common.util.IOUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any, K : Any> T.sameAndEqual(n: K): Boolean = ((this.javaClass == n.javaClass) && (this == n))

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

fun RecyclerView.setRv(
    activity: Context,
    listAdapter: ListAdapter<out Any, out RecyclerView.ViewHolder>,
    orientation: Int = RecyclerView.VERTICAL,
    reverse: Boolean = false
) {
    layoutManager = LinearLayoutManager(activity, orientation, reverse)
    adapter = listAdapter
}

fun String.getEncryptedName() =
    if (this.endsWith("pdf")) "${this}_encrypted.dat" else "${this}.pdf_encrypted.dat"

fun View.showSnackbar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    anchorView: BottomNavigationView? = null,
    action: Boolean = false,
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

fun File.encryptFile(context: Context, name: String): Boolean {
    return try {
        val encryptedFile = context.getEncryptedFile(name)
        encryptedFile.openFileOutput().use { output ->
            output.write(this.readBytes())
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.getDirectoryName() =
    "${this.getExternalFilesDir(Environment.getDataDirectory().absolutePath)}/${Environment.DIRECTORY_DOCUMENTS}"

private fun Context.getEncryptedFile(name: String): EncryptedFile {
    return EncryptedFile.Builder(
        File("${getDirectoryName()}/$name"),
        this,
        this.getKeyAlias(),
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()
}

fun File.decryptFile(context: Context, title: String): File? {
    val encryptedFile = context.getEncryptedFile(title)

    try {
        encryptedFile.openFileInput().use { input ->
            val tempFile = File("${context.getDirectoryName()}/${this.name}.pdf")
            if (!tempFile.exists())
                tempFile.createNewFile()
            tempFile.deleteOnExit()
            val out = FileOutputStream(tempFile)
            val long = IOUtils.copyStream(input, out)
            return tempFile
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}
