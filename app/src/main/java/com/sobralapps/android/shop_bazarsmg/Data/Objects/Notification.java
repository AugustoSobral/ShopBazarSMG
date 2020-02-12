package com.sobralapps.android.shop_bazarsmg.Data.Objects;

import java.util.Date;

public class Notification {

    private String notification_titulo;
    private String notification_pergunta_resposta;
    private Date timestamp;

    public Notification(){

    }

    public String getNotification_pergunta_resposta() {
        return notification_pergunta_resposta;
    }

    public void setNotification_pergunta_resposta(String notification_pergunta_resposta) {
        this.notification_pergunta_resposta = notification_pergunta_resposta;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotification_titulo() {
        return notification_titulo;
    }

    public void setNotification_titulo(String notification_titulo) {
        this.notification_titulo = notification_titulo;
    }
}
