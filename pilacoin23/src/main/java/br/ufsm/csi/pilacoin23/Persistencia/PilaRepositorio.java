package br.ufsm.csi.pilacoin23.Persistencia;

import br.ufsm.csi.pilacoin23.model.PilaCoinJson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PilaRepositorio extends JpaRepository<PilaCoinJson,  Long> {
    List<PilaCoinJson> findBynomeCriador(String alexandre);
}
