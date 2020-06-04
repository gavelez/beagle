package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.builtIn.TextModuleContract

/**
 * Empty implementation.
 */
@Suppress("unused")
data class TextModule(
    override val id: String = "",
    override val text: CharSequence = "",
    @ColorInt override val color: Int? = null
) : TextModuleContract {

    override fun createCells() = emptyList<Cell<*>>()
}