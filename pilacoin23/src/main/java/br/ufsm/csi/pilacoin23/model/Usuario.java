package br.ufsm.csi.pilacoin23.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
@Entity
public class Usuario {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private byte[] chavePublica;
    @Column(unique = true, nullable = false)
    private String nome;
}
