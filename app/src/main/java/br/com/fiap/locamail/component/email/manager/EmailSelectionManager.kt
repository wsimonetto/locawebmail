package br.com.fiap.locamail.component.email.manager

object EmailSelectionManager {
    private var selection: Boolean = false

    fun getSelection(): Boolean {
        return selection
    }

    fun setSelection(value: Boolean) {
        selection = value
    }
}

