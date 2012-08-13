package net.qmap.app.core.service.impl;

import java.util.List;

import net.qmap.app.core.assembler.MapEventAssembler;
import net.qmap.app.core.dao.MapEventDao;
import net.qmap.app.core.dto.MapEventDto;
import net.qmap.app.core.enums.EventPeriod;
import net.qmap.app.core.service.MapService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class MapServiceImpl implements MapService {

	private MapEventDao dao;
	private MapEventAssembler assembler;
	
	@Override
	@Transactional
	public List<MapEventDto> loadEventsByPeriodAndMagnitude(EventPeriod period, double minMagnitude) {
		return assembler.toDto(dao.getEventsByPeriodAndMagnitude(period, minMagnitude));
	}

	@Override
	@Transactional
	public MapEventDto read(Integer id) {
		return assembler.toDto(dao.read(id));
	}

	// IoC

	@Required
	public void setDao(MapEventDao dao) {
		this.dao = dao;
	}

	@Required
	public void setAssembler(MapEventAssembler assembler) {
		this.assembler = assembler;
	}
}
