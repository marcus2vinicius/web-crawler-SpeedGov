package model.nfs;

/**
 * Created by Marcus Vincicius on 13/07/2015.
 */
public enum EnumDocumento {
    CNPJ("J"), CPF("F"), Estrangeiro("E");

    public String value;

    EnumDocumento(String value){
        this.value = value;
    }
}
