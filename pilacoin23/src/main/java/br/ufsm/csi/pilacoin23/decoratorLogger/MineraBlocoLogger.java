package br.ufsm.csi.pilacoin23.decoratorLogger;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceMineraBloco;
import br.ufsm.csi.pilacoin23.model.Bloco;
import br.ufsm.csi.pilacoin23.util.Logger;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
@Component
public class MineraBlocoLogger implements InterfaceMineraBloco {

    private final InterfaceMineraBloco mineraBloco;

    public MineraBlocoLogger(InterfaceMineraBloco mineraBloco) {
        this.mineraBloco = mineraBloco;
    }

    @Override
    public Bloco criaBloco(BigInteger dificuldade, Long numeroBloco) {
        Bloco bloco = mineraBloco.criaBloco(dificuldade, numeroBloco);

        // Adiciona funcionalidade de logging
        logMineracao(bloco);

        return bloco;
    }

    public void logMineracao(Bloco bloco) {
        // Adicione aqui a lógica de logging, conforme necessário
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtual = sdf.format(Calendar.getInstance().getTime());

        String logMessage = String.format("[minera-bloco] - %s - %s", dataHoraAtual,"blocoMinerado nonce =" +  bloco.getNonce(),"numeroBloco =" + bloco.getNumeroBloco());

        //System.out.println(logMessage);

        // Grava o log no arquivo log.txt
        Logger logger = new Logger();
        logger.gravarLogEmArquivo(logMessage);
    }
}