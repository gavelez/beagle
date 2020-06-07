package com.pandulapeter.beagle.modules

import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.ExpandableModule
import com.pandulapeter.beagle.common.contracts.module.PersistableModule
import java.util.UUID

/**
 * Displays a list of check boxes represented by [BeagleListItemContract] instances. Any number of items can be selected at any given time.
 *
 * @param id - A unique identifier for the module. Optional, random string by default.
 * @param title - The title of the module that will be displayed in the header of the list.
 * @param isExpandedInitially - Whether or not the list is expanded the first time the module becomes visible. Optional, false by default.
 * @param items - The list of items that should be displayed.
 * @param initialValue - The ID of the item that should be selected initially. If [shouldBePersisted] is true, the value coming from the local storage will override this parameter so it will only be used the first time the app is launched.
 * @param shouldBePersisted - Can be used to enable or disable persisting the selected value on the local storage. This will only work if the module has a unique, constant ID. Optional, false by default.
 * @param onValueChanged - Callback called when the changes the selection. The parameter is the ID of the selected item.
 */
//TODO: Rename parameters
data class MultipleSelectionListModule(
    override val id: String = UUID.randomUUID().toString(),
    override val title: CharSequence,
    override val isExpandedInitially: Boolean = false,
    val items: List<BeagleListItemContract>,
    override val initialValue: Set<String>,
    override val shouldBePersisted: Boolean = false,
    override val onValueChanged: (selectedIds: Set<String>) -> Unit
) : ExpandableModule<MultipleSelectionListModule>, PersistableModule<Set<String>, MultipleSelectionListModule> {

    override fun createModuleDelegate(): Nothing = throw IllegalStateException("Built-in Modules should never create their own Delegates.")
}