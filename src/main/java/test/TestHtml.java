package test;

import model.Pagamento;
import model.nfs.Atividade;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Marcus Vincicius on 25/06/2015.
 */
public class TestHtml {
    public static void main(String[] args) {
        String html1 =  "coloque o html";
        String html2 =  "coloque o html";
        String html3 =  "coloque o html";
        String html4 = "coloque o html";
        String html5 = "coloque o html";
        Document doc = Jsoup.parse(html1+html2+html3+html4+html5);
        Elements els = doc.select("select#vNFECODTRIMUN").select("option");
        List<Atividade> listA = new ArrayList<Atividade>();
            for (Element e: els){
                    Atividade a = new Atividade();
                    a.Valor = e.attr("value");
                    a.Texto = e.text();
                    listA.add(a);
            }
    }
}
