package edu.kit.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.semanticweb.yars.turtle.ParseException;
import org.semanticweb.yars.turtle.TurtleParseException;
import org.semanticweb.yars.turtle.TurtleParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cl.uchile.dcc.blabel.label.GraphColouring.HashCollisionException;
import edu.kit.service.HasherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/hasher")
@Api(tags = "Hasher")
@CrossOrigin
public class HasherController {
    @Autowired
    HasherService hasherService;

    /**
     * Retrieve the hash associated to the rdf content in the message body, with the
     * (http) baseURI specified in the corresponding request header. Returns an
     * turtle statement:
     * <baseURI> <http://www.w3.org/2000/10/swap/crypto#md5>
     * '0xHASH'^^<http://www.w3.org/2001/XMLSchema#hexBinary> .
     * 
     * @param baseURI {@link RequestHeader}
     * @param rdf {@link RequestBody}
     * @return turtle statement: baseURI crypto:md5 hash^^xsd:hexBinary
     */
    @ApiOperation(value = "Post a requeest for a ld-hash!", notes = "Retrieve the hash "
            + "associated to the rdf content in the message body, with the (http) baseURI specified in the corresponding request header. "
            + "Returns an turtle statement: "
            + "<baseURI> <http://www.w3.org/2000/10/swap/crypto#md5> '0xHASH'^^<http://www.w3.org/2001/XMLSchema#hexBinary> . ")
    @RequestMapping(method = RequestMethod.POST, consumes = "text/turtle", produces = "text/turtle")
    public ResponseEntity<String> getHash(@RequestHeader("baseURI") String baseURI, @RequestBody String rdf) {
        // setup for parsing
        Charset cs = StandardCharsets.UTF_8;
        InputStream rdfStream = new ByteArrayInputStream(rdf.getBytes(cs));
        // parsing
        TurtleParser nxp = new TurtleParser();
        try {
            nxp.parse(rdfStream, cs, new URI(baseURI));
        } catch (TurtleParseException | ParseException | URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        // hashing
        String hash;
        try {
            hash = this.hasherService.hashGraph(nxp);
        } catch (InterruptedException | HashCollisionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        // response
        return ResponseEntity.ok("<" + baseURI + "> <http://www.w3.org/2000/10/swap/crypto#md5> '" + hash
                + "'^^<http://www.w3.org/2001/XMLSchema#hexBinary> .");
    }
}
