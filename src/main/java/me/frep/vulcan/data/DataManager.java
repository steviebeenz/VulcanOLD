package me.frep.vulcan.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private List<PlayerData> dataPlayers;

    public DataManager() {
        dataPlayers = new ArrayList<>();
    }

    public void createPlayerData(Player p) {
        dataPlayers.add(new PlayerData(p));
    }

    public void removePlayerData(PlayerData p) {
        dataPlayers.remove(p);
    }

    public PlayerData getPlayerData(Player p) {
        for (PlayerData data : dataPlayers) {
            if (data.player == p) return data;
        } return null;
    }

    public List<PlayerData> getDataPlayers() {
        return dataPlayers;
    }
}
