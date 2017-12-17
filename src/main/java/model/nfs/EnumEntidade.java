package model.nfs;

/**
 * Created by Marcus Vincicius on 13/07/2015.
 */
public enum EnumEntidade {
    Privado("PRV"), Publico("PUB"), Mista("MST");

    public String value;

    EnumEntidade(String value){
        this.value = value;
    }
}
