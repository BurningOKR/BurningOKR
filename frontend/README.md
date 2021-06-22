# Introduction 
This is the Frontend project for the Tool Burning OKR by **Brockhaus AG**.
For the corresponding Backend please refer to the other Project in this repository.

This tool is designed to help to organise and communicate corporate objectives, which follow the OKR planning framework.

It allows the user to set Objectives for a given company Okr Unit, which may or may not be formulated vague and then quantify 
these objectives by associating key results, which should be numerically measurable and can therefor be used to track the 
progress on the associated objectives.

In addition this, this tool allows the user to review the objectives set in previous time intervals and get an accurate prediction 
on to what amount the objectives set will be fulfilled by the end of the set time interval. 

This project was build using Angular6.

# Getting Started
### Installation process

- install node.js. (https://nodejs.org/en/)
- `npm install @angular/cli -g`
- `npm install` in project folder to load dependencies

With the AngularCLI installed you should be able to open the application in an IDE of your choice.

### Development

To start the development server use command `npm start` (note that a running backend on port 8080 is required or configure proxy.conf.json)

# Build and Test
For a release build use `npm run build`

To run the unit tests in project use `npm run test`

The frontend develop-branch gets deployed to test system when changes are pushed to the server.

http://javatrainingkit.westeurope.cloudapp.azure.com/app/

Here is the access timetable for the server 

![alt text](zeitplan_azure_vm.png)

# Deployment 
The master-branch gets deployed to production, therefore master should only receive thoroughly tested commits.
To inspect the productive Service visit:

https://okr.brockhaus-ag.de/


