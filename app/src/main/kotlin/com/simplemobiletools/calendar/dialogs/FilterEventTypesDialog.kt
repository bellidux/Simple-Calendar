package com.simplemobiletools.calendar.dialogs

import android.support.v7.app.AlertDialog
import com.simplemobiletools.calendar.R
import com.simplemobiletools.calendar.activities.SimpleActivity
import com.simplemobiletools.calendar.adapters.FilterEventTypeAdapter
import com.simplemobiletools.calendar.extensions.config
import com.simplemobiletools.calendar.extensions.dbHelper
import com.simplemobiletools.calendar.models.EventType
import com.simplemobiletools.commons.extensions.setupDialogStuff
import kotlinx.android.synthetic.main.dialog_filter_event_types.view.*
import java.util.*

class FilterEventTypesDialog(val activity: SimpleActivity, val callback: () -> Unit) {
    var dialog: AlertDialog
    var eventTypes = ArrayList<EventType>()
    val view = activity.layoutInflater.inflate(R.layout.dialog_filter_event_types, null)

    init {
        val eventTypes = activity.dbHelper.fetchEventTypes()
        val displayEventTypes = activity.config.displayEventTypes
        view.filter_event_types_list.adapter = FilterEventTypeAdapter(activity, eventTypes, displayEventTypes)

        dialog = AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, { dialogInterface, i -> confirmEventTypes() })
                .setNegativeButton(R.string.cancel, null)
                .create().apply {
            activity.setupDialogStuff(view, this, R.string.filter_events_by_type)
        }
    }

    private fun confirmEventTypes() {
        val selectedItems = (view.filter_event_types_list.adapter as FilterEventTypeAdapter).getSelectedItemsSet()
        if (activity.config.displayEventTypes != selectedItems) {
            activity.config.displayEventTypes = selectedItems
            callback()
        }
        dialog.dismiss()
    }
}
