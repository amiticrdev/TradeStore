# Trade Store Spring Boot Application.

#### Build the Application:
``` mvn install ```

#### Run the tests:
``` mvn test ```

#### Start the Application:
``` mvn spring-boot:run ```


##### Database: 
Im memory DB H2 is used to store the trades, Saving the Data in file - /data/demo so that its still available when the VM exits

h2-console
http://localhost:8080/h2-console/

To add data with script:
run scripts in H2- /src/main/resources/trade_store.sql


##### Start Localhost:
http://localhost:8080


##### Sample Request Payload for POST request (POST: http://localhost:8080/tradeStore):

```
{  
    "tradeId": "T5",
    "version": 4,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2021-05-20",
    "createdDate": "2015-03-14",
    "expired": "N"
}
```
