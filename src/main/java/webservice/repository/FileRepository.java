package webservice.repository;

import org.springframework.data.repository.CrudRepository;
import webservice.model.File;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    File findById(String id);
    List<File> findByFileName(String fileName);
    List<File> findByEncrypted(String encrypted);
    List<File> findByExtension(String extension);
    List<File> findByUserName(String userName);

}
