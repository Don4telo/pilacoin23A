package br.ufsm.csi.pilacoin23.interfaces;

import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import lombok.SneakyThrows;

import java.math.BigInteger;

public interface InterfaceMineraPila {


    // Este método tenta criar instâncias de PilaCoin para mineração
    @SneakyThrows
    PilaCoinJson criaPila(BigInteger dificuldade);
}
