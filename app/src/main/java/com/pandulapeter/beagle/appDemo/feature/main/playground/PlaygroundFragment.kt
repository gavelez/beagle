package com.pandulapeter.beagle.appDemo.feature.main.playground

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.AddModuleFragment
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundAdapter
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.consume
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : ListFragment<PlaygroundViewModel, PlaygroundListItem>(R.string.playground_title), ModuleRepository.Listener {

    override val viewModel by viewModel<PlaygroundViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) = if (viewHolder is ModuleViewHolder) makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START or ItemTouchHelper.END
            ) else 0

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) =
                if (viewHolder is ModuleViewHolder && target is ModuleViewHolder) consume { viewModel.onModulesSwapped(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition) } else false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = viewModel.onModuleRemoved(viewHolder.bindingAdapterPosition)

        }).attachToRecyclerView(binding.recyclerView)
        if (viewModel.modules.isEmpty()) {
            registerListener()
        } else {
            binding.root.postDelayed({ registerListener() }, 300)
        }
    }

    override fun createAdapter() = PlaygroundAdapter(
        scope = viewModel.viewModelScope,
        onAddModuleClicked = ::navigateToAddModule,
        onGenerateCodeClicked = ::generateCode
    )

    override fun getBeagleModules() = viewModel.modules.map { it.module }

    override fun onModuleListChanged() {
        viewModel.refreshModules()
        refreshBeagle()
    }

    private fun navigateToAddModule() = parentFragment?.childFragmentManager?.handleReplace(
        transitionType = TransitionType.MODAL,
        addToBackStack = true,
        newInstance = AddModuleFragment.Companion::newInstance
    ) ?: Unit

    private fun generateCode() = binding.recyclerView.showSnackbar(R.string.coming_soon) //TODO: Open the dialog with the code snippet

    private fun registerListener() = viewModel.moduleRepository.registerListener(viewLifecycleOwner, this)

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}