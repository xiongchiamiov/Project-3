package roundup;

import gridgame.HallOfFameEntry;

public class RoundupHallOfFameEntry
{
	public static HallOfFameEntry<String> generate(int seconds, int gameNumber, String moveList, String name)
	{
		return new HallOfFameEntry<String>(seconds / 60 + ":" + String.format("%02d", seconds % 60) + " " + gameNumber + " " + moveList, name);
	}
}

