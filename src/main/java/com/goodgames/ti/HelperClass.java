package com.goodgames.ti;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;


class Artifact {
	
	int created_sec;
	ArrayList<Tag> tags;
	int id;
	int size;
	String scm_id;
	String created;
	String filekey;
	ArrayList<Comment> comments;
	String pea_id;
	String version;
	String[] values;
	
}

class Tag {
	String tag;
	int id;
}

class Comment {
	String content;
	String created;
	String user;
	int id;
}

class Pea {
	
	String pod;
	String description;
	String name;
	int id;
	
}

class Pod {
	
	String bucket_name;
	String description;
	String name;	
}

class DownloadLink {	
	String url;	
}

class UploadURLResponse {
	String message;
	String data; 
}

class Response {
	
	int code;
	String message;
	String json;
	
	public Response(HttpURLConnection connection) throws IOException {
		this.code = connection.getResponseCode();
		this.message = connection.getResponseMessage();
		if (this.code >= 400) { 
			// if code = 4xx or 5xx, call getInputStream will
			// produce java.io.FileNotFoundException.
			this.json = Helpers.getJSON(connection.getErrorStream());
		} else {
			this.json = Helpers.getJSON(connection.getInputStream());
		};
	}
	
	public Response(int code, String message, String json) throws IOException {
		this.code = code;
		this.message = message;
		this.json = json;
	}
	
	public void showResponse() {
		System.out.println("Service returned response code " + this.code);
		System.out.println("Service returned response message " + this.message);
		System.out.println("Service returned JSON: " + this.json);
	}
}


//Exceptions

class ExpiredURLException extends Exception {
	public ExpiredURLException(String message) {
        super(message);
    }
}

class UploadException extends Exception {
    public UploadException(String message) {
        super(message);
    }
}

// Credentials

class Credentials {
	String name;
	String token;
}

