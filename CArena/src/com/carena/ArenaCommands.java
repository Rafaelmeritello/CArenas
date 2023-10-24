package com.carena;
import java.util.Collection;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor; 
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
public class ArenaCommands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
		String argumento_comando;
		String argumento_arena;
		// verifica se o jogador digitou o comando
		try {
			argumento_comando = args[0].toLowerCase();
		}catch (Exception e) {
			argumento_comando = "";
			
		}
	// fim da verificacao, o comando desejado esta na argumentocomando. args[0]
		
		
	// verifica se o jogador digitou a arena valida
		try {
			argumento_arena = args[1];
		}catch (Exception e) {
			argumento_arena = null;
		}
	// fim da verificacao, a arena desejada esta na argumentoarena. args[1]
		
		
		if(args.length < 1) {
			Player p = (Player) s;
			p.performCommand("arenas");
			return true;
		}
		
		
		
		
		// inicio do comando de entrar na arena
		
		if(EntrarArena(s, argumento_comando, argumento_arena)) {
			return true;
		}
		
		
		
		// fim do comando entrar na arena
		
        if(SairArena(s, argumento_comando)) {
        	return true;
        }
    
		
		
		
		return false;
	}

	
	
	
	
	
	
	private boolean SairArena(CommandSender s, String argumento_comando) {
		if (argumento_comando.equalsIgnoreCase("sair")) {
        	Player p = (Player) s;
        	if(!main.EM_ARENA.containsKey(p.getName())) {
        		p.sendMessage(main.playermessage("Você não esta em uma arena"));
        		return true;
        	}
        	if(main.EM_ESPERA.contains(p.getName())) {
        		p.sendMessage("§cVocê ja digitou esse comando, aguarde");
        		return true;
        	}
        	
        	main.EM_ESPERA.add(p.getName());

        	int segundos = main.defaultconfig.getInt("Delay_ao_sair");

        	p.sendMessage(main.playermessage("Aguarde @segundos segundos").replace("@segundos", segundos + ""));
            BukkitRunnable delay = new BukkitRunnable() {
                public void run() {
                	
                if(main.EM_ESPERA.contains(p.getName())) {
                	
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + p.getName());
                	
                	main.EM_ESPERA.remove(p.getName());
                
                }
                	
                }
            };
            delay.runTaskLater(main.plugin, segundos*17); // Atraso de 10 segundos (20 ticks)
            return true;
        }
		return false;
	}
	
	
	

	private boolean EntrarArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("entrar")) {
			
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.playermessage(ChatColor.RED + "Arena não informada"));
				return true;
			}
			if(! main.arenasconfig.getKeys(false).contains(argumento_arena)) {
				s.sendMessage(main.playermessage("Esta arena não existe."));
				return true;
			}
			
			if(main.getarena(argumento_arena).getInt("manutencao") == 1 && !s.hasPermission("carena.admin")) {
				s.sendMessage(main.playermessage(main.defaultconfig.getString("mensagem_arena_manutencao").replaceAll("&", "§").replaceAll("@arena", argumento_arena)    ));
				return true;
			}

			// fim da verificacao
			
			

			
		    ConfigurationSection localv = main.getarena(argumento_arena).getConfigurationSection("local");
		    if (localv == null || !localv.contains("x") || !localv.contains("y") || !localv.contains("z")) {
		        s.sendMessage(main.playermessage("A arena informada não possui uma localização definida. "));
		        return true;
		    }

			
			
		    
		    
		    
		    
			Player p = (Player) s;
		
			if(main.getarena(argumento_arena).getConfigurationSection("itens") == null  && main.getarena(argumento_arena).getConfigurationSection("armaduras") == null) {
				ConfigurationSection local = main.arenasconfig.getConfigurationSection(argumento_arena+".local");
				Double x = local.getDouble("x");
				Double y = local.getDouble("y");
				Double z = local.getDouble("z");
				Float yawolhar = Float.parseFloat(local.getDouble("yawholhar") + "");
				Float pitolhar = Float.parseFloat(local.getDouble("pitholhar") + "");
		
				String mundo = local.getString("mundo");
				
				Location newLocation = new Location(Bukkit.getWorld(mundo), x, y, z, yawolhar, pitolhar);
				p.teleport(newLocation);
				
				if(main.get_arena_count(argumento_arena) == 0 ) {
					
					main.liderar(argumento_arena,s.getName(),main.defaultconfig.getBoolean("anunciar_ldr_na_entrada"));
					
				}
				
				main.EM_ARENA.put(p.getName(), argumento_arena);
				
				
				
		
				
				
				if(main.defaultconfig.getBoolean("enviar_broadcast_ao_entrar_na_arena_normal") == true) {
					String mensagem = main.defaultconfig.getString("broadcast_ao_entrar_na_arena_normal");
					mensagem = mensagem.replace("&", "§");
					mensagem = mensagem.replace("@arena", argumento_arena);
					mensagem = mensagem.replace("@jogador", p.getName());
				for(Player po : Bukkit.getOnlinePlayers()) {

					po.sendMessage(main.playermessage(mensagem));
				}
				}
				
				return true;
			}
			
			
			
			
			
			
		
			
			// verificar se o inventario esta vazio
			for (ItemStack item : p.getInventory().getContents()) {
			    if (item != null && item.getType() != Material.AIR) {
			        p.sendMessage(main.playermessage(ChatColor.RED + "Esvazie o inventario para entrar nessa arena."));
			        return true;
			      
			    }
			}
			
			for (ItemStack item : p.getInventory().getArmorContents()) {
			    if (item != null && item.getType() != Material.AIR) {
			        p.sendMessage(main.playermessage(ChatColor.RED + "Esvazie o inventario para entrar nessa arena. você esta com armaduras"));
			       
			        return true;
			      
			    }
			}

			if(main.defaultconfig.getConfigurationSection("saida_das_arenas") == null) {
				s.sendMessage("§c as arenas estão em manutenção no momento, consulte um administrador ou tente novamente mais tarde");
				return true;
			}
			//fim da verificacao
			
			ConfigurationSection local = main.getarena(argumento_arena).getConfigurationSection("local");;
			Double x = local.getDouble("x");
			Double y = local.getDouble("y");
			Double z = local.getDouble("z");
			Float yawolhar = Float.parseFloat(local.getDouble("yawholhar") + "");
			Float pitolhar = Float.parseFloat(local.getDouble("pitholhar") + "");
	
			String mundo = local.getString("mundo");
			
			Location newLocation = new Location(Bukkit.getWorld(mundo), x, y, z, yawolhar, pitolhar);
			p.teleport(newLocation);
			
			
			

			ConfigurationSection itens = main.getarena(argumento_arena).getConfigurationSection("itens");
			ConfigurationSection armaduras = main.getarena(argumento_arena).getConfigurationSection("armaduras");
		    if (itens == null && armaduras == null) {
		        s.sendMessage(main.playermessage("A arena informada não possui itens definidos"));
		        return true;
		    }
			if(itens != null) {
			    for (PotionEffect effect : p.getActivePotionEffects()) {
		            p.removePotionEffect(effect.getType());
		          
		        }
			Set<String> keys = (Set<String>) itens.getKeys(false);
			
			for(String key: keys) {
				ConfigurationSection item = (ConfigurationSection) itens.getConfigurationSection(key);
				String id = item.getString("id");
			
				String quantidade = item.getString("quantidade");
				ItemStack itemdado = new ItemStack(Material.getMaterial(Integer.parseInt(id)), Integer.parseInt(quantidade));
				ItemMeta meta = (ItemMeta) item.get("meta");
			    short data = (short) item.getDouble("data");
			    itemdado.setDurability(data);
				itemdado.setItemMeta(meta);
				p.getInventory().addItem(itemdado);
			}
			}
			
			int i = 0;
			if(armaduras != null) {
				Set<String> keys = (Set<String>) armaduras.getKeys(false);
				ItemStack[] armadurasNovas = new ItemStack[4];
				for(String key: keys) {
					ConfigurationSection item = (ConfigurationSection) armaduras.getConfigurationSection(key);
					String id = item.getString("id");
				
					String quantidade = item.getString("quantidade");
					ItemStack itemdado = new ItemStack(Material.getMaterial(Integer.parseInt(id)), Integer.parseInt(quantidade));
					ItemMeta meta = (ItemMeta) item.get("meta");
				    short data = (short) item.getInt("data");
				    itemdado.setDurability(data);
					itemdado.setItemMeta(meta);
					
					itemdado.setItemMeta(meta);
					armadurasNovas[i] = itemdado;
					i++;
				}
				p.getInventory().setArmorContents(armadurasNovas);
		    
			}
			
			
			
			if(main.get_arena_count(argumento_arena) == 0) {
			
				main.liderar(argumento_arena,s.getName(),main.defaultconfig.getBoolean("anunciar_ldr_na_entrada"));
			
				
			}
			main.EM_ARENA.put(p.getName(), argumento_arena);
			p.setGameMode(GameMode.SURVIVAL);
			if(main.defaultconfig.getBoolean("enviar_broadcast_ao_entrar_na_arena_especial") == true) {
				
				String mensagem = main.defaultconfig.getString("broadcast_ao_entrar_na_arena_especial");
				mensagem = mensagem.replace("&", "§");
				mensagem = mensagem.replace("@arena", argumento_arena);
				mensagem = mensagem.replace("@jogador", p.getName());
			for(Player po : Bukkit.getOnlinePlayers()) {

				po.sendMessage(main.playermessage(mensagem));
			}
			}
			
			return true;
			
		}
		return false;
	}
	

}
