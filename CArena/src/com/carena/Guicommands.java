package com.carena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor; 
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_8_R3.Item;

import org.bukkit.enchantments.Enchantment;
import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileCount;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Set;
public class Guicommands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
	if (cmd.getName().equalsIgnoreCase("arenas")) {
		Player p = (Player) s;
		Inventory inv = Bukkit.createInventory(null, 27, main.defaultconfig.getString("titulogui").replaceAll("&", "§")); // Cria um inventário de baú com 27 espaços e título "Arenas"
		

		for(String nomearena : main.arenasconfig.getKeys(false)) {
			ConfigurationSection arena = main.arenasconfig.getConfigurationSection(nomearena);
			if(arena.getKeys(false).contains("icone") && arena.getKeys(false).contains("posicaogui")){
				int icone = arena.getInt("icone.id");
				int posicao = Integer.parseInt( arena.getString("posicaogui"));
				ItemStack item = new ItemStack(Material.getMaterial(icone));
				ItemMeta im = item.getItemMeta();
				im.setDisplayName(arena.getString("displaygui").replaceAll("&", "§"));
				
				// incluindo descricao
				if(arena.getKeys(false).contains("descricaogui")) {
					List<String> descricao = arena.getStringList("descricaogui");
					for (int i = 0; i < descricao.size(); i++) {
					    String linha = descricao.get(i);
					    linha = linha.replaceAll("&", "§").replaceAll("@count", main.get_arena_count(nomearena) + "");
					    descricao.set(i, linha);
					    
					}
					descricao.add("");
					if(main.arenasconfig.getInt(nomearena + ".manutencao") == 1) {
						descricao.add(" §c• " + nomearena + " Em manutenção no momento" );
					}else {
						descricao.add(" §7• §aClique para entrar arena " + nomearena );
					}
				
					descricao.add(" ");
					im.setLore(descricao);
				}
				// fim da inclusao da descricao
				
			    short data = (short) arena.getInt("icone.data");
			    item.setDurability(data);
			
				item.setItemMeta(im);
			    inv.setItem(posicao, item);
				
			}
		}

		p.openInventory(inv);
	    return true;
	}
	
	
	
		return false;
	}

}
