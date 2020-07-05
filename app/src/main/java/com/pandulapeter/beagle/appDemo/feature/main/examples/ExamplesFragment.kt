package com.pandulapeter.beagle.appDemo.feature.main.examples

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.AuthenticationFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureToggles.FeatureTogglesFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.NetworkRequestInterceptorFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.SimpleSetupFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.staticData.StaticDataFragment
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExamplesFragment : ListFragment<ExamplesViewModel, ExamplesListItem>(R.string.examples_title) {

    override val viewModel by viewModel<ExamplesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        refreshBeagle()
    }

    override fun createAdapter() = ExamplesAdapter(viewModel.viewModelScope, ::onCaseStudySelected)

    override fun getBeagleModules() = mutableListOf<Module<*>>().apply {
        add(createTextModule(R.string.examples_beagle_text_1))
        // Opening Fragments within a backgrounded Activity is not a great idea.
        if (!requireContext().packageName.contains("activity", ignoreCase = true)) {
            add(createTextModule(R.string.examples_beagle_text_2))
            add(PaddingModule())
            addAll(CaseStudy.values().map { caseStudy ->
                TextModule(
                    id = caseStudy.id,
                    text = "• ${getString(caseStudy.title)}",
                    onItemSelected = { onCaseStudySelected(caseStudy) }
                )
            })
        }
    }

    private fun onCaseStudySelected(caseStudy: CaseStudy, view: View? = null) = when (caseStudy) {
        CaseStudy.SIMPLE_SETUP -> navigateTo(SimpleSetupFragment.Companion::newInstance, view)
        CaseStudy.STATIC_DATA -> navigateTo(StaticDataFragment.Companion::newInstance, view)
        CaseStudy.FEATURE_TOGGLES -> navigateTo(FeatureTogglesFragment.Companion::newInstance, view)
        CaseStudy.NETWORK_REQUEST_INTERCEPTOR -> navigateTo(NetworkRequestInterceptorFragment.Companion::newInstance, view)
        CaseStudy.AUTHENTICATION -> navigateTo(AuthenticationFragment.Companion::newInstance, view)
        else -> binding.root.showSnackbar(caseStudy.title)
    }

    private inline fun <reified T : ExamplesDetailFragment<*, *>> navigateTo(crossinline newInstance: () -> T, sharedElement: View?) = parentFragment?.childFragmentManager?.handleReplace(
        addToBackStack = true,
        //TODO: Unpredictable, frequent glitches: sharedElements =  listOf(sharedElement),
        transitionType = TransitionType.MODAL,
        newInstance = newInstance
    ) ?: Unit

    companion object {
        fun newInstance() = ExamplesFragment()
    }
}