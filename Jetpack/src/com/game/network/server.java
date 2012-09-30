package com.game.network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class server extends Thread {
	// Var init
	private JSONParser parser;
	private Socket echoSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	// Var fetchData
	private JSONObject jsonObject;
	private String[] playerArray;
	private String[] bulletsArray;
	private String[] gameArray;
	private String[] ennemies;
	private String[] ennemiesArrayA;
	private String[] ennemiesArrayB;
	private String[] ennemiesArrayC;
	private String[] ennemiesArrayD;
	private String[] ennemiesArrayE;
	private String[] ennemiesArrayF;
	private String[] ennemiesArrayG;
	private String[] ennemiesArrayH;
	private String[] ennemiesArrayI;
	private String[] ennemiesArrayJ;
	

	// Send data
	private JSONObject obj;
	private JSONArray listPlayer;
	private JSONArray listBullets;
	private JSONArray listGame;
	private JSONArray listEnnemies;

	// Connection & initialisation
	public void initConnection() {
		start();
	}

	@Override
	public void run() {
		System.out.println("Connecting..");
		echoSocket = null;
		out = null;
		in = null;

		parser = new JSONParser();

		try {
			echoSocket = new Socket("kepler.step.polymtl.ca", 8012);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			//System.err.println("Don't know about host: taranis.");
			//System.exit(1);
		} catch (IOException e) {
			//System.err.println("Couldn't get I/O for " + "the connection to: taranis.");
			//System.exit(1);
		}

		//System.out.println("Connected.");

		///System.out.println("Fetching");
		String input = "";

		obj = new JSONObject();

		try {
			while ((input = in.readLine()) != null) {
				Object obj = (JSONObject) parser.parse(input);
				jsonObject = (JSONObject) obj;
				//System.out.println("Fetching123");
				/*
				 * - PLAYER x y angle score
				 */

				playerArray = new String[4];
				
				try {
					JSONArray player = (JSONArray) jsonObject.get("player");
					Iterator<String> iterator = player.iterator();
	
					for (int i = 0; iterator.hasNext(); i++) {
						playerArray[i] = String.valueOf(iterator.next());
					}
				}catch (Exception e){
					
				}
				/*
				 * - Bullets x y angle type avail
				 */

				bulletsArray = new String[5];
				try{
					JSONArray bullets = (JSONArray) jsonObject.get("bullets");
					Iterator<String> iteratorBullets = bullets.iterator();
	
					for (int i = 0; iteratorBullets.hasNext(); i++) {
						bulletsArray[i] = String.valueOf(iteratorBullets.next());
					}
				}catch (Exception e){
				
				}
				/*
				 * - Game score1
				 */

				gameArray = new String[4];

				try {
					JSONArray game = (JSONArray) jsonObject.get("game");
					Iterator<String> iteratorGame = game.iterator();

					for (int i = 0; iteratorGame.hasNext(); i++) {
						gameArray[i] = String.valueOf(iteratorGame.next());
					}
				} catch (Exception e) {
					// throw up
				}

				/*
				 * - Ennemies x y angle type
				 */

				ennemiesArrayA = new String[2];
				ennemiesArrayB = new String[2];
				ennemiesArrayC = new String[2];
				ennemiesArrayD = new String[2];
				ennemiesArrayE = new String[2];
				ennemiesArrayF = new String[2];
				ennemiesArrayG = new String[2];
				ennemiesArrayH = new String[2];
				ennemiesArrayI = new String[2];
				ennemiesArrayJ = new String[2];
				

				try {
					JSONArray ennemiesA = (JSONArray) jsonObject.get("a");
					JSONArray ennemiesB = (JSONArray) jsonObject.get("b");
					JSONArray ennemiesC = (JSONArray) jsonObject.get("c");
					JSONArray ennemiesD = (JSONArray) jsonObject.get("d");

					
					ennemiesArrayA[0] = String.valueOf(ennemiesA.get(0));
					ennemiesArrayA[1] = String.valueOf(ennemiesA.get(1));
					
					ennemiesArrayB[0] = String.valueOf(ennemiesB.get(0));
					ennemiesArrayB[1] = String.valueOf(ennemiesB.get(1));
					
					ennemiesArrayC[0] = String.valueOf(ennemiesC.get(0));
					ennemiesArrayC[1] = String.valueOf(ennemiesC.get(1));
					
					ennemiesArrayD[0] = String.valueOf(ennemiesD.get(0));
					ennemiesArrayD[1] = String.valueOf(ennemiesD.get(1));
					
					//System.out.println(ennemiesArrayA[0]);
					//System.out.println(ennemiesArrayB[0]);
					//System.out.println(ennemiesArrayC[0]);
					//System.out.println(ennemiesArrayD[0]);					
					
				} catch (Exception e) {
					// throw up
				}
//				// /-------------- TO BE DELETED ---------
//				// Test Player
//				System.out.println("PlayerX: " + getPlayerInfo("x"));
//				System.out.println("PlayerY: " + getPlayerInfo("y"));
//				System.out.println("PlayerAngle: " + getPlayerInfo("angle"));
//				System.out.println("PlayerType: " + getPlayerInfo("type"));
//
//				// Test Bullets
//				System.out.println("BulletX: " + getBulletInfo("x"));
//				System.out.println("BulletY: " + getBulletInfo("y"));
//				System.out.println("BulletAngle: " + getBulletInfo("angle"));
//				System.out.println("BulletType: " + getBulletInfo("type"));
//
//				setPlayerData("2", "2", "2", "2");
//				setBulletData("1", "1", "1", "1", "1");
//				sendData();

			}

		} catch (ParseException e) {

		} catch (IOException e) {

		}

	}

	/*
	 * ----------- PLAYER GETTERS --------------- If the requested data isn't
	 * available, returns NULL Float playerY = Float.parseFloat(playerArray[1]);
	 */

	public String getPlayerInfo(String key) {
		int caseValue;

		if (key.equals("x"))
			caseValue = 0;
		else if (key.equals("y"))
			caseValue = 1;
		else if (key.equals("angle"))
			caseValue = 2;
		else if (key.equals("type"))
			caseValue = 3;
		else if (key.equals("avail"))
			caseValue = 4;
		else
			return "null";

		return playerArray[caseValue];
	}

	/*
	 * ----------- Bullet GETTERS --------------- If the requested data isn't
	 * available, returns NULL
	 */

	public String getBulletInfo(String key) {
		int caseValue;

		if (key.equals("x"))
			caseValue = 0;
		else if (key.equals("y"))
			caseValue = 1;
		else if (key.equals("angle"))
			caseValue = 2;
		else if (key.equals("type"))
			caseValue = 3;
		else if (key.equals("avail"))
			caseValue = 4;
		else
			return "null";

		return bulletsArray[caseValue];
	}

	/*
	 * ----------- Game GETTERS --------------- If the requested data isn't
	 * available, returns NULL
	 */

	public String getGameInfo(String key) {
		int caseValue = 0;

		// if(key.equals("x")) caseValue = 0;
		// else if(key.equals("y")) caseValue = 1;
		// else return "null";

		return gameArray[caseValue];
	}

	/*
	 * ----------- Ennemies GETTERS --------------- If the requested data isn't
	 * available, returns NULL
	 */

	public String getEnnemiesInfo(int key, String val) {
		int caseValue;
		
		if(key == 0 && val.equals("x")) return ennemiesArrayA[0];
		else if(key == 0 && val.equals("y")) return ennemiesArrayA[1];
		
		else if(key == 1 && val.equals("x")) return ennemiesArrayB[0];
		else if(key == 1 && val.equals("y")) return ennemiesArrayB[1];

		else if(key == 2 && val.equals("x")) return ennemiesArrayC[0];
		else if(key == 2 && val.equals("y")) return ennemiesArrayC[1];
		
		else if(key == 3 && val.equals("x")) return ennemiesArrayD[0];
		else if(key == 3 && val.equals("y")) return ennemiesArrayD[1];
		
		else return null;
	}
	/*
	 * 
	 * ----------- SETTERS ----------------
	 */

	public void setPlayerData(String x, String y, String angle, String type) {
		listPlayer = new JSONArray();

		listPlayer.add(x);
		listPlayer.add(y);
		listPlayer.add(angle);
		listPlayer.add(type);

		obj.put("player", listPlayer);
	}

	public void setBulletData(String x, String y, String angle, String type, String avail) {
		listBullets = new JSONArray();

		listBullets.add(x);
		listBullets.add(y);
		listBullets.add(angle);
		listBullets.add(type);
		listBullets.add(avail);

		obj.put("bullets", listBullets);
	}

	public void setGameData(String p1Score, String p2Score) {
		
	}

	// Reste null a moins que dead
	public void setEnnemiesData(String x) {

	}

	//
	public void sendData() {
		out.println(obj);

		// Reinit objet
		obj = new JSONObject();
	}

	public void endConnection() {
		try {
			out.close();
			in.close();
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// /------------------
}