package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.list.delegates.NetworkLogListDelegate
import com.pandulapeter.beagle.core.util.model.NetworkLogEntry
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.consume

internal class NetworkLogItemViewHolder private constructor(
    itemView: View,
    onItemSelected: (String) -> Unit,
    onItemLongTapped: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val id get() = itemView.tag as String
    private val checkBox = itemView.findViewById<CheckBox>(R.id.beagle_check_box)
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemLongTapped(id)
        }
    }

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemSelected(id)
            }
        }
        itemView.setOnLongClickListener {
            consume {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongTapped(id)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.entry.id
        textView.setText(
            NetworkLogListDelegate.format(
                entry = uiModel.entry,
                formatter = BeagleCore.implementation.appearance.logShortTimestampFormatter
            )
        )
        checkBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val entry: NetworkLogEntry,
        val isSelected: Boolean
    ) : BugReportListItem {

        override val id: String = "networkLog_${entry.id}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (String) -> Unit,
            onItemLongTapped: (String) -> Unit
        ) = NetworkLogItemViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_network_log_item, parent, false),
            onItemSelected = onItemSelected,
            onItemLongTapped = onItemLongTapped
        )
    }
}