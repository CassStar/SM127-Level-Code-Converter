package util;

import java.util.ArrayList;

import level.AreaCode;
import level.LevelObject;
import main.ConversionType;
import objects.*;

public class ConversionUtility {
	
	public static LevelObject convertDownToZeroSixZero(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof EnchantedGearObject) {
			
			object = new EnchantedGearObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof RainbowStarObject) {
			
			object = new RainbowStarObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof GhostPepperObject) {
			
			object = new GhostPepperObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof SuperFeatherObject) {
			
			object = new SuperFeatherObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
		}
		
		return object;
	}
	
	public static Object[] convertDownToZeroSixOne(LevelObject object,ConversionType conversionType,AreaCode toArea,int i,
			int numberOfWarpPipes) throws Exception {
		
		if (object instanceof MushroomTopObject) {
			
			object = new MushroomTopObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
			
		} else if (object instanceof SignObject) {
			
			object = new SignObject(object.stringData.substring(0,
					object.stringData.length()-",BL0,BL0".length()),conversionType);
			
		} else if (object instanceof WarpPipeTopObject) {
			
			LevelObject destinationPipe = toArea.findMatchingPipe(object,i);
			
			object = new WarpPipeTopObject(""+object.objectID+",0,"+
					object.objectData[2].toString()+","+
					object.objectData[3].toString()+","+
					object.objectData[4].toString()+","+
					object.objectData[5].toString()+","+
					object.objectData[6].toString()+","+
					object.objectData[7].toString()+","+
					(destinationPipe == null?
					object.objectData[2].toString():
					destinationPipe.objectData[2].toString())+
					",BL1",conversionType);
			
			numberOfWarpPipes++;
			
		} else if (object instanceof DoorObject) {
			
			object = new DoorObject(""+object.objectID+",0,"+
					object.objectData[2].toString()+","+
					object.objectData[3].toString()+","+
					object.objectData[4].toString()+","+
					object.objectData[5].toString()+","+
					object.objectData[6].toString()+","+
					object.objectData[8].toString()+","+
					object.objectData[8].toString(),conversionType);
			
		} else if (object instanceof ArrowObject) {
			
			object = new ArrowObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
		}
		
		return new Object[] {object,numberOfWarpPipes};
	}
	
	public static LevelObject convertDownToZeroSevenZero(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof MetalPlatformObject) {
			
			object = new MetalPlatformObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
			
		} else if (object instanceof TwistedTreeTopObject) {
			
			object = new TwistedTreeTopObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
			
		} else if (object instanceof WarpPipeTopObject) {
			
			object = new WarpPipeTopObject(""+object.objectID+",0,"+
					object.objectData[2].toString()+","+
					object.objectData[3].toString()+","+
					object.objectData[4].toString()+","+
					object.objectData[5].toString()+","+
					object.objectData[6].toString()+","+
					object.objectData[7].toString()+","+
					object.objectData[8].toString()+","+
					object.objectData[9].toString(),
					conversionType);
			
		} else if (object instanceof GoombaObject) {
			
			object = new GoombaObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
			
		} else if (object instanceof DoorObject) {
			
			object = new DoorObject(""+object.objectID+",0,"+
					object.objectData[2].toString()+","+
					object.objectData[3].toString()+","+
					object.objectData[4].toString()+","+
					object.objectData[5].toString()+","+
					object.objectData[6].toString()+","+
					"STnone,STnone,"+
					object.objectData[7].toString(),conversionType);
			
		} else if (object instanceof WoodenPlatformObject) {
			
			object = new WoodenPlatformObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
			
		}
		
		return object;
	}
	
	public static LevelObject convertDownToZeroSevenTwo(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof ArrowObject) {
			
			// Remove last boolean value.
			object = new ArrowObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof HoverFluddObject) {
			
			// Remove last boolean value.
			object = new HoverFluddObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof RocketFluddObject) {
			
			// Remove last boolean value.
			object = new RocketFluddObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof TurboFluddObject) {
			
			// Remove last boolean value.
			object = new TurboFluddObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof SmallWaterBottleObject) {
			
			// Remove last boolean value.
			object = new SmallWaterBottleObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof LargeWaterBottleObject) {
			
			// Remove last boolean value.
			object = new LargeWaterBottleObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof KoopaTroopaObject) {
			
			// Remove last boolean value.
			object = new KoopaTroopaObject(object.stringData.substring(0,
					object.stringData.length()-",BL0".length()),conversionType);
			
		} else if (object instanceof RecoveryHeartObject) {
			
			// Remove last 2 values.
			object = new RecoveryHeartObject(object.stringData.substring(
					0,object.stringData.indexOf(",CL")),conversionType);
			
		} else if (object instanceof MovingPlatformObject) {
			
			// Remove last value.
			object = new MovingPlatformObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",C2")),conversionType);
			
		} else if (object instanceof OnOffControlledMovingPlatformObject) {
			
			// Remove last value.
			object = new OnOffControlledMovingPlatformObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",C2")),conversionType);
			
		}
		
		return object;
	}
	
	public static LevelObject convertDownToZeroEightZero(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof SawBladeObject) {
			
			// Remove last value.
			object = new SawBladeObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",FL")),conversionType);
			
		} else if (object instanceof WarpZoneObject) {
			
			// Remove last value.
			object = new WarpZoneObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",BL")),conversionType);
			
		} else if (object instanceof OnOffControlledMovingPlatformObject) {
			
			// Remove last value.
			object = new OnOffControlledMovingPlatformObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",FL")),conversionType);
			
		} else if (object instanceof CheckpointObject) {
			
			// Remove last value.
			object = new CheckpointObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",BL")),conversionType);
			
		} else if (object instanceof FluidControllerObject) {
			
			// Remove last two values.
			object = new FluidControllerObject(object.stringData.substring(
					0,object.stringData.substring(0,object.stringData.lastIndexOf(",FL")).lastIndexOf(",FL")),
					conversionType);
			
		} else if (object instanceof ArrowObject) {
			
			// Remove last two values.
			object = new ArrowObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",BL")),conversionType);
			
		} else if (object instanceof MovingPlatformObject) {
			
			// Remove last value.
			object = new MovingPlatformObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",FL")),conversionType);
			
		} else if (object instanceof DoorObject) {
			
			// Remove last value.
			object = new DoorObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",BL")),conversionType);
			
		} else if (object instanceof CannonObject) {
			
			// Remove last value.
			object = new CannonObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",FL")),conversionType);
			
		} else if (object instanceof BulletBillBlasterObject) {
			
			// Remove last value.
			object = new BulletBillBlasterObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",FL")),conversionType);
			
		} else if (object instanceof WarpPipeTopObject) {
			
			// Remove last value.
			object = new WarpPipeTopObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",BL")),conversionType);
			
		} else if (object instanceof MushroomTopObject) {
			
			// Remove last two values.
			object = new MushroomTopObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",BL")),conversionType);
			
		} else if (object instanceof ShineSpriteObject) {
			
			// Remove last value.
			object = new ShineSpriteObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",IT")),conversionType);
			
		}
		
		return object;
	}
	
	public static LevelObject convertDownToZeroNineZero(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof BonePlatformObject) {
			
			// Remove last value.
			object = new BonePlatformObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",CL")),conversionType);
			
		} else if (object instanceof ConditionLockedDoorObject) {
			
			// Remove last two values.
			object = new ConditionLockedDoorObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",ST")),conversionType);
			
		} else if (object instanceof PearlPlatformObject) {
			
			// Remove last value.
			object = new PearlPlatformObject(object.stringData.substring(
					0,object.stringData.lastIndexOf(",CL")),conversionType);
			
		}
		
		return object;
	}
	
	public static LevelObject convertUpToZeroSixOne(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof EnchantedGearObject) {
			
			object = new EnchantedGearObject(object.stringData+",BL1",conversionType);
			
		} else if (object instanceof RainbowStarObject) {
			
			object = new RainbowStarObject(object.stringData+",BL1",conversionType);
			
		} else if (object instanceof GhostPepperObject) {
			
			object = new GhostPepperObject(object.stringData+",BL1",conversionType);
			
		} else if (object instanceof SuperFeatherObject) {
			
			object = new SuperFeatherObject(object.stringData+",BL1",conversionType);
		}
		
		return object;
	}
	
	public static Object[] convertUpToZeroSevenZero(LevelObject object,ConversionType conversionType,AreaCode toArea,
			ArrayList <LevelObject> postConversionAdditions,int numberOfWarpPipes) throws Exception {
		
		if (object instanceof MushroomTopObject) {
			
			object = new MushroomTopObject(object.stringData+",CL1x0x0",
					conversionType);
			
		} else if (object instanceof SignObject) {
			
			object = new SignObject(object.stringData+",BL0,BL0",conversionType);
			
		} else if (object instanceof WarpPipeTopObject) {
			
			double[] pipeDestinationCoordinates = (
					double[]) object.objectData[8].getValue(),
					pipeCoordinates = (double[]) object.objectData[2].getValue();
			
			object = new WarpPipeTopObject(""+object.objectID+","+
					object.objectData[2].toString()+","+
					object.objectData[3].toString()+","+
					object.objectData[4].toString()+","+
					object.objectData[5].toString()+","+
					object.objectData[6].toString()+","+
					object.objectData[7].toString()+","+
					"STWarpPipe"+numberOfWarpPipes+","+
					"CL0x1x0,"+"BL1",conversionType);
			
			if (!Utility.areSameVectors(pipeCoordinates,pipeDestinationCoordinates)) {
				
				LevelObject destinationPipe = new WarpPipeTopObject(object.stringData,
						conversionType);
				
				destinationPipe.objectData[2].setValue(new double[] {
						pipeDestinationCoordinates[0],
						pipeDestinationCoordinates[1]
				});
				
				postConversionAdditions.add(destinationPipe);
			}
			
			numberOfWarpPipes++;
			
		} else if (object instanceof DoorObject) {
			
			toArea.normalizeDoors();
			
			object = new DoorObject(""+object.objectID+",0,"+
					object.objectData[2].toString()+","+
					object.objectData[3].toString()+","+
					object.objectData[4].toString()+","+
					object.objectData[5].toString()+","+
					object.objectData[6].toString()+","+
					"STnone,STnone,"+
					object.objectData[8].toString(),conversionType);
			
		} else if (object instanceof ArrowObject) {
			
			object = new ArrowObject(object.stringData+",CL1x0x0",conversionType);
		}
		
		return new Object[] {object,postConversionAdditions,numberOfWarpPipes};
	}
	
	public static LevelObject convertUpToZeroSevenTwo(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof MetalPlatformObject) {
			
			object = new MetalPlatformObject(object.stringData+",CL1x1x1",
					conversionType);
			
		} else if (object instanceof TwistedTreeTopObject) {
			
			object = new TwistedTreeTopObject(object.stringData+",CL0.97x0.5x0.16",
					conversionType);
			
		} else if (object instanceof WarpPipeTopObject) {
			
			object = new WarpPipeTopObject(object.stringData+",BL1",
					conversionType);
			
		} else if (object instanceof GoombaObject) {
			
			object = new GoombaObject(object.stringData+",CL1x0x0",conversionType);
			
		} else if (object instanceof DoorObject) {
			
			object = new DoorObject(object.stringData+",BL1",
					conversionType);
			
		} else if (object instanceof WoodenPlatformObject) {
			
			object = new WoodenPlatformObject(object.stringData+",CL1x0x0",conversionType);
		}
		
		return object;
	}
	
	public static LevelObject convertUpToZeroEightZero(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof TwistedTreeTopObject) {
			
			TwistedTreeTopObject compareObject = (TwistedTreeTopObject) object;
			double[] colour = compareObject.colour;
			
			// Testing if the colour is set to default.
			if (colour[0] == 0.97 && colour[1] == 0.5 && colour[2] == 0.16) {
				
				// Properly converting the colour to the new default for 0.8.0 :)
				compareObject.colour = new double[] {1,0,0};
			}
			
			object = compareObject;
		}
		
		return object;
	}
	
	public static LevelObject convertUpToZeroNineZero(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof SawBladeObject) {
			
			// Add last value
			object = new SawBladeObject(object.stringData+",FL0",conversionType);
			
		} else if (object instanceof WarpZoneObject) {
			
			// Add last value.
			object = new WarpZoneObject(object.stringData+",BL0",conversionType);
			
		} else if (object instanceof OnOffControlledMovingPlatformObject) {
			
			// Add last value.
			object = new OnOffControlledMovingPlatformObject(object.stringData+",FL0",conversionType);
			
		} else if (object instanceof CheckpointObject) {
			
			// Add last value.
			object = new CheckpointObject(object.stringData+",BL1",conversionType);
			
		} else if (object instanceof FluidControllerObject) {
			
			// Add last two values.
			object = new FluidControllerObject(object.stringData+",FL0,FL0",conversionType);
			
		} else if (object instanceof ArrowObject) {
			
			// Add last two values.
			object = new ArrowObject(object.stringData+",BL0,CL1x1x1x1",conversionType);
			
		} else if (object instanceof MovingPlatformObject) {
			
			// Add last value.
			object = new MovingPlatformObject(object.stringData+",FL0",conversionType);
			
		} else if (object instanceof DoorObject) {
			
			// Add last value.
			object = new DoorObject(object.stringData+",BL0",conversionType);
			
		} else if (object instanceof CannonObject) {
			
			// Add last value.
			object = new CannonObject(object.stringData+",FL1.5",conversionType);
			
		} else if (object instanceof BulletBillBlasterObject) {
			
			// Add last value.
			object = new BulletBillBlasterObject(object.stringData+",FL0",conversionType);
			
		} else if (object instanceof WarpPipeTopObject) {
			
			// Add last value.
			object = new WarpPipeTopObject(object.stringData+",BL0",conversionType);
			
		} else if (object instanceof MushroomTopObject) {
			
			// Add last two values.
			object = new MushroomTopObject(object.stringData+",BL0,IT650",conversionType);
			
		} else if (object instanceof ShineSpriteObject) {
			
			// Add last value.
			object = new ShineSpriteObject(object.stringData+",IT0",conversionType);
			
		}
		
		return object;
	}
	
	public static LevelObject convertUpToZeroNineOne(LevelObject object,ConversionType conversionType) throws Exception {
		
		if (object instanceof BonePlatformObject) {
			
			// Add last value.
			object = new BonePlatformObject(object.stringData+",CL1x1x1x1",conversionType);
			
		} else if (object instanceof ConditionLockedDoorObject) {
			
			// Add last two values.
			object = new ConditionLockedDoorObject(object.stringData+
					",STSorry%21%20You%20need%20%7bnum%7d%20%7bcol%7d%20to%20open%20this%20door%21,BL0",conversionType);
			
		} else if (object instanceof PearlPlatformObject) {
			
			// Add last value.
			object = new PearlPlatformObject(object.stringData+",CL1x1x1x1",conversionType);
			
		}
		
		return object;
	}
}
