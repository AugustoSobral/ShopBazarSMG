package com.sobralapps.android.shop_bazarsmg.Data.Objects;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class AnuncioForFirebase implements Serializable {

    private int anuncio_tipo;
    private String titulo;
    private String descricao;
    private String categoria;
    private String preco;
    private String ano_veiculo;
    private int produto_novo_usado;
    private int imovel_aluguel_venda;
    private int servico_preco_definido_combinar;
    private String contato_phone_call;
    private String contato_tel_call;
    private String contato_wpp;

    private String image0;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String image6;
    private String image7;

    private String userId;
    private String documentId;
    private boolean anuncio_ativo;

    private Date timestamp;

    public AnuncioForFirebase(){

    }

    public AnuncioForFirebase(int anuncio_tipo, String titulo, String descricao, String categoria,
                              String preco, String ano_veiculo, int produto_novo_usado, int imovel_aluguel_venda,
                              int servico_preco_definido_combinar, String contato_phone_call, String contato_tel_call,
                              String contato_wpp, String image0, String image1, String image2, String image3, String image4,
                              String image5, String image6, String image7) {
        this.anuncio_tipo = anuncio_tipo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.ano_veiculo = ano_veiculo;
        this.produto_novo_usado = produto_novo_usado;
        this.imovel_aluguel_venda = imovel_aluguel_venda;
        this.servico_preco_definido_combinar = servico_preco_definido_combinar;
        this.contato_phone_call = contato_phone_call;
        this.contato_tel_call = contato_tel_call;
        this.contato_wpp = contato_wpp;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;
        this.image7 = image7;
    }

    public boolean isAnuncio_ativo() {
        return anuncio_ativo;
    }

    public void setAnuncio_ativo(boolean anuncio_ativo) {
        this.anuncio_ativo = anuncio_ativo;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getContato_phone_call() {
        return contato_phone_call;
    }

    public void setContato_phone_call(String contato_phone_call) {
        this.contato_phone_call = contato_phone_call;
    }

    public String getContato_tel_call() {
        return contato_tel_call;
    }

    public void setContato_tel_call(String contato_tel_call) {
        this.contato_tel_call = contato_tel_call;
    }

    public String getContato_wpp() {
        return contato_wpp;
    }

    public void setContato_wpp(String contato_wpp) {
        this.contato_wpp = contato_wpp;
    }

    public int getAnuncio_tipo() {
        return anuncio_tipo;
    }

    public void setAnuncio_tipo(int anuncio_tipo) {
        this.anuncio_tipo = anuncio_tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getAno_veiculo() {
        return ano_veiculo;
    }

    public void setAno_veiculo(String ano_veiculo) {
        this.ano_veiculo = ano_veiculo;
    }

    public int getProduto_novo_usado() {
        return produto_novo_usado;
    }

    public void setProduto_novo_usado(int produto_novo_usado) {
        this.produto_novo_usado = produto_novo_usado;
    }

    public int getImovel_aluguel_venda() {
        return imovel_aluguel_venda;
    }

    public void setImovel_aluguel_venda(int imovel_aluguel_venda) {
        this.imovel_aluguel_venda = imovel_aluguel_venda;
    }

    public int getServico_preco_definido_combinar() {
        return servico_preco_definido_combinar;
    }

    public void setServico_preco_definido_combinar(int servico_preco_definido_combinar) {
        this.servico_preco_definido_combinar = servico_preco_definido_combinar;
    }

    public String getImage0() {
        return image0;
    }

    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getImage7() {
        return image7;
    }

    public void setImage7(String image7) {
        this.image7 = image7;
    }
}
