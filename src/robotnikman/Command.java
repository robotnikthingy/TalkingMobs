package robotnikman;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor{

	private TalkingMobs plugin;	
	
	public Command(TalkingMobs plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String arg2, String[] arg3) {
		
		Player player = (Player) sender;
		
		plugin.reloadConfig();
		try {
			plugin.createFiles();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		player.sendMessage("Talking Mobs Reloaded!");
		
		return true;
	}

}
