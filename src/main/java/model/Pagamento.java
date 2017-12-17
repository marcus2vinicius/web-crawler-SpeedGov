package model;

import java.util.Date;

/**
 * Created by Marcus Vincicius on 25/06/2015.
 */
public class Pagamento {
    private String PA;
    private String NumeroDocumento;
    private Date vencimento;

    public String getPA() {
        return PA;
    }

    public void setPA(String PA) {
        this.PA = PA;
    }

    public String getNumeroDocumento() {
        return NumeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        NumeroDocumento = numeroDocumento;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }
}
