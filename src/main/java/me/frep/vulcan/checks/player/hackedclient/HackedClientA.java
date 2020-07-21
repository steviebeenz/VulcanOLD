package me.frep.vulcan.checks.player.hackedclient;

import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;

public class HackedClientA extends Check {

    public HackedClientA() {
        super("HackedClientA", "Hacked Client (Type A)", CheckType.OTHER, true, true, 1);
    }

}
