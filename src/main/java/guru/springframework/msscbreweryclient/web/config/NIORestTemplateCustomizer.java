package guru.springframework.msscbreweryclient.web.config;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NIORestTemplateCustomizer implements RestTemplateCustomizer {

    private final Integer connectionTimeout;
    private final Integer ioThreadCount;
    private final Integer soTimeout;
    private final Integer maxTotalCOnnections;
    private final Integer defaultMaxTotalConnections;

    public NIORestTemplateCustomizer(@Value("${sfg.connectiontimeout}")Integer connectionTimeout,
                                     @Value("${sfg.iothreadcount}")Integer ioThreadCount,
                                     @Value("${sfg.sotimeout}")Integer soTimeout,
                                     @Value("${sfg.maxtotalconnections}") Integer maxTotalCOnnections,
                                     @Value("${sfg.defaultmaxtotalconnections}") Integer defaultMaxTotalConnections) {
        this.connectionTimeout = connectionTimeout;
        this.ioThreadCount = ioThreadCount;
        this.soTimeout = soTimeout;
        this.maxTotalCOnnections = maxTotalCOnnections;
        this.defaultMaxTotalConnections = defaultMaxTotalConnections;
    }

    public ClientHttpRequestFactory clientHttpRequestFactory() throws IOReactorException {
        final DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.custom()
            .setConnectTimeout(connectionTimeout)
            .setIoThreadCount(ioThreadCount)
            .setSoTimeout(soTimeout)
            .build());

        final PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connectionManager.setDefaultMaxPerRoute(defaultMaxTotalConnections*5);//100
        connectionManager.setMaxTotal(maxTotalCOnnections*10);//1000

        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        return new HttpComponentsAsyncClientHttpRequestFactory(httpAsyncClient);
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        try{
            restTemplate.setRequestFactory(clientHttpRequestFactory());
        } catch (IOReactorException e){
            e.printStackTrace();
        }
    }
}
