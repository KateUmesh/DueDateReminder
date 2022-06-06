package com.duedatereminder.model

import android.net.Uri

class UploadFileModel{
    var fileName :String? = null
    var filePath : Uri? = null


    constructor()

    constructor( fileName:String?,filePath:Uri){
        this.fileName = fileName
        this.filePath = filePath

    }
}
