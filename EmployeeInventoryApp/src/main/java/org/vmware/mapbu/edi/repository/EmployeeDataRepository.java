/**
 * Employee Data Inventory Application - JPA repository to help CRUD operations on Employee records in the database.
 * related operations.
 * @author Aditya Ranjan Dash
 *
 */
package org.vmware.mapbu.edi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vmware.mapbu.edi.model.EmployeeData;

@Repository
public interface EmployeeDataRepository extends JpaRepository<EmployeeData, Long> {

	public EmployeeData findByName(String name);

}