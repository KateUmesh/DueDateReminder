package com.duedatereminder.docpicker.ui.common.bottom_sheet_filter


data class DocFilterModel(var docType: String = "", var isSelected: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null) {
            return false
        }

        if (!(other is DocFilterModel)) {
            return false
        }
        return docType == other.docType
    }
}