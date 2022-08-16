package com.vitu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BucketService {

    public static final Logger log = Logger.getLogger(BucketService.class.getName());

    private final AmazonS3 amazonS3;

    public BucketService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void criar(String nome) {
        if (amazonS3.doesBucketExistV2(nome)) {
            log.log(Level.INFO ,() -> "Criando bucket: " + nome);
            return;
        }
        log.info( () -> "Criando bucket: " + nome);
        amazonS3.createBucket(nome);
    }

    public List<Bucket> listar() {
        return amazonS3.listBuckets();
    }

    public List<?> listarObjetos(String bucketNome) {
        return amazonS3.listObjectsV2(bucketNome).getObjectSummaries();
    }

}
