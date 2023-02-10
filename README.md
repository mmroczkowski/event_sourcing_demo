# Event Sourcing Demo

This project is a demonstration of event sourcing, a pattern used in software development which can help track changes to an application over time.

## Features

- Demonstration of event sourcing pattern
- Contains test data, so all endpoints are presentable from very beginning
- Declares very simple REST endpoints
- Has one-command build
- No dependencies to any databases or other services

## Prerequisites

- Java 18 or higher
- Maven
- Git

## Building and Running

To build the project, run the following command from the root directory:

```
mvn clean install
```

The project can then be run by executing the main class EventSourcingDemo.class.

## Endpoints

Application has four endpoints:

- GET /accounts - gets projection of all accounts in the system
- GET /transactionHistory?accountId=[uuid]&sinceDate=[dd/MM/yyyy] - gets history for specific account
- POST /openAccount - opens new account, post body must contain:
```
{
    "accountNumber": "[accountNumber]",
    "initialDeposit": "[initialAmount]",
    "creditLine": "[creditLineAmount]"
}
```
- POST /executePayment - executes credit or debit transfer on account, post body must contain:
```
{
    "accountId": "[uuid]",
    "amount": "[transferAmount]"
}
```

## Usage

Application contains test data, so /account and /transactionHistory endpoints can be called immediately after startup.
User can always add accounts using /openAccount and then /executePayment on any account.