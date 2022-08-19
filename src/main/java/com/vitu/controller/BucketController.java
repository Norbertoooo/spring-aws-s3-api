package com.vitu.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vitu.service.BucketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/bucket")
public class BucketController {

    private static final Logger log = LoggerFactory.getLogger(BucketController.class);

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping("/{nome}")
    public ResponseEntity<?> criar(@PathVariable String nome) {
        log.info("Requisição para criar novo bucket com nome: {}", nome);
        bucketService.criar(nome);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Bucket>> listar() {
        log.info("Requisição para listar buckets existentes");
        return ResponseEntity.ok(bucketService.listar());
    }

    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> adicionarObjeto(@RequestParam String bucketNome, @RequestPart MultipartFile arquivo) {
        log.info("Requisição para adiciona novo objeto ao bucket: {}", bucketNome);
        bucketService.adicionarObjeto(bucketNome, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{bucketNome}/objetos")
    public ResponseEntity<List<S3ObjectSummary>> listarObjetos(@PathVariable String bucketNome) {
        log.info("Requisição para listar objetos do bucket: {}", bucketNome);
        return ResponseEntity.ok(bucketService.listarObjetos(bucketNome));
    }

    @DeleteMapping("/{bucketNome}/objetos/{objetoNome}")
    public ResponseEntity<?> excluirObjeto(@PathVariable String bucketNome, @PathVariable String objetoNome) {
        log.info("Requisição para excluir objeto: {} do bucket: {}", bucketNome, objetoNome);
        bucketService.excluirObjeto(bucketNome, objetoNome);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bucketNome}")
    public ResponseEntity<?> excluirBucket(@PathVariable String bucketNome) {
        log.info("Requisição para excluir bucket: {}", bucketNome);
        bucketService.excluirBucket(bucketNome);
        return ResponseEntity.noContent().build();
    }

}
