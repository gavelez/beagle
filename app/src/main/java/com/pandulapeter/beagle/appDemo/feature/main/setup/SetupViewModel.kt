package com.pandulapeter.beagle.appDemo.feature.main.setup

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.GithubButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.HeaderViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.RadioButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SpaceViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlin.properties.Delegates

class SetupViewModel : ListViewModel<SetupListItem>() {

    private val _items = MutableLiveData<List<SetupListItem>>()
    override val items: LiveData<List<SetupListItem>> = _items
    private var selectedUiVariant by Delegates.observable(UiVariant.ACTIVITY) { _, _, _ -> refreshItems() }
    private var selectedSection by Delegates.observable<Section?>(Section.WELCOME) { _, _, _ -> refreshItems() }
    private var hasSectionJustChanged = true

    init {
        refreshItems()
    }

    fun onRadioButtonSelected(position: Int) {
        UiVariant.fromResourceId((_items.value?.get(position) as? RadioButtonViewHolder.UiModel?)?.titleResourceId)?.let {
            selectedUiVariant = it
        }
    }

    fun onHeaderSelected(position: Int) {
        Section.fromResourceId((_items.value?.get(position) as? HeaderViewHolder.UiModel?)?.titleResourceId).let {
            selectedSection = if (it == selectedSection) null else it
            hasSectionJustChanged = true
        }
    }

    fun shouldBeFullSize(position: Int) = _items.value?.get(position) !is RadioButtonViewHolder.UiModel

    fun shouldSetAppBarToNotLifted() = hasSectionJustChanged.also {
        hasSectionJustChanged = false
    }

    private fun refreshItems() {
        _items.value = mutableListOf<SetupListItem>().apply {
            addWelcomeSection()
            addInitializationSection()
            addModuleConfigurationSection()
            addTroubleshootingSection()
        }
    }

    private fun MutableList<SetupListItem>.addWelcomeSection() {
        add(HeaderViewHolder.UiModel(R.string.setup_header_1, selectedSection == Section.WELCOME))
        if (selectedSection == Section.WELCOME) {
            add(TextViewHolder.UiModel(R.string.setup_text_1))
            add(GithubButtonViewHolder.UiModel())
            add(TextViewHolder.UiModel(R.string.setup_hint))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<SetupListItem>.addInitializationSection() {
        add(HeaderViewHolder.UiModel(R.string.setup_header_2, selectedSection == Section.INITIALIZATION))
        if (selectedSection == Section.INITIALIZATION) {
            add(TextViewHolder.UiModel(R.string.setup_text_2))
            add(
                CodeSnippetViewHolder.UiModel(
                    "allprojects {\n" +
                            "    repositories {\n" +
                            "        …\n" +
                            "        maven { url \"https://jitpack.io\" }\n" +
                            "    }\n" +
                            "}"
                )
            )
            add(TextViewHolder.UiModel(R.string.setup_text_3))
            add(SpaceViewHolder.UiModel())
            addAll(UiVariant.values().map { uiVariant ->
                RadioButtonViewHolder.UiModel(uiVariant.titleResourceId, uiVariant == selectedUiVariant)
            })
            add(TextViewHolder.UiModel(R.string.setup_text_4))
            add(
                CodeSnippetViewHolder.UiModel(
                    id = "codeSnippet_gradle",
                    codeSnippet = "dependencies {\n" +
                            "    …\n" +
                            //"    def beagleVersion = \"2.x.y\"\n" +
                            "    debugImplementation \"com.github.pandulapeter.beagle:ui-${when (selectedUiVariant) {
                                UiVariant.ACTIVITY -> "activity"
                                UiVariant.BOTTOM_SHEET -> "bottom-sheet"
                                UiVariant.DIALOG -> "dialog"
                                UiVariant.DRAWER -> "drawer"
                                UiVariant.VIEW -> "view"
                            }
                            }:\$beagleVersion\"\n" +
                            "    releaseImplementation \"com.github.pandulapeter.beagle:noop:\$beagleVersion\"\n" +
                            "}"
                )
            )
            add(TextViewHolder.UiModel(R.string.setup_text_5))
            add(CodeSnippetViewHolder.UiModel("Beagle.initialize(this)"))
            add(TextViewHolder.UiModel(R.string.setup_text_6))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<SetupListItem>.addModuleConfigurationSection() {
        add(HeaderViewHolder.UiModel(R.string.setup_header_3, selectedSection == Section.MODULE_CONFIGURATION))
        if (selectedSection == Section.MODULE_CONFIGURATION) {
            add(TextViewHolder.UiModel(R.string.setup_text_7))
            add(CodeSnippetViewHolder.UiModel("Beagle.setModules(module1, module2, …)"))
            add(TextViewHolder.UiModel(R.string.setup_text_8))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<SetupListItem>.addOtherFeaturesSection() {
        add(HeaderViewHolder.UiModel(R.string.setup_header_4, selectedSection == Section.OTHER_FEATURES))
        if (selectedSection == Section.OTHER_FEATURES) {
            add(TextViewHolder.UiModel(R.string.setup_text_9))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<SetupListItem>.addTroubleshootingSection() {
        add(HeaderViewHolder.UiModel(R.string.setup_header_5, selectedSection == Section.TROUBLESHOOTING))
        if (selectedSection == Section.TROUBLESHOOTING) {
            add(TextViewHolder.UiModel(R.string.setup_text_9))
            add(SpaceViewHolder.UiModel())
        }
    }

    private enum class UiVariant(@StringRes val titleResourceId: Int) {
        ACTIVITY(R.string.setup_variant_activity),
        BOTTOM_SHEET(R.string.setup_variant_bottom_sheet),
        DIALOG(R.string.setup_variant_dialog),
        DRAWER(R.string.setup_variant_drawer),
        VIEW(R.string.setup_variant_view);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }

    private enum class Section(@StringRes val titleResourceId: Int) {
        WELCOME(R.string.setup_header_1),
        INITIALIZATION(R.string.setup_header_2),
        MODULE_CONFIGURATION(R.string.setup_header_3),
        OTHER_FEATURES(R.string.setup_header_4),
        TROUBLESHOOTING(R.string.setup_header_5);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}