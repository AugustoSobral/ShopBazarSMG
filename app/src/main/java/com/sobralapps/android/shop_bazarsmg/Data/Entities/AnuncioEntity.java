package com.sobralapps.android.shop_bazarsmg.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.sobralapps.android.shop_bazarsmg.Constants;

import java.io.Serializable;

@Entity(tableName = Constants.ANUNCIO_TABLE_NAME)
public class AnuncioEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "anuncio_type_column")
    private int anuncioType;

    @ColumnInfo(name = "anuncio_title_column")
    private String anuncioTitle;

    @ColumnInfo(name = "anuncio_description_column")
    private String anuncioDescription;

    @ColumnInfo(name = "anuncio_category_column")
    private String anuncioCategory;

    @ColumnInfo(name = "anuncio_price_column")
    private String anuncioPrice;


    @ColumnInfo(name = "anuncio_ano_veiculo_column")
    private String ano_veiculo;

    @ColumnInfo(name = "anuncio_novo_usado_column")
    private int novo_usado;

    @ColumnInfo(name = "anuncio_aluguel_venda_column")
    private int aluguel_venda;

    @ColumnInfo(name = "anuncio_servico_preco_definido_column")
    private int servico_preco_definido_combinar;

    @ColumnInfo(name = "anuncio_contact_cell_column")
    private String anuncioContactCell;

    @ColumnInfo(name = "anuncio_contact_tel_column")
    private String anuncioContactTel;

    @ColumnInfo(name = "anuncio_contact_wpp_column")
    private String anuncioContactWpp;

    @ColumnInfo(name = "anuncio_image0")
    private String image0;

    @ColumnInfo(name = "anuncio_image1")
    private String image1;

    @ColumnInfo(name = "anuncio_image2")
    private String image2;

    @ColumnInfo(name = "anuncio_image3")
    private String image3;

    @ColumnInfo(name = "anuncio_image4")
    private String image4;

    @ColumnInfo(name = "anuncio_image5")
    private String image5;

    @ColumnInfo(name = "anuncio_image6")
    private String image6;

    @ColumnInfo(name = "anuncio_image7")
    private String image7;



    @Ignore
    public AnuncioEntity(){

    }

    public AnuncioEntity(int anuncioType, String anuncioTitle, String anuncioDescription, String anuncioCategory, String anuncioPrice, String ano_veiculo, int novo_usado, int aluguel_venda, int servico_preco_definido_combinar, String anuncioContactCell, String anuncioContactTel, String anuncioContactWpp, String image0, String image1, String image2, String image3, String image4, String image5, String image6, String image7) {
        this.anuncioType = anuncioType;
        this.anuncioTitle = anuncioTitle;
        this.anuncioDescription = anuncioDescription;
        this.anuncioCategory = anuncioCategory;
        this.anuncioPrice = anuncioPrice;
        this.ano_veiculo = ano_veiculo;
        this.novo_usado = novo_usado;
        this.aluguel_venda = aluguel_venda;
        this.servico_preco_definido_combinar = servico_preco_definido_combinar;
        this.anuncioContactCell = anuncioContactCell;
        this.anuncioContactTel = anuncioContactTel;
        this.anuncioContactWpp = anuncioContactWpp;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;
        this.image7 = image7;
    }

    public String getAnuncioContactCell() {
        return anuncioContactCell;
    }

    public void setAnuncioContactCell(String anuncioContactCell) {
        this.anuncioContactCell = anuncioContactCell;
    }

    public String getAnuncioContactTel() {
        return anuncioContactTel;
    }

    public void setAnuncioContactTel(String anuncioContactTel) {
        this.anuncioContactTel = anuncioContactTel;
    }

    public String getAnuncioContactWpp() {
        return anuncioContactWpp;
    }

    public void setAnuncioContactWpp(String anuncioContactWpp) {
        this.anuncioContactWpp = anuncioContactWpp;
    }

    public String getAnuncioPrice() {
        return anuncioPrice;
    }

    public void setAnuncioPrice(String anuncioPrice) {
        this.anuncioPrice = anuncioPrice;
    }

    public String getAno_veiculo() {
        return ano_veiculo;
    }

    public void setAno_veiculo(String ano_veiculo) {
        this.ano_veiculo = ano_veiculo;
    }

    public int getAluguel_venda() {
        return aluguel_venda;
    }

    public void setAluguel_venda(int aluguel_venda) {
        this.aluguel_venda = aluguel_venda;
    }

    public int getServico_preco_definido_combinar() {
        return servico_preco_definido_combinar;
    }

    public void setServico_preco_definido_combinar(int servico_preco_definido_combinar) {
        this.servico_preco_definido_combinar = servico_preco_definido_combinar;
    }

    public int getNovo_usado() {
        return novo_usado;
    }

    public void setNovo_usado(int novo_usado) {
        this.novo_usado = novo_usado;
    }

    public String getAnuncioCategory() {
        return anuncioCategory;
    }

    public void setAnuncioCategory(String anuncioCategory) {
        this.anuncioCategory = anuncioCategory;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnuncioType() {
        return anuncioType;
    }

    public void setAnuncioType(int anuncioType) {
        this.anuncioType = anuncioType;
    }

    public String getAnuncioTitle() {
        return anuncioTitle;
    }

    public void setAnuncioTitle(String anuncioTitle) {
        this.anuncioTitle = anuncioTitle;
    }

    public String getAnuncioDescription() {
        return anuncioDescription;
    }

    public void setAnuncioDescription(String anuncioDescription) {
        this.anuncioDescription = anuncioDescription;
    }
}
