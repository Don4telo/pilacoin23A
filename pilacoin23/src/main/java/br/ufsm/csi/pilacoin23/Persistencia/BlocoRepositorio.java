package br.ufsm.csi.pilacoin23.Persistencia;

import br.ufsm.csi.pilacoin23.model.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlocoRepositorio extends JpaRepository< Bloco, Long> {
    List<Bloco> findByNomeUsuarioMinerador(String alexandre);
}
