/**
 * Employee Data Inventory Application - File storage and task service class.
 * related operations.
 * @author Aditya Ranjan Dash
 *
 */
package org.vmware.mapbu.edi.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vmware.mapbu.edi.constants.EDIConstants;
import org.vmware.mapbu.edi.utility.FileUtility;
import org.vmware.mapbu.edi.model.EmployeeData;
import org.vmware.mapbu.edi.repository.EmployeeDataRepository;

@Service
public class FileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
    private TaskService taskService;
	
	@Autowired
	private EmployeeDataRepository employeeDataRepository;
	
	@Value("${edi.file.storage.dir}")
	private String storageDirectory;
	
	public String processFile(MultipartFile uploadedFile) throws Exception {
		LOGGER.info("File Service():=>processFile(): START");
		
		String taskId = FileUtility.generateUniqueTaskId();
		taskService.updateTask(taskId, EDIConstants.INITIALIZED);
		LOGGER.info("New task initialized, TASK ID:" + taskId + " - " + EDIConstants.INITIALIZED);
		
		String filePath = saveFile(uploadedFile);
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			try {
				parseData(filePath,taskId);
			} catch (Exception e) {
				LOGGER.error("Failed to Process the file.", e);
			}
		});
		
		LOGGER.info("File Service():=>processFile(): END");
		return taskId;
	}
	
	private void parseData(String filePath, String taskId) throws Exception {

		LOGGER.info("File Service():=>parseData(): START");
		List<EmployeeData> employeeList = new ArrayList<EmployeeData>();
		
		taskService.updateTask(taskId, EDIConstants.PARSING_DATA);	//Updating the task status
		LOGGER.info("[" + taskId +"] - Task status Updated:" + EDIConstants.PARSING_DATA);
		
		
		LOGGER.info("Searching for the file in the path {}", filePath);
		try(LineIterator fileContents= FileUtils.lineIterator(new File(filePath))){
			while(fileContents.hasNext()){
				String line = fileContents.nextLine();
				if(!line.isEmpty())
					employeeList.add(parseData(line));
	        }
		} catch (Exception e) {
			LOGGER.error("Failed to access the file in directory.", e);
			throw e;
		}
		
		taskService.updateTask(taskId, EDIConstants.SAVING_DATA);
		LOGGER.info("[" + taskId +"] - Task status Updated:" + EDIConstants.SAVING_DATA);
		LOGGER.info("Parsing Completed, Total number of records found: " + employeeList.size());
		
		employeeDataRepository.saveAll(employeeList);
		
		taskService.updateTask(taskId, "Complete");
		LOGGER.info("[" + taskId +"] - Task status Updated:" + EDIConstants.COMPLETED);
		
		LOGGER.info("File Service():=>parseData(): END");
	
	}

	private String saveFile(MultipartFile uploadedFile) throws Exception {
		LOGGER.info("Saving the File in the Directory {}", storageDirectory);
		
		if(!new File(storageDirectory).exists())
        {
            new File(storageDirectory).mkdir();
        }
		File file = new File(storageDirectory + uploadedFile.getOriginalFilename());
		try {
			uploadedFile.transferTo(file);
			LOGGER.info("Suceessfully saved the file in the location", storageDirectory);
		} catch (Exception e) {
			LOGGER.error("Failed to store the file in tempopray directory.", e);
			throw e;
		}
		
		return file.getPath();
	}

	public void parseData(MultipartFile file, String taskId) {
		LOGGER.info("File Service():=>parseData(): START");
		List<EmployeeData> employeeList = new ArrayList<EmployeeData>();
		
		taskService.updateTask(taskId, EDIConstants.PARSING_DATA);	//Updating the task status
		LOGGER.info("[" + taskId +"] - Task status Updated:" + EDIConstants.PARSING_DATA);
		
		try (BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), "UTF-8"))) {
			employeeList =  fileReader.lines().map(line -> parseData(line)).collect(Collectors.toList());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		taskService.updateTask(taskId, EDIConstants.SAVING_DATA);
		LOGGER.info("[" + taskId +"] - Task status Updated:" + EDIConstants.SAVING_DATA);
		LOGGER.info("Parsing Completed, Total number of records found: " + employeeList.size());
		
		employeeDataRepository.saveAll(employeeList);
		
		taskService.updateTask(taskId, "Complete");
		LOGGER.info("[" + taskId +"] - Task status Updated:" + EDIConstants.COMPLETED);
		
		LOGGER.info("File Service():=>parseData(): END");
	}
	
	public EmployeeData parseData(String data) {
		String[] empdata = data.trim().split(" ");
		return new EmployeeData(empdata[0], empdata[1]);
	}
}
