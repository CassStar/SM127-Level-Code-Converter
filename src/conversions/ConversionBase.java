package conversions;

import level.*;

public interface ConversionBase {
	
	public AreaCode convertBackerBG(AreaCode fromArea,AreaCode toArea);
	public AreaCode convertFronterBG(AreaCode fromArea,AreaCode toArea);
	public AreaCode convertMusicIDs(AreaCode fromArea,AreaCode toArea);
	public LevelTile[] convertTiles(AreaCode toArea,LevelTile[] tileArray);
	public Object[] convertObjects(AreaCode toArea,LevelObject[] objectArray,boolean[] conversionsDone) throws Exception;
}
