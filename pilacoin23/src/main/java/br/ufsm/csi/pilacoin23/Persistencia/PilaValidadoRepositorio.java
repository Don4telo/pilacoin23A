package br.ufsm.csi.pilacoin23.Persistencia;

import br.ufsm.csi.pilacoin23.model.PilaValidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PilaValidadoRepositorio extends JpaRepository<PilaValidado, Long> {


    @Query("SELECT pv, pcj FROM PilaValidado pv JOIN pv.pilaCoinJson pcj")
    List<Object[]> findAllPilaValidadoWithPilaCoinJson();
}
