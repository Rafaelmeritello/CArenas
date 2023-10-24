package com.carena;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Eventos implements Listener {

	
	
	
	
	
    @EventHandler
    public void aosair(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(main.get_player_arena(p.getName()) != null) {
 
        	 
        	if(main.LIDERANDO_ARENA.get(main.get_player_arena(p.getName())).equals(p.getName())  )   {
        		main.LIDERANDO_ARENA.put(main.get_player_arena(p.getName()), null);
        	}
        	if(main.arenaespecial(main.get_player_arena(p.getName()))) {
				for(ItemStack item : p.getInventory().getContents()) {
					p.getInventory().remove(item);
					
				}
				for(ItemStack item : p.getInventory().getArmorContents()) {
					p.getInventory().setArmorContents(null);
					
				}
        	}
        	main.EM_ARENA.remove(p.getName());
        }
      
    }
    
    
    
    
    
    @EventHandler
    public void aocair(PlayerKickEvent e) {
        Player p = e.getPlayer();

        if(main.get_player_arena(p.getName()) != null) {
 
        	 
        	if(main.LIDERANDO_ARENA.get(main.get_player_arena(p.getName())).equals(p.getName())  )   {
        		main.LIDERANDO_ARENA.put(main.get_player_arena(p.getName()), null);
        	}
        	if(main.arenaespecial(main.get_player_arena(p.getName()))) {
				for(ItemStack item : p.getInventory().getContents()) {
					p.getInventory().remove(item);
					
				}
				for(ItemStack item : p.getInventory().getArmorContents()) {
					p.getInventory().setArmorContents(null);
					
				}
        	}
        	
        	main.EM_ARENA.remove(p.getName());
        }
      
    }
    
    
    
    
    
    
    @EventHandler
    public void aoteleportar(PlayerTeleportEvent e) {
    	if (main.get_player_arena(e.getPlayer().getName()) != null) {
        Player player = e.getPlayer();

       
            
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + player.getName());
            
        
        
        }
    }
    
    
    
    
    
    
    @EventHandler
    public void aoDigitarComando(PlayerCommandPreprocessEvent evento) {
    	if(main.get_player_arena(evento.getPlayer().getName()) != null) {
    		
    	
    		String comando = evento.getMessage().toLowerCase();
    		List<String> comandos = main.defaultconfig.getStringList("comandos_permitidos");
    		boolean comandoPermitido = false;
    	
    		for (String cmd : comandos) {
    		    if (comando.startsWith(cmd + " ")|| comando.equalsIgnoreCase(cmd) || comando.equalsIgnoreCase("/arena sair")) {
    		    	
    		        comandoPermitido = true;
    		        break;
    		    }
    		}
    		if (!comandoPermitido) {
    		    evento.setCancelled(true);
    		    Player p = evento.getPlayer();
    		    p.sendMessage(main.playermessage(main.defaultconfig.getString("comando_nao_permitido").replaceAll("&", "§")));
    		}
        
    }
    }
    
    @EventHandler
    public void aomover(PlayerMoveEvent e) {
        if (main.EM_ESPERA.contains(e.getPlayer().getName())) {
            Location from = e.getFrom();
            Location to = e.getTo();
            double minDistance = 0.1; // valor mínimo de distância para considerar que o jogador se moveu
            if (from.distance(to) < minDistance) {
                // O jogador não se moveu significativamente, não faz nada.
                return;
            }
            main.EM_ESPERA.remove(e.getPlayer().getName());
            e.getPlayer().sendMessage(main.playermessage(main.defaultconfig.getString("player_mover_ao_sair").replaceAll("&", "§")));
        }
    }
    
    @EventHandler
    public void aomorrer(PlayerDeathEvent e) {
    	try {
        Player p = (Player) e.getEntity();
        String playerarena = main.get_player_arena(p.getName());
        
        if(main.get_player_arena(p.getName()) != null) {
        	 
            if(main.arenaespecial(playerarena)) {
            		 e.getDrops().clear();    
            }
            if(main.LIDERANDO_ARENA.get(playerarena).equals(p.getName()) ) {
            	main.liderar(playerarena, p.getKiller().getName(),true);
            	main.LIDERES_ANTERIORES.put(playerarena, p.getName());
            	
            }

            
   
            }
    	}catch (Exception e1) {
			return;
		}
    }
    
    @EventHandler
    public void aorespawn(PlayerRespawnEvent e) {
    	
    	if(main.get_player_arena(e.getPlayer().getName()) != null) {
    	
            BukkitRunnable delay = new BukkitRunnable() {
                public void run() {
                	
        
                	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + e.getPlayer().getName());
                	
            
                
                
                	
                }
            };
            delay.runTaskLater(main.plugin, 1*6);
    	
    	}
    }
    

    
    @EventHandler
    public void aodropar(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if(main.get_player_arena(p.getName()) != null) {
        	if(main.arenaespecial(main.get_player_arena(p.getName()))) {
                p.sendMessage(main.playermessage("§c Você não pode dropar itens nesta arena, '/arena sair' para sair"));
                e.setCancelled(true);
        	}

        }
    }
    
    
    
    @EventHandler
    public void aoreiniciar(PluginDisableEvent e) {
		Set<String> jogadoresNaArena = new HashSet<String>(main.EM_ARENA.keySet()); // cria uma cópia da lista original
	    for(String p : jogadoresNaArena) {

	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + Bukkit.getPlayer(p).getName());
	    }
    }
    
    @EventHandler
    public void aoentrar(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(main.get_player_arena(p.getName()) != null) {
        	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + p.getName());
        }
    }
    
    @EventHandler
    public void aoPegarItem(PlayerPickupItemEvent evento) {
       
        if (main.get_player_arena(evento.getPlayer().getName()) != null) {
        	if(main.arenaespecial(main.get_player_arena(evento.getPlayer().getName()))) {
            evento.setCancelled(true);
        	}
        
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    	if(main.get_player_arena(event.getWhoClicked().getName()) != null) {
    		if(main.arenaespecial(main.get_player_arena(event.getWhoClicked().getName()))) {
    			event.getWhoClicked().sendMessage(main.defaultconfig.getString("mecher_inventario").replaceAll("&", "§").replaceAll("@prefix", main.defaultconfig.getString("playermessageprefix").replaceAll("&", "§")));
    			
    			event.setCancelled(true);
    			
    		}
    	}	
    	if(event.getInventory().getTitle().equalsIgnoreCase(main.defaultconfig.getString("titulogui").replaceAll("&", "§"))) {
    		if(event.getRawSlot() > 26) {

    			event.setCancelled(true);
    		}
 
    	
    	if( event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getClickedInventory().getTitle().equalsIgnoreCase(main.defaultconfig.getString("titulogui").replaceAll("&", "§"))) {
    	 	Player p = (Player) event.getWhoClicked();
    	 	List<String> lore = event.getCurrentItem().getItemMeta().getLore();
    	 	if(lore.get(lore.size()-2).contains("Clique para entrar arena")) {
    	 	 	p.performCommand("arena entrar "+ ChatColor.stripColor( lore.get(lore.size()-2).split("Clique para entrar arena ")[1] ));
        	 	p.closeInventory();
    	 	}else {
    	 		
    			p.sendMessage(main.playermessage(main.defaultconfig.getString("mensagem_arena_manutencao").replaceAll("&", "§").replaceAll("@arena", ChatColor.stripColor( lore.get(lore.size()-2).split(" Em manutenção no momento")[0].split("• ")[1]))));
    	 		p.closeInventory();
    	 		event.setCancelled(true);
    	 	}
    	 	
    	}
    	event.setCancelled(true);
    	}
    	
    	
    }


    




    
}
