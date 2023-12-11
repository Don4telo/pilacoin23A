package br.ufsm.csi.pilacoin23.semUso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ConversorData {
    public  Date converteData (String data) {


        try {
            // Convertendo a string para um valor longo
            long timestamp = Long.parseLong(data);

            // Criando um objeto Instant usando o método estático ofEpochMilli
            Instant instant = Instant.ofEpochMilli(timestamp);

            // Agora 'instant' contém a representação de data e hora correspondente ao timestamp
            //System.out.println("Data convertida: " + instant);
            Date dataCriacao =  Date.from(instant);
            return dataCriacao;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Tratar exceção se a conversão falhar
            return null;
        }

    }
}
