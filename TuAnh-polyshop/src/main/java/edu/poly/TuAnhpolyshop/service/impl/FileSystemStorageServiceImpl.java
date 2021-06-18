package edu.poly.TuAnhpolyshop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.poly.TuAnhpolyshop.config.StorageProperties;
import edu.poly.TuAnhpolyshop.exception.StorageException;
import edu.poly.TuAnhpolyshop.exception.StorageFileNotFoundException;
import edu.poly.TuAnhpolyshop.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService{

	private final Path rooLocation;
	
	@Override
	public String getStoredFilename(MultipartFile file ,String id) {
		
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return"p"+id+"."+ext;
	}
	public FileSystemStorageServiceImpl(StorageProperties properties) {
		this.rooLocation = Paths.get(properties.getLocation());
		
	}
	@Override
	public void store(MultipartFile file, String storedFilename) {
		
		try {
			if(file.isEmpty()) {
				throw new StorageException("Failed to store");//sau khi kiểm tra rỗng thì ném ra ngoại lệ
			}
			Path destinationFile =this.rooLocation.relativize(Paths.get(storedFilename)).normalize().toAbsolutePath();
			
			if(!destinationFile.getParent().equals(this.rooLocation.toAbsolutePath())) {
				throw new StorageException("Cannot store file outside curent directory");
			}
			try(InputStream inputStream = file.getInputStream()){
				Files.copy(inputStream,destinationFile,StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			throw new StorageException("Failes to stỏe file",e);
		}
	}
	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			org.springframework.core.io.Resource resource =new UrlResource(file.toUri());
			if(resource.exists()|| resource.isReadable()) {
				return resource;
			}
			throw new StorageFileNotFoundException("Cold not read file:" + filename);
		} catch (Exception e) {
			throw new StorageFileNotFoundException("Could not read file:" + filename);
		}
	}
		@Override
		public Path load(String filename) {
			
			return rooLocation.resolve(filename);
		}
		@Override
		public void delete(String storedFilename) throws IOException {
			Path destinationFile =rooLocation.resolve(Paths.get(storedFilename)).normalize().toAbsolutePath();
			 
			Files.delete(destinationFile);
		}
		@Override
		public void init() {
			try {
				Files.createDirectories(rooLocation);
				System.out.println(rooLocation.toString());
			} catch (Exception e) {
				throw new StorageException("Could not initialize storage",e);
			}
		}
	}

