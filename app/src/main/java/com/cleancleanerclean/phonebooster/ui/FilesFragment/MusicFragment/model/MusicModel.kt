package com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.model


class MusicModel(
    var name: String,
    var duration: String,
    var size: String,
    var path: String,
    var id: Int
) {
    var isCheckboxIsVisible = false //todo add setters and getters
    var isChecked = false //todo add setters and getters
    var album: String? = null
    var artist: String? = null

}
