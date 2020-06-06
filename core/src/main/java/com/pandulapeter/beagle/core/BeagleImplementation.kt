package com.pandulapeter.beagle.core

import android.app.Application
import android.content.Context
import android.graphics.Canvas
import android.view.ContextThemeWrapper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.manager.DebugMenuInjector
import com.pandulapeter.beagle.core.manager.ListManager
import com.pandulapeter.beagle.core.manager.LocalStorageManager
import com.pandulapeter.beagle.core.manager.MemoryStorageManager
import com.pandulapeter.beagle.core.manager.OverlayListenerManager
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.manager.VisibilityListenerManager
import com.pandulapeter.beagle.core.util.extension.hideKeyboard
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class BeagleImplementation(private val uiManager: UiManagerContract) : BeagleContract {

    override var isUiEnabled by Delegates.observable(true) { _, _, newValue ->
        if (!newValue) {
            hide()
        }
    }
    override val currentActivity get() = debugMenuInjector.currentActivity
    var appearance = Appearance()
        private set
    var behavior = Behavior()
        private set
    internal val memoryStorageManager by lazy { MemoryStorageManager() }
    internal lateinit var localStorageManager: LocalStorageManager
        private set
    private val shakeDetector by lazy { ShakeDetector { show() } }
    private val debugMenuInjector by lazy { DebugMenuInjector(uiManager) }
    private val visibilityListenerManager by lazy { VisibilityListenerManager() }
    private val overlayListenerManager by lazy { OverlayListenerManager() }
    private val listManager by lazy { ListManager() }

    init {
        BeagleCore.implementation = this
    }

    override fun initialize(application: Application, appearance: Appearance, behavior: Behavior) =
        (behavior.shakeThreshold == null || shakeDetector.initialize(application)).also {
            this.appearance = appearance
            this.behavior = behavior
            this.localStorageManager = LocalStorageManager(application)
            debugMenuInjector.register(application)
        }

    override fun show() = (currentActivity?.let { if (it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) uiManager.show(it) else false } ?: false)

    override fun hide() = (currentActivity?.let { uiManager.hide(it) } ?: false)

    override fun setModules(vararg modules: Module<*>) = listManager.setModules(modules.toList())

    override fun <M : Module<M>> findModule(id: String) = listManager.findModule<M>(id)

    override fun <M : Module<M>> findModuleDelegate(type: KClass<out M>) = listManager.findModuleDelegate<M>(type)

    override fun refreshCells() = listManager.refreshList()

    override fun invalidateOverlay() = debugMenuInjector.invalidateOverlay()

    override fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) = visibilityListenerManager.addListener(listener, lifecycleOwner)

    override fun removeVisibilityListener(listener: VisibilityListener) = visibilityListenerManager.removeListener(listener)

    override fun clearVisibilityListeners() = visibilityListenerManager.clearListeners()

    override fun addOverlayListener(listener: OverlayListener, lifecycleOwner: LifecycleOwner?) = overlayListenerManager.addListener(listener, lifecycleOwner)

    override fun removeOverlayListener(listener: OverlayListener) = overlayListenerManager.removeListener(listener)

    override fun clearOverlayListeners() = overlayListenerManager.clearListeners()

    fun createOverlayLayout(activity: FragmentActivity) = uiManager.createOverlayLayout(activity)

    fun notifyVisibilityListenersOnShow() = visibilityListenerManager.notifyVisibilityListenersOnShow()

    fun notifyVisibilityListenersOnHide() = visibilityListenerManager.notifyVisibilityListenersOnHide()

    fun notifyOverlayListenersOnDrawOver(canvas: Canvas) = overlayListenerManager.notifyOverlayListenersOnDrawOver(canvas)

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit

    fun setupRecyclerView(recyclerView: RecyclerView) = listManager.setupRecyclerView(recyclerView)

    fun getThemedContext(context: Context) = appearance.themeResourceId?.let { ContextThemeWrapper(context, it) } ?: context
}