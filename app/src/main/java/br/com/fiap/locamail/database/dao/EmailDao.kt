package br.com.fiap.locamail.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.locamail.model.Email

@Dao
interface EmailDao {

    @Insert
    fun salvar(email: Email): Long

    @Update
    fun atualizar(email: Email): Int

    @Delete
    fun excluir(email: Email): Int

    @Query("SELECT * FROM emails WHERE id = :id")
    suspend fun getEmailById(id: Int): Email?

    @Query("SELECT * FROM emails WHERE id = :id AND na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    suspend fun buscarEmailPeloId(id: Int): Email?

    @Query("SELECT * FROM emails WHERE id = :id AND na_lixeira = 1 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    suspend fun buscarEmailLixeiraPeloId(id: Int): Email?

    @Query("SELECT * FROM emails WHERE id = :id AND na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 1 AND no_rascunho = 0")
    suspend fun buscarEmailSpamPeloId(id: Int): Email?

    @Query("SELECT * FROM emails WHERE id = :id AND na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 1")
    suspend fun buscarEmailRascunhoPeloId(id: Int): Email?

    @Query("SELECT * FROM emails WHERE id = :id AND na_lixeira = 0 AND no_arquivo = 1 AND no_spam = 0 AND no_rascunho = 0")
    suspend fun buscarEmailArquivoPeloId(id: Int): Email?

    @Query("SELECT * FROM emails WHERE id = :id AND na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    suspend fun buscarEmailEnviadoPeloId(id: Int): Email?

    //UPDATE LIXEIRA
    @Query("UPDATE emails SET na_lixeira = :isInLixeira WHERE id IN (:ids)")
    fun updateEmailsNaLixeira(ids: List<Int>, isInLixeira: Boolean): Int

    @Query("UPDATE emails SET na_lixeira = :isInLixeira WHERE id IN (:ids)")
    fun updateEmailsNaLixeiraPush(ids: List<Long>, isInLixeira: Boolean): Int

    @Query("UPDATE emails SET na_lixeira = :isInLixeira WHERE id = :id")
    fun updateEmailsNaLixeiraMsgView(id: Int, isInLixeira: Boolean): Int

    //UPDATE SPAM
    @Query("UPDATE emails SET no_spam = :isInSpam WHERE id IN (:ids)")
    fun updateEmailsNoSpam(ids: List<Int>, isInSpam: Boolean): Int

    @Query("UPDATE emails SET no_spam = :isInSpam WHERE id IN (:ids)")
    fun updateEmailsNoSpamPush(ids: List<Long>, isInSpam: Boolean): Int

    @Query("UPDATE emails SET no_spam = :isInSpam WHERE id = :id")
    fun  updateEmailsNoSpamMsgView(id: Int, isInSpam: Boolean): Int

    //UPDATE ARQUIVO
    @Query("UPDATE emails SET no_arquivo = :isInArquivo WHERE id IN (:ids)")
    fun updateEmailsNoArquivo(ids: List<Int>, isInArquivo: Boolean): Int

    @Query("UPDATE emails SET no_arquivo = :isInArquivo WHERE id IN (:ids)")
    fun updateEmailsNoArquivoPush(ids: List<Long>, isInArquivo: Boolean): Int

    @Query("UPDATE emails SET no_arquivo = :isInArquivo WHERE id = :id")
    fun  updateEmailsNoArquivoMsgView(id: Int, isInArquivo: Boolean): Int

    //UPDATE LIDO
    @Query("UPDATE emails SET estado_leitura = :wasRead WHERE id IN (:ids)")
    fun updateEmailsLidoPush(ids: List<Long>, wasRead: Boolean): Int

    //UPDATE IMPORTANTE
    @Query("UPDATE emails SET importante = :isImportant WHERE id IN (:ids)")
    fun updateEmailsImportantePush(ids: List<Long>, isImportant: Boolean): Int


     @Query("SELECT * FROM emails WHERE na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    fun listarEmailsCaixaEntrada(): List<Email>

    @Query("SELECT * FROM emails WHERE na_lixeira = 1 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    fun listarEmailsLixeira(): List<Email>

    @Query("SELECT * FROM emails WHERE no_arquivo = 1 AND na_lixeira = 0 AND no_spam = 0 AND no_rascunho = 0")
    fun listarEmailsArquivo(): List<Email>

    @Query("SELECT * FROM emails WHERE no_spam = 1 AND na_lixeira = 0 AND no_arquivo = 0 AND no_rascunho = 0")
    fun listarEmailsSpam(): List<Email>

    @Query("SELECT * FROM emails WHERE no_rascunho = 1 AND na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0")
    fun listarEmailsRascunho(): List<Email>

    @Query("SELECT * FROM emails WHERE na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    fun listarEmailsEnviado(): List<Email>

    @Query("DELETE FROM emails WHERE id IN (:ids)")
    fun excluirEmailsPorIds(ids: List<Int>): Int

    //SEARCH
    @Query("""
    SELECT * FROM emails 
    WHERE na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0
    AND (remetente LIKE '%' || :query || '%' 
    OR assunto LIKE '%' || :query || '%' 
    OR mensagem LIKE '%' || :query || '%')
""")
    suspend fun buscarEmails(query: String): List<Email>

    @Query("SELECT * FROM emails WHERE importante = 1 AND na_lixeira = 0 AND no_arquivo = 0 AND no_spam = 0 AND no_rascunho = 0")
    suspend fun buscarEmailsImportantes(): List<Email>

}