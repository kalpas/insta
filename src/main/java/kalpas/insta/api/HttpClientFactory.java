package kalpas.insta.api;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClients;

public class HttpClientFactory {

    public static CloseableHttpClient getHttpClient() {
        int timeout = 30;// 300 s timeout (5 min)

        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000).build();

        DefaultServiceUnavailableRetryStrategy retryStrategy = new DefaultServiceUnavailableRetryStrategy(3, 3000);

        // HttpRequestRetryHandler handler = new
        // DefaultHttpRequestRetryHandler() {
        //
        // @Override
        // public boolean retryRequest(IOException exception, int
        // executionCount, HttpContext context) {
        // if (exception instanceof InterruptedIOException) {
        // return true;
        // } else
        // return super.retryRequest(exception, executionCount, context);
        // }
        // };

        return HttpClients.custom().setDefaultRequestConfig(config).setServiceUnavailableRetryStrategy(retryStrategy)
                .build();
    }
}
