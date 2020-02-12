package com.sobralapps.android.shop_bazarsmg.Data.Objects;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class AvisoPerguntaRespostaForFirebase implements Serializable {

    private String aviso_titulo;
    private String aviso_pergunta_resposta;
    private String idDocumentPergunta;
    private String anuncioId;
    private String id_user_que_perguntou;
    private String avisoId;
    private boolean avisoRead;

    private Date aviso_timestamp;

    public AvisoPerguntaRespostaForFirebase(){

    }

    public boolean isAvisoRead() {
        return avisoRead;
    }

    public void setAvisoRead(boolean avisoRead) {
        this.avisoRead = avisoRead;
    }

    @Exclude
    public String getAvisoId() {
        return avisoId;
    }

    public void setAvisoId(String avisoId) {
        this.avisoId = avisoId;
    }

    public String getId_user_que_perguntou() {
        return id_user_que_perguntou;
    }

    public void setId_user_que_perguntou(String id_user_que_perguntou) {
        this.id_user_que_perguntou = id_user_que_perguntou;
    }

    public String getAviso_titulo() {
        return aviso_titulo;
    }

    public void setAviso_titulo(String aviso_titulo) {
        this.aviso_titulo = aviso_titulo;
    }

    public String getAviso_pergunta_resposta() {
        return aviso_pergunta_resposta;
    }

    public void setAviso_pergunta_resposta(String aviso_pergunta_resposta) {
        this.aviso_pergunta_resposta = aviso_pergunta_resposta;
    }

    public String getIdDocumentPergunta() {
        return idDocumentPergunta;
    }

    public void setIdDocumentPergunta(String idDocumentPergunta) {
        this.idDocumentPergunta = idDocumentPergunta;
    }

    public String getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(String anuncioId) {
        this.anuncioId = anuncioId;
    }

    public Date getAviso_timestamp() {
        return aviso_timestamp;
    }

    public void setAviso_timestamp(Date aviso_timestamp) {
        this.aviso_timestamp = aviso_timestamp;
    }
}
