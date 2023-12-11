package br.ufsm.csi.pilacoin23.interfaces;

import br.ufsm.csi.pilacoin23.model.Bloco;
import lombok.SneakyThrows;

import java.math.BigInteger;

public interface InterfaceMineraBloco {
    @SneakyThrows
    Bloco criaBloco(BigInteger dificuldade, Long numeroBloco);
}
