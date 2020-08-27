package com.pandulapeter.beagle.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.core.view.InternalDebugMenuView

internal class DebugMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_debug_menu)
        supportActionBar?.hide()
        findViewById<Toolbar>(R.id.beagle_toolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
            navigationIcon = tintedDrawable(com.pandulapeter.beagle.core.R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val debugMenu = findViewById<InternalDebugMenuView>(R.id.beagle_debug_menu)
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        val input = Insets(
                            left = it.systemWindowInsetLeft,
                            top = it.systemWindowInsetTop,
                            right = it.systemWindowInsetRight,
                            bottom = it.systemWindowInsetBottom
                        )
                        val output = BeagleCore.implementation.appearance.applyInsets?.invoke(input) ?: Insets(
                            left = it.systemWindowInsetLeft,
                            top = 0,
                            right = it.systemWindowInsetRight,
                            bottom = it.systemWindowInsetBottom
                        )
                        debugMenu.applyInsets(output.left, output.top, output.right, output.bottom)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = output.bottom } }
                    }
                }
                requestApplyInsets()
            }
        }
        if (savedInstanceState == null) {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
        }
    }

    override fun onStart() {
        super.onStart()
        (BeagleCore.implementation.uiManager as ActivityUiManager).debugMenuActivity = this
    }

    override fun onStop() {
        (BeagleCore.implementation.uiManager as ActivityUiManager).let {
            if (it.debugMenuActivity == this) {
                it.debugMenuActivity = null
                if (isFinishing) {
                    BeagleCore.implementation.notifyVisibilityListenersOnHide()
                }
            }
        }
        super.onStop()
    }


    private fun Context.tintedDrawable(@DrawableRes drawableResourceId: Int, @ColorInt tint: Int) = AppCompatResources.getDrawable(this, drawableResourceId)?.let { drawable ->
        DrawableCompat.wrap(drawable.mutate()).apply {
            DrawableCompat.setTint(this, tint)
            DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
        }
    }

    @SuppressLint("ResourceAsColor")
    @ColorInt
    private fun Context.colorResource(@AttrRes id: Int): Int {
        val resolvedAttr = TypedValue()
        theme.resolveAttribute(id, resolvedAttr, true)
        return ContextCompat.getColor(this, resolvedAttr.run { if (resourceId != 0) resourceId else data })
    }

}