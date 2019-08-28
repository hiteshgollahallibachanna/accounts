# Accounts
Simple accounts api with atm as a client. This use in memory map to store accounts.
Current only 2 account have been created to demo this app.
<br>
```curl
pan: 111-222 pin : 1234
pan: 333-444 pin: 9090
```


#Assumption
1. Account with pan (personal account number) and pin si created ahead of time.
 
##Running/building the application
Requires Java 11
<br>To build
``./gradlew clean build jacocoTestReport``

To start the application
``./gradlew run
``
<br>

#Sample request
#####To login in 
```curl
curl --location --request POST 'http://localhost:8080/accounts/v1/login' \
--header 'Content-Type: application/json' \
--data-raw '{
"pan": "111-222",
"pin": 1234
}'

response
{
    "first_name": "foo",
    "last_name": "bar",
    "pan": "111-222",
    "signed_in": true
}
```

####To log out
```curl
curl --location --request GET 'http://localhost:8080/accounts/v1/logout/111-222'
```

####To deposit
```curl
curl --location --request POST 'http://localhost:8080/accounts/v1/deposit' \
--header 'Content-Type: application/json' \
--data-raw '{
"pan": "111-222",
"amount": 50.123
}'

response 
{
    "type": "DEPOSIT",
    "amount": 50.123,
    "pan": "111-222",
    "created": "2021-10-28T09:43:59.28-05:00"
}
```

####To withdraw
```curl
curl --location --request POST 'http://localhost:8080/accounts/v1/withdraw' \
--header 'Content-Type: application/json' \
--data-raw '{
"pan": "111-222",
"amount": 50
}'

response
{
    "type": "WITHDRAW",
    "amount": 50,
    "pan": "111-222",
    "created": "2021-10-28T11:47:27.056995-05:00"
}
```

####To view balance
```curl
curl --location --request GET 'http://localhost:8080/accounts/v1/111-222'

response
{
    "first_name": "foo",
    "last_name": "bar",
    "pan": "111-222",
    "pin": 1234,
    "signed_in": true,
    "balance": 48,
    "transactions": [
        {
            "type": "DEPOSIT",
            "amount": 100,
            "pan": "111-222",
            "created": "2021-10-28T11:42:22.476472-05:00"
        },
        {
            "type": "WITHDRAW",
            "amount": 1,
            "pan": "111-222",
            "created": "2021-10-28T11:42:26.467421-05:00"
        },
        {
            "type": "WITHDRAW",
            "amount": 1,
            "pan": "111-222",
            "created": "2021-10-28T11:42:54.545232-05:00"
        },
        {
            "type": "WITHDRAW",
            "amount": 50,
            "pan": "111-222",
            "created": "2021-10-28T11:47:27.056995-05:00"
        }
    ]
}
```

#Docker image
Dockerfile is included
to build docker image
``docker build -t hit/accounts-v1 .``
<br>to run the docker image
``docker run -p 8080:8080 hit/accounts-v1``

#Running client application
AtmMachine class is available which is wrapper around calling the rest endpoint.
To run use
``./gradlew atmMachine``