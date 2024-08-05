package br.com.fiap.locamail.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emails")
data class Email(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val remetente: String,
    val destinatario: String,
    val copiaPara: String?,
    val assunto: String,
    val mensagem: String,
    @ColumnInfo(name = "data_envio")
    val dataEnvio: Long,
    @ColumnInfo(name = "hora_envio")
    val horaEnvio: Long,
    @ColumnInfo(name = "data_recebimento")
    val dataRecebimento: Long,
    @ColumnInfo(name = "hora_recebimento")
    val horaRecebimento: Long,
    val prioridade: String,
    @ColumnInfo(name = "estado_resposta")
    val estadoResposta: Boolean,
    @ColumnInfo(name = "data_resposta")
    val dataResposta: Long,
    @ColumnInfo(name = "hora_resposta")
    val horaResposta: Long,
    @ColumnInfo(name = "estado_leitura")
    val lido: Boolean,
    @ColumnInfo(name = "na_lixeira")
    val lixeira: Boolean,
    @ColumnInfo(name = "no_arquivo")
    val arquivo: Boolean,
    @ColumnInfo(name = "no_spam")
    val spam: Boolean,
    @ColumnInfo(name = "no_rascunho")
    val rascunho: Boolean,
    val importante: Boolean,
)