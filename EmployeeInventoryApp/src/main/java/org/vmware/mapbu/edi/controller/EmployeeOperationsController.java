/**
 * Employee Data Inventory Application - Controller class used for various employee
 * related operations.
 * @author Aditya Ranjan Dash
 *
 */

package org.vmware.mapbu.edi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vmware.mapbu.edi.model.EmployeeData;
import org.vmware.mapbu.edi.service.EmployeeService;
import org.vmware.mapbu.edi.service.FileService;

@RestController
@RequestMapping("/api")
public class EmployeeOperationsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeOperationsController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
    private FileService fileService;

	@PostMapping(value = "/employee", consumes = {" multipart/mixed", "multipart/form-data"}, produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile, @RequestParam("action") String action,
			final HttpServletRequest request) {
		LOGGER.info("Employee upload action invoked" + request.getRequestURI());
		
		if(!action.equalsIgnoreCase("upload"))
			return new ResponseEntity<String>("Please upload file with action as 'upload'", HttpStatus.NOT_IMPLEMENTED);

		LOGGER.info("fileName : " + uploadfile.getOriginalFilename());
		LOGGER.info("contentSize : " + uploadfile.getSize());

		if (uploadfile.isEmpty()) {
			return new ResponseEntity<String>("Empty file selected, kindly select a new one.", HttpStatus.OK);
		}

		/** File will get processed to file system and database */
		String taskId;
		try {
			taskId = fileService.processFile(uploadfile);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("Task Id: " + taskId,
				new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/employees")
	private ResponseEntity<?> getEmployees(final HttpServletRequest request) {
		LOGGER.info("Employee getEmployees action invoked" + request.getRequestURI());
		try {
			List<EmployeeData> allEmployees = new ArrayList<EmployeeData>();
			
			allEmployees = employeeService.getAllEmployeess();
			if (allEmployees.isEmpty()) {
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(allEmployees, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<EmployeeData> getEmployeeById(@PathVariable("id") long id, final HttpServletRequest request) {
		LOGGER.info("Employee getEmployeeById invoked" + request.getRequestURI());
		EmployeeData empl = employeeService.findEmployeeById(id);

		if (empl != null) {
			return new ResponseEntity<>(empl, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/employees", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<EmployeeData> createEmployee(@RequestBody EmployeeData data, final HttpServletRequest request) {
		LOGGER.info("Employee createEmployee invoked" + request.getRequestURI());
		try {
			EmployeeData empCreated = employeeService.saveOrUpdate(data);
			return new ResponseEntity<>(empCreated, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/employees/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<EmployeeData> updateEmployee(@PathVariable("id")  long id, @RequestBody EmployeeData data, final HttpServletRequest request) {
		LOGGER.info("Employee updateEmployee invoked" + request.getRequestURI());
		EmployeeData empData = employeeService.findEmployeeById(id);
		if (empData != null) {
			empData.setName(data.getName());
			empData.setAge(data.getAge());
			EmployeeData empData2 = employeeService.saveOrUpdate(empData);
			return new ResponseEntity<>(empData2, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") long id, final HttpServletRequest request) {
		LOGGER.info("Employee deleteEmployee invoked" + request.getRequestURI());
	  try {
		  employeeService.removeEmployee(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  } catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	}
	
	@DeleteMapping("/employees")
	public ResponseEntity<HttpStatus> deleteAllEmployees(final HttpServletRequest request) {
		LOGGER.info("Employee deleteAllEmployees invoked" + request.getRequestURI());
		try {
			employeeService.removeAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
