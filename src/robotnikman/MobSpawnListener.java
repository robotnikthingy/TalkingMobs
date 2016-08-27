package robotnikman;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import Robotnikman.AICore;
import Robotnikman.ai_API;

import JMegaHal.JMegaHAL;

public class MobSpawnListener implements Listener{

	private TalkingMobs plugin;
	private Random rand = new Random();
	private Random randomchance = new Random();
	private Random messagesplitrand = new Random();
	AICore aicore = (AICore)Bukkit.getPluginManager().getPlugin("AI-Core");

	public MobSpawnListener (TalkingMobs plugin){
		this.plugin = plugin;
	}




	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		//chance out of 100
		int Chance = randomchance.nextInt(100);
		int AIChance = randomchance.nextInt(100);
		int StringLength = plugin.lenght_of_response;

		//plugin.getLogger().info("CreatureSpawnEvent");
		LivingEntity spawnedlivingentity = event.getEntity();
		Entity spawnedentity = event.getEntity();
		String MobName = null;
		
		if(plugin.UseMobNameAsSeed == true){
			MobName = spawnedentity.getName();
		}
		//		EntityType spawnedentitytype = event.getEntityType();

		if (spawnedlivingentity instanceof Monster){
			if(Chance <= plugin.ChanceOfSpeaking && plugin.ChanceOfSpeaking != 0) {
				if(plugin.IsAIEnable == true && AIChance <= plugin.ChanceOfAISpeaking){
					plugin.debug("mob will spawn with AI message");
					MobName = aicore.GenerateSentence();
					plugin.debug("AI generated message is: " + MobName);
				}else{
					plugin.debug("mob will spawn with message from message file");
					MobName = plugin.NamesList.get(rand.nextInt(plugin.NamesList.size()));
					plugin.debug("Message from file is: " + MobName);
				}

				//see if we need to trim the string
				if (StringLength != -1){
					plugin.debug("Mob sentence will be shortned. Before being shortened: " + MobName);
					MobName = MobName.substring(0, Math.min(MobName.length(), StringLength));
					plugin.debug("After being shortened: " + MobName);
				}

				spawnedlivingentity.setCustomName(MobName);
				spawnedentity.setCustomName(MobName);

				spawnedlivingentity.setCustomNameVisible(true);
				spawnedentity.setCustomNameVisible(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event){
		int AIChance = randomchance.nextInt(100);
		int StringLength = plugin.lenght_of_response;
		Entity spawnedentity = event.getEntity();
		String MobName = null;
		
		if(plugin.UseMobNameAsSeed == true){
			MobName = spawnedentity.getName();
		}
		
		
//		String AttackedContextString = plugin.AttackContexts.get(randomattackcontextstring.nextInt(plugin.AttackContexts.size()));
		

		if (spawnedentity instanceof Monster){
			plugin.debug("mob was attacked");
			if(plugin.change_response_on_attack == true){
				if(plugin.IsAIEnable == true && AIChance <= plugin.chance_of_ai_response_attacked && plugin.chance_of_ai_response_attacked != 0){
					plugin.debug("mob will spawn with AI message");
//					if (plugin.enable_context == true){
//						plugin.debug("using context for AI message");
//						plugin.debug("Context String is: " + AttackedContextString);
//						//add some context to the message
//						MobName = plugin.hal.getSentence(AttackedContextString);
//					}else 
					if (MobName == null){
						MobName = AICore.GenerateSentence();
					}else{
						//try and make the mob talk more about what it was talking about
						MobName = AICore.GenerateSentence(MobName.split(" ")[messagesplitrand.nextInt(MobName.split(" ").length)]);
					}
					plugin.debug("AI generated message is: " + MobName);
				}else{
					plugin.debug("mob will spawn with message from message file");
					MobName = plugin.NamesList.get(rand.nextInt(plugin.NamesList.size()));
					plugin.debug("Message from file is: " + MobName);
				}

				//see if we need to trim the string
				plugin.debug("lenght_of_response is: " + StringLength);
				if (StringLength != -1){
					plugin.debug("Mob sentence will be shortned. Before being shortened: " + MobName);
					MobName = MobName.substring(0, Math.min(MobName.length(), StringLength));
					plugin.debug("After being shortened: " + MobName);
				}

				spawnedentity.setCustomName(MobName);
				spawnedentity.setCustomNameVisible(true);
				
//				if (plugin.getServer().getPluginManager().getPlugin("mcMMO")!=null){
//				       plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//				            @Override
//				            public void run() {
//								spawnedentity.setCustomName(MobName);
//								spawnedentity.setCustomNameVisible(true);
//				            }
//				       }, 20L * 4); //4 is number of seconds
//				}
			}
		}
	}
}