package test;

import model.Pagamento;
import process.NfsPetrolinaExecute;
import process.SimplesNacionalExecute;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Marcus Vincicius on 23/06/2015.
 */
public class TestMain {
    public static void main(String[] args) {
        SimplesNacionalExecute sne = new SimplesNacionalExecute();
        sne.getCaptcha();
        System.out.print("Digite Captcha: ");
        while (!sne.login("20.164.021/0001-50",new Scanner(System.in).next())){
            System.out.print("Digite Captcha: ");
        }
/*
        if(sne.isLogged())
            sne.getExtrato("");
        else System.out.println("Not Logged!");
*/


        List<Pagamento> pagamentos = sne.getPagamentos("2015");
        sne.getPagamentoPDF();
        //NfsPetrolinaExecute nfs = new NfsPetrolinaExecute();

    }
}
