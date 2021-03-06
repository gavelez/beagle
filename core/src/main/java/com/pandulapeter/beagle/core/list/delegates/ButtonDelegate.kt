package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.cells.ButtonCell
import com.pandulapeter.beagle.modules.ButtonModule

@Deprecated("Remove together with ButtonModule")
internal class ButtonDelegate : Module.Delegate<ButtonModule> {

    override fun createCells(module: ButtonModule): List<Cell<*>> = listOf<Cell<*>>(
        ButtonCell(
            id = module.id,
            text = module.text,
            isEnabled = true,
            icon = null,
            onButtonPressed = module.onButtonPressed
        )
    )
}