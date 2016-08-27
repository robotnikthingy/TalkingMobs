package robotnikman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class TalkingMobs extends JavaPlugin{

    private TalkingMobs plugin = this;	
    private File configf;
    
    public int ChanceOfSpeaking = getConfig().getInt("chance_of_text");
    public int ChanceOfAISpeaking = getConfig().getInt("chance_of_ai_response");
    public boolean IsAIEnable = getConfig().getBoolean("enable_ai_response", true);
    public int lenght_of_response = getConfig().getInt("lenght_of_response", 30);
    public boolean talk_only_when_attacked = getConfig().getBoolean("talk_only_when_attacked", false);
    public boolean change_response_on_attack = getConfig().getBoolean("change_response_on_attack", true);
    public boolean enable_context = getConfig().getBoolean("enable_context", true);
    public int chance_of_using_context = getConfig().getInt("chance_of_using_context", 90);
    public int chance_of_ai_response_attacked = getConfig().getInt("chance_of_ai_response_attacked", 90);
    public ArrayList<String> AttackContexts = (ArrayList<String>) getConfig().getStringList("attacked");
    public ArrayList<String> DeathContexts = (ArrayList<String>) getConfig().getStringList("death");
    
    
    private FileConfiguration config;    
    
	public File NamesFile;
    public ArrayList<String> NamesList = new ArrayList<String>();
    
    @Override
    public void onEnable(){
    	
    	//command
    	//this.getCommand("kit").setExecutor(new Command(this));
    	
    	//see if texts file exists
        try {
			createFiles();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NamesFile = new File(this.getDataFolder(), "texts.txt");
		
		
        //read names from file into memory

    	// New BufferedReader.
    	BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(NamesFile.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
    	// Add all lines from file to ArrayList.
    	while (true) {
    	    String line = "";
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    if (line == null) {
    		break;
    	    }
    	    NamesList.add(line);
    	}

    	// Close it.
    	try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	
    	//listener
    	getServer().getPluginManager().registerEvents(new MobSpawnListener(plugin), this);
    	
    }
    
    
    void createFiles() throws InvalidConfigurationException {

        configf = new File(getDataFolder(), "config.yml");
        NamesFile = new File(this.getDataFolder(), "texts.txt");
        config = new YamlConfiguration();

        
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            copy(getResource("config.yml"), configf);
        }
        
        if (!NamesFile.exists()){
            copy(getResource("texts.txt"), NamesFile);
        }


        try {
            config.load(configf);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void debug(String message){
    	boolean debug = getConfig().getBoolean("debug");
    	if(debug == true){
    		getLogger().info(message);
    	}
    }
}
