package com.leisucn.demo.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.DeleteResponse;
import com.coreos.jetcd.kv.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception{

        Properties sys = new Properties();
        sys.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

        String host = sys.getProperty("etcd.server.host");
        String port = sys.getProperty("etcd.server.port");

        assert host != null;

        String server = String.format("http://%s:%s",host,port);
        LOG.info("etcd server is {}", server);


        Client client = Client.builder()
                .endpoints(server)
                .build();

        KV kvClient = client.getKVClient();

        ByteSequence key  = ByteSequence.fromString("test_key");
        ByteSequence value = ByteSequence.fromString("test_value");

        // put the key-value
        kvClient.put(key, value).get();
        // get the CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);
        // get the value from CompletableFuture
        GetResponse response = getFuture.get();
        LOG.info("get response is {}", response);
        // delete the key
        DeleteResponse deleteRangeResponse = kvClient.delete(key).get();
        LOG.info("delete response is {}", deleteRangeResponse);
    }
}
