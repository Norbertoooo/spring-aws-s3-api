package com.vitu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BucketService {

    private static final Logger log = LoggerFactory.getLogger(BucketService.class);

    private final AmazonS3 amazonS3;

    public BucketService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void criar(String nome) {
        if (amazonS3.doesBucketExistV2(nome)) {
            log.info("Criando bucket: {}", nome);
            return;
        }
        log.info("Criando bucket: {}", nome);
        amazonS3.createBucket(nome);
    }

    public List<Bucket> listar() {
        return amazonS3.listBuckets();
    }

    public List<S3ObjectSummary> listarObjetos(String bucketNome) {
        return amazonS3.listObjectsV2(bucketNome).getObjectSummaries();
    }

    public void adicionarObjeto(String bucketNome, MultipartFile arquivo) {
        if (amazonS3.doesBucketExistV2(bucketNome)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            try {
                amazonS3.putObject(bucketNome, arquivo.getOriginalFilename(), arquivo.getInputStream(), objectMetadata);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void excluirObjeto(String bucketNome, String objetoNome) {
        if (amazonS3.doesBucketExistV2(bucketNome)) {
            amazonS3.deleteObject(bucketNome, objetoNome);
        }
    }

    public void excluirBucket(String bucketNome) {
        if (amazonS3.doesBucketExistV2(bucketNome)) {
            amazonS3.deleteBucket(bucketNome);
        }
    }

    public CopyObjectResult copiarObjeto(String bucketNomeOrigem, String objetoNome, String bucketNomeDestino) {
        return amazonS3.copyObject(bucketNomeOrigem, objetoNome, bucketNomeDestino, objetoNome);
    }

    public byte[] downloadObjeto(String bucketNome, String objetoNome) throws IOException {
        S3ObjectInputStream objectContent = amazonS3.getObject(bucketNome, objetoNome).getObjectContent();
        return IOUtils.toByteArray(objectContent);
    }

    public CopyObjectResult moverObjeto(String bucketNomeOrigem, String objetoNome, String bucketNomeDestino) {
        if (amazonS3.doesBucketExistV2(bucketNomeOrigem) && amazonS3.doesBucketExistV2(bucketNomeDestino)) {
            CopyObjectResult copyObjectResult = amazonS3.copyObject(bucketNomeOrigem, objetoNome, bucketNomeDestino, objetoNome);
            amazonS3.deleteObject(bucketNomeOrigem, objetoNome);
            return copyObjectResult;
        }
        return null;
    }

}
