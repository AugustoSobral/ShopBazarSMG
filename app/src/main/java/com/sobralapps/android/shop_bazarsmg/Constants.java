package com.sobralapps.android.shop_bazarsmg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String ANUNCIO_TABLE_NAME = "anuncio_table";
    public static final int PRODUTO_TYPE_CODE = 1;
    public static final int VEICULO_TYPE_CODE = 2;
    public static final int IMOVEL_TYPE_CODE = 3;
    public static final int SERVICO_TYPE_CODE = 4;

    public static final int SEM_ESPECIFICACAO_CODE = 0;
    public static final int NOVO_CODE = 1;
    public static final int USADO_CODE = 2;
    public static final int SERVICO_PRECO_DEFINIDO_CODE = 1;
    public static final int SERVICO_PRECO_COMBINAR_CODE = 2;
    public static final int IMOVEL_ALUGUEL_CODE = 1;
    public static final int IMOVEL_VENDA_CODE = 2;
    public static final int IMOVEL_TEMPORADA_CODE = 3;

    public static final List<String> produtosCategory = new ArrayList<String>(
            Arrays.asList("Acessórios para veículos",
                    "Agro, Indústria e Comércio",
                    "Alimentos e Bebidas",
                    "Animais",
                    "Antiguidades",
                    "Arte e Artesanato",
                    "Beleza e Cuidado Pessoal",
                    "Brinquedos e Hobbies",
                    "Calçados, Roupas e Bolsas",
                    "Casa, Móveis e Decoração",
                    "Celulares, Eletrônicos, Áudio e Vídeo",
                    "Eletrodomésticos",
                    "Esportes e Fitness",
                    "Ferramentas e Construção",
                    "Games",
                    "Informática",
                    "Ingressos",
                    "Instrumentos Musicais",
                    "Joias e Relógios",
                    "Livros",
                    "Saúde",
                    "Outros"
            ));

    public static final List<String> servicosCategory = new ArrayList<>(
            Arrays.asList("Academia e Esportes",
                    "Advocacia",
                    "Alimentação",
                    "Animas, Pet Shop, Veterinário",
                    "Beleza , Estética e Bem Estar",
                    "Educação, Aulas Particulares, Cursos",
                    "Festas e Eventos",
                    "Gráfica e Impressão",
                    "Manutenção, Construção",
                    "Marketing, Publicidade e Propaganda",
                    "Saúde",
                    "Suporte Técnico, Informática",
                    "Vestuário, Confecções",
                    "Veículos e Transporte",
                    "Viagens e Turismo",
                    "Outros Serviços"

            ));

    public static final List<String> imoveisCategory = new ArrayList<>(
            Arrays.asList(
                    "Apartamentos",
                    "Casas",
                    "Chácaras",
                    "Fazendas",
                    "Galpões",
                    "Lojas Comerciais",
                    "Sítios",
                    "Terrenos"
    ));

    public static final List<String> veiculosCategory = new ArrayList<>(
            Arrays.asList("Caminhões",
                    "Carretas",
                    "Carros Antigos",
                    "Carros e Caminhonetes",
                    "Motos",
                    "Náutica",
                    "Veículos Pesados, Maquinaria Agrícola",
                    "Ônibus"
    ));

    public static final List<String> ano_veiculo = new ArrayList<>(
            Arrays.asList("2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985"
            ));
}
