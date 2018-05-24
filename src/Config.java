
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author victor
 */
public class Config implements Serializable {
    private String playerName1;
    private String playerName2;
    private int level;
    
    private static Config instance = null;
    
    private Config() {
        playerName1 = "NoName";
        playerName2 = "NoName";
        level = 0;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(
                    new FileInputStream("config.dat"));
            Config readInstance = (Config) input.readObject();
            playerName1 = readInstance.getPlayerName1();
            playerName2 = readInstance.getPlayerName2();
            level = readInstance.getLevel();
        } catch (IOException ex) {
            // Do nothing.
        } catch (ClassNotFoundException ex) {
            // Do nothing
        } finally{
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                }
                
            }
        }
    }
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getPlayerName1() {
        return playerName1;
    }
    public String getPlayerName2() {
        return playerName2;
    }

    public void setPlayerName1(String playerName) {
        this.playerName1 = playerName;
        save();
    }
     public void setPlayerName2(String playerName) {
        this.playerName2 = playerName;
        save();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        save();
    }

    public int getDeltaTime() {
        switch (level) {
            case 0: return 500;
            case 1: return 200;
            case 2: return 100;
            case 3: return 40;
        }
        return 500;
    }
    
    public void save() {
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream( 
                    new FileOutputStream("config.dat"));
            output.writeObject(this);
        } catch (IOException ex) {
            
        } finally{
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                }
                
            }
        }
        
                
    }
    
}
