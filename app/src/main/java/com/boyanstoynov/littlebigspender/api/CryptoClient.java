package com.boyanstoynov.littlebigspender.api;

import android.os.Looper;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.boyanstoynov.littlebigspender.util.Constants.API_CRYPTO_CURRENCY_CODE_QUERY_STRING;
import static com.boyanstoynov.littlebigspender.util.Constants.API_FIAT_CURRENCY_CODE_QUERY_STRING;
import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_API_URL;

/**
 * Client that connects to external API and fetches the price of a
 * cryptocurrency in a fiat currency. Creating class much implement the
 * ClientCallback interface in order to be notified of the result of the
 * request.
 *
 * @author Boyan Stoynov
 */
public class CryptoClient {

    /**
     * Interface that must be implemented by objects initialising the
     * CryptoClient. Provides callback methods for whether the fetch
     * was successful or not.
     */
    public interface ClientCallback {
        void onFetchUnsuccessful();
        void onFetchSuccessful(BigDecimal convertedPrice);
    }

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ClientCallback callbackListener;

    /**
     * Constructor that takes a ClientCallback object that needs to be
     * informed of whether the fetch was successful or not.
     * @param callbackListener ClientCallback listener
     */
    public CryptoClient(ClientCallback callbackListener) {
        this.callbackListener = callbackListener;
    }

    /**
     * Fetches the price of 1 unit of the specified cryptocurrency into
     * the specified fiat currency.
     * @param cryptocurrencyCode Code of cryptocurrency of interest
     * @param fiatCurrencyCode Code of fiat currency to convert into
     */
    public void fetchPrice(String cryptocurrencyCode, final String fiatCurrencyCode) {
        // Build API request URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(CRYPTO_API_URL).newBuilder();
        urlBuilder.addQueryParameter(API_CRYPTO_CURRENCY_CODE_QUERY_STRING, cryptocurrencyCode);
        urlBuilder.addQueryParameter(API_FIAT_CURRENCY_CODE_QUERY_STRING, fiatCurrencyCode);
        String url = urlBuilder.build().toString();

        //Build Request
        Request request = new Request.Builder()
                .url(url)
                .build();

        //Send request to API
        httpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
                callbackListener.onFetchUnsuccessful();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String stringResponse = response.body().string();

                try {
                    JSONObject json = new JSONObject(stringResponse);
                    Looper.prepare();
                    callbackListener.onFetchSuccessful(new BigDecimal(json.getString(fiatCurrencyCode)));
                } catch (JSONException e) {
                    callbackListener.onFetchUnsuccessful();
                }
            }
        });
    }
}
