package serveroutgoing;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinServer extends JavaPlugin implements Listener{
	File file2 = new File("Signs.yml");
	File file = new File("Infos.yml");
	FileReader reader;
	FileWriter writer;
	FileReader reader2;
	                     ArrayList<Location> IsOn = new ArrayList<Location>();
	public static HashMap<String, String> IPSave = new HashMap<String, String>();
	public void onEnable(){




	StartCountdownSign();
	
		try {
			
			reader = new FileReader(file);
			reader2 = new FileReader(file2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String in2;
		BufferedReader br2 = new BufferedReader(reader2);
		try {
			while((in2 = br2.readLine()) != null){
				String[] splitted = in2.split("_");
			IsOn.add(new Location(Bukkit.getWorld("world"), Double.parseDouble(splitted[0]), Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String in;
		BufferedReader br = new BufferedReader(reader);
		try {
			while((in = br.readLine()) != null){
				String[] sachen = in.split(" ");
				IPSave.put(sachen[0], sachen[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	Bukkit.getServer().getPluginManager().registerEvents(this, this);

	}
@Override
	public void onDisable() {
		super.onDisable();
	}

	
@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return super.onCommand(sender, command, label, args);
	}

@EventHandler
public void JoinInteract(PlayerInteractEvent e){
	if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        
        Block b = e.getClickedBlock();
       

            if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
System.out.println("sign");
          
                Sign sign = (Sign) b.getState();
                String[] lines = sign.getLines();
                if(lines[0].equalsIgnoreCase("- Server -")){
                	if(!(IsOn.contains(b.getLocation()))){
                	 IsOn.add(b.getLocation());
                	 try {
                		 writer = new FileWriter(file2);
                		 writer.flush();
						writer.write(b.getLocation().getX() + "_" + b.getLocation().getY() + "_" + b.getLocation().getZ());
						writer.flush();
					System.out.println("Writed");
					writer.close();
                	 } catch (IOException e1) {
					e1.printStackTrace();
					}
                	 System.out.println("Not in");
                	}
                	else {
                		Connect(e.getPlayer(), lines[1]);
                	      
                	}
                          }
            }
            }
}
public void Connect(Player p, String ServerName){

Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    ByteArrayOutputStream b = new ByteArrayOutputStream();
   DataOutputStream out = new DataOutputStream(b);
  try {
        out.writeUTF("Connect");
        out.writeUTF(ServerName);
     } catch (IOException ex) {
    }

     p.sendPluginMessage(this, "BungeeCord", b.toByteArray());

}
public void StartCountdownSign(){
	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

		@Override
		public void run() {
			for(Location loc : IsOn){
				if(loc.getBlock().getType() == Material.WALL_SIGN|| loc.getBlock().getType() == Material.SIGN_POST){
				Sign s = (Sign) loc.getBlock().getState();
				if(s.getLine(0).equalsIgnoreCase("- server -")){
					
				
				String[] Info = new GetInfos("").INFOS(IPSave.get(s.getLine(1))).split("_");
				Info[0] = ChatColor.translateAlternateColorCodes('&', Info[0]);
				s.setLine(2, Info[0]);
				System.out.println(Info[0]);
				s.setLine(3, Info[1] + "/" + Info[2]);
				System.out.println(Info[1] + " " + Info[2]);
					s.update();
			}
				}
		}
	}
		
	}, 10L, 5L);
}

}

