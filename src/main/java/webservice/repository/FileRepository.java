package webservice.repository;

import org.springframework.data.repository.CrudRepository;
import webservice.model.File;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    File findByFileName(String fileName);
    List<File> findByEncrypted(String encrypted);
    List<File> findByCategory(String Category);
    List<File> findByUserName(String userName);
}
