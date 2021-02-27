/**
 * Employee Data Inventory Application - Test class for employee controller
 * related operations.
 * @author Aditya Ranjan Dash
 *
 */
package org.vmware.mapbu.edi.test.contoller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan({"org.vmware.mapbu.edi.*"})
public class EmployeeOperationsControllerTest {
    
    MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setup() {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void contextLoads() {
    	
    }

    //@Test
	public void test() throws Exception {

		 MockMultipartFile file = new MockMultipartFile("file", "data.txt", MediaType.TEXT_PLAIN_VALUE,
					"Aditya 30".getBytes());

	        mockMvc.perform(
	                multipart("/api/employee?action=upload ")
	                        .file(file)
	                        //.file(metadata)
	                        .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(content().string("testcontract.pdf"));
             
	}
	
	@Test
	public void getEmployeeTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/202264")
		          .accept(MediaType.APPLICATION_JSON_VALUE))
		          .andExpect(status().isOk())
		          .andExpect(jsonPath("$.name").value("Aditya1"));
	}
	
	@Test
	public void createEmployeeTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/employees/")
		          .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)
		          .content("{\"name\":\"John\",\"age\":30}")  
				)
					
		          .andExpect(status().isOk())
		          .andExpect(jsonPath("$.name").value("John"));
	}
	
	@Test
	public void updateEmployeeTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/306260")
		          .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)
		          .content("{\"name\":\"John\",\"age\":50}"))
		          .andExpect(status().isOk())
		          .andExpect(jsonPath("$.name").value("John"))
		          .andExpect(jsonPath("$.age").value("50"));
	}
	
	@Test
	public void deleteEmployeeTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/306260")
		          .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
		          .andExpect(status().isOk());
	}
}