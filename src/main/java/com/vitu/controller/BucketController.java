package com.vitu.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vitu.controller.request.CopyObjectRequest;
import com.vitu.service.BucketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(value = "/objetos", consumes = {"multipart/form-data"})
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

    @PostMapping("/objetos/copy")
    public ResponseEntity<CopyObjectResult> copiarObjeto(@RequestBody CopyObjectRequest body) {
        CopyObjectResult copyObjectResult = bucketService.copiarObjeto(body.bucketNomeOrigem(), body.objetoNome(),
                body.bucketNomeDestino());
        return ResponseEntity.status(HttpStatus.CREATED).body(copyObjectResult);
    }

    @GetMapping("/objetos")
    public ResponseEntity<ByteArrayResource> downloadObjeto(@RequestParam String bucketNome, @RequestParam String objetoNome) throws IOException {
        byte[] downloadObjeto = bucketService.downloadObjeto(bucketNome, objetoNome);
        ByteArrayResource byteArrayResource = new ByteArrayResource(downloadObjeto);
        return ResponseEntity.ok()
                .contentLength(downloadObjeto.length)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objetoNome + "\"")
                .body(byteArrayResource);
    }

}
