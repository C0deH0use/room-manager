# Room Occupancy Manager


### How to run the service?
the app can be simply run with the following command: 
```
make run
```

This command will run the api via docker compose. The service is going to be build the service and then run the app, all configured via the Dockerfile at root.  

The api will be exposed on port 8080.

### How to stop the app?
To stop the service you can simply run: 
```
    make destroy
```

### How to test the service against requests?
For ease of testing I've created four HTTP tests that can be started using IntelliJ's Http Test Tool.   
Simple go to: `http-test/rooms.http` [file](http-test/rooms.http).
These are the four example test cases that are present with the task description.

For manual tests, the api exposes a POST endpoint at `http://localhost:8080/api/v1/rooms`.  
The request should contain two non-negative properties: `economy`: and `premium`.