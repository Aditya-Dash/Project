/**
 * Employee Data Inventory Application - Controller class used for various Task
 * related operations.
 * @author Aditya Ranjan Dash
 *
 */

package org.vmware.mapbu.edi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vmware.mapbu.edi.constants.EDIConstants;
import org.vmware.mapbu.edi.service.TaskService;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    private TaskService taskService;
   
    @GetMapping("/api/tasks/{taskId}")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId, final HttpServletRequest request) {
    	LOGGER.info("Task status action invoked" + request.getRequestURI());
        HashMap<String, String> map = new HashMap<>();

        if (!taskService.containsTask(taskId)) {
            map.put("Error", "Task Id does not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        String taskProgress = taskService.getTaskStatus(taskId);
        //Otherwise task is still running
        
        if (taskProgress.equals(EDIConstants.COMPLETED)) {
            map.put("message", taskProgress);
            taskService.removeTask(taskId);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
        	map.put("message", taskProgress);
        }
        return new ResponseEntity<>(map, HttpStatus.PARTIAL_CONTENT);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/api/tasks")
    public ResponseEntity<?> getTasks(final HttpServletRequest request) {
    	LOGGER.info("Task getTasks invoked" + request.getRequestURI());
        
    	HashMap<String, List<String>> map = new HashMap<>();
        List<String> existingTask = taskService.getAllTaskIds();
        
        if(existingTask.isEmpty())
        	return new ResponseEntity<>("No records", HttpStatus.NO_CONTENT);
        
        map.put("tasks", existingTask);
        return new ResponseEntity<>(existingTask, HttpStatus.OK);

    }


}