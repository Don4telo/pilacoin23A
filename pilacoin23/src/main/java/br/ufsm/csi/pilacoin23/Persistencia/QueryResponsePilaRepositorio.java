package br.ufsm.csi.pilacoin23.Persistencia;

import br.ufsm.csi.pilacoin23.model.QueryResponsePila;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryResponsePilaRepositorio extends JpaRepository<QueryResponsePila, Long> {
    @Override
    List<QueryResponsePila> findAll();
}
