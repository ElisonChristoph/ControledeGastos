package com.apps.elison.controledegastos;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class BuscaNota {
    private String[] valor= new String[3];
    private String resultado, chave;
    Document doc;

    public BuscaNota(String chave){
        this.chave= chave;
    }

    /**
     * Método realiza busca da página web onde estão todos os dados da nota,
     * retornando um documento que pode ser tratado através de métodos.
     * @return Document doc
     */
    public Document getWebPage(){

        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //realiza busca no site do Sefaz armazenando os dados em um documento HTML
                    doc = Jsoup.connect("https://www.sefaz.rs.gov.br/ASP/AAE_ROOT/NFE/SAT-WEB-NFE-NFC_QRCODE_1.asp?p="+chave).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){

        }
        return doc;

    }

    /**
     * Método que realiza busca recebendo um documento contendo a página,buscando através da classe
     * NFCDetalhe_Item, onde esta localizado o valor da nota, retornando através de uma String
     * @param pagina
     * @return valor
     */
    String getValor(final Document pagina){
        String[] valores = new String[200];
        int count = 0;
        Elements dados = pagina.getElementsByClass("NFCDetalhe_Item");
        for(Element elemento : dados) {
            valores[count] = elemento.getElementsByClass("NFCDetalhe_Item").text();
            count++;
        }
        return valor[0] = valores[count-8];
    }

    /**
     * Método que realiza busca recebendo um documento contendo a página,buscando através da classe
     * NFCCabecalho_SubTitulo, onde esta localizado o estabelecimetno que emitiu a nota, retornando
     * uma String com o Emissor
     * @param pagina
     * @return emissor
     */
    String getEmissor(final Document pagina){
        String[] valores = new String[200];
        int count = 0;
        Elements dados = pagina.getElementsByClass("NFCCabecalho_SubTitulo");
        for(Element elemento : dados) {
            valores[count] = elemento.getElementsByClass("NFCCabecalho_SubTitulo").text();
            count++;
        }
        valor[1] = valores[0];
        return valor[1];

    }
    /**
     * Método que realiza busca recebendo um documento contendo a página,buscando através da classe
     * NFCCabecalho_SubTitulo, onde esta localizado a data que foi emitida, retornando
     * uma String com o DataEmissao
     * @param pagina
     * @return dataEmissao
     */
    String getDataEmissao(final Document pagina){
        String[] valores = new String[200];
        int count = 0;
        Elements dados = pagina.getElementsByClass("NFCCabecalho_SubTitulo");
        for(Element elemento : dados) {
            valores[count] = elemento.getElementsByClass("NFCCabecalho_SubTitulo").text();
            count++;
        }
        resultado = valores[2];
        return valor[2] = resultado.substring(resultado.length()-19,resultado.length()-9);
    }

    String enviaDados(){
        String palavra = new String();
        for(int count = 0; count<valor.length; count++) {
            palavra = palavra+"\n"+valor[count];
        }
        return palavra;
    }

    /*Localizacao dos dados no documento html
    Emissor - NFCCabecalho_SubTitulo - posição vetor 0
    Data - NFCCabecalho_SubTitulo - posição vetor 4
    Valor - NFCDetalhe_Item - posição vetor count - 8
    */

}
