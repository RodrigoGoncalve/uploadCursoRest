package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.api.model.Comentario;
import com.algaworks.osworks.api.model.ComentarioImput;
import com.algaworks.osworks.api.model.ComentarioModel;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;
import com.algaworks.osworks.exception.EntidadeNaoEncontradaEception;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentario")
public class ComentarioController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public List<ComentarioModel>listar(@PathVariable Long ordemServicoId){
		OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaEception("Ordem de serviço não encontrada"));
		
		return toCollectionModel(ordemServico.getComentarios());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioModel adicionar(@PathVariable Long ordemServicoId,
			@Valid @RequestBody ComentarioImput comentarioImput) {
		Comentario comentario = gestaoOrdemServicoService.adicionaComentario(ordemServicoId,
				comentarioImput.getDescricao());
		return toModel(comentario);
	}
	
	private ComentarioModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioModel.class);
	}
	private List<ComentarioModel>toCollectionModel(List<Comentario>comentarios){
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}
}
