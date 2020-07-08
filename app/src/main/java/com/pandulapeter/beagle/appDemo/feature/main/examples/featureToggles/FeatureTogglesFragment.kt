package com.pandulapeter.beagle.appDemo.feature.main.examples.featureToggles

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.BeagleListItemContractImplementation
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureToggles.list.FeatureTogglesAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureToggles.list.FeatureTogglesListItem
import com.pandulapeter.beagle.appDemo.utils.createLabelModule
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SliderModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeatureTogglesFragment : ExamplesDetailFragment<FeatureTogglesViewModel, FeatureTogglesListItem>(R.string.case_study_feature_toggles_title) {

    override val viewModel by viewModel<FeatureTogglesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Beagle.addUpdateListener(
            listener = object : UpdateListener {
                override fun onContentsChanged() = viewModel.refreshItems()
            },
            lifecycleOwner = this
        )
    }

    override fun createAdapter() = FeatureTogglesAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected,
        onCurrentStateCardPressed = { Beagle.show() },
        onResetButtonPressed = ::resetAll,
        onBulkApplySwitchToggled = { isEnabled ->
            viewModel.isBulkApplyEnabled = isEnabled
            refreshBeagle()
        })

    override fun getBeagleModules(): List<Module<*>> = listOf(
        createTextModule(R.string.case_study_feature_toggles_hint_1),
        createLabelModule(R.string.case_study_feature_toggles_switches),
        SwitchModule(
            id = TOGGLE_1_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_1),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        SwitchModule(
            id = TOGGLE_2_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_2),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        createLabelModule(R.string.case_study_feature_toggles_check_boxes),
        CheckBoxModule(
            id = TOGGLE_3_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_3),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        CheckBoxModule(
            id = TOGGLE_4_ID,
            text = getString(R.string.case_study_feature_toggles_toggle_4),
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        DividerModule("divider1"),
        createTextModule(R.string.case_study_feature_toggles_hint_2),
        MultipleSelectionListModule(
            id = CHECK_BOX_GROUP_ID,
            title = getText(R.string.case_study_feature_toggles_check_box_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_check_box_3))
            ),
            isExpandedInitially = true,
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            initiallySelectedItemIds = emptySet(),
            onSelectionChanged = { viewModel.refreshItems() }
        ),
        DividerModule("divider2"),
        createTextModule(R.string.case_study_feature_toggles_hint_3),
        SingleSelectionListModule(
            id = RADIO_BUTTON_GROUP_ID,
            title = getText(R.string.case_study_feature_toggles_radio_button_group),
            items = listOf(
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_1)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_2)),
                BeagleListItemContractImplementation(getString(R.string.case_study_feature_toggles_radio_button_3))
            ),
            isExpandedInitially = true,
            isValuePersisted = true,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            initiallySelectedItemId = getString(R.string.case_study_feature_toggles_radio_button_1),
            onSelectionChanged = { viewModel.refreshItems() }
        ),
        DividerModule("divider3"),
        createTextModule(R.string.case_study_feature_toggles_hint_4),
        createLabelModule(R.string.case_study_feature_toggles_slider_label),
        SliderModule(
            id = SLIDER,
            text = { getString(R.string.case_study_feature_toggles_slider_title, it) },
            isValuePersisted = true,
            initialValue = SLIDER_DEFAULT_VALUE,
            shouldRequireConfirmation = viewModel.isBulkApplyEnabled,
            onValueChanged = { viewModel.refreshItems() }
        ),
        createTextModule(R.string.case_study_feature_toggles_hint_5),
        createLabelModule(R.string.case_study_feature_toggles_text_input_label),
        TextModule(id = "textInput", text = getString(R.string.case_study_feature_toggles_text_input_title, "[coming soon]")) //TODO
    )

    private fun resetAll() {
        viewModel.toggle1?.setCurrentValue(Beagle, false)
        viewModel.toggle2?.setCurrentValue(Beagle, false)
        viewModel.toggle3?.setCurrentValue(Beagle, false)
        viewModel.toggle4?.setCurrentValue(Beagle, false)
        viewModel.multipleSelectionOptions?.setCurrentValue(Beagle, emptySet())
        viewModel.singleSelectionOption?.setCurrentValue(Beagle, getString(R.string.case_study_feature_toggles_radio_button_1))
        viewModel.slider?.setCurrentValue(Beagle, SLIDER_DEFAULT_VALUE)
        binding.recyclerView.showSnackbar(R.string.case_study_feature_toggles_state_reset)
    }

    companion object {
        const val TOGGLE_1_ID = "toggle1"
        const val TOGGLE_2_ID = "toggle2"
        const val TOGGLE_3_ID = "toggle3"
        const val TOGGLE_4_ID = "toggle4"
        const val CHECK_BOX_GROUP_ID = "checkBoxes"
        const val RADIO_BUTTON_GROUP_ID = "radioButtons"
        const val SLIDER = "slider"
        const val SLIDER_DEFAULT_VALUE = 5

        fun newInstance() = FeatureTogglesFragment()
    }
}