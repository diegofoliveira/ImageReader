package com.reader.image.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.reader.image.entity.Extracao;
import com.reader.image.repository.ExtracaoRepository;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
@Slf4j
public class ImageService {
    
    @Value( "${storageBase}" )
    private String storageBase;

    @Value( "${language}" )
    private String language;

    @Value( "${tessdata}" )
    private String tessData;

    private ExtracaoRepository extracaoRepository;

    public ImageService(ExtracaoRepository extracaoRepository){
        this.extracaoRepository = extracaoRepository;
    }

    public String getStringFromImage(MultipartFile file){
        var retorno = "";
        File image = loadFile(file);
        if(image != null){
            Tesseract tess4j = new Tesseract();
            tess4j.setDatapath(tessData);
            tess4j.setLanguage(language);
            try {
                retorno = tess4j.doOCR(image);            
            } catch (TesseractException e) {
                log.info(e.getMessage());
            }
        }        
        saveExtracao(retorno);
        return retorno;
    }   

    private File loadFile(MultipartFile file){        
                
        File path = new File(storageBase);
        File transferFile = new File(storageBase + "/" + file.getOriginalFilename()); 
        if(path.exists() && path.isDirectory()){           
       
            try {
                file.transferTo(transferFile);
            } catch (IllegalStateException e) {            
                log.info(e.getMessage());
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            return transferFile;
        }
      
       return null;
    }

    private void saveExtracao(String texto){
        Extracao extracao = new Extracao();
        extracao.setTexto(texto);
        extracao.setData(new Date());
        extracaoRepository.save(extracao);
    }
}
