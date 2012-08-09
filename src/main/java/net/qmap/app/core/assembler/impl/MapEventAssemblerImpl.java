package net.qmap.app.core.assembler.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import net.qmap.app.core.assembler.MapEventAssembler;
import net.qmap.app.core.domain.MapEvent;
import net.qmap.app.core.dto.MapEventDto;

public class MapEventAssemblerImpl implements MapEventAssembler {

	@Override
	public MapEventDto toDto(MapEvent entity) {
		Preconditions.checkArgument(entity != null);
		
		final MapEventDto result = new MapEventDto();
		
		result.setId(entity.getId());
		result.setDate(entity.getDate());
		result.setPublishDate(entity.getPublishDate());
		result.setLatitude(BigDecimal.valueOf(entity.getLatitude()));
		result.setLongitude(BigDecimal.valueOf(entity.getLongitude()));
		result.setMagnitude(BigDecimal.valueOf(entity.getMagnitude()));
		result.setLocation(entity.getLocation());
		
		return result;
	}

	@Override
	public MapEvent toEntity(MapEventDto dto) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MapEventDto> toDto(Collection<MapEvent> entities) {
		Preconditions.checkArgument(entities != null);
		
		final List<MapEventDto> result = Lists.newArrayListWithCapacity(entities.size());
		for (MapEvent entity : entities) {
			
			result.add(toDto(entity));
		}
		return result;
	}

}
