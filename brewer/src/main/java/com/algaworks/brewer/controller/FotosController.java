package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.FotoDTO;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.FotoStorageRunnable;

@RestController
@RequestMapping("/fotos")
public class FotosController {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	/*
	 * O MOTIVO PELO QUAL O ARQUIVO NÃO ESTAVA SENDO ENVIADO ERA O NOME DO MAPEAMENTO
	 * ESSE ENDPOINT SE DIFERE DO OUTRO PORQUE ELE É UM @RestController, LOGO ELE É UMA REQUISÇÃO REST
	 * EM REQUISIÇÕES HTTP VOCÊ ENVIA PARÂMETROS, SEJA VIA URL OU NO CORPO
	 * NESSE CASO O PARÂMETRO 'files[]' ESTAVA ESCRITO ERRADO.
	 * HAVIA UM ESPAÇO ENTRE O NOME E OS COLCHETES: 'files []'
	 * PORTANTO O ENDPOINT ENTENDIA QUE NADA ESTAVA SENDO ENVIADO!
	 * 
	 * 
	 * SE VOCÊ LER A DOCUMENTAÇÃO DO UIKIT, NA SEÇÃO "Component Options", O DEFAULT PARA O NOME DO PARÂMETRO QUE VAI ENVIAR
	 * O ARQUIVO VIA JAVASCRIPT É files[], ELE PODE SER MODIFICADO, ENTRETANTO, NÃO FOI DECLARADO E O DEFAULT É ESSE!
	 * REFEÊNCIA: https://getuikit.com/docs/upload
	 * 
	 * 
	 */
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam("files[]" /*AQUI QUE ESTAVA O CAÔ*/) MultipartFile[] files) {
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();
		
		Thread thread = new Thread(new FotoStorageRunnable(files,resultado, fotoStorage));
		thread.start();
		
		return resultado;
	}

	@GetMapping("/temp/{nome:.*}")
	public byte[] recuperarFotoTemporaria(@PathVariable String nome) {
		 return fotoStorage.recuperarFotoTemporaria(nome);
	}
	
	@GetMapping("/{nome:.*}")
	public byte[] recuperar(@PathVariable String nome) {
		 return fotoStorage.recuperar(nome);
	}
	
	
}
