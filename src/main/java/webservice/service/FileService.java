package webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservice.model.File;
import webservice.repository.FileRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository repository;

    public void saveFile(File file) {
        repository.save(file);
    }

    public File fetchByFileName(String fileName) {
        return repository.findByFileName(fileName);
    }

    public List<File> fetchByCategory(String category) {
        List<File> result = new ArrayList<>();

        for(File s: repository.findByCategory(category)){
            result.add(s);
        }
        return result;
    }
    public List<File> findAll(){
        return (List<File>) repository.findAll();
    }


    public List<File> fetchByEncrypted(String encrypted) {
        List<File> result = new ArrayList<>();

        for(File s: repository.findByEncrypted(encrypted)){
            result.add(s);
        }
        return result;

    }

    public List<File> fetchByUserName(String userName) {
        List<File> result = new ArrayList<>();

        for(File s: repository.findByUserName(userName)){
            result.add(s);
        }
        return result;
    }
}

