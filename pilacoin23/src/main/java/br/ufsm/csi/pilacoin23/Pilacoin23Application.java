package br.ufsm.csi.pilacoin23;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@EntityScan(basePackages = "br.ufsm.csi.pilacoin23.model") // Substitua pelo pacote onde suas entidades estão localizadas
public class Pilacoin23Application {

	public static void main(String[] args) {

		SpringApplication.run(Pilacoin23Application.class, args);
		System.out.println("");
		System.out.println(" XXXXXX XX  XX   XXXXXXX  ");
		System.out.println("  X   X  X   X    X   X   ");
		System.out.println("  XXXXX  X   X    XXXXX   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
		System.out.println("  X      X   X    X   X     Pilacoin23Application comecou  ");
		System.out.println(" XXX    XXX XXXXX X   X    ");
		System.out.println("");

	}
}
