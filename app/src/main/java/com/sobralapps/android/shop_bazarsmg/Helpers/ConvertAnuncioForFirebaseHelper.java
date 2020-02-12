package com.sobralapps.android.shop_bazarsmg.Helpers;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AnuncioForFirebase;
import com.sobralapps.android.shop_bazarsmg.Data.Entities.AnuncioEntity;

public class ConvertAnuncioForFirebaseHelper {

    public static AnuncioForFirebase convertAnuncioForFirebase(AnuncioEntity anuncioEntity){

        AnuncioForFirebase anuncioForFirebase = new AnuncioForFirebase(anuncioEntity.getAnuncioType(),
                anuncioEntity.getAnuncioTitle(),
                anuncioEntity.getAnuncioDescription(), anuncioEntity.getAnuncioCategory(), anuncioEntity.getAnuncioPrice(),
                anuncioEntity.getAno_veiculo(), anuncioEntity.getNovo_usado(), anuncioEntity.getAluguel_venda(),
                anuncioEntity.getServico_preco_definido_combinar(), anuncioEntity.getAnuncioContactCell(),
                anuncioEntity.getAnuncioContactTel(), anuncioEntity.getAnuncioContactWpp(), anuncioEntity.getImage0(),
                anuncioEntity.getImage1(), anuncioEntity.getImage2(), anuncioEntity.getImage3(),
                anuncioEntity.getImage4(), anuncioEntity.getImage5(), anuncioEntity.getImage6(), anuncioEntity.getImage7());

        return anuncioForFirebase;

    }

}
