package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;
import com.algaworks.osworks.domain.service.CadastroClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CadastroClienteService cadastroClienteService;
	
	@GetMapping
	public List<Cliente> lista(){
		return clienteRepository.findAll();
		//return clienteRepository.findByNome("João da Silva");
	}
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente>cliente =clienteRepository.findById(clienteId);
		
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adiocionar(@Valid @RequestBody Cliente cliente) {
		return cadastroClienteService.salvar(cliente);
	}
	
	//@PutMapping(value="/{clientId}")
	@RequestMapping(value = "/{clienteId}", method = RequestMethod.PUT)
	public ResponseEntity<Cliente>atualizar(@Valid @PathVariable Long clienteId,
			@RequestBody Cliente cliente){
		
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();		
	}
		
		cliente.setId(clienteId);
		cliente = cadastroClienteService.salvar(cliente);
		
	return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> remover(@PathVariable Long clienteId){
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		cadastroClienteService.excluir(clienteId);
		return ResponseEntity.noContent().build();
	}
}
