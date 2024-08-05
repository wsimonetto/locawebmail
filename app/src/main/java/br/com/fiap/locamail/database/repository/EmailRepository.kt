package br.com.fiap.locamail.database.repository

import android.content.Context
import br.com.fiap.locamail.database.dao.EmailDb
import br.com.fiap.locamail.model.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EmailRepository(context: Context) {

    private var db = EmailDb.getDatabase(context).emailDao()

    fun salvar(email: Email): Long {
        return db.salvar(email)
    }

    fun atualizar(email: Email): Int {
        return db.atualizar(email)
    }

    fun excluir(email: Email): Int {
        return db.excluir(email)
    }

    suspend fun getEmailById(emailId: Int): Email? {
        return withContext(Dispatchers.IO) {
            db.getEmailById(emailId)
        }
    }

    suspend fun buscarEmailPeloId(id: Int): Email? {
        return db.buscarEmailPeloId(id)
    }

    private fun getLocalDateFromTimestamp(timestamp: Long): LocalDate {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.date
    }

    fun buscarEmailsPorDataRecebimento(timestamp: Long): List<Email> {
        val dataRecebimentoDesejada = getLocalDateFromTimestamp(timestamp)
        val emails = db.listarEmailsCaixaEntrada() // Supondo que isso retorna todos os emails
        return emails.filter { email ->
            val dataRecebimentoEmail = getLocalDateFromTimestamp(email.dataRecebimento)
            dataRecebimentoEmail == dataRecebimentoDesejada
        }
    }

    suspend fun buscarEmailLixeiraPeloId(id: Int): Email? {
        return db.buscarEmailLixeiraPeloId(id)
    }

    suspend fun buscarEmailSpamPeloId(id: Int): Email? {
        return db.buscarEmailSpamPeloId(id)
    }

    suspend fun buscarEmailRascunhoPeloId(id: Int): Email? {
        return db.buscarEmailRascunhoPeloId(id)
    }

    suspend fun buscarEmailArquivoPeloId(id: Int): Email? {
        return db.buscarEmailArquivoPeloId(id)
    }

    suspend fun buscarEmailEnvidadoPeloId(id: Int): Email? {
        return db.buscarEmailEnviadoPeloId(id)
    }

    fun listarEmailsCaixaEntradaOrdenadosPorIdDecrescente(): List<Email> {
        return db.listarEmailsCaixaEntrada().sortedByDescending { it.id }
    }

    fun listarEmailsNaLixeiraOrdenadosPorIdDecrescente(): List<Email> {
        return db.listarEmailsLixeira().sortedByDescending { it.id }
    }

    fun listarEmailsNoSpamOrdenadosPorIdDecrescente(): List<Email> {
        return db.listarEmailsSpam().sortedByDescending { it.id }
    }

    fun listarEmailsNoRascunhoOrdenadosPorIdDecrescente(): List<Email> {
        return db.listarEmailsRascunho().sortedByDescending { it.id }
    }

    fun listarEmailsNoArquivoOrdenadosPorIdDecrescente(): List<Email> {
        return db.listarEmailsArquivo().sortedByDescending { it.id }
    }

    fun listarEmailsNoEnviadoOrdenadosPorIdDecrescente(): List<Email> {
        return db.listarEmailsEnviado().sortedByDescending { it.id }
    }

    fun listarEmailsCaixaEntrada(): List<Email> {
        return db.listarEmailsCaixaEntrada()
    }

    private fun listarEmailsSpam(): List<Email> {
        return db.listarEmailsSpam()
    }

    fun excluirEmailsPorIds(selectedIds: List<Int>): Int {
        return db.excluirEmailsPorIds(selectedIds)
    }

    //LIXEIRA
    fun atualizarEmailsNaLixeira(selectedIds: List<Int>, isInLixeira: Boolean): Int {
        return db.updateEmailsNaLixeira(selectedIds, isInLixeira)
    }
    fun atualizarEmailsNaLixeiraPush(email: Email, isInLixeira: Boolean): Int {
        return db.updateEmailsNaLixeiraPush(listOf(email.id), isInLixeira)
    }
    suspend fun atualizarEmailsNaLixeiraMsgView(emailId: Int, isInLixeira: Boolean): Int {
        return withContext(Dispatchers.IO) {
            db.updateEmailsNaLixeiraMsgView(emailId, isInLixeira)
        }
    }

    //SPAM
    fun atualizarEmailsNoSpam(selectedIds: List<Int>, isInSpam: Boolean): Int {
        return db.updateEmailsNoSpam(selectedIds, isInSpam)
    }
    fun atualizarEmailsNoSpamPush(email: Email, isInSpam: Boolean): Int {
        return db.updateEmailsNoSpamPush(listOf(email.id), isInSpam)
    }
    suspend fun atualizarEmailsNoSpamMsgView(emailId: Int, isInSpam: Boolean): Int {
        return withContext(Dispatchers.IO) {
            db.updateEmailsNoSpamMsgView(emailId, isInSpam)
        }
    }

    //ARQUIVO
    fun atualizarEmailsNoArquivo(selectedIds: List<Int>, isInArquivo: Boolean): Int {
        return db.updateEmailsNoArquivo(selectedIds, isInArquivo)
    }
    fun atualizarEmailsNoArquivoPush(email: Email, isInArquivo: Boolean): Int {
        return db.updateEmailsNoArquivoPush(listOf(email.id), isInArquivo)
    }
    suspend fun atualizarEmailsNoArquivoMsgView(emailId: Int, isInArquivo: Boolean): Int {
        return withContext(Dispatchers.IO) {
            db.updateEmailsNoArquivoMsgView(emailId, isInArquivo)
        }
    }

    //lIDO
    fun atualizarEmailsLidoPush(email: Email, wasRead: Boolean): Int {
        return db.updateEmailsLidoPush(listOf(email.id), wasRead)
    }

    //IMPORTANTE
    fun atualizarEmailsImportantePush(email: Email, isImportant: Boolean): Int {
        return db.updateEmailsImportantePush(listOf(email.id), isImportant)
    }

    // CAIXA DE ENTRADA
    suspend fun findNextAvailableEmailId(currentId: Int): Int? {
        // Implementar a lógica para encontrar o próximo ID de email disponível
        var nextId = currentId + 1
        while (true) {
            val email = db.buscarEmailPeloId(nextId)
            if (email != null) {
                return nextId
            }
            nextId++
        }
        return null
    }

    private fun listarTodosOsEmails(): List<Email> {
        return emptyList()
    }

    fun isLastEmail(emailId: Int): Boolean {
        val allEmails = listarTodosOsEmails() // Obter todos os emails
        return allEmails.lastOrNull()?.id?.toInt() == emailId // Verificar se é o último ID na lista
    }

    //LIXEIRA
    suspend fun findNextAvailableEmailLixeiraId(currentId: Int): Int? {
        // Implementar a lógica para encontrar o próximo ID de email disponível
        var nextId = currentId + 1
        while (true) {
            val email = db.buscarEmailLixeiraPeloId(nextId)
            if (email != null) {
                return nextId
            }
            nextId++
        }
        return null
    }

    private fun listarTodosOsEmailsLixeira(): List<Email> {
        return emptyList()
    }

    fun isLastEmailixeira(emailId: Int): Boolean {
        val allEmails = listarTodosOsEmailsLixeira() // Obter todos os emails
        return allEmails.lastOrNull()?.id?.toInt() == emailId // Verificar se é o último ID na lista
    }

    //SPAM
    suspend fun findNextAvailableEmailSpamId(currentId: Int): Int? {
        // Implementar a lógica para encontrar o próximo ID de email disponível
        var nextId = currentId + 1
        while (true) {
            val email = db.buscarEmailSpamPeloId(nextId)
            if (email != null) {
                return nextId
            }
            nextId++
        }
        return null
    }

    fun isLastEmailSpam(emailId: Int): Boolean {
        val allEmails = listarEmailsSpam() // Obter todos os emails
        return allEmails.lastOrNull()?.id?.toInt() == emailId // Verificar se é o último ID na lista
    }

    suspend fun buscarEmails(query: String): List<Email> {
        return withContext(Dispatchers.IO) {
            db.buscarEmails("%$query%")
        }
    }

    suspend fun listarEmailsImportantes(): List<Email> {
        return db.buscarEmailsImportantes()
    }

}