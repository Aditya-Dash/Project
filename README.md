# Project
 Employee data
Employee Data Inventory Application is a gradle project which can be import directly as a eclipse project and can be run directly 
as Eclipse project. 

The exposed apis can tested using Postman or any availble unit testcase.

Changes required:
================
1.Applciation.properties (needs database configurations)


APIs available
==============
1./api/employee?action=upload [post] - file upload required
2./api/employees [GET]	- retrieves all available employee record
3./api/employees/{id} [GET] - retrieves a single employee if available
4./api/employees [POST]	- Create a new employee object 
5./api/employees/{id} [PUT] - Update a single employee details
6./api/employees/{id} [DELETE] - Removes a single employe record
7./api/employees [DELETE] - Removes all employees