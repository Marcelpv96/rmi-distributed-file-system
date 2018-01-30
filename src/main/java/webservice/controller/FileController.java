package webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webservice.model.File;
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

    @RequestMapping(value = "/file/name/{fileName}", method = RequestMethod.GET)
    public @ResponseBody
    File fetchDataByFileName(@PathVariable("fileName") String fileName){
        return fileService.fetchByFileName(fileName);
    }

    @RequestMapping(value = "/file/category/{category}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByCategory(@PathVariable("category") String category){
        return fileService.fetchByCategory(category);
    }

    @RequestMapping(value = "/file/encrypted/{encrypted}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByEncryped(@PathVariable("encrypted") String encrypted){
        return fileService.fetchByEncrypted(encrypted);
    }

    @RequestMapping(value = "/file/user/{userName}", method = RequestMethod.GET)
    public @ResponseBody
    List<File> fetchDataByUserNAme(@PathVariable("userName") String userName){
        return fileService.fetchByUserName(userName);
    }

    @RequestMapping(value = "/file/all/", method = RequestMethod.GET)
    public @ResponseBody
    List<File> findAll(){
        return fileService.findAll();
    }

}
