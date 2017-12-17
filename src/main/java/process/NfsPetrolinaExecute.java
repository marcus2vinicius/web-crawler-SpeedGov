package process;
import model.nfs.Atividade;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

/**
 * Created by Marcus Vincicius on 30/06/2015.
 */
public class NfsPetrolinaExecute {
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
    private static final String url1 = "http://servicos.speedgov.com.br";
    private static final String url2 = "http://speedgov.com.br";
    private Map datas = new HashMap<String, String>();
    private Map cookies = new HashMap<String, String>();
    private Elements inputHidden;
    private boolean logged = false;
    private String cnpj = "00000000000";
    private String senha = "000000";


    public NfsPetrolinaExecute(){
        if(getHomePage())
            if(login())
                logged = true;
        if(logged){
            getHomeNfs();
        }
    }

    private void getHomeNfs() {
    }

    private boolean login() {
        datas.put("cnpj", cnpj);
        String timeStamp =  String.valueOf(new Date().getTime());
        //new Timestamp(System.currentTimeMillis());
        try {
            //http://speedgov.com.br/satpet/servlet//apportal101?20164021000150,lfm0607,callback=jsonCallback&_=1435707293186
            Connection.Response r3 = Jsoup.connect(url2 + "/satpet/servlet//apportal101?"+cnpj+","+senha+",callback=jsonCallback&_="+timeStamp)
                    .method(Connection.Method.GET)
                    .userAgent(userAgent)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .header("Content-Type","application/json")
                    .header("Referer", "http://servicos.speedgov.com.br/acesso")
                    .execute();
            cookies.putAll(r3.cookies());
/*            r3 = Jsoup.connect(url2 + "/satpet/servlet//apportal101?"+cnpj+","+senha+",callback=jsonCallback&_="+timeStamp)
                    .method(Connection.Method.GET)
                    .userAgent(userAgent)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .header("Content-Type","application/json")
                    .header("Referer", "http://servicos.speedgov.com.br/acesso")
                    .execute();
            System.out.println("");*/
            String ret = r3.body();
            ret = ret.substring(ret.indexOf('{'),ret.length());
            ret = ret.substring(0, ret.indexOf('}') + 1);
            JSONObject jsonObject = new JSONObject(ret);
            jsonObject.toString();
            ret = jsonObject.getString("url");
              r3 = Jsoup.connect(ret)
                    .method(Connection.Method.GET)
                    .userAgent(userAgent)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .header("Content-Type","application/json")
                    .header("Referer", "http://servicos.speedgov.com.br/acesso")
                    .execute();
            Document doc = r3.parse();

            cookies.putAll(r3.cookies());
            datas.clear();
            Elements inputHidden = doc.select("input[type=hidden]");
            for(Element element: inputHidden)
                datas.put(element.attr("name"), element.attr("value"));

            /*Todos inputs*/
            Elements inputs = doc.getElementsByTag("input");
            for(Element element: inputs)
                datas.put(element.attr("name"), element.attr("value"));

            //Usando URL Fixa OK
          r3 = Jsoup.connect("http://speedgov.com.br/satpet/servlet//hiss325?DO1HKuvQv3L2vFmDG+Q8ga+oA0dB2vWgj00TZsd+LqA=")
                    .method(Connection.Method.GET)
                    .userAgent(userAgent)
                    .cookies(cookies)
                    .followRedirects(true)
                    .header("Referer", "http://servicos.speedgov.com.br/acesso")
                    .execute();
            Document docc = r3.parse();
            inputs = docc.getElementsByTag("input");
            Elements textA = docc.getElementsByTag("textarea");
            elementToListAtividade(docc);
            /*Get Url Area NF
            03 cookies
            37 datas

            r3 = Jsoup.connect("http://speedgov.com.br/satpet/servlet//hiss101?cd5c8390d90b581dd0ddc5250346d333,S18LsVwsVoafSRrermqL4s6yrmyfawNKM4GT8SqVObk=,gx-no-cache="+timeStamp)
                    .method(Connection.Method.GET)
                    .header("Content-Type", "application/json;charset=utf-8")
                    .userAgent(userAgent)
                    .cookies(cookies)
                    .data(datas)
                    .followRedirects(true)
                    .ignoreContentType(true)
                    .header("Referer", "http://servicos.speedgov.com.br/acesso")
                    .execute();

            System.out.print(r3.body());
        */
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Atividade> elementToListAtividade(Document doc) {
        Elements els = doc.select("select#vNFECODTRIMUN").select("option");
        List<Atividade> listA = new ArrayList<Atividade>();
        for (Element e: els){
            Atividade a = new Atividade();
            a.Valor = e.attr("value");
            a.Texto = e.text();
            listA.add(a);
        }
        return listA;
    }

    private boolean getHomePage(){
        try {
            //Initial Page
            Connection.Response r1 = Jsoup.connect(url1 + "/inicio?contexto=32")
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .data("Connection","keep-alive")
                    .timeout(30 * 1000)
                    .followRedirects(true)
                    .execute();
            cookies = r1.cookies();
            Document d = r1.parse();
            return true;
        }catch (Exception e){e.printStackTrace();return false;}
    }
}
