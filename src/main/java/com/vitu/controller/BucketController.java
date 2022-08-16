package com.vitu.controller;

import com.vitu.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/bucket")
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping("/{nome}")
    public ResponseEntity<?> criar(@PathVariable String nome) {
        bucketService.criar(nome);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(bucketService.listar());
    }

    @PostMapping
    public ResponseEntity<?> adicionarObjeto(@RequestParam("arquivo") MultipartFile arquivo) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
