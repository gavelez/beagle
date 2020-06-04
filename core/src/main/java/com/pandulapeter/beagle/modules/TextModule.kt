package com.pandulapeter.beagle.modules

import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.builtIn.TextModuleContract
import com.pandulapeter.beagle.core.list.cells.TextCell
import java.util.UUID

/**
 * Displays text.
 *
 * @param id - A unique identifier for the module. Optional, auto-generated by default.
 * @param text - The text to display.
 * @param color - The resolved color for the text. Optional, color from theme is used by default.
 */
class TextModule(
    override val id: String = "text_${UUID.randomUUID()}",
    override val text: CharSequence,
    @ColorInt override val color: Int? = null
) : TextModuleContract {

    override fun createCells() = listOf<Cell<*>>(TextCell(id, text, color))
}