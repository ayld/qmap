package net.qmap.app.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.qmap.app.core.domain.MapEvent;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class CloudMapEventDaoImpl extends MapEventDaoImpl {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(CloudMapEventDaoImpl.class);
	
	@Override
	protected List<MapEvent> eventsFromNativeQuery(String sql) {
		final Query query = getSession().createSQLQuery(sql);
		
		@SuppressWarnings("unchecked")
		final List<Object[]> rawResult = Optional.fromNullable((List<Object[]>) query.list()).or(new ArrayList<Object[]>(0));
		
		final List<MapEvent> result = Lists.newLinkedList();
		for (Object[] row : rawResult) {
			
			final MapEvent event = new MapEvent();
			event.setId((Integer) row[0]);
			
			Object rowValue = row[2];
			final Double latitude = Optional.fromNullable((Double) rowValue).or(Double.valueOf(0.0d));
			event.setLatitude(latitude);
			
			rowValue = row[4];
			final Double longitude = Optional.fromNullable((Double) rowValue).or(Double.valueOf(0.0d));
			event.setLongitude(longitude);
			
			rowValue = row[5];
			final Double magnitude = Optional.fromNullable((Double) rowValue).or(Double.valueOf(0.0d));
			event.setMagnitude(magnitude);
			
			event.setDate(null);
			
			rowValue = row[3];
			final String location = Optional.fromNullable(rowValue.toString()).or("");
			event.setLocation(location);
			
			rowValue = row[6];
			final Date publishedDate = Optional.fromNullable((Date) rowValue).or(new Date());
			event.setPublishDate(publishedDate);
			
			result.add(event);
		}
		
		return result;
	}
}
