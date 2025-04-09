package org.whania.block.entity.reactor;

import org.joml.Vector2i;
import org.whania.block.AirBlock;
import org.whania.block.Block;
import org.whania.block.blocks.reactor.*;
import org.whania.init.Config;
import org.whania.util.energy.containers.SimpleEnergyStorage;

import org.whania.util.MyPair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReactorControllerBlockEntity {

	public static final int RF_PER_PULSE = Config.rfPerPulse;
	public static final int ABSORBER_RATE = Config.absorberRate;
	public static final int VENT_BASE_RATE = Config.ventBaseRate;
	public static final int VENT_RELATIVE_RATE = Config.ventRelativeRate;
	private static final int REACTOR_STACK_HEIGHT = 1;

	private final HashMap<Vector2i, BaseReactorBlock> activeComponents = new HashMap<>();
			// 2d local position on the first layer containing the entity.reactor blocks
	private final HashMap<Vector2i, Integer> componentHeats = new HashMap<>();
			// same grid, contains the current heat of the component
	private final HashMap<Vector2i, ComponentStatistics> componentStats = new HashMap<>();
			// mainly for client displays, same grid

	public SimpleEnergyStorage energyStorage = new SimpleEnergyStorage();
	public boolean active = false;

	// heat is only used for entity.reactor rods and heat pipes
	// rods generate heat. Multi-cores and reflectors (expensive) change this
	// heat pipes move heat to themselves
	// vents remove heat from the hottest neighbor component
	// absorbers remove fixed heat amount from all neighboring blocks

	public ReactorControllerBlockEntity(Block[][] grid) {
		for(int i = 0; i < grid.length; i++) {
			var v = grid[i];
			for(int j = 0; j < v.length; j++) {
				var block = v[j];
				if(block instanceof BaseReactorBlock baseReactorBlock) {
					activeComponents.putIfAbsent(new Vector2i().add(i, j), baseReactorBlock);
					componentHeats.putIfAbsent(new Vector2i().add(i, j), 0);
				}
			}
		}
		enable();
	}
// ' ', S, P, V, R, 1, 2, 4

	public ReactorControllerBlockEntity(String[] grid) {
		for(int i = 0; i < grid.length; i++) {
			var v = grid[i];
			for(int j = 0; j < v.length(); j++) {
				var block = switch(v.charAt(j)) {
					case 'S' -> ReactorAbsorberBlock.INSTANCE;
					case 'P' -> ReactorHeatPipeBlock.INSTANCE;
					case 'V' -> ReactorHeatVentBlock.INSTANCE;
					case 'R' -> ReactorReflectorBlock.INSTANCE;
					case '1' -> ReactorRodBlock.REACTOR_ROD;
					case '2' -> ReactorRodBlock.REACTOR_DOUBLE_ROD;
					case '4' -> ReactorRodBlock.REACTOR_QUAD_ROD;
					default -> AirBlock.INSTANCE;
				};

				if(block instanceof BaseReactorBlock baseReactorBlock) {
					activeComponents.putIfAbsent(new Vector2i().add(i, j), baseReactorBlock);
					componentHeats.putIfAbsent(new Vector2i().add(i, j), 0);
				}
			}
		}
		enable();
	}

	public MyPair<List<MyPair<Vector2i, ComponentStatistics>>, Long> tick() {

		if(!active || activeComponents.isEmpty()) return null;

		var hottestHeat = 0;

		for(var localPos : activeComponents.keySet()) {
			var component = activeComponents.get(localPos);
			var componentHeat = componentHeats.get(localPos);

			if(component instanceof ReactorRodBlock rodBlock) {

				var receivedPulses = rodBlock.getInternalPulseCount();

				boolean hasFuel = rodBlock.isHasFuel();
				var heatCreated = 0;

				if(hasFuel) {
					// check how many pulses are received from neighbors / reflectors
					for(var neighborPos : getNeighborsInBounds(localPos, activeComponents.keySet())) {

						var neighbor = activeComponents.get(neighborPos);
						if(neighbor instanceof ReactorRodBlock neighborRod) {
							receivedPulses += neighborRod.getRodCount();
						} else if(neighbor instanceof ReactorReflectorBlock) {
							receivedPulses += rodBlock.getRodCount();
						}
					}

					if(!isDisabled()) {
						energyStorage.insertIgnoringLimit(
								(long) RF_PER_PULSE * receivedPulses * REACTOR_STACK_HEIGHT, false);
					}

					// generate heat per pulse
					heatCreated = (receivedPulses / 2 * receivedPulses + 4);
					componentHeat += heatCreated;

				} else {
					receivedPulses = 0;
				}

				componentStats.put(
						localPos, new ComponentStatistics((short) receivedPulses, componentHeat, (short) heatCreated));

			} else if(component instanceof ReactorHeatPipeBlock) {

				var sumGainedHeat = 0;

				// take heat in from neighbors
				for(var neighbor : getNeighborsInBounds(localPos, activeComponents.keySet())) {
					var neighborHeat = componentHeats.get(neighbor);
					if(neighborHeat <= componentHeat) continue;
					var diff = neighborHeat - componentHeat;
					var gainedHeat = Math.min(diff / 4 + 10, diff);
					neighborHeat -= gainedHeat;
					componentHeats.put(neighbor, neighborHeat);
					componentHeat += gainedHeat;
					sumGainedHeat += gainedHeat;
				}

				componentStats.put(localPos, new ComponentStatistics((short) 0, componentHeat, (short) sumGainedHeat));

			} else if(component instanceof ReactorAbsorberBlock) {

				var sumRemovedHeat = 0;

				// 是否存在吸热装置（是否可用）
				// 直接默认可以使用
				// take heat in from neighbors and remove it
				for(var neighbor : getNeighborsInBounds(localPos, activeComponents.keySet())) {
					var neighborHeat = componentHeats.get(neighbor);
					if(neighborHeat <= 0) continue;
					neighborHeat -= ABSORBER_RATE;
					sumRemovedHeat += ABSORBER_RATE;
					componentHeats.put(neighbor, neighborHeat);
				}

				componentStats.put(localPos, new ComponentStatistics((short) 0, 0, (short) sumRemovedHeat));
			} else if(component instanceof ReactorHeatVentBlock) {

				// remove heat from the hottest neighbor

				var hottestPos = localPos;
				var max = 0;
				for(var neighbor : getNeighborsInBounds(localPos, activeComponents.keySet())) {
					var neighborHeat = componentHeats.get(neighbor);
					if(neighborHeat <= max) continue;
					hottestPos = neighbor;
					max = neighborHeat;
				}

				var removed = 0;
				if(max != 0) {
					var neighborHeat = max;
					removed = Math.min(neighborHeat / VENT_RELATIVE_RATE + VENT_BASE_RATE, neighborHeat);
					neighborHeat -= removed;
					componentHeats.put(hottestPos, neighborHeat);
				}

				componentStats.put(localPos, new ComponentStatistics((short) 0, 0, (short) removed));

			}

			componentHeats.put(localPos, componentHeat);

			if(componentHeat > hottestHeat)
				hottestHeat = componentHeat;

		}

		return sendUINetworkData();

	}

	public void enable() {
		active = true;
	}

	private static Set<Vector2i> getNeighborsInBounds(Vector2i pos, Set<Vector2i> keys) {

		var res = new HashSet<Vector2i>(4);

		var a = new Vector2i(pos).add(-1, 0);
		if(keys.contains(a)) res.add(a);
		var b = new Vector2i(pos).add(0, 1);
		if(keys.contains(b)) res.add(b);
		var c = new Vector2i(pos).add(1, 0);
		if(keys.contains(c)) res.add(c);
		var d = new Vector2i(pos).add(0, -1);
		if(keys.contains(d)) res.add(d);

		return res;
	}

	public MyPair<List<MyPair<Vector2i, ComponentStatistics>>, Long> sendUINetworkData() {

		if(!active || activeComponents.isEmpty()) return null;

		var positionsFlat = activeComponents.keySet();
		return new MyPair<>(
				positionsFlat.stream().map(pos -> new MyPair<>(
						pos, componentStats.getOrDefault(pos, ComponentStatistics.EMPTY))).toList(),
				energyStorage.getAmount()
		);
	}

	public record ComponentStatistics(short receivedPulses, int storedHeat, short heatChanged) {
		public static final ComponentStatistics EMPTY = new ComponentStatistics((short) 0, -1, (short) 0);
	}

	private boolean isDisabled() {
		return !active;
	}
}
