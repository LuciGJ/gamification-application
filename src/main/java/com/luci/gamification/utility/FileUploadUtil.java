package com.luci.gamification.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import jakarta.activation.MimetypesFileTypeMap;

public class FileUploadUtil {

	// utility class used to handle file upload
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);

		// create directory if it does not exist
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		Path filePath = uploadPath.resolve(fileName);
		String mimetype = new MimetypesFileTypeMap().getContentType(filePath.toFile());
		String type = mimetype.split("/")[0];
		
		// check if the file is an image, if not throw an exception
		
		if (type.equals("image")) {
			
			// copy the file
			
			try (InputStream inputStream = multipartFile.getInputStream()) {

				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Could not save image file: " + fileName, e);
			}
		} else {
			throw new IOException("The file is not a valid image: " + fileName);
		}
	}

	
	// method to get the file's extension by returning everything starting from "."
	public static String getFileExtension(String name) {
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return name.substring(lastIndexOf);
	}

}
