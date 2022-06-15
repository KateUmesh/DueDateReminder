package com.duedatereminder.docpicker.ui.components.file

import java.io.Serializable


data class DocModel(var id: String = "",
                    var name: String = "",
                    var size: Int,
                    var filePath: String = "",
                    var mimeType:String? ="",
                    var isSelected: Boolean = false ): Serializable {



    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null) {
            return false
        }

        if (!(other is DocModel)) {
            return false
        }
        return id == other.id
    }
}
