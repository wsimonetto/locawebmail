package br.com.fiap.locamail.component.email.state

object EmailListState {

    private val emailIds: MutableList<Int> = mutableListOf()

     fun getEmailIds(): List<Int> {
        return emailIds.toList()
    }

    fun addEmailId(emailId: Int) {
        emailIds.add(emailId)
    }

    fun removeEmailId(emailId: Int) {
        emailIds.remove(emailId)
    }

    fun clearEmailIds() {
        emailIds.clear()
    }

}