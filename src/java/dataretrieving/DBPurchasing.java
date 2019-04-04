/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataretrieving;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:we [dbdata.purchasing]<br>
 * USAGE:
 * <pre>
 *        DBPurchasing client = new DBPurchasing();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Vaehnor
 */
public class DBPurchasing {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://ws.docencia.ces.siani.es/a07/DBAccessWS/webresources/";

    public DBPurchasing() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("dbdata.purchasing");
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)
     */
    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)
     */
    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * @param requestEntity request data@return response object (instance of responseType class)
     */
    public void create_XML(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * @param requestEntity request data@return response object (instance of responseType class)
     */
    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public void close() {
        client.close();
    }
    
}
