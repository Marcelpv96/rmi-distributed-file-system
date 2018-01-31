package webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webservice.model.File;
import webservice.repository.FileRepository;
import webservice.service.FileService;

import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/file", consumes = "application/json",method = RequestMethod.POST)
    public void create(@RequestBody File file){
        fileService.saveFile(file);
    }

    @RequestMapping(value = "/file/id/{id}", method = RequestMethod.GET)
    public @ResponseBody
    File fetchDataByID(@PathVariable("id") String id){
        return fileService.fetchById(id);
    }

    @RequestMapping(value = "/file/name/{fileName}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByFileName(@PathVariable("fileName") String fileName){
        return fileService.fetchByFileName(fileName);
    }

    @RequestMapping(value = "/file/extension/{extension}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByExtension(@PathVariable("extension") String extension){
        return fileService.fetchByExtension(extension);
    }

    @RequestMapping(value = "/file/user/{userName}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByUserNAme(@PathVariable("userName") String userName){
        return fileService.fetchByUserName(userName);
    }

    @RequestMapping(value = "/file/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id){
        fileService.deleteFile(id);
    }

    //TODO
    @RequestMapping(value = "/file/encrypted/{encrypted}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByEncryped(@PathVariable("encrypted") String encrypted){
        return fileService.fetchByEncrypted(encrypted);
    }

    @RequestMapping(value = "/file/all/", method = RequestMethod.GET)
    public @ResponseBody
    List<File> findAll(){
        return fileService.findAll();
    }

}
