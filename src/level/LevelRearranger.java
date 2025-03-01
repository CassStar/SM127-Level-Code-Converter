package level;

import java.util.ArrayList;

import main.ProgramLogger;
import main.Converter.AreaGrid;
import main.ProgramLogger.LogType;
import objects.SpawnPlayer1Object;
import objects.SpawnPlayer2Object;
import types.Vector2Type;
import util.Utility;

public class LevelRearranger {
	
	private String code;
	private LevelCode levelCode;
	private boolean placedMarioSpawn,placedLuigiSpawn;
	
	LevelRearranger(LevelCode base,String code,AreaGrid grid) {
		
		levelCode = base;
		
		int numberOfBrackets = 0,index = 0,numberOfAreas;
		ArrayList<Integer> arrayStartIndexes = new ArrayList<Integer>();
		
		while (code.substring(index,code.length()).indexOf('[') != -1) {
			
			index += code.substring(index,code.length()).indexOf('[')+1;
			arrayStartIndexes.add(index);
			numberOfBrackets++;
		}
		
		arrayStartIndexes.remove(0);
		
		numberOfAreas = numberOfBrackets-1;
		String[] areas = new String[numberOfAreas];
		
		for (int i = 0;i < areas.length-1;i++) {
			
			areas[i] = code.substring(arrayStartIndexes.get(i),arrayStartIndexes.get(i+1)-3);
		}
		
		areas[areas.length-1] = code.substring(arrayStartIndexes.get(areas.length-1),code.length()-1);
		
		Vector2Type levelDimensions = getLevelDimensions(areas,grid);
		
		String newLevelCode = createLevelFromGrid(code,areas,grid,levelDimensions);
		
		if (newLevelCode == null) {
			
			this.code = null;
			return;
			
		}
		
		this.code = new String(newLevelCode);
	}
	
	String getCode() {
		
		return code;
	}
	
	Vector2Type getLevelDimensions(String[] areas,AreaGrid areaGrid) {
		
		int[][] widthsAndHeights = getWidthsAndHeights(areas,areaGrid);
		
		int[] maxHeights = widthsAndHeights[1];
		int[] maxWidths = widthsAndHeights[0];
		
		int levelHeight = 0,levelWidth = 0;
		
		for (int width:maxWidths) {
			
			levelWidth += width;
		}
		
		for (int height:maxHeights) {
			
			levelHeight += height;
		}
		
		return new Vector2Type("V2"+levelWidth+"x"+levelHeight);
	}
	
	int[][] getWidthsAndHeights(String[] areas,AreaGrid areaGrid) {
		
		Vector2Type[] areaDimensions = new Vector2Type[areas.length];
		
		for (int i = 0;i < areaDimensions.length;i++) {
			
			areaDimensions[i] = new Vector2Type(areas[i].substring(0,areas[i].indexOf(',')));
		}
		
		int[][] grid = areaGrid.getGrid();
		
		int[] maxHeights = new int[grid.length];
		int[] maxWidths = new int[grid[0].length];
		
		
		for (int j = 0;j < grid.length;j++) {
			
			int maxHeight = 0;
			
			for (int i = 0;i < grid[0].length;i++) {
				
				double areaHeight = 0;
				
				try {
					double[] areaDimension = (double[]) areaDimensions[grid[j][i]].getValue();
					
					areaHeight = areaDimension[1];
					
				} catch(IndexOutOfBoundsException e) {	
				}
				
				if (areaHeight > maxHeight) {
					
					maxHeight = (int) areaHeight;
				}
			}
			
			maxHeights[j] = maxHeight;
		}
		
		for (int i = 0;i < grid[0].length;i++) {
			
			int maxWidth = 0;
			
			for (int j = 0;j < grid.length;j++) {
				
				double areaWidth = 0;
				
				try {
					double[] areaDimension = (double[]) areaDimensions[grid[j][i]].getValue();
					
					areaWidth = areaDimension[0];
					
				} catch(IndexOutOfBoundsException e) {	
				}
				
				if (areaWidth > maxWidth) {
					
					maxWidth = (int) areaWidth;
				}
			}
			
			maxWidths[i] = maxWidth;
		}
		
		return new int[][] {maxWidths,maxHeights};
	}
	
	String createLevelFromGrid(String code,String[] areas,AreaGrid areaGrid,Vector2Type levelDimensions) {
		
		ProgramLogger.logMessage("Creating new level code from grid:\n"+areaGrid.toString()+"\n",LogType.INFO);
		
		int currentIndex = 0,nextIndex = 0;
		
		nextIndex = code.indexOf(',');
		
		String preTileValues = getPreTileValues(code);
		
		String codeVersion = code.substring(currentIndex,nextIndex);
		
		code = code.substring(nextIndex+1,code.length());
		nextIndex = code.indexOf(',');
		
		String levelName = code.substring(currentIndex,nextIndex);
		
		StringBuilder levelCode = new StringBuilder();
		
		levelCode.append(codeVersion);
		levelCode.append(',');
		levelCode.append(levelName);
		levelCode.append(',');
		
		code = code.substring(nextIndex+1,code.length());
		nextIndex = code.indexOf(',');
		
		// More data needs to be added to the level code depending on the version
		if (Utility.versionGreaterThanVersion(codeVersion,"0.4.9")) {
			
			String levelAuthor = code.substring(currentIndex,nextIndex);
			
			code = code.substring(nextIndex+1,code.length());
			nextIndex = code.indexOf(',');
			
			String levelDescription = code.substring(currentIndex,nextIndex);
			
			code = code.substring(nextIndex+1,code.length());
			nextIndex = code.indexOf(',');
			
			String levelThumbnail = code.substring(currentIndex,nextIndex);
			
			code = code.substring(nextIndex+1,code.length());
			nextIndex = code.indexOf(']')+1;
			
			levelCode.append(levelAuthor);
			levelCode.append(',');
			levelCode.append(levelDescription);
			levelCode.append(',');
			levelCode.append(levelThumbnail);
			levelCode.append(',');
			
		} else {
			
			levelCode.append("Unkown");
			levelCode.append(',');
			levelCode.append("This%20level%20has%20no%20description.");
			levelCode.append(',');
			levelCode.append("");
			levelCode.append(',');
		}
		
		String levelHotBar = code.substring(currentIndex,nextIndex);
		
		levelCode.append(levelHotBar);
		levelCode.append(",[");
		levelCode.append(levelDimensions.toString());
		levelCode.append(',');
		levelCode.append(preTileValues);
		levelCode.append("~");
		
		ProgramLogger.logMessage("Re-arranging level tiles...",LogType.INFO);
		
		String allLevelTiles = getAllLevelTiles(areas.clone(),areaGrid);
		
		if (allLevelTiles == null) {
			
			return null;
		}
		
		ProgramLogger.logMessage("Done level tiles. Re-arranging level objects...",LogType.INFO);
		String allLevelObjects = getAllLevelObjects(areas.clone(),areaGrid);
		
		levelCode.append(allLevelTiles);
		levelCode.append("~");
		levelCode.append(allLevelObjects);
		levelCode.append(']');
		
		ProgramLogger.logMessage("Finisehd re-arranging level.",LogType.INFO);
		
		return levelCode.toString();
	}
	
	String getPreTileValues(String code) {
		
		code = code.substring(code.indexOf('[')+1);
		code = code.substring(code.indexOf('[')+1);
		
		code = code.substring(code.indexOf(',')+1);
		
		return code.substring(0,code.indexOf('~'));
	}
	
	String getAllLevelTiles(String[] areas,AreaGrid areaGrid) {
		
		StringBuilder tiles = new StringBuilder();
		
		int[][] widthsAndHeights = getWidthsAndHeights(areas,areaGrid);
		int[] widths = widthsAndHeights[0];
		int[] heights = widthsAndHeights[1];
		
		boolean exceedsMaximumBounds = getExceedsAreaBounds(widths,heights);
		
		if (exceedsMaximumBounds) {
			
			ProgramLogger.logMessage("Re-arranged level would exceed maximum area bounds!\n"
					+ "Areas can only be a max of 1500 units in width and height.\n"
					+ "Please re-arrange the areas in a way that does not exceed this limit.\n",LogType.ERROR);
			
			return null;
		}
		
		int[][] grid = areaGrid.getGrid();
		
		for (int row = 0;row < grid.length;row++) {
			
			ProgramLogger.logMessage("Getting row tiles...",LogType.DEBUG);
			ArrayList<LevelTile> rowTiles = getRowTiles(grid[row],areas,widths,heights[row]);
			
			ProgramLogger.logMessage("Condensing row tiles...",LogType.DEBUG);
			ArrayList<LevelTile> condensedTiles = condenseTilesArray(rowTiles);
			
			ProgramLogger.logMessage("Adding row tiles...",LogType.DEBUG);
			tiles.append(arrayToString(condensedTiles));
			
			tiles.append("%20");
		}
		
		tiles = combineRowTiles(tiles);
		
		return tiles.toString();
	}
	
	StringBuilder combineRowTiles(StringBuilder tiles) {
		
		StringBuilder combined = new StringBuilder();
		String[] rows = tiles.toString().split("%20");
		
		for (int layer = 0;layer < 4;layer++) {
			
			for (String row:rows) {
				
				String rowLayer = row.split("~")[layer];
				combined.append(rowLayer);
				combined.append(',');
			}
			
			combined.deleteCharAt(combined.length()-1);
			combined.append('~');
		}
		
		combined.deleteCharAt(combined.length()-1);
		
		return combined;
	}
	
	ArrayList<LevelTile> getRowTiles(int[] rowGrid,String[] areas,int[] widths,int height) {
		
		ArrayList<LevelTile> rowTiles = new ArrayList<LevelTile>();
		ArrayList<LevelTile> overflowTiles = new ArrayList<LevelTile>();
		String[] gridAreas = new String[rowGrid.length];
		int [] tildeCounter = new int[gridAreas.length];
		boolean[] layerTransition = new boolean[gridAreas.length],checkedHeight = new boolean[gridAreas.length];
		
		int[] areaWidths = new int[widths.length],areaHeights = new int[widths.length];
		int xValue = 0;
		
		for (int i = 0;i < rowGrid.length;i++) {
			
			if (rowGrid[i] >= 0 && areas[rowGrid[i]] != null) {
				
				int start = areas[rowGrid[i]].indexOf('~')+1;
				int end = areas[rowGrid[i]].lastIndexOf('~');
				int dimensionsEnd = areas[rowGrid[i]].indexOf(',');
				double[] dimensions = (double[]) new Vector2Type(areas[rowGrid[i]].substring(0,dimensionsEnd)).getValue();
				
				areaWidths[i] = (int) dimensions[0];
				areaHeights[i] = (int) dimensions[1];
				
				overflowTiles.add(null);
				gridAreas[i] = new String(areas[rowGrid[i]].substring(start,end));
				
			} else if (rowGrid[i] == -1) {
				
				areaWidths[i] = widths[i];
				areaHeights[i] = height;
				StringBuilder base = new StringBuilder();
				
				for (int k = 0;k < 4;k++) {
					
					base.append("000*");
					base.append(widths[i]*height);
					base.append('~');
				}
				
				base.deleteCharAt(base.length()-1);
				
				gridAreas[i] = base.toString();
				overflowTiles.add(null);
			}
		}
		
		while (hasTilesLeft(gridAreas,overflowTiles)) {
			
			for (int i = 0;i < rowGrid.length;) {
				
				if (!checkedHeight[i]) {
					
					if (areaHeights[i] != 0 && areaHeights[i] < height) {
						
						LevelTile buffer = new LevelTile("000*"+areaWidths[i]*(height-areaHeights[i]));
						
						overflowTiles.set(i,buffer.clone());
					}
					
					checkedHeight[i] = true;
				}
				
				if (overflowTiles.get(i) != null) {
					
					LevelTile overflowTile = overflowTiles.get(i).clone();
					int originalAmount = overflowTile.getTileAmount();
					
					overflowTile.setTileAmount(Math.min(originalAmount,areaWidths[i]-xValue));
					
					rowTiles.add(overflowTile.clone());
					overflowTiles.get(i).setTileAmount(overflowTiles.get(i).getTileAmount()-overflowTile.getTileAmount());
					xValue += overflowTile.getTileAmount();
					
					// Overflow tiles are empty.
					if (overflowTiles.get(i).getTileAmount() <= 0) {
						
						overflowTiles.set(i,null);
						
						// Reached the end of a row.
						if (xValue == areaWidths[i]) {
							
							// Area width less than column width.
							if (areaWidths[i] < widths[i]) {
								
								LevelTile buffer = new LevelTile("000*"+(widths[i]-areaWidths[i]));
								
								rowTiles.add(buffer.clone());
							}
							
							if (layerTransition[i] && valuesAreSame(tildeCounter) && valuesAreSame(overflowTiles)) {
								
								for (int j = 0;j < layerTransition.length;j++) {
									
									layerTransition[j] = false;
									checkedHeight[j] = false;
								}
								// Layer seperator.
								rowTiles.add(null);
							}
							
							xValue = 0;
							i++;
							continue;
						}
					
					}
					// Reached the end of a row.
					if (xValue == areaWidths[i]) {
						
						if (areaWidths[i] < widths[i]) {
							
							LevelTile buffer = new LevelTile("000*"+(widths[i]-areaWidths[i]));
							
							rowTiles.add(buffer.clone());
						}
						
						xValue = 0;
						i++;
						continue;
					}
				}
				
				int commaIndex = 0,tildeIndex = 0;
				
				if (gridAreas[i] != null) {
					
					commaIndex = gridAreas[i].indexOf(',');
					tildeIndex = gridAreas[i].indexOf('~');
				}
				
				if (gridAreas[i] == null && !layerTransition[i]) {
					
					rowTiles.add(new LevelTile("000*"+areaWidths[i]));
					xValue = 0;
					i++;
					
				} else if (layerTransition[i]) {
					
					if (valuesAreSame(tildeCounter) && valuesAreSame(overflowTiles)) {
						
						// Area width less than column width.
						if (areaWidths[i] < widths[i]) {
							
							LevelTile buffer = new LevelTile("000*"+(widths[i]-areaWidths[i]));
							
							rowTiles.add(buffer.clone());
						}
						
						for (int j = 0;j < layerTransition.length;j++) {
							
							layerTransition[j] = false;
							checkedHeight[j] = false;
						}
						// Layer seperator.
						rowTiles.add(null);
					}
					
					i++;
					
				} else if ((commaIndex < tildeIndex && commaIndex != -1) || tildeIndex == -1 && commaIndex != -1) {
					
					String tileString = gridAreas[i].substring(0,gridAreas[i].indexOf(','));
					gridAreas[i] = gridAreas[i].substring(gridAreas[i].indexOf(',')+1);
					
					LevelTile nextTile = new LevelTile(tileString);
					int originalAmount = nextTile.getTileAmount();
					
					nextTile.setTileAmount(Math.min(originalAmount,areaWidths[i]-xValue));
					
					rowTiles.add(nextTile.clone());
					xValue += nextTile.getTileAmount();
					
					// Reached end of row.
					if (xValue == areaWidths[i]) {
						
						// Area width less than column width.
						if (areaWidths[i] < widths[i]) {
							
							LevelTile buffer = new LevelTile("000*"+(widths[i]-areaWidths[i]));
							
							rowTiles.add(buffer.clone());
						}
						
						if (originalAmount-nextTile.getTileAmount() > 0) {
							
							LevelTile overflowTile = nextTile.clone();
							
							overflowTile.setTileAmount(originalAmount-nextTile.getTileAmount());
							overflowTiles.set(i,overflowTile);
						}
						
						xValue = 0;
						i++;
					}
					
				} else if (tildeIndex != -1) {
					
					String tileString = gridAreas[i].substring(0,gridAreas[i].indexOf('~'));
					gridAreas[i] = gridAreas[i].substring(gridAreas[i].indexOf('~')+1);
					
					LevelTile nextTile = new LevelTile(tileString);
					int originalAmount = nextTile.getTileAmount();
					
					nextTile.setTileAmount(Math.min(originalAmount,areaWidths[i]-xValue));
					
					rowTiles.add(nextTile.clone());
					xValue += nextTile.getTileAmount();
					
					// Reached end of row.
					if (xValue == areaWidths[i]) {
						
						// Area width less than column width.
						if (areaWidths[i] < widths[i]) {
							
							LevelTile buffer = new LevelTile("000*"+(widths[i]-areaWidths[i]));
							
							rowTiles.add(buffer.clone());
						}
						
						if (originalAmount-nextTile.getTileAmount() > 0) {
							
							LevelTile overflowTile = nextTile.clone();
							
							overflowTile.setTileAmount(originalAmount-nextTile.getTileAmount());
							overflowTiles.set(i,overflowTile);
						}
						
						layerTransition[i] = true;
						tildeCounter[i]++;
						
						if (overflowTiles.get(i) == null && valuesAreSame(tildeCounter) && valuesAreSame(overflowTiles)) {
							
							for (int j = 0;j < layerTransition.length;j++) {
								
								layerTransition[j] = false;
								checkedHeight[j] = false;
							}
							// Layer seperator.
							rowTiles.add(null);
						}
						
						xValue = 0;
						i++;
					}
				} else {
					
					String tileString = gridAreas[i];
					gridAreas[i] = null;
					
					LevelTile nextTile = new LevelTile(tileString);
					int originalAmount = nextTile.getTileAmount();
					
					nextTile.setTileAmount(Math.min(originalAmount,areaWidths[i]-xValue));
					
					rowTiles.add(nextTile.clone());
					xValue += nextTile.getTileAmount();
					
					// Reached end of row.
					if (xValue == areaWidths[i]) {
						
						// Area width less than column width.
						if (areaWidths[i] < widths[i]) {
							
							LevelTile buffer = new LevelTile("000*"+(widths[i]-areaWidths[i]));
							
							rowTiles.add(buffer.clone());
						}
						
						if (originalAmount-nextTile.getTileAmount() > 0) {
							
							LevelTile overflowTile = nextTile.clone();
							
							overflowTile.setTileAmount(originalAmount-nextTile.getTileAmount());
							overflowTiles.set(i,overflowTile);
						}
						
						xValue = 0;
						i++;
					}
				}
			}
		}
		
		return rowTiles;
	}
	
	boolean hasTilesLeft(String[] areas,ArrayList<LevelTile> overflow) {
		
		for (int i = 0;i < areas.length;i++) {
			
			if (areas[i] != null && areas[i].length() > 0) {
				
				return true;
			}
			
			if (overflow == null) {
				
				continue;
			}
			
			if (overflow.get(i) != null) {
				
				return true;
			}
		}
		
		return false;
	}
	
	boolean valuesAreSame(int[] values) {
		
		if (values == null || values.length == 0) {
			
			return false;
		}
		
		int compare = values[0];
		
		for (int i = 1;i < values.length;i++) {
			
			if (values[i] != compare) {
				
				return false;
			}
		}
		
		return true;
	}
	
	boolean valuesAreSame(ArrayList<LevelTile> values) {
		
		if (values == null || values.size() == 0) {
			
			return false;
		}
		
		boolean allNull = true;
		
		for (LevelTile value:values) {
			
			if (value != null) {
				
				allNull = false;
				break;
			}
		}
		
		return allNull;
	}
	
	ArrayList<LevelTile> condenseTilesArray(ArrayList<LevelTile> tiles) {
		
		ArrayList<LevelTile> newArray = new ArrayList<LevelTile>();
		LevelTile currentTile = null,previousTile = null;
		int totalAmount = 0;
		
		for (int i = 0;i < tiles.size();i++) {
			
			currentTile = tiles.get(i);
			
			if (currentTile == null) {
				
				previousTile.setTileAmount(totalAmount);
				newArray.add(previousTile.clone());
				newArray.add(null);
				totalAmount = 0;
				
			} else if (currentTile.getTileAmount() == 0) {
				
				previousTile.setTileAmount(totalAmount);
				
				newArray.add(previousTile.clone());
				
			} else if (previousTile == null || (currentTile.tileID == previousTile.tileID &&
					currentTile.tilePallete == previousTile.tilePallete)) {
				
				totalAmount += currentTile.getTileAmount();
				previousTile = currentTile.clone();
				
			} else {
				
				previousTile.setTileAmount(totalAmount);
				
				if (totalAmount > 0) {
					newArray.add(previousTile.clone());
				}
				
				totalAmount = currentTile.getTileAmount();
				previousTile = currentTile.clone();
			}
		}
		previousTile.setTileAmount(totalAmount);
		newArray.add(previousTile.clone());
		
		return newArray;
	}
	
	String arrayToString(ArrayList<LevelTile> condensedTiles) {
		
		StringBuilder output = new StringBuilder();
		
		for (int i = 0;i < condensedTiles.size();i++) {
			
			if (condensedTiles.get(i) == null) {
				
				// Delete extra comma, add tilde.
				output.deleteCharAt(output.length()-1);
				output.append('~');
				
			} else {
				
				output.append(condensedTiles.get(i).toString()+",");
			}
		}
		// Delete trailing comma.
		output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
	
	boolean getExceedsAreaBounds(int[] widths,int[] heights) {
		
		int max = 1500,total = 0;
		
		for (int width:widths) {
			
			total += width;
		}
		
		if (total > max) {
			
			return true;
		}
		
		total = 0;
		
		for (int height:heights) {
			
			total += height;
		}
		
		if (total > max) {
			
			return true;
		}
		
		return false;
	}
	
	String getAllLevelObjects(String[] areas,AreaGrid areaGrid) {
		
		StringBuilder objects = new StringBuilder();
		
		int[][] widthsAndHeights = getWidthsAndHeights(areas,areaGrid);
		int[] widths = widthsAndHeights[0];
		int[] heights = widthsAndHeights[1];
		
		int[][] grid = areaGrid.getGrid();
		
		int currentX,currentY = 0;
		
		for (int row = 0;row < grid.length;row++) {
			
			currentX = 0;
			
			for (int column = 0;column < grid[0].length;column++) {
				
				LevelObject[] areaObjects;
				
				if (grid[row][column] != -1) {
					
					areaObjects = getAreaObjects(areas[grid[row][column]],heights[row],currentX*32,currentY*32);
					
					String objectsString = arrayToString(areaObjects);
					
					if (objectsString.length() != 0) {
						objects.append(objectsString);
						objects.append('|');
					}
					
				} else {
					
					// No abjects exist in null areas.
				}
				
				currentX += widths[column];
			}
			
			currentY += heights[row];
		
		}
		
		objects.deleteCharAt(objects.length()-1);
		
		return objects.toString();
	}
	
	LevelObject[] getAreaObjects(String area,int rowHeight,int xOffset,int yOffset) {
		
		if (area == null) {
			
			return new LevelObject[] {};
		}
		
		int dimensionsEnd = area.indexOf(',');
		double[] dimensions = (double[]) new Vector2Type(area.substring(0,dimensionsEnd)).getValue();
		int areaHeight = (int) dimensions[1];
		
		LevelObject[] objects = null;
		
		for (int tildeCounter = 5;tildeCounter > 0;tildeCounter--) {
			
			area = area.substring(area.indexOf('~')+1);
		}
		
		try {
			
			objects = new AreaCode(levelCode).parseObjects(area);
			
		} catch (Exception e) {
			
			ProgramLogger.logMessage(e.getMessage(),LogType.ERROR);
		}
		
		for (LevelObject object:objects) {
			
			double[] currentPosition = object.getPosition();
			
			currentPosition[0] += xOffset;
			currentPosition[1] += yOffset;
			
			// Adjusting for differences between area height and overall row height.
			currentPosition[1] += (rowHeight-areaHeight)*32;
			
			object.setPosition(currentPosition);
		}
		return objects;
	}
	
	String arrayToString(LevelObject[] objects) {
		
		StringBuilder stringObjects = new StringBuilder();
		
		for (LevelObject object:objects) {
			
			if (object instanceof SpawnPlayer1Object) {
				
				if (placedMarioSpawn) {
					continue;
				}
				
				placedMarioSpawn = true;
				
			} else if (object instanceof SpawnPlayer2Object) {
				
				if (placedLuigiSpawn) {
					continue;
				}
				
				placedLuigiSpawn = true;
			}
			
			if (Utility.versionGreaterThanVersion(levelCode.conversionType.gameVersionFrom,"0.6.9")) {
				
				stringObjects.append(object.toString(true));
				
			} else {
				
				stringObjects.append(object.toString());
			}
			
			stringObjects.append('|');
		}
		
		if (!stringObjects.isEmpty()) {
			stringObjects.deleteCharAt(stringObjects.length()-1);
		}
		
		return stringObjects.toString();
	}
}