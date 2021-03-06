package com.pandulapeter.beagle.core.list.delegates

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.list.cells.ExpandedItemTextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ExpandableModuleDelegate
import com.pandulapeter.beagle.modules.LongTextModule

internal class LongTextDelegate : ExpandableModuleDelegate<LongTextModule> {

    override fun canExpand(module: LongTextModule) = when (val text = module.text) {
        is Text.CharSequence -> text.charSequence.isNotBlank()
        is Text.ResourceId -> true
    }

    override fun MutableList<Cell<*>>.addItems(module: LongTextModule) {
        add(
            ExpandedItemTextCell(
                id = "text_${module.id}",
                text = module.text,
                isEnabled = true,
                shouldEllipsize = false,
                onItemSelected = null
            )
        )
    }
}