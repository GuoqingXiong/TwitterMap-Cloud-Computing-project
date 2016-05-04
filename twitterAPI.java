package twitterAPI;



import twitter4j.conf.ConfigurationBuilder;

import java.util.Date;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.simple.*;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class twitterAPI {

	
	private static String consumerKey = "", 
			consumerSeceret = "",
			token = "", 
			tokenSecret = "";

	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
			.setOAuthConsumerKey(consumerKey)
			.setOAuthConsumerSecret(consumerSeceret)
			.setOAuthAccessToken(token)
			.setOAuthAccessTokenSecret(tokenSecret);				
		
		String[] keyWords = {"oscar","award"};//the relationship between the keywords is or
		FilterQuery fq = new FilterQuery();
		fq.track(keyWords);
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		 		try {
		 				
		 			StatusListener listener = new StatusListener() {
		 				@Override
		 				public void onStatus(Status status) {
		 					try {
		 						if (status.getGeoLocation() != null && status.getLang().equalsIgnoreCase("en")) {
		 							String createAt=status.getCreatedAt().toString();
		 							long idStr=status.getId();
		 							String text = status.getText();
		 							GeoLocation coordinates = status.getGeoLocation();
		 				
		 							JSONObject json = new JSONObject();
		 							
		 							json.put("id_str", idStr);
		 							json.put("created_at", createAt);
		 							json.put("text", text);
		 							json.put("coordinates", coordinates.toString());
		 							String index = json.toString();
		 							System.out.println(index);
		 						}
		 					} catch (Exception e) {
		 						e.printStackTrace();
		 					}
		 				}

				@Override
				public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
					System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
				}

				@Override
				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
					System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
				}

				@Override
				public void onScrubGeo(long userId, long upToStatusId) {
					System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
				}

				@Override
				public void onStallWarning(StallWarning warning) {
					System.out.println("Got stall warning:" + warning);
				}

				@Override
				public void onException(Exception ex) {
					ex.printStackTrace();
				}								
			};
			twitterStream.addListener(listener);
			twitterStream.filter(fq);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
