package com.pivotallabs.tracker;

import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiResponse;
import com.pivotallabs.api.ApiResponseCallbacks;
import com.pivotallabs.api.Xmls;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class RecentActivities extends ArrayList<RecentActivity> {
    private ApiGateway apiGateway;
    private TrackerAuthenticator trackerAuthenticator;

    public RecentActivities(ApiGateway apiGateway, TrackerAuthenticator trackerAuthenticator) {
        this.apiGateway = apiGateway;
        this.trackerAuthenticator = trackerAuthenticator;
    }

    public void update() {
        apiGateway.makeRequest(new RecentActivityRequest(trackerAuthenticator.getToken()), new RecentActivityApiResponseCallbacks());
    }

    private class RecentActivityApiResponseCallbacks implements ApiResponseCallbacks {
        @Override
        public void onSuccess(ApiResponse response) {

            try {
                String responseBody = response.getResponseBody();

                Document document = Xmls.getDocument(responseBody);
                NodeList activityNodeList = document.getElementsByTagName("activity");
                for (int i = 0; i < activityNodeList.getLength(); i++) {
                      add(new RecentActivity().applyXmlElement((Element)activityNodeList.item(i)));
                }
            } catch (ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        @Override
        public void onFailure(ApiResponse response) {
            System.out.println("Failure retrieving recent activity: " + response.getResponseCode() + ":" + response.getResponseBody());
        }

        @Override
        public void onComplete() {
        }

    }
}