package com.goodgames.ti;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.net.URL;
import java.nio.file.Files;
import java.util.*;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class APICaller {
	
	private String name;
	private String token;
	private String host = "api01.peapod.hh-wev.ggs-net.com";
	private int port = 50000;
	private static final int BUFFER_SIZE = 4096;
	
	public APICaller(String name, String token) {
		this.name = name;
		this.token = token;
	}
	
	private void enableAuth(HttpURLConnection connection) {
		String userPassword = this.name + ":" + this.token;
		String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
		//refer to http://stackoverflow.com/questions/2054687/java-fetch-url-with-httpbasic-authentication
		encoding = encoding.replaceAll("\n", "");
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		//return connection;
	}
	
	private void post(HttpURLConnection connection, String urlParameters) throws Exception {
		
		byte[] postData = urlParameters.getBytes("UTF-8");

		connection.setRequestMethod("POST");
		connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(postData.length));
		connection.setRequestProperty( "charset", "utf-8");
		enableAuth(connection);		
		
  		//You need to set it to true if you want to send (output) a request body, 
  		//for example with POST or PUT requests. With GET, you do not usually send a body,   		
  		//so you do not need it.
        //Sending the request body itself is done via the connection's output stream.
        

		connection.setDoOutput(true);      
		DataOutputStream wr = new DataOutputStream( connection.getOutputStream());
		wr.write(postData);
	}
	
	private void delete(HttpURLConnection connection) throws ProtocolException{
	    connection.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded");
	    connection.setRequestMethod("DELETE");
	}
	
    public Response downloadFileByURL(String fileURL, String fileName) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = System.getProperty("user.dir") + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();

            httpConn.disconnect();
            return new Response(responseCode, "", String.format("File %s downloaded", fileName));
        } 
        httpConn.disconnect();
        return new Response(httpConn);
    }
    
    public Response uploadFileByURL(String url, String filePath) throws MalformedURLException, IOException, ExpiredURLException, UploadException {
    	
    	File binaryFile = new File(filePath);
    	String charset =  java.nio.charset.StandardCharsets.UTF_8.name();
    	HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    	connection.setDoOutput(true);
    	connection.setRequestMethod("PUT");
    	connection.setRequestProperty("Accept", "*/*");
    	connection.setRequestProperty("Expect", "100-continue");
    	
    	try (
    		    OutputStream output = connection.getOutputStream();
    		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
    		) {
    		    // Send binary file.
    		    Files.copy(binaryFile.toPath(), output);
    		    output.flush(); // Important before continuing with writer!
    		}
    	
    	//get all headers
    	Map<String, List<String>> map = connection.getHeaderFields();
    	//for (Map.Entry<String, List<String>> entry : map.entrySet()) {
    	//	System.out.println("Key : " + entry.getKey() +
    	//                 " ,Value : " + entry.getValue());
    	//}
    	
    	if (connection.getResponseCode() == 201 || connection.getResponseCode() == 200 ) {
	    	Response res = new Response(connection);
	    	//res.showResponse();
	    	return res;
    	} else if (connection.getResponseCode() == 403 ) {
    		throw new ExpiredURLException("Upload failed due to a lack of permission, probably your upload link isn't valid anymore.");
    	} else {
    		throw new UploadException(String.format("An unexpected error occurred during your upload of file: %s!", binaryFile.getName()));
    	}
    }
    
	/////////POD OPERATIONS////////
	
	public Response listPods() throws IOException {
		
		String s = String.format("http://%s:%d/pods", this.host, this.port);
		////System.out.println("URL is " + s);
		URL url = new URL(s); 
		
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		Response res = new Response(connection);
		return res;
	}
	
	public Response createPod(String postData) throws Exception {
		
		String s = String.format("http://%s:%d/pods", this.host, this.port);
		////System.out.println("URL is " + s);
		URL url = new URL(s); 
		
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		enableAuth(connection);		
		this.post(connection, postData);
		
		Response res = new Response(connection);
		return res;
	}
	
	public Response deletePod(String pod) throws Exception {
		
		Response getPeasResponse = this.getPeas(pod);
		
		if (getPeasResponse.code != 404) {
			
			if (getPeasResponse.code != 200) {
				return getPeasResponse;
			}
			TypeToken<Pea> theTypeToken = new TypeToken<Pea>() {};
			List<Pea> peas = Helpers.parseItems(getPeasResponse.json, "peas", theTypeToken);
			for (Pea pea : peas) {
			    deletePea(pea.pod, pea.name);
			}
		}
		
		String s = String.format("http://%s:%d/pods/%s", this.host, this.port, pod);
		URL url = new URL(s); 
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		Response getPodResponse = new Response(connection);
		
		if (getPodResponse.code != 200) {
			return getPodResponse;
		}
		
		String sDelete = String.format("http://%s:%d/pods?pod=%s", this.host, this.port, pod);
		URL urlDelete = new URL(sDelete); 
		HttpURLConnection connectionDelete=(HttpURLConnection) urlDelete.openConnection();
		enableAuth(connectionDelete);
		delete(connectionDelete);
		
		return new Response(connectionDelete);
	}
	
/////////PEA OPERATIONS////////
	
	public Response getPeas(String pod) throws IOException, JSONException {
	
	
	String s = String.format("http://%s:%d/peas/%s", this.host, this.port, pod);
	////System.out.println("URL is " + s);
	URL url = new URL(s); 
	
	HttpURLConnection connection=(HttpURLConnection) url.openConnection();
	connection.setRequestMethod("GET");

	return new Response(connection);
}

    public Response createPea(String postData) throws Exception {
	
	String s = String.format("http://%s:%d/peas", this.host, this.port);
	//System.out.println("URL is " + s);
	URL url = new URL(s); 
	HttpURLConnection connection=(HttpURLConnection) url.openConnection();
	enableAuth(connection);
	this.post(connection, postData);
	Response res = new Response(connection);
	return res;
}
    public Response deletePea(String pod, String pea) throws Exception {
    	
	String s = String.format("http://%s:%d/peas/%s/%s", this.host, this.port, pod, pea);
	////System.out.println("URL is " + s);
	URL url = new URL(s); 
	HttpURLConnection connection=(HttpURLConnection) url.openConnection();
	enableAuth(connection);
	connection.setRequestMethod("GET");

	if (connection.getResponseCode() >= 400) {
		return new Response(connection);
	}
	else {
		String json = Helpers.getJSON(connection.getInputStream());
		TypeToken<Pea> theTypeToken = new TypeToken<Pea>() {};
		List<Pea> peas = Helpers.parseItems(json, "peas", theTypeToken);
		Pea thePea = peas.get(0);
		
		String sDelete = String.format("http://%s:%s/peas?pea_id=%s", this.host, this.port, thePea.id);
		URL urlDelete = new URL(sDelete); 
		HttpURLConnection connectionDelete=(HttpURLConnection) urlDelete.openConnection();
		enableAuth(connectionDelete);
		delete(connectionDelete);
		
		Response res = new Response(connectionDelete);
		return res;
	}
}

	/////////ARTIFACTS OPERATIONS////////
	
	public Response getArtifacts(String pod, String pea) throws IOException, JSONException {
		
		
		String s = String.format("http://%s:%d/artifacts/%s/%s", this.host, this.port, pod, pea);
		URL url = new URL(s); 
		
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		enableAuth(connection);
		
		return new Response(connection);
	}
	
	public Response getArtifacts(String pod, String pea, String version) throws IOException, JSONException {
		
		//Authenticator.setDefault(new Helpers.MyAuthenticator());
		
		String s = String.format("http://%s:%d/artifacts/%s/%s/%s", this.host, this.port, pod, pea, version);
		URL url = new URL(s); 
		
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		enableAuth(connection);
		return new Response(connection);
	}
	
	public int getArtifactID(String pod, String pea, String version) throws IOException, JSONException {
		Response artifactResponse = getArtifacts(pod, pea, version);
		TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
		List<Artifact> artifacts = Helpers.parseItems(artifactResponse.json, "artifacts", theTypeToken);
		return artifacts.get(0).id;		
	}
	
	public Response downloadArtifact(String pod, String pea, String version) throws JSONException, IOException {
		
		Response getArtifactResponse = getArtifacts(pod, pea, version);
		if (getArtifactResponse.code != 200) {
			return getArtifactResponse;
		} 
		TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
		List<Artifact> artifacts = Helpers.parseItems(getArtifactResponse.json, "artifacts", theTypeToken);
		String fileKey = artifacts.get(0).filekey;
		
		String s = String.format("http://%s:%d/download/%s/%s/%s", this.host, this.port, pod, pea, version);
		URL url = new URL(s); 
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		String json = Helpers.getJSON(connection.getInputStream());
		Gson gson = new Gson();
		DownloadLink link =  gson.fromJson(json, DownloadLink.class);
		
		String[] parts = fileKey.split("/");
		String filename = parts[2];
		
		return this.downloadFileByURL(link.url, filename);
	}
	
	public Response uploadArtifact(String pod, String pea, String version, String filePath) throws Exception {
		
		File binaryFile = new File(filePath);
		
		if (!binaryFile.exists()) {
			throw new UploadException(String.format("File %s cannot be found", filePath));
		}
		String fileAbsolutePath = binaryFile.getAbsolutePath();
		String filename = binaryFile.getName();
		long size = binaryFile.length();
		
		// get upload URL
		String getUploadURLString = String.format("http://%s:%s/location/%s/%s/%s/%s", this.host, this.port, pod, pea, version, filename);
		URL url = new URL(getUploadURLString); 
		
		HttpURLConnection getUploadURLconnection=(HttpURLConnection) url.openConnection();
		getUploadURLconnection.setRequestMethod("GET");
		enableAuth(getUploadURLconnection);
		
		if (getUploadURLconnection.getResponseCode() != 200) {
			Response getUploadURLResponse = new Response(getUploadURLconnection);
			//getUploadURLResponse.showResponse();
			return getUploadURLResponse;
		}
		
		String json = Helpers.getJSON(getUploadURLconnection.getInputStream());
		Gson gson = new Gson();
		UploadURLResponse uploadURLResponseInstance =  gson.fromJson(json, UploadURLResponse.class);
		
		String uploadAWSURL = uploadURLResponseInstance.data; // This is the upload URL
		try {
			this.uploadFileByURL(uploadAWSURL, fileAbsolutePath);
		} catch (ExpiredURLException e) {
			throw e;
		} catch (UploadException e) {
			throw e;
		}
		
		//post metadata
		
		String postData = String.format("pod=%s&pea=%s&version=%s&filename=%s&size=%d", pod, pea, version, filename, size);
		String s = String.format("http://%s:%s/artifacts", this.host, this.port);
		////System.out.println("URL is " + s);
		URL uplodaMetadataURL = new URL(s); 
		HttpURLConnection connection=(HttpURLConnection) uplodaMetadataURL.openConnection();
		enableAuth(connection);
		this.post(connection, postData);
		Response uploadMetadataResponse = new Response(connection);
		if (uploadMetadataResponse.code != 201) {
			String[] parts = uploadAWSURL.split("?");
			String[] partsNames = parts[0].split("/");
			String bucketName = parts[1];
			String fileKey = partsNames[2] + partsNames[3] + partsNames[4];
			System.out.println("Errors during posting metada, rollbak:");
			System.out.println(String.format("Bucket name is: %s", bucketName));
			System.out.println(String.format("File key  is: %s", fileKey));
			// There should be some codes to delete the artifact in Amazon S3.
			// However, since Peapod API is not able to return a pre-signed URL for deleting,
			// there is currently no way to rollback. Instead, such an artifcat will be notified
			// in the peapod master jenkins instance.
		}
		return uploadMetadataResponse;
	}
	
	public Response deleteArtifact(String pod, String pea, String version) throws JSONException, IOException {
		
		Response getArtifactResponse = getArtifacts(pod, pea, version);
		if (getArtifactResponse.code != 200) {
			return getArtifactResponse;
		} 
		TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
		List<Artifact> artifacts = Helpers.parseItems(getArtifactResponse.json, "artifacts", theTypeToken);
		int fileID = artifacts.get(0).id;
		
		String sDelete = String.format("http://%s:%d/artifacts?artifact_id=%s", this.host, this.port, fileID);
		URL urlDelete = new URL(sDelete); 
		HttpURLConnection connectionDelete=(HttpURLConnection) urlDelete.openConnection();
		enableAuth(connectionDelete);
		delete(connectionDelete);
		
		Response res = new Response(connectionDelete);
		return res;
	}
	/////////TAG OPERATIONS////////
	
	public Response deleteTag(String pod, String pea, String version, String tag) throws IOException, JSONException {
		
		Response getArtifactResponse = getArtifacts(pod, pea, version);
		if (getArtifactResponse.code != 200) {
			return getArtifactResponse;
		} 
		TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
		List<Artifact> artifacts = Helpers.parseItems(getArtifactResponse.json, "artifacts", theTypeToken);
		int artifactID = artifacts.get(0).id;
		
		String sDelete = String.format("http://%s:%d/artifacts?artifact_id=%s&tag=%s", this.host, this.port, artifactID, tag);
		URL urlDelete = new URL(sDelete); 
		HttpURLConnection connectionDelete=(HttpURLConnection) urlDelete.openConnection();
		enableAuth(connectionDelete);
		delete(connectionDelete);
		
		Response res = new Response(connectionDelete);
		return res;
	}
	
	public Response createTag(String pod, String pea, String version, String tag) throws Exception {
		
		Response getArtifactResponse = getArtifacts(pod, pea, version);
		if (getArtifactResponse.code != 200) {
			return getArtifactResponse;
		} 
		TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
		List<Artifact> artifacts = Helpers.parseItems(getArtifactResponse.json, "artifacts", theTypeToken);
		int artifactID = artifacts.get(0).id;
		
		String postData = String.format("artifact_id=%d&tag=%s", artifactID, tag);
		String s = String.format("http://%s:%s/tags", this.host, this.port);
		////System.out.println("URL is " + s);
		URL createTagURL = new URL(s); 
		HttpURLConnection connection=(HttpURLConnection) createTagURL.openConnection();
		enableAuth(connection);
		post(connection, postData);
		
		Response res = new Response(connection);
		return res;
	}
	
	public Response deleteComment(int comment_ID) throws IOException, JSONException {		
		
		String sDelete = String.format("http://%s:%d/comments?comment_id=%d", this.host, this.port, comment_ID);
		URL urlDelete = new URL(sDelete); 
		HttpURLConnection connectionDelete=(HttpURLConnection) urlDelete.openConnection();
		enableAuth(connectionDelete);
		delete(connectionDelete);
		
		Response res = new Response(connectionDelete);
		return res;
	}
	
	public Response createComment(String pod, String pea, String version, String comment) throws Exception {
		
		Response getArtifactResponse = getArtifacts(pod, pea, version);
		if (getArtifactResponse.code != 200) {
			return getArtifactResponse;
		} 
		TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
		List<Artifact> artifacts = Helpers.parseItems(getArtifactResponse.json, "artifacts", theTypeToken);
		int artifactID = artifacts.get(0).id;
		
		String postData = String.format("artifact_id=%d&comment=%s", artifactID, comment);
		String s = String.format("http://%s:%s/comments", this.host, this.port);
		////System.out.println("URL is " + s);
		URL createCommentURL = new URL(s); 
		HttpURLConnection connection=(HttpURLConnection) createCommentURL.openConnection();
		enableAuth(connection);
		post(connection, postData);
		
		Response res = new Response(connection);
		return res;
	}
}
