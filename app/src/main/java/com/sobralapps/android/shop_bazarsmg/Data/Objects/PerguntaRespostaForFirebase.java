package com.sobralapps.android.shop_bazarsmg.Data.Objects;

import java.util.Date;

public class PerguntaRespostaForFirebase {

    private String pergunta;
    private String resposta;
    private String userIdPergunta;
    private String anuncioId;
    private String userPerguntaName;

    private Date timestamp;

    public PerguntaRespostaForFirebase(){

    }

    public PerguntaRespostaForFirebase(String pergunta, String resposta, String userIdPergunta, String anuncioId, Date timestamp) {
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.userIdPergunta = userIdPergunta;
        this.anuncioId = anuncioId;
        this.timestamp = timestamp;
    }

    public String getUserPerguntaName() {
        return userPerguntaName;
    }

    public void setUserPerguntaName(String userPerguntaName) {
        this.userPerguntaName = userPerguntaName;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getUserIdPergunta() {
        return userIdPergunta;
    }

    public void setUserIdPergunta(String userIdPergunta) {
        this.userIdPergunta = userIdPergunta;
    }

    public String getAnuncioId() {
        return anuncioId;
    }

    public void setAnuncioId(String anuncioId) {
        this.anuncioId = anuncioId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
