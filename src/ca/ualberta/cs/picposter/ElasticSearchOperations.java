/**
 * 
 */
package ca.ualberta.cs.picposter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.cs.picposter.model.PicPostModel;

import com.google.gson.Gson;

/**
 * @author wyatt
 *
 */
public class ElasticSearchOperations {

	public static void pushPicPostModel(final PicPostModel model) {
		Thread thread = new Thread() {

			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				// Create objects
				Gson gson = new Gson();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/wyatt/");
				// HttpPut request = new HttpPut("http://cmput301.softwareprocess.es:8080/testing/tamurphy/1");
				
				// JSONify model
				String jsonString = gson.toJson(model);
				try {
					// Add to entity
					request.setEntity(new StringEntity(jsonString));
					// run it
					HttpResponse response = client.execute(request);
					
					Log.w("ElasticSearch", response.getStatusLine().toString());
					
					// get the entity
					response.getStatusLine().toString();
					HttpEntity entity = response.getEntity();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					
					while (output != null) {
						Log.w("Elastisearch", output);
						output = reader.readLine();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		};
		
		thread.start();
	}
	
	public static void searchPicPostModels(final String searchString) {
		Thread thread = new Thread() {

			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				try {
					String searchQuery = "http://cmput301.softwareprocess.es:8080/testing/wyatt/_search?q=text:" + java.net.URLEncoder.encode(searchString,"UTF-8");
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(searchQuery);
					request.setHeader("Accept", "application/json");
					
					HttpResponse response = client.execute(request);
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					
					String output;
					String json = "";
					
					while ((output = reader.readLine()) != null) {
						json += output;
					}
					
					Gson gson = new Gson();
					ElasticSearchSearchResponse elasticSearchSearchResponse = gson.fromJson(json, ElasticSearchSearchResponse.class);
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		
		thread.start();
	}
	
}
