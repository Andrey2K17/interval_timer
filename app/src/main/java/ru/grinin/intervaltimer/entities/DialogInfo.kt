package ru.grinin.intervaltimer.entities


data class DialogInfo(
    val isShow: Boolean,
    val text: String,
    val isError: Boolean = true,
    val title: String = "",
    ) {

    companion object {
        @JvmStatic
        val defaultDialog = DialogInfo(
            isShow = false, text = "",
        )
    }
}