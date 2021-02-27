/**
 * Employee Data Inventory Application - Task map class to hold running task status.
 * related operations.
 * @author Aditya Ranjan Dash
 *
 */
package org.vmware.mapbu.edi.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TaskService {
	    private final ConcurrentHashMap<String, String> tasklistMap = new ConcurrentHashMap<String, String>();
	    
	    public boolean containsTask(String taskId) {
	    	return tasklistMap.containsKey(taskId);
	    }
	    
	    public void updateTask(String taskId, String status) {
	    	tasklistMap.put(taskId, status);
	    }
	    
	    public void removeTask(String taskId) {
	    	tasklistMap.remove(taskId);
	    }
	    	
	    public String getTaskStatus(String taskId) {
	    	return tasklistMap.get(taskId);
	    }
	    
	    public List<String> getAllTaskIds() {
	    	return tasklistMap.keySet().stream().collect(Collectors.toList());
	    }
}
