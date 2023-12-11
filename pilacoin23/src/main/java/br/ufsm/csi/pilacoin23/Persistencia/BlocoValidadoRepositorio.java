package br.ufsm.csi.pilacoin23.Persistencia;

import br.ufsm.csi.pilacoin23.model.BlocoValidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlocoValidadoRepositorio extends JpaRepository<BlocoValidado, BlocoValidado> {
    @Query("SELECT bv, b FROM BlocoValidado bv JOIN bv.bloco b")
    List<Object[]> findAllWithBlock();
}
