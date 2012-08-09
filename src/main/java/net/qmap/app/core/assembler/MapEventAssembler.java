package net.qmap.app.core.assembler;

import java.util.Collection;
import java.util.List;

import net.qmap.app.core.domain.MapEvent;
import net.qmap.app.core.dto.MapEventDto;

public interface MapEventAssembler {
	
	public MapEventDto toDto(MapEvent entity);
	
	public List<MapEventDto> toDto(Collection<MapEvent> entities);
	
	public MapEvent toEntity(MapEventDto dto); 
}
