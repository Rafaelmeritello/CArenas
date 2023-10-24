package com.carena;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor; 
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.util.*;
import java.io.File;

public class CArenaCommands implements CommandExecutor{

	
	

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args){ 
		
		
		

		
		
		
		
		
		String argumento_comando;
		String argumento_arena;
		// verifica se o jogador digitou o comando
			try {
				argumento_comando = args[0].toLowerCase();
			}catch (Exception e) {
				argumento_comando = " ";
			}
		// fim da verificacao, o comando desejado esta na argumentocomando. args[0]
			
			
		// verifica se o jogador digitou a arena valida
			try {
				argumento_arena = args[1];
			}catch (Exception e) {
				argumento_arena = null;
			}
		// fim da verificacao, a arena desejada esta na argumentoarena. args[1]
			
			// check permissão
			if(!s.hasPermission("carena.admin")) {
				s.sendMessage(main.playermessage(main.defaultconfig.getString("sem_permissão_mensagem").replaceAll("&", "§")));
				return true;
			}
			// fim check permissão
			
			
			//check de comando reload
			
			if(argumento_comando.equalsIgnoreCase("reload") ){
				File arenasfile = new File(main.plugin.getDataFolder() , "arenas.yml");
				if(!arenasfile.exists()) {
					main.plugin.saveResource("arenas.yml", false);
				}
				
				File configFile = new File(main.plugin.getDataFolder() , "config.yml");
				if(!configFile.exists()) {
					main.plugin.saveResource("config.yml", false);
				}

				main.arenasconfig = YamlConfiguration.loadConfiguration(arenasfile);
				main.defaultconfig = YamlConfiguration.loadConfiguration(configFile);
				
				s.sendMessage(main.adminmessage("Reload Feito"));
				return true;
			}
			
			
			
			// fim de comando reload
			
			
			
			//check de saida das arenas
			
			if(!main.defaultconfig.getKeys(false).contains("saida_das_arenas") && !argumento_comando.equalsIgnoreCase("setsaida") ){
				s.sendMessage("§c As saidas das arenas não foram selecionadas");
				s.sendMessage("§c use /carena setsaida para definir uma saida antes de criar arenas");
				return true;
			}
			
			
			
			// fim do check da saida das arenas
			
			
			
			
			
			
		
			// comando de listar arenas
			if(ListarArenas(s, argumento_comando)) {
				return true;
			}
			// fim do comando de listar arenas
			
			
			


			
			
			
			// inicio do comando criar arena
			if(CriarArena(s, args, argumento_comando, argumento_arena)) {
				return true;
			}
			// fim do comando de criar arena
		
			
			
			
			
			
			
			
			// inicio comando de selecionar 
	
			if (SetItensArena(s, argumento_comando, argumento_arena)) {
				return true;
				//fim comando de selecionar itens
			}
			
			
			
			// comando de remover arenas
			if(RemoverArena(s, args, argumento_comando)) {
				return true;
			};
			
			
			// fim do comando de remover arenas
			
			
			
			
			
			
			
			
			
			
			
			// comando para pegar itens de uma arena
			if(GetItensArena(s, argumento_comando, argumento_arena)) {
				return true;
			};
			
			// fim do comando de pegar itens
			
			
			
			
			
			
			
			
			
			
			//comando de setlocal
			
			if(SetSpawnArena(s, argumento_comando, argumento_arena)) {
				return true;
			}
			//fim comando de setlocalidade
			
			
			
			
			// comando para tirar um jogador da arena
			
			if(TirarJogadorArena(s, argumento_comando, argumento_arena)) {
				return true;
			}
			
			
			//fim comando  tirar um jogador da arena
			
			
			
			
			
			// remover jogadores de uma arena especifica
			if(LimparArena(s, argumento_comando, argumento_arena)) {
				return true;
			}
			
			
			// fim comando de remover jogadores de uma arena
			
			
			// remover todos os jogadores das arenas
			if(LimparArenas(s, argumento_comando)) {
				return true;
			}
			
			
			// fim do comando de remover jogadores das arenas
			
			
			
			
			
			
			// commandos de manutenção
			
			
			
			
			if(AtivarmanutencaoArena(s, argumento_comando, argumento_arena)){
				return true;
			}
			
			
			
			if(desativarManutencaoArena(s, argumento_comando, argumento_arena)) {
				return true;
			}
			
			// fim comandos de manutenção
			
			
			
			
			// inicio comandos de gui
			
			
			
			if(IconeArena(s, args, argumento_comando, argumento_arena)) {
				return true;
			}
			
			
			
			
			
			
			if(PosicaoguiArena(s, args, argumento_comando, argumento_arena)) {
				return true;
			}
			
			

			
			
			// fim comandos gui
			
			
			
			//comando setsaida
			if(SetSaida_Arena(s, argumento_comando)) {
				return true;
			}
			
			// fim comando setsaida
			
			
			
			
			
			
			// comando de ajuda 
			if(AjudaComando(s, argumento_comando)) {
				return true;
			};
			

			
			// fim comando de ajuda
			if(argumento_comando.equalsIgnoreCase("atu")) {
				s.sendMessage(main.LIDERANDO_ARENA + "");
				return true;
			}
			
			if(argumento_comando.equalsIgnoreCase("ant")) {
				s.sendMessage(main.LIDERES_ANTERIORES + "");
				return true;
			}
			
			
			if(argumento_comando.equalsIgnoreCase("naarena")) {
				s.sendMessage(main.EM_ARENA + "");
				return true;
			}
			
			
		return false;
	
	

		

}


	private boolean RemoverArena(CommandSender s, String[] args, String argumento_comando) {
		if (argumento_comando.equalsIgnoreCase("remover") || argumento_comando.equalsIgnoreCase("deletar") || argumento_comando.equalsIgnoreCase("excluir")) {
		    if (args.length < 2) {
		        s.sendMessage(ChatColor.RED + "Uso correto: /arena remover <nome_da_arena>");
		        return true;
		    }
		    String arena = args[1];
		    if (main.arenaexiste(arena)) {
		        for(String a : main.arenasconfig.getKeys(false)) {
		        	if(a.equalsIgnoreCase(arena)) {
		        		main.arenasconfig.set(a, null);
		        	}
		        }
		        main.savearenasconfig();
		        s.sendMessage(main.adminmessage(ChatColor.AQUA +"Arena " +ChatColor.WHITE +arena + ChatColor.AQUA +" removida com sucesso!"));
		    } else {
		        s.sendMessage(main.adminmessage(ChatColor.RED +"Arena " +ChatColor.WHITE + arena +ChatColor.RED + " não encontrada!"));
		    }
		    return true;
		}
		return false;
	}


	
	
	
	private boolean GetItensArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("getitens")) {
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage( "Arena não informada"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe, utilize /carena criar (arena)"));
				return true;
			}
			if(!main.getarena(argumento_arena).isConfigurationSection("itens") &&!main.getarena(argumento_arena).isConfigurationSection("armaduras") ) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "esta arena não tem itens definidos"));
				return true;
			}
			// fim da verificacao

			Player p = (Player) s;
			ConfigurationSection itens = main.getarena(argumento_arena).getConfigurationSection("itens");
			ConfigurationSection armaduras = main.getarena(argumento_arena).getConfigurationSection("armaduras");
			if(itens != null) {
			Set<String> keys = (Set<String>) itens.getKeys(false);
			
			for(String key: keys) {
				ConfigurationSection item = (ConfigurationSection) itens.getConfigurationSection(key);
				String id = item.getString("id");
			
				String quantidade = item.getString("quantidade");
				ItemStack itemdado = new ItemStack(Material.getMaterial(Integer.parseInt(id)), Integer.parseInt(quantidade));
				ItemMeta meta = (ItemMeta) item.get("meta");

			    short data = (short) item.getInt("data");
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
					itemdado.setItemMeta(meta);
					armadurasNovas[i] = itemdado;
					i++;
				}
				p.getInventory().setArmorContents(armadurasNovas);
			}
			
			
			p.sendMessage(main.adminmessage("Itens da arena §f"+argumento_arena+ " §b foram adicionados ao seu inventario"));
			if(itens == null && armaduras == null) {
			p.sendMessage(main.adminmessage("§c a arena §f"+argumento_arena+ " §c tem itens vazios definidos, por isso você não recebeu nada no seu inventario."));
			}
		
			return true;
		}
		return false;
	}

	
	
	

	private boolean ListarArenas(CommandSender s, String argumento_comando) {
		if (argumento_comando.equalsIgnoreCase("listar")) {
		    Set<String> arenas = main.arenasconfig.getKeys(false);
		    List<String> arenasList = new ArrayList<>();
		    for (String arena : arenas) {
		        if (main.arenaexiste(arena)) {
		            arenasList.add(arena);
		        }
		    }

		    if (arenasList.size() > 0) {
		        s.sendMessage(main.adminmessage(ChatColor.AQUA +"Arenas registradas:"));
		        for (String arena : arenasList) {
		            s.sendMessage("§f- " + arena);
		        }
		        return true;
		    } else {
		        s.sendMessage("§cNão há nenhuma arena registrada no momento.");
		        return true;
		    }
		   
		}
		return false;
	}

	
	
	

	private boolean SetSpawnArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("setspawn")) {
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe"));
				return true;
			}
			Player p = (Player) s;
			
			

			main.getarena(argumento_arena).set("tem_lider", false);
			main.getarena(argumento_arena).set("local.x", p.getLocation().getX());
			main.getarena(argumento_arena).set("local.y", p.getLocation().getY()+0.27);
			main.getarena(argumento_arena).set("local.z", p.getLocation().getZ());
			main.getarena(argumento_arena).set("local.yawholhar", p.getLocation().getYaw());
			main.getarena(argumento_arena).set("local.pitholhar", p.getLocation().getPitch());
			
			main.getarena(argumento_arena).set("local.mundo", p.getLocation().getWorld().getName());
			main.savearenasconfig();
			p.sendMessage(main.adminmessage(ChatColor.AQUA + "local da arena " + ChatColor.WHITE + argumento_arena + ChatColor.AQUA + " definido como sua localidade atual." ));
			return true;
		
		}
		return false;
	}

	
	
	

	private boolean CriarArena(CommandSender s, String[] args, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("criar")) {
			
			// verifica se ele digitou uma arena, e se ela existe
				try {
				if(main.arenaexiste(args[1])) {
					s.sendMessage(main.adminmessage(ChatColor.RED + "Esta arena Já existe" ));
					return true; 
				}
				}catch (Exception e) {
					s.sendMessage(main.adminmessage(ChatColor.RED + "Digite /carena criar (nome da arena)" ));
					return true;
				}
				// fim da verificacao

				
				
					// cria a arena
					main.arenasconfig.createSection(args[1]);
					
					main.savearenasconfig();
					s.sendMessage(main.adminmessage(ChatColor.AQUA+ "Arena "+ChatColor.WHITE + args[1]+ ChatColor.AQUA +" criada" ));
					s.sendMessage(main.adminmessage("Utilize o comando /carena setspawn §f" + argumento_arena + "§b no local do da arena"));
						
				return true;

		}
		return false;
	}

	
	
	

	private boolean TirarJogadorArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("tirar")) {
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage("§cDigite o jogador para retirar da arena"));
				return true;
			}
			Player p;
		
			p = Bukkit.getPlayer(argumento_arena);
			if(!p.isOnline()) {
				s.sendMessage(main.adminmessage("O jogador não esta online"));
				return true;
			}
			if(main.EM_ARENA.containsKey(p.getName())) {
				String playerarena = main.get_player_arena(p.getName());
				if(main.LIDERANDO_ARENA.get(playerarena) == p.getName() ) {
					main.LIDERANDO_ARENA.put(playerarena, null);
					main.LIDERES_ANTERIORES.put(playerarena, p.getName());
					
					if(main.get_arena_count(playerarena) > 1) {
					
						
						@SuppressWarnings("unchecked")
						List<String> players = new ArrayList<String>(main.EM_ARENA.keySet());
					
						for(int i = 0; i < players.size(); i++){
						
							if( players.get(i) != p.getName()) {
								
								
								if(playerarena.equals(  main.get_player_arena(players.get(i)))) {

								main.liderar(main.get_player_arena(p.getName()), players.get(i),true);
								}
								break;
							}
						}

						
					}
					
					
				}
				
				if(main.arenaespecial(main.get_player_arena(p.getName()))) {
				for(ItemStack item : p.getInventory().getContents()) {
					if(item == null || item.getTypeId() == 397) {
						continue;
					}
						p.getInventory().remove(item);
					
					
					
				}
				for(ItemStack item : p.getInventory().getArmorContents()) {
					p.getInventory().setArmorContents(null);
					
				}
				}
				
			
				main.EM_ARENA.remove(p.getName());
				main.levarspawn(p);
				p.sendMessage(main.playermessage( ChatColor.AQUA + "Você saiu da arena"));
				s.sendMessage(main.adminmessage("Jogador Removido da arena."));
				return true;
			}else {
				s.sendMessage(main.adminmessage("este jogador não esta na arena"));
				return true;
			}
			
		}
		return false;
	}

	
	
	

	private boolean LimparArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("limpararena")) {
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe"));
				return true;
			}
			Set<String> jogadoresNaArena = new HashSet<String>(main.EM_ARENA.keySet()); // cria uma cópia da lista original
		    for(String p : jogadoresNaArena) {
		    	if(main.get_player_arena(p).equalsIgnoreCase(argumento_arena)){
		    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + Bukkit.getPlayer(p).getName());
		    	}
		        
		    }
			
			for(Player p : Bukkit.getOnlinePlayers()) {
			
			p.sendMessage(main.playermessage(main.defaultconfig.getString("limpar_arena_broadcast").replaceAll("&", "§").replaceAll("@admin", s.getName()).replaceAll("@arena", argumento_arena)    ));
			}
			s.sendMessage(main.adminmessage(ChatColor.AQUA + "Todos os jogadores sairam da arena"));
			return true;
			
		}
		return false;
	}

	
	
	

	private boolean LimparArenas(CommandSender s, String argumento_comando) {
		if(argumento_comando.equalsIgnoreCase("limpararenas")) {
			Set<String> jogadoresNaArena = new HashSet<String>(main.EM_ARENA.keySet()); // cria uma cópia da lista original
		    for(String p : jogadoresNaArena) {
		        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + Bukkit.getPlayer(p).getName());
		    }
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(main.playermessage(main.defaultconfig.getString("limpar_arenas_broadcast").replaceAll("&", "§").replaceAll("@admin", s.getName())));
			}
			
			s.sendMessage(main.adminmessage(ChatColor.AQUA + "Todos os jogadores sairam da arena"));
			return true;
			
		}
		return false;
	}


	
	
	
	private boolean AtivarmanutencaoArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("ativarmanu")) {
			// verificão se a arena foi informada e é valida
			
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe, utilize /carena criar (arena)"));
				return true;
			}

			if(main.getarena(argumento_arena).getInt("manutencao") == 1) {
				s.sendMessage(main.adminmessage("§c a arena §f" + argumento_arena + "§c já esta em manutenção"));
				return true;
			}
			
			
			// fim da verificacao
			main.getarena(argumento_arena).set("manutencao", 1);
			main.savearenasconfig();
			Set<String> jogadoresNaArena = new HashSet<String>(main.EM_ARENA.keySet()); // cria uma cópia da lista original
		    for(String p : jogadoresNaArena) {
		    	if(main.get_player_arena(p).equalsIgnoreCase(argumento_arena)){
		    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "carena tirar " + Bukkit.getPlayer(p).getName());
		    	}
		        
		    }
			s.sendMessage(main.adminmessage("Todos os jogadores das arenas serão removidos em 3 segundos."));
			for(Player p : Bukkit.getOnlinePlayers()) {
			
				for(String msg : main.defaultconfig.getStringList("Arena_entrou_manu") ) {
					s.sendMessage(msg.replaceAll("&", "§").replaceAll("@arena", argumento_arena).replaceAll("@admin", s.getName()));
				}
			}
			return true;
		}
		return false;
	}


	
	
	
	private boolean desativarManutencaoArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("desativarmanu")) {
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe, utilize /carena criar (arena)"));
				return true;
			}
			if(main.getarena(argumento_arena).getInt("manutencao") == 0) {
				s.sendMessage(main.adminmessage("§c a arena §f" + argumento_arena + "§c não esta em manutenção"));
				return true;
			}
			// fim da verificacao
			main.getarena(argumento_arena).set("manutencao", 0);
			main.savearenasconfig();
			s.sendMessage(main.adminmessage("Arena §f"+argumento_arena+"§b agora não esta mais em manutenção"));
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(main.adminmessage("§f" + s.getName() + "§b Removeu o estado de manutenção da arena §f" + argumento_arena));
			}
			return true;
		}
		return false;
	}


	
	
	
	private boolean AjudaComando(CommandSender s, String argumento_comando) {
		if(argumento_comando.equalsIgnoreCase("ajuda") ||  argumento_comando.equalsIgnoreCase("help") || argumento_comando == null || argumento_comando.equals(" ") ) {
			s.sendMessage("§f====================================");
			s.sendMessage("§6Plugin de administração e criação de arenas");
			s.sendMessage("§f====================================");
			s.sendMessage("");
		
			s.sendMessage("§e/carena criar§b (arena) §7-§f Cria uma nova arena");
			s.sendMessage("§e/carena remover§b (arena) §7-§f Deleta uma arena");
			s.sendMessage("§e/carena setitens §b (arena)§b (nome da arena) §7-§f Seleciona os itens de uma arena de acordo com seu inventario. transforma uma arena normal em uma arena especial");
			s.sendMessage("§e/carena getitens§b (arena) §7-§f Recebe todos os itens definidos da arena especial desejada");
			s.sendMessage("§e/carena setspawn§b (nome da arena)§b (arena) §7-§f Seleciona a localização da arena de acordo com a sua localização");
			s.sendMessage("§e/carena ativarmanu§b (arena) §7-§f Ativa o modo de manutenção da arena especial");
			s.sendMessage("§e/carena desativarmanu§b (arena) §7-§f Desativa o modo de manutenção da arena especial");
			s.sendMessage("§e/carena limpararenas§b §7-§f Remove todos os jogadores de todas as arenas");
			
			s.sendMessage("§e/carena limpararena§b (arena)§b §7-§f Remove todos os jogadores de uma arena");
			s.sendMessage("§e/carena tirar§b (jogador) §7-§f Tira um jogador da arena");
			s.sendMessage("§e/carena icone§b (arena) (posicaogui)§b §7-§f Seleciona o icone da arena no gui");
			s.sendMessage("§e/carena posicaogui§b (arena) (numero no bau)§b §7-§f Seleciona a posição do icone da arena no gui");
			s.sendMessage("§e/carena setsaida §7-§f Seleciona o local onde os jogadores vão ao sair das arenas");
			s.sendMessage("");
			s.sendMessage("§bArenas especiais >> arenas que tem itens definidos");
			s.sendMessage("§bArenas normais >> arenas que não tem itens definidos");
			s.sendMessage("§f====================================");
			return true;
			
		}
		return false;
	}


	
	
	
	private boolean IconeArena(CommandSender s, String[] args, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("icone")) {
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage( ChatColor.RED +"Esta arena não existe, utilize /carena criar (arena)"));
				return true;
			}
			try {
				Integer.parseInt(args[2]);
			}catch (Exception e) {
				s.sendMessage(main.adminmessage("utilize /carena icone (arena) (posicao), a posição deve ser um numero valido"));
				s.sendMessage(ChatColor.AQUA + "A posição representa a posição no bau do gui, começando por 0 indo até 26");
				return true;
			}
			// fim da verificacao
			Player p = (Player) s;
			ItemStack item = p.getItemInHand();
			if(item == null || item.getType() == Material.AIR) {
				p.sendMessage(main.adminmessage(ChatColor.RED + "segure o item do icone na mão"));
				return true;
			}
			
			//adicionando area de descricao
			if(!main.getarena(argumento_arena).getKeys(false).contains("descricaogui")) {
			List<String> desc = new ArrayList<String>();
			desc.add("&b"+argumento_arena);
			
			main.getarena(argumento_arena).set("descricaogui", desc);
			}
			// fim de adicionar a descricao
			
			main.getarena(argumento_arena).set(".posicaogui",args[2]);
			if(!main.getarena(argumento_arena).getKeys(false).contains("displaygui")) {
			main.getarena(argumento_arena).set("displaygui", "&e"+argumento_arena);
			}
			main.getarena(argumento_arena).set("icone.id",p.getItemInHand().getTypeId());
			main.getarena(argumento_arena).set("icone.data", p.getItemInHand().getDurability());
			main.savearenasconfig();
			p.sendMessage(main.adminmessage("o icone da sua mão foi adicionado para a arena.§f "+ argumento_arena));
			p.sendMessage("§b na posição §f" + args[2]);
			
			return true;
		}
		return false;
	}

	
	
	

	private boolean PosicaoguiArena(CommandSender s, String[] args, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("posicaogui")) {
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada"));
				return true;
			}

			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe, utilize /carena criar (arena)"));
				return true;
			}
			if(!main.getarena(argumento_arena).getKeys(false).contains("icone")) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Esta arena não possui um icone, digite /carena icone (arena) com um item na mão para definir"));
				return true;
			}
			try {
				Integer.parseInt(args[2]);
			}catch (Exception e) {
				s.sendMessage(main.adminmessage("utilize /carena posicaogui (arena) (posicao), a posição deve ser um numero valido"));
				return true;
			}
			// fim da verificacao
			Player p = (Player) s;

			main.getarena(argumento_arena).set("posicaogui",args[2]);
			main.savearenasconfig();
			p.sendMessage(main.adminmessage("o icone da da arena §f" + argumento_arena + " §b foi setado na posição §f") + args[2]	);
			p.sendMessage(ChatColor.AQUA + "Agora, para editar a descrição do icone, utilize o arquivo de configuração do plugin."	);
			
			return true;
		}
		return false;
	}

	
	
	

	private boolean SetSaida_Arena(CommandSender s, String argumento_comando) {
		if(argumento_comando.equalsIgnoreCase("setsaida")) {
			Player p = (Player) s;

			
			main.defaultconfig.createSection("saida_das_arenas");
			main.defaultconfig.getConfigurationSection("saida_das_arenas").set("x", p.getLocation().getX());
			main.defaultconfig.getConfigurationSection("saida_das_arenas").set("y", p.getLocation().getY());
			main.defaultconfig.getConfigurationSection("saida_das_arenas").set("z", p.getLocation().getZ());
			main.defaultconfig.getConfigurationSection("saida_das_arenas").set("pitch", p.getLocation().getPitch());
			main.defaultconfig.getConfigurationSection("saida_das_arenas").set("yaw", p.getLocation().getYaw());
			main.defaultconfig.getConfigurationSection("saida_das_arenas").set("mundo", p.getLocation().getWorld().getName());
			main.savedefaultconfig();
			s.sendMessage(main.playermessage("Saida das arenas foi definida de acordo com a sua localização atual"));
			return true;
			
		}
		return false;
	}
	
	
	
	

	private boolean SetItensArena(CommandSender s, String argumento_comando, String argumento_arena) {
		if(argumento_comando.equalsIgnoreCase("setitens")) {
			// verificão se a arena foi informada e é valida
			if(argumento_arena == null) {
				s.sendMessage(main.adminmessage(ChatColor.RED + "Arena não informada, utilize /carena setitens (arena)"));
				return true;
			}
			if(!main.arenaexiste(argumento_arena)) {
				s.sendMessage(main.adminmessage("Esta arena não existe, utilize /carena criar (arena)"));
				return true;
			}
			// fim da verificacao
			
			
			Player p = (Player) s;

			ItemStack[] itens = p.getInventory().getContents();
			ItemStack[] armaduras = p.getInventory().getArmorContents();

			if(main.inventariovazio(armaduras) && main.inventariovazio(itens)) {
				if(main.getarena(argumento_arena).getConfigurationSection("itens") != null || main.getarena(argumento_arena).getConfigurationSection("armaduras") != null ) {
					s.sendMessage("§b Como seu inventario esta vazio, a arena agora é uma arena normal");
				}else {
					s.sendMessage("§b Como seu inventario esta vazio, a arena continua sendo uma arena normal ");
					return true;
				}
				
			}//
		if(!main.inventariovazio(armaduras)) {

			int armaduraindex = 0;
			for(ItemStack item: armaduras) {
			    if (item != null) { // Verifica se o item não é nulo ou um item de ar
			    	    main.getarena(argumento_arena).set("armaduras."+armaduraindex+".id", item.getTypeId());
			    	    main.getarena(argumento_arena).set("armaduras."+armaduraindex+".data", item.getDurability());
			    	    main.getarena(argumento_arena).set("armaduras."+armaduraindex+".meta", item.getItemMeta());
			    	    main.getarena(argumento_arena).set("armaduras."+armaduraindex+".quantidade", item.getAmount());
				         
			            armaduraindex ++;
			        
			    }
			}
		}else {
			main.getarena(argumento_arena).set("armaduras", "{}");
		}
			
			
			
			
if(!main.inventariovazio(itens)) {
			int itemindex = 0;
			for(ItemStack item: itens ) {
			    if (item != null && item.getType() != Material.AIR) { // Verifica se o item não é nulo ou um item de ar
			    		main.getarena(argumento_arena).set("itens."+itemindex+".id", item.getTypeId());
			    		main.getarena(argumento_arena).set("itens."+itemindex+".data", item.getDurability());
			    		main.getarena(argumento_arena).set("itens."+itemindex+".meta", item.getItemMeta());
			    		main.getarena(argumento_arena).set("itens."+itemindex+".quantidade", item.getAmount());
				         
			        	itemindex ++;
			        
			    }
			}
}else{
		
 main.getarena(argumento_arena).set("itens", "{}");
 main.savearenasconfig();
 
 return true;
}




		
			main.savearenasconfig();

			
			s.sendMessage(main.adminmessage("Itens da arena "+ argumento_arena + " foram definidos de acordo com os itens do seu inventario."));

		return true;
		
			
}
		return false;
	}
	
	
	
	

}
