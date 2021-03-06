import java.net.*;
import java.io.*;
import java.util.*;

public class Connection implements Runnable {

    private static HashMap<String, Connection> connections = new HashMap<String, Connection>();
    private static int readyCount = 0;

    private Socket conn;
    private DataOutputStream out;
    private DataInputStream in;
    private PazerfausterServer server;
    private String username;
    private String ipAddress;
    private int port;
    private boolean isReady = false;

    public Connection (
            Socket conn,
            DataOutputStream out,
            DataInputStream in,
            PazerfausterServer server
        ) {

        this.conn = conn;
        this.out = out;
        this.in = in;
        this.server = server;
    }

    public void run() {

        try{
            // Getting username from client
            this.username = in.readUTF();
        }catch(IOException e){
            // Handle connection issue
            return;
        }
        catch(Exception e) {

        }

        // Getting username from client
        System.out.println(username + " has connected");

        // Check if username exists in the server
        if(connections.containsKey(username)) {
            try{
                out.writeUTF("?fail");
            } catch(Exception e) {}

            return;
        }


        // Username does not exist in server
        try{
            out.writeUTF("?success");
        } catch(Exception e) {}


        // At this point,
        this.broadcast(this.username + " has connected.\n");
        connections.put(username, this); //Eligible to receive broadcasts
        Connection.printConnectedUsers();

        // Main listening for inputs
        String msg = null;
        while(conn.isConnected()) {
            try {
                msg = in.readUTF();
            }
            catch(Exception e) {
                // Error in connection
                System.out.println(this.username + " has disconnected");
                Connection.removeConnection(this.username);
                this.broadcast(this.username + " has disconnected.\n");
                Connection.printConnectedUsers();
                break;
            }

            // If users are ready;
            if(msg.equals("?ready") && !this.isReady) {
                System.out.println(this.username + " is ready.");
                this.isReady = true;
                readyCount++;
                if(readyCount == 2) {
                    this.broadcast("?start");
                    GameState.startUDPServer();
                }
                continue;
            }

            this.broadcast(this.username + ": " + msg + "\n");
        }

    }

    public void write(String message) {
        try {
            out.writeUTF(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        // Send messages to out
        try {
            for(Iterator ite=connections.keySet().iterator();ite.hasNext();){
                String name=(String)ite.next();
                Connection c = (Connection)connections.get(name);
                c.write(message);
            }
        }
        catch (Exception e) {
            System.out.println("Error in broadcasting");
            e.printStackTrace();
        }
    }

    public static HashMap<String, Connection> getConnections(){
        return connections;
    }

    public static int getNumOfConnections(){
        return connections.size();
    }
    public Socket getSocket(){
        return conn;
    }

    private String getUsername(){
        return this.username;
    }

    public static void removeConnection(String s){
        if(connections.get(s).isReady()) {
            readyCount--;
        }

        connections.remove(s);
    }

    public boolean isReady() {
        return this.isReady;
    }

    public static void printConnectedUsers() {
        int i = 1;

        System.out.println("Connected users: " + connections.size());

        for(Iterator ite=connections.keySet().iterator();ite.hasNext();){
            String name=(String)ite.next();
            System.out.println((i++) +") " + name);
        }
    }
}
