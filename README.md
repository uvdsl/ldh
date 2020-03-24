# LDH - Linked Data Hasher 
This is just a quick and dirty RESTful-Server to hash Linked Data.
Currently, it only talkes [turtle](https://www.w3.org/TR/turtle/).
The hasher is available at '/hasher'.
For documentation and testing, Swagger is in place at '/swagger'.


## Request
Request-Headers (baseURI is your document's uri):
```
Accept: "text/turtle"
Content-Type: "text/turtle"
baseURI: "http://exampleInput.org/"
```
Request-Body is the rdf content in trutle format.

## Response: 200 OK
You get something back like: 
```
<baseURI> <http://www.w3.org/2000/10/swap/crypto#md5> '0xHASH'^^<http://www.w3.org/2001/XMLSchema#hexBinary> . 
```

## Response: 4XX - Client Error
When you get a 4XX error, you may have forgotten the baseURI-header or your turtle-syntax is messed up. If the server can't parse what you feed it, you will get a 400 Bad Request. You can see what went wrong in the message body.

## Response: 5XX - Server Error
Unlikely, but nonetheless: If the server goes wild while hashing, a 500 Internal Server Error is given back. You can see what went wrong in the message body.

##

![Publish Docker image to GitHub Package Registry (GPR)](https://github.com/uvdsl/ldh/workflows/Publish%20Docker%20image%20to%20GitHub%20Package%20Registry%20(GPR)/badge.svg)
