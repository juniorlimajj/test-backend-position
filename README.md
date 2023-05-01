[![Validate Build and Tests](https://github.com/CocusPT-development/josejunior_27042023/actions/workflows/actions.yml/badge.svg?branch=main)](https://github.com/CocusPT-development/josejunior_27042023/actions/workflows/actions.yml)

# Description

The main objective of this Challenge is to implement a Rest API using Java language to manage a specific resource: Doctor Label.

To obtain training data for ML-based diagnosis, Cocus ask doctors to label medical cases by reading the EHR (electronic health record) of a patient encounter and labelling it with a diagnosis (condition).

#### An EHR is a text file that may look something like this:

The patient is a 32 year old female who presents to the Urgent Care complaining of sore throat that started 7 days ago. Developed into post nasal drip and then cough. No measured fevers. Had chills and body aches at the onset that resolved after the first day. A little facial headache with the congestion at times; better today. Some pressure on and off in the ears, no pain, hearing loss or tinnitus. Cough is mostly dry, sometimes productive of clear sputum. Denies shortness of breath. Never smoker. Has never needed inhalers. No history of pneumonia. Currently treating with ibuprofen which helps. Tried some over-the-counter Mucinex ES and vitamin C.

#### A condition consists of an ICD-10 condition code and description, e.g.:
* F411    Generalized anxiety disorder
* J00     Acute nasopharyngitis

# Backend: Microservices
The application should be spitted in two parts, Doctor Labelling and Labels.

### Doctor Labelling - Service

1. Doctor Labelling contain these fields:
    1.	Case id
    1.	Case description
    1.	Doctor id
    1.	Label
    1.	Time to label the case should be recorded for each decision
1. Doctor Labelling should be able to manage and persist the Doctor Labels, providing the following operations:
    1. Create a new label in the case
    1. Create a new case
    1. Retrieve all cases
    1. Retrieve a specific case
    1. Retrieve cases filter by label
    1. Delete old label in the case
    1. Update an existing case
### Label – Service

1. Label contain these fields:
    1. Label Code
    1. Label description

1. Label should be able to manage and persist the Labels, providing the following operations:
    1. Create a new Label
    1. Get a labels filter by id
    1. Update an existing label
    1. Delete an existing label
   ##### Note: This Microservice Labels can have downtimes

### Client

The client should allow the reviewer to call all the operations of the Rest API without having to handle the connection by himself. There are no restrictions on the programming language for this component.

Example: Postman Request, Insomnia Requests, etc..

### Example
1. Client wants to retrieve details all cases. Client calls Doctor Label microservice which is responsible by retrieving the data from its own persistent layer and retrieve the needed information from the Label microservice.
1. Client wants to associate a new label to a case. The necessary information is provided to Doctor labelling service which is responsible by ensuring data integrity and persist the need information.

# Requirements

* Candidates must provide README file containing instructions on how to launch the backend and how to run the client.
* Implementing the backend solution using Java Language (recent LTS java version)
* Use any kind of database (PostgreSQL, MariaDB, DynamoDB, MongoDB,Cassandra,…)
* Docker is used to start database and/or microservices
* OpenAPI/Swagger spec is generated for the created Rest API
* Provide any kind of client to interact with API
* Using any cache system
* Implementation of Unit and Integration tests
* Implementation of logging
* The solution should be submited in this repository

# Plus
* Database has some kind of version control
* Implementation of Smoke tests
