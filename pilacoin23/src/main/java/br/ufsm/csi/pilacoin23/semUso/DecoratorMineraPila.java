package br.ufsm.csi.pilacoin23.semUso;

import br.ufsm.csi.pilacoin23.interfaces.InterfaceMineraPila;
import br.ufsm.csi.pilacoin23.model.PilaCoinJson;

import java.math.BigInteger;

public class DecoratorMineraPila implements InterfaceMineraPila {
    @Override
    public PilaCoinJson criaPila(BigInteger dificuldade) {

        System.out.println("escreve em arquivo....");
        return null;
    }
}
