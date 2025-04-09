package org.whania;


import org.whania.block.AirBlock;
import org.whania.block.Block;
import org.whania.block.blocks.reactor.ReactorAbsorberBlock;
import org.whania.block.blocks.reactor.ReactorHeatPipeBlock;
import org.whania.block.blocks.reactor.ReactorReflectorBlock;
import org.whania.block.blocks.reactor.ReactorRodBlock;
import org.whania.block.entity.reactor.ReactorControllerBlockEntity;

import java.util.HashSet;
import java.util.List;

public class Main {

	private static List<ReactorControllerBlockEntity> getBlock() {
		var blocks1 = new Block[][]{
				{
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorReflectorBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE
				},
				{
						ReactorAbsorberBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorRodBlock.REACTOR_QUAD_ROD,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorAbsorberBlock.INSTANCE
				},
				{
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE
				},
				{
						AirBlock.INSTANCE,
						ReactorAbsorberBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorAbsorberBlock.INSTANCE,
						AirBlock.INSTANCE
				}
		};
		var blocks2 = new String [] {
				"PPRPP",
				"SP4PS",
				"PPPPP",
				"PSPSP",
		};
		return List.of(new ReactorControllerBlockEntity(blocks1), new ReactorControllerBlockEntity(blocks2));
	}

	public static void main(String[] args) {
		var controllers = getBlock();
		var totalSimulation = 10000;
		HashSet<String> hash = new HashSet<>();
		for(ReactorControllerBlockEntity controller : controllers) {
			System.out.println("Tick, MaxStoredHeat, MaxHeatChanged, ProducedEnergy");
			for(int i = 0; i < totalSimulation; i++) {
				var data = controller.tick();
				var elem = data.getLeft().toString();
				if(hash.contains(elem)) {
					System.out.printf("Cycled at tick %d:%n", i);
					System.out.println(elem);
					var energy = data.getRight();
					var maxStoredHeat = data.getLeft().stream().map(
							each -> each.getRight().storedHeat()
					).max(Long::compare).orElse(-1);
					var maxHeatChanged = data.getLeft().stream().map(
							each -> each.getRight().heatChanged()
					).max(Long::compare).orElse((short) -1);
					System.out.printf("%d, %d, %d, %d%n", i, maxStoredHeat, maxHeatChanged, energy);
					break;
				}
				hash.add(elem);
			}
			System.out.println("-----");
			hash.clear();
		}

	}

}
