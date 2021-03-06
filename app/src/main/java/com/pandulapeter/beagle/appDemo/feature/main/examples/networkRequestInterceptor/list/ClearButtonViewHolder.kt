package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorClearButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class ClearButtonViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorClearButtonBinding,
    onClearButtonPressed: () -> Unit
) : BaseViewHolder<ItemNetworkRequestInterceptorClearButtonBinding, ClearButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClearButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "clearButton"
    ) : NetworkRequestInterceptorListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onClearButtonPressed: () -> Unit
        ) = ClearButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_network_request_interceptor_clear_button, parent, false),
            onClearButtonPressed = onClearButtonPressed
        )
    }
}