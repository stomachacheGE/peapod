package com.goodgames.ti;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Peapod {

	private APICaller apiCaller;
	
	public Peapod(String name, String token) {
	 	this.apiCaller = new APICaller(name, token);
	}
	
	private void listPods() throws IOException, JSONException {
		Response response = this.apiCaller.listPods();
		if ((response.code - 200) < 100) {
			TypeToken<Pod> theTypeToken = new TypeToken<Pod>() {};
			List<Pod> pods = Helpers.parseItems(response.json, "pods", theTypeToken);
			//res.showResponse();
			System.out.println(String.format("Found %d pods:", pods.size()));
			for (Pod pod : pods) {
			    Gson gson = new Gson();
				String json = gson.toJson(pod);
				System.out.println(json);
			}
		} else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void createPod(String podName) throws Exception {
		//String body = String.format("{\"pod\":\"%s\", \"description\":\"\"}", podName);
		String body = String.format("pod=%s", podName);
		Response response = this.apiCaller.createPod(body);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void deletePod(String podName) throws Exception {
		//String body = String.format("{\"pod\":\"%s\", \"description\":\"\"}", podName);
		//String body = String.format("pod=%s&name=%s&description=", podName, peaName);
		//System.out.println(body);
		Response response = this.apiCaller.deletePod(podName);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
			System.out.println(String.format("Pod \"%s\" successfully deleted.", podName));
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void listPeas(String podName) throws IOException, JSONException {
		//String body = String.format("{\"pod\":\"%s\", \"description\":\"\"}", podName);
		//String body = String.format("pod=%s", podName);
		//System.out.println(body);
		Response response = this.apiCaller.getPeas(podName);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			TypeToken<Pea> theTypeToken = new TypeToken<Pea>() {};
			List<Pea> peas = Helpers.parseItems(response.json, "peas", theTypeToken);
			//res.showResponse();
			System.out.println(String.format("Found %d peas:", peas.size()));
			for (Pea pea : peas) {
			    Gson gson = new Gson();
				String json = gson.toJson(pea);
				System.out.println(json);
			}
		} else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
		
	}
	
	private void createPea(String podName, String peaName) throws Exception {
		//String body = String.format("{\"pod\":\"%s\", \"description\":\"\"}", podName);
		String body = String.format("pod=%s&name=%s&description=", podName, peaName);
		Response response = this.apiCaller.createPea(body);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.println(response.json);
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void createPea(String podName, String peaName, String description) throws Exception {
		//String body = String.format("{\"pod\":\"%s\", \"description\":\"\"}", podName);
		String body = String.format("pod=%s&name=%s&description=%s", podName, peaName, description);
		Response response = this.apiCaller.createPea(body);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void deletePea(String podName, String peaName) throws Exception {
		//String body = String.format("{\"pod\":\"%s\", \"description\":\"\"}", podName);
		//String body = String.format("pod=%s&name=%s&description=", podName, peaName);
		//System.out.println(body);
		Response response = this.apiCaller.deletePea(podName, peaName);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
			System.out.println(String.format("Pea \"%s\" successfully deleted.", peaName));
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void getArtifacts(String podName, String peaName, String version) throws Exception {
		Response response = this.apiCaller.getArtifacts(podName, peaName, version);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
			List<Artifact> artifacts = Helpers.parseItems(response.json, "artifacts", theTypeToken);
			for (Artifact artifact : artifacts) {
				Gson gson = new Gson();
				String json = gson.toJson(artifact);
				System.out.println(json);
			}
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void getArtifacts(String podName, String peaName) throws Exception {
		Response response = this.apiCaller.getArtifacts(podName, peaName);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			TypeToken<Artifact> theTypeToken = new TypeToken<Artifact>() {};
			List<Artifact> artifacts = Helpers.parseItems(response.json, "artifacts", theTypeToken);
			System.out.println(String.format("Found %d artifacts for Pea <%s> in Pod <%s>", artifacts.size(), peaName, podName));
			for (Artifact artifact : artifacts) {
				Gson gson = new Gson();
				String json = gson.toJson(artifact);
				System.out.println(json);
			}
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	
	private void downloadArtifact(String podName, String peaName, String version) throws Exception {
		Response response = this.apiCaller.downloadArtifact(podName, peaName, version);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.println(response.json);
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void uploadArtifact(String podName, String peaName, String version, String filePath) throws Exception {
		Response response = this.apiCaller.uploadArtifact(podName, peaName, version, filePath);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.println(response.json);
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void deleteArtifact(String podName, String peaName, String version) throws Exception {
		Response response = this.apiCaller.deleteArtifact(podName, peaName, version);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.println(response.json);
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
		
	private void createTag(String podName, String peaName, String version, String tag) throws Exception {
		Response response = this.apiCaller.createTag(podName, peaName, version, tag);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
			System.out.println(String.format("Tag \"%s\" successfully created.", tag));
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void deleteTag(String podName, String peaName, String version, String tag) throws IOException, JSONException {
		Response response = this.apiCaller.deleteTag(podName, peaName, version, tag);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
			System.out.println(String.format("Tag \"%s\" successfully deleted.", tag));
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}

	private void createComment(String podName, String peaName, String version, String comment) throws Exception {
		Response response = this.apiCaller.createComment(podName, peaName, version, comment);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
			System.out.println(String.format("Comment \"%s\" successfully created.", comment));
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private void deleteComment(int commentID) throws IOException, JSONException {
		Response response = this.apiCaller.deleteComment(commentID);
		//2xx HTTP response code represents success
		if ((response.code - 200) < 100) {
			System.out.print(response.json);
			System.out.println(String.format("Comment with \"%d\" successfully deleted.", commentID));
		}
		else {
			Gson gson = new Gson();
			String json = gson.toJson(response);
			System.out.println(json);
		}
	}
	
	private static void showHelp() {
		System.out.println();
		System.out.println("Set Credentials:");
		System.out.println("java -jar peapod.jar -setCredentials <user>:<token>");
		System.out.println();
		System.out.println("Pods Operations:");
		System.out.println("java -jar peapod.jar -listPods");
		System.out.println("java -jar peapod.jar -createPod <podName>");
		System.out.println("java -jar peapod.jar -deletePod <podName>");
		System.out.println();
		System.out.println("Peas Operations:");
		System.out.println("java -jar peapod.jar -listPeas <podName>");
		System.out.println("java -jar peapod.jar -createPea <podName>:<peaName> [-description <description>]");
		System.out.println("java -jar peapod.jar -deletePea <podName>:<peaName>");
		System.out.println();
		System.out.println("Aritifacts Operations:");
		System.out.println("java -jar peapod.jar -getArtifact <podName>:<peaName>:<version>");
		System.out.println("java -jar peapod.jar -downloadArtifact <podName>:<peaName>:<version>");
		System.out.println("java -jar peapod.jar -uploadArtifact <podName>:<peaName>:<version> -filepath <filepath>");
		System.out.println("java -jar Peapod.jar -deleteArtifact <podName>:<peaName>:<version>");
		System.out.println();
		System.out.println("Tag Operations:");
		System.out.println("java -jar peapod.jar -createTag <podName>:<peaName>:<version> -tag <tag>");
		System.out.println("java -jar peapod.jar -deleteTag <podName>:<peaName>:<version> -tag <tag>");
		System.out.println();
		System.out.println("Comment Operations:");
		System.out.println("java -jar peapod.jar -createComment <podName>:<peaName>:<version> -comment <comment>");
		System.out.println("java -jar peapod.jar -deleteComment <commentID>");
		
	}
	
	private static Credentials getCredentials() throws IOException{


	    //to load application's properties, we use this class
	    Properties mainProperties = new Properties();

	    FileInputStream file;

	    //the base folder is ./, the root of the main.properties file  
	    String path = "./peapod.credentials";

	    //load the file handle for main.properties
	    file = new FileInputStream(path);

	    //load all the properties from this file
	    mainProperties.load(file);

	    //we have loaded the properties, so close the file handle
	    file.close();

	    //retrieve the property we are intrested, the app.version
	    Credentials credential = new Credentials();
	    
	    credential.name = mainProperties.getProperty("user");
	    credential.token = mainProperties.getProperty("token");
	    return credential;
	}

	private static void setCredentials(String user, String token) throws IOException {
		List<String> lines = new ArrayList<String>();
		lines.add(String.format("user=%s", user));
		lines.add(String.format("token=%s", token));
		Path file = Paths.get("./peapod.credentials");
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	public static void main(String[] args) throws Exception {
		
		String method = null;
		
		if (args.length > 0) {
			method = args[0];
		} else {
			System.out.println("No operation provided. Run with option \"-h\" or \"-help\" for usage.");
			System.exit(1);
		}
		
		if (method.equals("-setCredentials")){
			if (args.length < 2) {
				System.out.println("You must give your credentials!");
				System.out.println("Usage: java -jar Peapod.jar -setCredentials <username>:<token>");
				System.exit(1);
			} else if (args.length == 2) {
				String[] parts = args[1].split(":");
				if (parts.length == 2) {
					Peapod.setCredentials(parts[0], parts[1]);
					System.exit(0);
				} else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -setCredentials <username>:<token>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -setCredentials <username>:<token>");
				System.exit(1);
			}
		} else if (method.equals("-h") || method.equals("-help")) {
			Peapod.showHelp();
			System.exit(0);
		} else {
			;
		}
		
		String name = null;
		String token = null;
		
		try {
			Credentials credential = getCredentials();
			name = credential.name;
			token = credential.token;
		} catch (IOException ios) {
			if((System.getProperty("user")!=null) && (System.getProperty("token")!=null)) {
				name = System.getProperty("user");
				token = System.getProperty("token");
			} else {
				System.out.println("ERROR! No peapod credentials provided. You have 2 options to set credentials:");
				System.out.println("1 - set credentials so that a property file will be created:");
				System.out.println("java -jar peapod.jar -setCredentials <user>:<token>");
				System.out.println("2 - provide credentials when you run commands:");	
				System.out.println("java -Duser=<user> -Dtoken=<token> -jar peapod.jar -<operation>");
				System.exit(1);
			}
		}
		
		//String name = "jenkins";
		//String token = "CHGXKQTZOXSMRJKESATGDMXHVBQWVBFMOTJRIGLRRJMTWFWZBIZPFFKLIBXAGION";
				
		Peapod client = new Peapod(name, token);
		
		
		//name = System.getProperty("name");
		//System.out.println(name);
		
		switch(method) {
		
		default:			
			System.out.println("Invalid Arguments. Run with option \"-h\" or \"-help\" for usage.");
			break;
		
		case "-h": case "-help":
			Peapod.showHelp();
			break;
		
			
		case "-listPods": 
			client.listPods();
			break;
	    
		case "-createPod":
			
			if (args.length < 2) {
				System.out.println("You must give a pod name!");
				System.out.println("Usage: java -jar Peapod.jar -createPod <podName>");
				System.exit(1);
			} else if (args.length == 2) {
				client.createPod(args[1]);
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -createPod <podName>");
			}
			break;
		
		case "-deletePod":
			
			if (args.length < 2) {
				System.out.println("You must give at least a pod name!");
				System.out.println("Usage: java -jar Peapod.jar -deletePod <podName>");
				System.exit(1);
			} else if (args.length == 2) {
				client.deletePod(args[1]);
			}else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -deletePod <podName>");
			}
			break;
	    
		case "-listPeas":
			
			if (args.length < 2) {
				System.out.println("You must give a pod name!");
				System.out.println("Usage: java -jar Peapod.jar -listPeas <podName>");
				System.exit(1);
			} else if (args.length == 2) {
				client.listPeas(args[1]);
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -listPeas <podName>");
			}
			break;
			
		case "-createPea":
			
			if (!(args.length == 2 || args.length == 4)) {
				System.out.println("You must give at least a pod name and a pea name! Description is optional.");
				System.out.println("Usage: java -jar Peapod.jar -createPea <podName>:<peaName>");
				System.out.println("       java -jar Peapod.jar -createPea <podName>:<peaName> -description <description>");
				System.exit(1);
			} else {
				String[] parts = args[1].split(":");
				if (parts.length==2 && args.length==2) {
					client.createPea(parts[0], parts[1]);
				} else if (parts.length == 2 && args.length == 4) {
					if (args[2].equals("-description")) {
						client.createPea(parts[0], parts[1], args[3]);
					}
					else {
						System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
						System.out.println("Usage: java -jar Peapod.jar -createPea <podName>:<peaName>");
						System.out.println("       java -jar Peapod.jar -createPea <podName>:<peaName> -description <description>");
						System.exit(1);
					}
				} else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -createPea <podName>:<peaName>");
					System.out.println("       java -jar Peapod.jar -createPea <podName>:<peaName> -description <description>");
					System.exit(1);
				}
			}
			break;
			
		case "-deletePea":
			
			if (args.length < 2) {
				System.out.println("You must give at least a pod name and a pea name!");
				System.out.println("Usage: java -jar Peapod.jar -deletePea <podName>:<peaName>");
				System.exit(1);
			} else if (args.length == 2) {
				String[] parts = args[1].split(":");
				if (parts.length == 2) {
					client.deletePea(parts[0], parts[1]);
				} else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -deletePea <podName>:<peaName>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -deletePea <podName>:<peaName>");
				System.exit(1);
			}
			break;
		
		case "-getArtifact":
		
			if (args.length < 2) {
				System.out.println("You must give pod name, pea name! Version is optional");
				System.out.println("Usage: java -jar Peapod.jar -getArtifact <podName>:<peaName>:<version>");
				System.exit(1);
			} else if (args.length == 2) {
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.getArtifacts(parts[0], parts[1], parts[2]);
				}else if (parts.length == 2) {
					client.getArtifacts(parts[0], parts[1]);
				}else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("You must give pod name, pea name! Version is optional");
					System.out.println("Usage: java -jar Peapod.jar -getArtifact <podName>:<peaName>:<version>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -getArtifact <podName>:<peaName>:<version>");
				System.exit(1);
			}
			break;
			
		case "-downloadArtifact":
			
			if (args.length < 2) {
				System.out.println("You must give pod name, pea name and version!");
				System.out.println("Usage: java -jar Peapod.jar -downloadArtifact <podName>:<peaName>:<version>");
				System.exit(1);
			} else if (args.length == 2){
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.downloadArtifact(parts[0], parts[1], parts[2]);
				} else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -downloadArtifact <podName>:<peaName>:<version>");
					System.exit(1);
				} 
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -downloadArtifact <podName>:<peaName>:<version>");
				System.exit(1);
			}
			break;
			
		case "-uploadArtifact":
			
			if (args.length < 4) {
				System.out.println("You must give pod name, pea name, version and filepath.");
				System.out.println("Usage: java -jar Peapod.jar -uploadArtifact <podName>:<peaName>:<version> -filepath <filepath>");
				System.exit(1);
			} else if (args.length == 4 && args[2].equals("-filepath")) {
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.uploadArtifact(parts[0], parts[1], parts[2], args[3]);
				} else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -uploadArtifact <podName>:<peaName>:<version> -filepath <filepath>");
					System.exit(1);
				} 		    
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[4]));
				System.out.println("Usage: java -jar Peapod.jar -uploadArtifact <podName>:<peaName>:<version> -filepath <filepath>");
				System.exit(1);
			}
			break;
			
		case "-deleteArtifact":
			
			if (args.length < 2) {
				System.out.println("You must give pod name, pea name and version!");
				System.out.println("Usage: java -jar Peapod.jar -deleteArtifact <podName>:<peaName>:<version>");
				System.exit(1);
			} else if (args.length == 2) {
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.deleteArtifact(parts[0], parts[1], parts[2]);
				} else {
					System.out.println("Invalid arguments! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -deleteArtifact <podName>:<peaName>:<version>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -deleteArtifact <podName>:<peaName>:<version>");
				System.exit(1);
			}
			break;
		
		case "-createTag":
			
			if (args.length < 4) {
				System.out.println("You must give pod name, pea name, version and tag.");
				System.out.println("Usage: java -jar Peapod.jar -createTag <podName>:<peaName>:<version> -tag <tag>");
				System.exit(1);
			} else if (args.length ==4 && args[2].equals("-tag")) {
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.createTag(parts[0], parts[1], parts[2], args[3]);
				} else {
					System.out.println("Invalid argument! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -createTag <podName>:<peaName>:<version> -tag <tag>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -createTag <podName>:<peaName>:<version> -tag <tag>");
				System.exit(1);
			}
			break;
		
		case "-deleteTag":
			
			if (args.length < 4) {
				System.out.println("You must give pod name, pea name, version and tag.");
				System.out.println("Usage: java -jar Peapod.jar -deleteTag <podName>:<peaName>:<version> -tag <tag>");
				System.exit(1);
			} else if (args.length ==4 && args[2].equals("-tag")) {
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.deleteTag(parts[0], parts[1], parts[2], args[3]);
				} else {
					System.out.println("Invalid argument! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -deleteTag <podName>:<peaName>:<version> -tag <tag>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -deleteTag <podName>:<peaName>:<version> -tag <tag>");
				System.exit(1);
			}
			break;
		
		case "-createComment":
			
			if (args.length < 4) {
				System.out.println("You must give pod name, pea name, version and comment.");
				System.out.println("Usage: java -jar Peapod.jar -createComment <podName>:<peaName>:<version> -comment <comment>");
				System.exit(1);
			} else if (args.length ==4 && args[2].equals("-comment")) {
				String[] parts = args[1].split(":");
				if (parts.length == 3) {
					client.createComment(parts[0], parts[1], parts[2], args[3]);
				} else {
					System.out.println("Invalid argument! Please check.");
					System.out.println("Usage: java -jar Peapod.jar -createComment <podName>:<peaName>:<version> -comment <comment>");
					System.exit(1);
				}
			} else {
				System.out.println(String.format("Invalid argument \"%s\"! Please check.", args[2]));
				System.out.println("Usage: java -jar Peapod.jar -createComment <podName>:<peaName>:<version> -comment <comment>");
				System.exit(1);
			}
			break;
		
		case "-deleteComment":
			
			if (args.length < 2) {
				System.out.println("You must give a comment ID.");
				System.out.println("Usage: java -jar Peapod.jar -deleteComment <commentID>");
				System.exit(1);
			} else if (args.length == 2) {
			    client.deleteComment(Integer.parseInt(args[1]));
			} else {
				System.out.println("Invalid argument! Please check.");
				System.out.println("Usage: java -jar Peapod.jar -deleteComment <commentID>");
				System.exit(1);
			}
			break;
			
		}
		
		//client.getArtifacts(pod, pea);
	
	}
	
}
