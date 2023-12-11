package br.ufsm.csi.pilacoin23.Persistencia;

import br.ufsm.csi.pilacoin23.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
