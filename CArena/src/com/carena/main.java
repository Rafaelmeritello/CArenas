package com.carena;

import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("unchecked")
public class main extends JavaPlugin{

public static FileConfiguration arenasconfig;

public static FileConfiguration defaultconfig;

public static File root;

public static JavaPlugin plugin;

public static List<String> EM_ESPERA = new ArrayList<String>();
public static Map<String, String> EM_ARENA = new HashMap<String, String>();
public static Map<String, String> LIDERANDO_ARENA = new HashMap<String, String>();
public static Map<String, String> LIDERES_ANTERIORES = new HashMap<String, String>();




public static boolean inventariovazio(ItemStack[] inv) {
    for (ItemStack item : inv) {
        if (item != null && item.getType() != Material.AIR) {
            return false; // se encontrar algum item não vazio e que não seja do tipo ar, retorna false
        }
    }
    return true; // se não encontrar nenhum item não vazio e que não seja do tipo ar, retorna true (inventário vazio)
}






public static boolean savearenasconfig() {
	try {
		main.arenasconfig.save(new File(main.root, "arenas.yml"));
		return true;
	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
}

public static boolean savedefaultconfig() {
	try {
		main.defaultconfig.save(new File(main.root, "config.yml"));
		return true;
	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
}








public static boolean arenaexiste(String arena) {
	// retorna true caso a sessão na configuração exista e false caso não exista
	if(main.getarena(arena) == null){
		return false;
	}
	return true;
}





public static String adminmessage(String text) {
	return main.defaultconfig.getString("adminmessageprefix").replaceAll("&", "§")+ChatColor.AQUA+ " " +text;
}







public static String playermessage(String text) {
	return main.defaultconfig.getString("playermessageprefix").replaceAll("&", "§")+ChatColor.AQUA+ " " +text;
}






public static void liderar(String arena, String player, boolean anunciar) {

	main.LIDERANDO_ARENA.put(arena, player);
	List<String> messages = main.defaultconfig.getStringList("liderou_arena");
	if(LIDERES_ANTERIORES.get(arena) == player) {
	
		
			return;
		
	}

	if(!main.getarena(arena).getBoolean("tem_lider")) {
		return;
	}
	if(!anunciar) {
		return;
	}
	
	for(Player p : Bukkit.getOnlinePlayers()) {
	
		for(String msg : messages) {
			p.sendMessage(msg.replaceAll("@player", player).replaceAll("@arena", arena).replaceAll("@prefix", main.defaultconfig.getString("playermessageprefix").replaceAll("&", "§")));
		}
		
		
	}
}


public static ConfigurationSection getarena(String arena) {
	Set<String> arenas = main.arenasconfig.getKeys(false);
	for(String a : arenas) {
		if(a.equalsIgnoreCase(arena)) {
			return main.arenasconfig.getConfigurationSection(a);
		}
	}
	
	return null;
	
}

public static void levarspawn(Player p) {
	ConfigurationSection local = main.defaultconfig.getConfigurationSection("saida_das_arenas");
	Double y = local.getDouble("y");
	Double z = local.getDouble("z");
	Double x = local.getDouble("x");
	String mundo = local.getString("mundo");
	Float yawolhar = Float.parseFloat(local.getDouble("yaw") + "");
	Float pitolhar = Float.parseFloat(local.getDouble("pith") + "");

	Location newLocation = new Location(Bukkit.getWorld(mundo), x, y, z, yawolhar, pitolhar);

	p.teleport(newLocation);
}






public static boolean arenaespecial(String arena) {
	if(main.arenasconfig.getConfigurationSection(arena+".itens") != null || main.arenasconfig.getConfigurationSection(arena+".armaduras") != null){
		return  true;
	}
	return false;
}


public static String get_player_arena(String player) {
	return main.EM_ARENA.get(player);
}


public static int get_arena_count(String arena) {
    int count = 0;
    for(String player : main.EM_ARENA.keySet()) {
        if(main.EM_ARENA.get(player).equals(arena)) {
            count++;
        }
    }
    return count;
}




// fim funções


@Override
public void onEnable() {
	File arenasfile = new File(getDataFolder() , "arenas.yml");
	main.plugin = this;
	if(!arenasfile.exists()) {
		saveResource("arenas.yml", false);
	}
	
	File configFile = new File(getDataFolder() , "config.yml");
	if(!configFile.exists()) {
		saveResource("config.yml", false);
	}

	main.arenasconfig = YamlConfiguration.loadConfiguration(arenasfile);
	main.defaultconfig = YamlConfiguration.loadConfiguration(configFile);
	
	main.root = getDataFolder();
	getCommand("carena").setExecutor(new CArenaCommands());
	getCommand("arena").setExecutor(new ArenaCommands());
	getCommand("arenas").setExecutor(new Guicommands());
	getServer().getPluginManager().registerEvents(new Eventos(), this);
	
	

	
	
}


}
