/**
 * Employee Data Inventory Application - Spring Boot base Application configuration
 * @author Aditya Ranjan Dash
 *
 */
package org.vmware.mapbu.edi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.vmware.mapbu.edi.*"})
public class EDIApplication implements CommandLineRunner{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EDIApplication.class);
	
	public static void main(String[] args) {
		LOGGER.info("STARTING THE APPLCIATION");
		SpringApplication.run(EDIApplication.class, args);
		
		LOGGER.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("EXECUTING: Command line runner");
		
		for (int i = 0; i < args.length; i++) {
			LOGGER.info("args[{}]: {} ", i, args[i]);
		}
	}
}
