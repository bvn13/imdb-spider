package ru.bvn13.imdbspider.spider.extractor;

import ru.bvn13.imdbspider.exceptions.extractor.HtmlExtractorException;
import ru.bvn13.imdbspider.exceptions.extractor.MalformedUrlException;
import ru.bvn13.imdbspider.exceptions.extractor.ConnectionEstablishingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Map;

/**
 * @author boyko_vn at 09.01.2019
 */
public class HtmlExtractor {

    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";

    private static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public String getHtml(String url, Map<String, String> headers) throws HtmlExtractorException {

        URL obj = null;

        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            throw new MalformedUrlException(String.format("Wrong url: %s", url), e);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            throw new ConnectionEstablishingException(String.format("Unable to open connection by utl: %s", url), e);
        }

        connection.setRequestProperty("Accept", "text/html");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new ConnectionEstablishingException(String.format("Wrong protocol GET for utl: %s", url), e);
        }

        BufferedReader in = null;
        try {
            String inputLine;
            StringBuilder response = new StringBuilder();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        } catch (IOException e) {
            throw new ConnectionEstablishingException(String.format("Could not get input stream for utl: %s", url), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
