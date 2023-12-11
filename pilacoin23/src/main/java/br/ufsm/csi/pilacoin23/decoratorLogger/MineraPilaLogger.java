package br.ufsm.csi.pilacoin23.decoratorLogger;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceMineraPila;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import br.ufsm.csi.pilacoin23.util.Logger;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
@Component
public class MineraPilaLogger implements InterfaceMineraPila {
    private final InterfaceMineraPila mineraPila;

    public MineraPilaLogger(InterfaceMineraPila mineraPila) {
        this.mineraPila = mineraPila;
    }

    @Override
    public PilaCoinJson criaPila(BigInteger dificuldade) {
        PilaCoinJson pilaCoin = mineraPila.criaPila(dificuldade);

        // Adiciona funcionalidade de logging
        logMineracao(pilaCoin);

        return pilaCoin;
    }

    private void logMineracao(PilaCoinJson pilaCoin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtual = sdf.format(Calendar.getInstance().getTime());

        String logMessage = String.format("[minera-pila] - %s - %s", dataHoraAtual,"pilaMinerado nonce =" +  pilaCoin.getNonce());

        //System.out.println(logMessage);

        // Grava o log no arquivo log.txt
        Logger logger = new Logger();
        logger.gravarLogEmArquivo(logMessage);
    }


}

