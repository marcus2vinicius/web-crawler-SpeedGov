package process;

import model.Pagamento;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SimplesNacionalExecute {
	private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36";
	private static final String url = "http://www8.receita.fazenda.gov.br";
    private Map datas = new HashMap<String, String>();
    private Map cookies = new HashMap<String, String>();
    private Elements inputHidden;
    private boolean logged = false;

    public SimplesNacionalExecute() {
        if(getHomePage())
            try {
                throw new Exception("Not Connected!");
            } catch (Exception e) {}
    }

    private boolean getHomePage(){
        try {
            //Initial Page
            Response r1 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/Default.aspx")
                    .userAgent(userAgent)
                    .method(Method.GET)
                    .timeout(30 * 1000)
                    .execute();

            cookies = r1.cookies();
            Document docs = r1.parse();
            if(docs.select("form").first().attr("action").equals("Default.aspx")) {
                inputHidden = docs.select("input[type=hidden]");
                datas.put(inputHidden.get(0).attr("name"), "lnkContinuar");//input hidden
                datas.put(inputHidden.get(1).attr("name"), inputHidden.get(1).attr("value"));//input hidden
                datas.put(inputHidden.get(2).attr("name"), inputHidden.get(2).attr("value"));//input hidden
                datas.put(inputHidden.get(3).attr("name"), inputHidden.get(3).attr("value"));//input hidden
                datas.put("idSom","");
                return true;
            }
        }catch (Exception e){return false;}
        return false;
    }

    public FileOutputStream getCaptcha(){
        try {
            Response r2 = Jsoup.connect(url + "/scripts/srf/intercepta/captcha.aspx?opt=image")
                    .userAgent(userAgent)
                    .method(Method.GET)
                    .timeout(30 * 1000)
                    .ignoreContentType(true)
                    .cookies(cookies)
                    .execute();

            cookies.putAll(r2.cookies());//acrescenta os cookies da imagem

            FileOutputStream imageStrem = (new FileOutputStream(new java.io.File("captcha.jpg")));
            imageStrem.write(r2.bodyAsBytes());
            imageStrem.close();
            return imageStrem;
        }catch (Exception e){return null;}
    }

    public boolean login(String cnpj, String captcha){
        try{
            datas.put("cnpj",cnpj);
            datas.put("txLetraCap",captcha);
            //Requisicao Login
            Response r3 = Jsoup.connect(url+"/simplesnacional/aplicacoes/atspo/pgmei.app/default.aspx")
                    .method(Method.POST)
                    .cookies(cookies)
                    .data(datas)
                    .userAgent(userAgent)
                    .referrer(url + "/simplesnacional/aplicacoes/atspo/pgmei.app/default.aspx")
                    .followRedirects(true)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4")
                    .header("Connection	", "keep-alive")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Host", "www8.receita.fazenda.gov.br")
                    .execute();
            Document d = r3.parse();
            if(d.select("form").first().attr("action").equals("branco.aspx")) {
                inputHidden = d.select("input[type=hidden]");
                logged = true;
                return true;
            }
        }catch (Exception e){return false;}
        return false;
    }

    public FileOutputStream getExtrato(String ano){
        if(!isLogged())
            return null;
        try {
            if(ano.isEmpty())
                ano = String.valueOf(new Date().getYear()+1900);
            datas.clear();
            datas.put(inputHidden.get(0).attr("name"), "ctl00$MenuApl");//input hidden __EVENTTARGET
            datas.put(inputHidden.get(1).attr("name"), "Consulta\\Extrato");//input hidden EventArgument __EVENTARGUMENT
            datas.put(inputHidden.get(2).attr("name"), inputHidden.get(2).attr("value"));//input hidden __VIEWSTATE
            datas.put(inputHidden.get(3).attr("name"), inputHidden.get(3).attr("value"));// __EVENTVALIDATION
            datas.put(inputHidden.get(4).attr("name"), inputHidden.get(4).attr("value"));// versao
            datas.put(inputHidden.get(5).attr("name"), inputHidden.get(5).attr("value"));// exercicio
            datas.put(inputHidden.get(6).attr("name"), inputHidden.get(6).attr("value"));// CaminhoImagem

            Response r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/branco.aspx")
                    .method(Method.POST)
                    .cookies(cookies)
                    .data(datas)
                    .userAgent(userAgent)
                    .referrer(url + "/simplesnacional/aplicacoes/atspo/pgmei.app/default.aspx")
                    .followRedirects(true)
                    .execute();
            boolean isPageConsulta = r3.parse().select("form").first().attr("action").equals("InformaAC.aspx");
            if (isPageConsulta) {
                inputHidden = r3.parse().select("input[type=hidden]");
                datas.clear();
                datas.put(inputHidden.get(0).attr("name"), "ctl00$conteudo$LinkButton1");//input hidden __EVENTTARGET
                datas.put(inputHidden.get(1).attr("name"),""/* inputHidden.get(1).attr("value")*/);//input hidden __EVENTARGUMENT
                datas.put(inputHidden.get(2).attr("name"),"" /*inputHidden.get(2).attr("value")*/);//input hidden __LASTFOCUS
                datas.put(inputHidden.get(3).attr("name"), inputHidden.get(3).attr("value"));//input hidden __VIEWSTATE
                datas.put(inputHidden.get(4).attr("name"), inputHidden.get(4).attr("value"));//input hidden __EVENTVALIDATION
                datas.put(inputHidden.get(5).attr("name"),""/* inputHidden.get(5).attr("value")*/);//input hidden versao
                datas.put(inputHidden.get(6).attr("name"), inputHidden.get(6).attr("value"));//input hidden exercicio
                datas.put(inputHidden.get(7).attr("name"),""/*inputHidden.get(7).attr("value")*/);//input hidden CaminhoImagem
                datas.put(inputHidden.get(8).attr("name"),""/* inputHidden.get(8).attr("value")*/);//input hidden dtConsolid
                datas.put("ctl00$conteudo$DropExerc", ano);//ctl00$conteudo$DropExerc

                r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/branco.aspx")
                        .method(Method.POST)
                        .cookies(cookies)
                        .data(datas)
                        .userAgent(userAgent)
                        .referrer(url + "/simplesnacional/aplicacoes/atspo/pgmei.app/default.aspx")
                        .ignoreContentType(true)
                        .execute();
                FileOutputStream pdfStream = new FileOutputStream(new java.io.File("extrato.pdf"));
                pdfStream.write(r3.bodyAsBytes());
                pdfStream.close();
                return pdfStream;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public List<Pagamento> getPagamentos(String ano){
        Pagamento p = null;
        List<Pagamento> ps = new ArrayList<Pagamento>();
        if(!isLogged())
            return null;
        try {
            if (ano.isEmpty())
                ano = String.valueOf(new Date().getYear() + 1900);
            datas.clear();
            datas.put(inputHidden.get(0).attr("name"), "ctl00$MenuApl");//input hidden __EVENTTARGET
            datas.put(inputHidden.get(1).attr("name"), "Emitir Guia de Pagamento (DAS)\\Pagamento Mensal");//input hidden EventArgument __EVENTARGUMENT
            datas.put(inputHidden.get(2).attr("name"), inputHidden.get(2).attr("value"));//input hidden __VIEWSTATE
            datas.put(inputHidden.get(3).attr("name"), inputHidden.get(3).attr("value"));// __EVENTVALIDATION
            datas.put(inputHidden.get(4).attr("name"), inputHidden.get(4).attr("value"));// versao
            datas.put(inputHidden.get(5).attr("name"), inputHidden.get(5).attr("value"));// exercicio
            datas.put(inputHidden.get(6).attr("name"), inputHidden.get(6).attr("value"));// CaminhoImagem
            datas.put("dtConsolid","");
            datas.put("__LASTFOCUS","");
            datas.put("ctl00$conteudo$DropExerc","2009");

            Response r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/branco.aspx")
                    .method(Method.POST)
                    .cookies(cookies)
                    .data(datas)
                    .userAgent(userAgent)
                    .referrer(url + "/simplesnacional/aplicacoes/atspo/pgmei.app/default.aspx")
                    .followRedirects(true)
                    .execute();
            boolean isPageConsulta = r3.parse().select("form").first().attr("action").equals("InformaAC.aspx");
            if (isPageConsulta) {
                inputHidden = r3.parse().select("input[type=hidden]");
                datas.clear();
                datas.put(inputHidden.get(0).attr("name"), "ctl00$conteudo$LinkButton1");//input hidden __EVENTTARGET
                datas.put(inputHidden.get(1).attr("name"), inputHidden.get(1).attr("value"));//input hidden __EVENTARGUMENT
                datas.put(inputHidden.get(2).attr("name"), inputHidden.get(2).attr("value"));//input hidden __LASTFOCUS
                datas.put(inputHidden.get(3).attr("name"), inputHidden.get(3).attr("value"));//input hidden __VIEWSTATE
                datas.put(inputHidden.get(4).attr("name"), inputHidden.get(4).attr("value"));//input hidden __EVENTVALIDATION
                datas.put(inputHidden.get(5).attr("name"), inputHidden.get(5).attr("value"));//input hidden versao
                datas.put(inputHidden.get(6).attr("name"), inputHidden.get(6).attr("value"));//input hidden exercicio
                datas.put(inputHidden.get(7).attr("name"), inputHidden.get(7).attr("value"));//input hidden CaminhoImagem
                datas.put(inputHidden.get(8).attr("name"), inputHidden.get(8).attr("value"));//input hidden dtConsolid
                datas.put("ctl00$conteudo$DropExerc", ano);//ctl00$conteudo$DropExerc

                r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/InformaAC.aspx")
                        .method(Method.POST)
                        .cookies(cookies)
                        .data(datas)
                        .userAgent(userAgent)
                        .followRedirects(true)
                        .referrer(url + "/simplesnacional/aplicacoes/atspo/pgmei.app/default.aspx")
                        .ignoreContentType(true)
                        .execute();

                if(r3.parse().select("form").first().attr("action").equals("SelecionarDas.aspx")){

                    inputHidden = r3.parse().select("input[type=hidden]");
                    datas.clear();

                    datas.put(inputHidden.get(0).attr("name"), "ctl00$conteudo$LinkButton1");
                    for (int i=1;i<inputHidden.size();i++)
                        datas.put(inputHidden.get(i).attr("name"), inputHidden.get(i).attr("value"));

                    for (int i=0;i<12;i++)
                        datas.put("ctl00$conteudo$chk"+i,"on");

                    datas.put("chkAll","on");
                    datas.put("ctl00$conteudo$RdBenef","RadioNao");

                   //Inicio
                   r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/SelecionarDas.aspx")
                            .method(Method.POST)
                            .cookies(cookies)
                            .data(datas)
                           .timeout(1000*1000)
                            .userAgent(userAgent)
                            .referrer(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/InformaAC.aspx")

                           .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                           .header("Accept-Encoding", "gzip, deflate")
                           .header("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4")
                           .header("Cache-Control", "max-age=0")
                           .header("Connection", "keep-alive")
                           .header("Content-Length","1843")
                           .header("Content-Type","application/x-www-form-urlencoded")

                           .execute();

                    Document doc = r3.parse();
/*
                    //Apurar
                    r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/apurar.aspx")
                            .method(Method.GET)
                            .cookies(cookies)
                            .userAgent(userAgent)
                            .timeout(1000*1000)
                            .followRedirects(true)
                            .execute();
                    //Document d = r3.parse();
*/

                    Elements elems = r3.parse().select("table.tabelaExtrato").select("tr");
                    for (Element tr: elems ){

                        Elements td = tr.select("td.celula");
                        if(td.size()==3){
                            p = new Pagamento();
                            p.setPA(td.get(0).text());
                            p.setNumeroDocumento(td.get(1).text());
                            try {
                                p.setVencimento(new SimpleDateFormat("dd/MM/yyyy").parse(td.get(2).text()));
                            }catch (Exception e){}
                            ps.add(p);
                        }
                    }
                }
            }

        }catch (Exception e){e.printStackTrace();}
        return ps;
    }
    /* Dependencias: Login, getPagamentos*/
    public FileOutputStream getPagamentoPDF(){
        try {
            //imprimir datas
            Response r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/imprimirDAS.aspx")
                    .method(Method.GET)
                    .cookies(cookies)
                    .userAgent(userAgent)
                    .timeout(1000 * 1000)
                    .execute();

            inputHidden = r3.parse().select("input[type=hidden]");
            datas.clear();
            for (Element el : inputHidden)
                datas.put(el.attr("name"), el.attr("value"));

            datas.remove("__EVENTTARGET");
            datas.put("__EVENTTARGET", "ctl00$conteudo$lnkImprimirVisualizar");
            r3 = Jsoup.connect(url + "/SimplesNacional/Aplicacoes/ATSPO/pgmei.app/imprimirDAS.aspx")
                    .method(Method.POST)
                    .cookies(cookies)
                    .data(datas)
                    .ignoreContentType(true)
                    .userAgent(userAgent)
                    .timeout(1000 * 1000)
                    .execute();

            FileOutputStream pdfStream = (new FileOutputStream(new java.io.File("pagamentos.pdf")));
            pdfStream.write(r3.bodyAsBytes());
            pdfStream.close();
            return pdfStream;
        }catch (Exception e){}
        return null;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
