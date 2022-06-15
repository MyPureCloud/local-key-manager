# Local Key Encryption Service

This repository is an example local key management service written in Java with Spring. Although it is possible to use this service with a few small changes, it is important to note that this is merely an example of how to use the API.

**Make sure that your local key manager implementations are reliable, highly available, and secure. If encryption keys are lost, it will no longer be possible to retrieve the recordings encrypted with them. Use this code at your own risk.**


## General Documentation and Explanation of work flows

Below is an explanation on how to set up the service. Although this service contains almost everything you would need to generate
key pairs and decrypt incoming payloads, it is worth noting the requirements around key pairs and decryption in case you are wanting
to use your own solution for key management. First, this part of the documentation will give an overview of the workflow, then
get into more of the specific details around the service.

### Registering a local key management service for your org with Genesys Cloud

A general work flow to get your local service acknowledged and production ready is listed below.

The following values will need to be replaced in the sample REST exchanges below:
* GENESYS_CLOUD_PUBLIC_API_PUBLIC_ADDRESS - Publicly addressable endpoint for API access to your organization's configuration (See  https://developer.genesys.cloud/api/rest/, for example this would be https://api.mypurecloud.com)
* LOCAL_KEY_MANAGER_PUBLIC_ADDRESS - Publicly addressable endpoint your local key management service is listening on.


1. First get your service running with https (port 443), and ensure all endpoints are working as expected, including authentication.
   You should also make sure you have an options action around your decrypt endpoint, so that we can verify the endpoint exists (during step 4 below)


2. Register a hawk authentication key for Genesys Cloud, so that Genesys Cloud can directly hit the service. Once you get the return values be sure to record them as you will need to send the results to the Genesys Cloud local configuration endpoint.

   `POST https://[LOCAL_KEY_MANAGER_PUBLIC_ADDRESS]/key-management/v1/auth` with header `Content-Type: application/json`

   ```
   {
       "id": "KEY_IDENTIFIER"
   }
   ```

   Response of 200 with body:

   ```
   {
     "id": "KEY_IDENTIFIER",
     "authKey": "GENERATED_AUTH_KEY",
     "algorithm": "sha256"
   }
   ```
3. Register your application with Genesys Cloud and get the proper OAuth2 credentials to access the public api.

4. Set up your local key manager key configuration in Genesys Cloud UI, see this article [Local key management](https://help.mypurecloud.com/articles/local-key-management/) for UI instruction. Or alternatively you could send over the api id, api key, decryption url and key configuration type to the public api key configuration endpoint.

    `POST https://GENESYSCLOUD_PUBLIC_API_PUBLIC_ADDRESS/api/v2/recording/keyconfigurations`  with `Content-Type: application/json` AND OAuth2 headers

    ```
    {
      "url": "https://LOCAL_KEY_MANAGER_INSTANCE_ADDRESS/key-management/v1/decrypt",
      "apiId": "KEY_IDENTIFIER",
      "apiKey": "GENERATED_AUTH_KEY",
      "keyConfigurationType": "LocalKeyManager"
    }
    ```

    Response status 200 with payload:

    ```
    {
      "id": "CONFIGURATION_ID",
      "url": "https://LOCAL_KEY_MANAGER_INSTANCE_ADDRESS/key-management/v1/decrypt",
      "apiId": "KEY_IDENTIFIER",
      "apiKey": "GENERATED_AUTH_KEY",
      "keyConfigurationType":"LocalKeyManager",
      "encryptionDomain":"Recording",
      "selfUri":"/api/v2/recording/keyconfigurations"
    }
    ```

   Make sure you record the configuration id returned, since it will be needed for creation of keypairs.

5. Create a keypair using the generate keypair endpoint on your local service.

    `POST https://[LOCAL_KEY_MANAGER_PUBLIC_ADDRESS]/key-management/v1/keypair`

    Response 200 with payload:

    ```
    {
      "id": "GENERATED_KEYPAIR_ID",
      "publicKey": "PUBLIC_KEY_BASE64",
      "dateCreated": 1479389944924
    }
     ```


6. Send the public key, config id, keypair id to the public api key creation endpoint.

    `POST https://GENESYS_CLOUD_PUBLIC_API_PUBLIC_ADDRESS/api/v2/recording/localkeys`

    ```
    {
       "configId": "CONFIGURATION_ID",
       "publicKey": "PUBLIC_KEY_BASE64",
       "keypairId": "GENERATED_KEYPAIR_ID"
     }
     ```

     Response 200 with payload:
     ```
     {
       "id": "GENERATED_KEYPAIR_ID",
       "createDate": "2016-11-17T14:24:19.211Z",
       "selfUri": "/api/v2/recording/recordingkeys"
     }
    ```

7. If the request is accepted a status code 200 is returned and your service should be ready to accept decryption requests.

Additional notes:

For more details about Genesys Cloud's public api, please visit: https://developer.genesys.cloud/

For step 2, you can use all of the code here in the service to help set up hawk authentication. The library in the pom
takes care of most of the nuances involved in setting up the authentication. To experiment with hawk authentication with postman,
you can follow this tutorial here on how to set it up. http://blog.getpostman.com/2015/12/05/postman-v3-2-is-out-with-hawk-authentication-support/

For step 3, visit https://developer.genesys.cloud/api/tutorials.html to get a better understanding of our oauth flows.
https://developer.genesys.cloud/api/rest/authorization/ also provides detailed information on how Genesys Cloud's authentication works.

For step 4, as mentioned above, make sure the decryption endpoint sent to Genesys Cloud has an OPTIONS method, so that Genesys Cloud
can verify if the endpoint exists or not. It is also worth mentioning that we require the endpoint to be accept a POST for decryption.

For step 5, Genesys Cloud currently requires RSA 3072 bit key pairs that are DER encoded. This is important because other types of
key encoding will NOT work. For security reasons, never share your private key with anyone. You may receive an error from Genesys Cloud
mentioning you service isn't ready. We know this because we send a sample decrypt request immediately after the keypair is registered to verify
the endpoint works and exists.

For step 6, this is important. At this time, the Edge will start using the provided public key sent with the keypair for encyption.
If for whatever reason, your service is not up, you will not be able to transcode or download recordings encrypted with the local key pair.
Make sure you do not remove ANY key pairs that have been used by Genesys Cloud at any point because those recordings will be locked forever.

The user id of the Genesys Cloud user making decryption requests or key pair generation requests that are sent to the local key encryption service via a HTTP header named `PureCloud-User-Id`.  

Even if you plan on not using this service, please take a minute to look around and read the code comments as they explain
certain details of decryption workflow with more specifics.

## Setting up the Environment

If you have ubuntu 14.04 (no other versions have been tested so use at your own risk),
you can run the mysql_setup.sh script provided in the root of this repository 
to get started by typing `bash mysql_setup.sh`.
You will be prompted with several questions, including java terms of use, mysql, and 
various packages wanting yes or no answers.

## Running the Application

Once you have cloned the repository, in the directory root, run:


`mvn clean package`

`java -jar target/key-manager.jar`


You can change the port to whatever you like.

## Using Different Databases

Currently, this service is using sqllite3, which is not suitable for production
environments. We strongly recommend that you switch out the temporary database
for whichever you choose. Below will contain a few examples on how to configure
a couple popular databases.

### Mysql

Using at least ubuntu 14.04, to install mysql from the command line type (if not using mysql_setup.sh):

`sudo apt-get install mysql-server`

`sudo mysql_secure_installation`

`sudo mysql_install_db`

You will have to fill out your credentials for the database. Make sure you remember
what they are because you will need them for later. You may be asked a series of
security based questions from there as well, so make sure you input the best options
for your company needs. Once you have installed mysql, you can verify that mysql service is running
by typing `sudo service mysql start` in the command line type. It should say
that it is already running. Once verified login to mysql.


`mysql -u <yourUsername> -p`


Then type your password. Once you are logged in, input:


`create database local_encryption;`


Once you have created the database, you can now exit the program by simply
typing `exit`. When you have exited mysql, go back to the local-key-encryption
repository on your machine. From there, use your favorite text editor to edit
the application.properties file in the root of the repository. The contents of that file should contain the 
configurations of the sqllite database. You can replace all of that with:


```
spring.datasource.url=jdbc:mysql://localhost/local_encryption
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```


Notice the last option is update, you can change that to `create-drop`, if you would
like the database to be dropped on each change to entity models. This is not recommended
for production environments.

From there, run the application.



## Working with SSL

Currently, there is a self signed certificate that is provided for ssl. This is not
production ready, and it should be changed out with a properly signed certificate.
There are several services that offer signing of certificates. A great free solution
comes from Let's Encrypt (https://letsencrypt.org/). Below are some tutorials in how to setup
a certificate with Let's Encrypt. 

https://tty1.net/blog/2015/using-letsencrypt-in-manual-mode_en.html

After you get the signed certificate, you will need to add the path to the application.properties file.
You will notice commented out properties in the application.properties file. Those will need to be uncommented to use the api. 
Note, based on the file type you choose, you may need to change the key-store-type from PKCS12 to whatever
format that you choose. It is worth noting that spring prefers jks for certificates.
Whatever your format is you can search on how to convert your file's format to the jks format.


## Using the local key manager API

The below documentation shows how to use the api. Even if you are not using 
this service, it is very important to read the documentation below as it provides
the interface that is needed to interact with Genesys Cloud.


### /key-management/v1/auth

All endpoints need some sort of authentication. Genesys Cloud's encryption endpoints require hawk authentication to
be able to use the application. Below are the endpoints and explanations for how to set up Hawk Authentication with the
service. 


#### POST

First, you will need to set up an identification for Genesys Cloud's consuming api. In order to do this, you can send a post
to the authentication endpoint.

##### Input
```
{
    "id": "Identification of the hawk authentication"
}
```
##### Success Output
```
{
    "id": "Id Input",
    "authKey": "The authorization key that will be shared with Genesys Cloud",
    "algorithm": "Algorithm used for hawk model encryption"
}

```

Once you have registered a hawk authenticate service, you can then send the details to Genesys Cloud local configuration resource.
It will require you to send the id, authkey, and the url to reach your service.

### /key-management/v1/keypair

This endpoint allows you to generate a new keypair. You will need to be authenticated to use the endpoint.

#### POST

The POST is an action endpoint and requires no body to generate a keypair. Whenever you generate a keypair, you will need to 
let Purecloud know, so that we can encrypt recordings with the key.

##### Input 
```
nothing : An action request
```
##### Success Output
```
{
    "id": "String",
    "publicKey": "String",
    "dateCreated": "Long"
}

```

#### GET

##### Success Output
```
[{
       "id": "String",
       "publicKey": "String",
       "dateCreated": "Long"
}]

```


### /key-management/v1/keypair/{keypair-id}

Gets a single keypair

#### GET

##### Success Output
```
{
    "id": "String",
    "publicKey": "String",
    "dateCreated": "Long"
}

```

Once you have created a keypair, you can send it to the post keypair endpoint. It will require knowing the local configuration id,
the base64 encoded public key, and the keypair id.


### /key-management/v1/decrypt

Decrypts an encrypted payload. This requires authentication as well.

##### Input
```

{
    "keypairId": "String",
    "body": "String"
}
```
##### Success Output
```

{
    "body": "String"
}

```

### key-management/v1/request-log

This feature is not required for key management or decryption, but is instead
presented as an example of how to use the `PureCloud-User-Id` header to produce
useful request logging information.

Whenever a request is made against an authenticated enpoint, the request is logged.  Logged information
includes the Genesys Cloud user id making the request, the request method (POST, PUT, GET, etc.), the URI used
in the request, and the date and time of the request.

The request log can be retrieved by making GET request to `key-management/v1/request-log`.  By default, it
will retrieve up to 100 of the most recent log entries.  Query request parameters may be
used in order to change the log entries retrieved.  You will need to be authenticated to use the endpoint.

The request log can be cleared of all existing entries by making a DELETE request to `key-management/v1/request-log`.  You will need to be authenticated to use the endpoint.

#### GET
##### Query parameters
* **maxEntries**  Sets the maximum number of request log entries that will be returned
* **earliestTime**  Sets the earliest/oldest date/time of request log entries that will be returned.  Must be specified in
ISO8601 format
* **latestTime**  Sets the latest/newest date/time of request log entries that will be returned.  Must be specified in
 ISO8601 format

##### Success Output (example)
```
[
    {
        "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
        "userId": "yyyyyyyy-yyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy",
        "requestUri": "/key-management/v1/decrypt",
        "requestMethod": "POST",
        "timestamp": [
            2015,
            8,
            31,
            13,
            49,
            22,
            444000000
        ],
        "timestamp-String": "2015-08-31T13:49:22.444"
    },
    {
        "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
        "userId": "yyyyyyyy-yyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy",
        "requestUri": "/key-management/v1/decrypt",
        "requestMethod": "OPTIONS",
        "timestamp": [
            2015,
            8,
            29,
            16,
            17,
            53,
            952000000
        ],
        "timestampString": "2015-08-29T16:17:53.952"
    }
]

```
