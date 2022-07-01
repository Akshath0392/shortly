### Simple application for shortening the url ###
This is a simple spring boot based application
* To generate short or tinny url easy of sharing
* To allow redirections to original url based on shorten url

#### Setup ####
* Extract application binary to a suitable location
* Update application.properties
* Run `sh bin/start.sh` to start the application
* Run `sh bin/stop.sh` to stop the application

#### API Endpoints ####

##### URL shortener #####
This endpoint can be used to generate a shortened url 

**Request**

```
POST /api/shortly/create
Content-Type: application/x-www-form-urlencoded
url=https://url.tld
```

Attributes
* url (mandatory) 

**Response**
```
{
    "success": true,
    "result" : {
        "url": "https://url.tld",
        "shorturl": "https://shorturl.tld"
    }
}
```

##### URL Redirecter #####
This endpoint will be used to redirect to actuall url upon acessing the short url

**Request**

```
GET /{id}
```

Attributes
* id (mandatory) 

**Response**

Redirection will be done to original url mapped to short url