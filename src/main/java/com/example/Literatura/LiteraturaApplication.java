package com.example.Literatura;

import com.example.Literatura.Repository.AutorRepository;
import com.example.Literatura.Repository.LibroRepository;
import com.example.Literatura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository repository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}
	@Override
	public void run(String... args) {
		Principal principal = new Principal(repository, autorRepository);
		principal.muestraElMenu();
	}
}
