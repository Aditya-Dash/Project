/**
 * Employee Data Inventory Application - Employee operations service class.
 * @author Aditya Ranjan Dash
 *
 */

package org.vmware.mapbu.edi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vmware.mapbu.edi.controller.EmployeeOperationsController;
import org.vmware.mapbu.edi.model.EmployeeData;
import org.vmware.mapbu.edi.repository.EmployeeDataRepository;

@Service
public class EmployeeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeOperationsController.class);

	@Autowired
	EmployeeDataRepository employeeDataRepository;

	public List<EmployeeData> getAllEmployeess() {
		LOGGER.info("Employee Service():=>getAllEmployeess(): START");
		
		List<EmployeeData> dataList = new ArrayList<EmployeeData>();
		employeeDataRepository.findAll().forEach(data -> dataList.add(data));
		LOGGER.info("Found number of record:" + dataList.size());
		
		debugStmnt("Records: " + dataList);
		
		LOGGER.info("Employee Service():=>getAllEmployeess(): END");
		return dataList;
	}

	public EmployeeData saveOrUpdate(EmployeeData data) {
		LOGGER.info("Employee Service():=>saveOrUpdate(): START");
		
		EmployeeData updateData =  employeeDataRepository.save(data);
		
		debugStmnt("Updating/Creating a employee: " + data);
		
		LOGGER.info("Employee Service():=>saveOrUpdate(): END");
		return updateData;
	}

	public EmployeeData findEmployeeByName(String name) {
		LOGGER.info("Employee Service():=>findEmployeeByName(): START");
		LOGGER.info("Searching in the inventory for name: " + name);
		
		EmployeeData empRecord =  employeeDataRepository.findByName(name);
		if(empRecord == null)
			LOGGER.info("No employee found based on name: " + name);
		else
			debugStmnt("Record: " + empRecord);
		
		LOGGER.info("Employee Service():=>findEmployeeByName(): END");
		return empRecord;
	}
	
	public EmployeeData findEmployeeById(long id) {
		LOGGER.info("Employee Service():=>findEmployeeById(): START");
		LOGGER.info("Searching in the inventory for Id: " + id);
		EmployeeData empRecord = null;
		Optional<EmployeeData> empOpt =  employeeDataRepository.findById(id);
		
		if(!empOpt.isPresent())
			LOGGER.info("No employee found based on id: " + id);
		else {
			empRecord = empOpt.get();
			debugStmnt("Record: " + empRecord);
		}
			
		LOGGER.info("Employee Service():=>findEmployeeById(): END");
		return empRecord;
	}

	public void removeEmployee(long empId) {
		LOGGER.info("Employee Service():=>removeEmployee(): START");
		EmployeeData record = findEmployeeById(empId);
		if(record != null) {
			LOGGER.info("Found Employee" + record.getName() + ". Deleting employee..");
			employeeDataRepository.delete(record);
		}
		if(findEmployeeById(empId) == null)
			LOGGER.info("Employee Deleted Successfully");
		LOGGER.info("Employee Service():=>removeEmployee(): END");
	}

	public void removeAll() {
		LOGGER.info("Employee Service():=>removeAll(): START");
		employeeDataRepository.deleteAll();
		
		if(employeeDataRepository.findAll().size() == 0)
			LOGGER.info("All Employees Deleted Successfully");
		LOGGER.info("Employee Service():=>removeAll(): END");
	}
	
	public void debugStmnt(String debugStmnt) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(debugStmnt);
		}
	}
}
