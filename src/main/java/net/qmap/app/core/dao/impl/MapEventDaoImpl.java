package net.qmap.app.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.qmap.app.core.dao.MapEventDao;
import net.qmap.app.core.domain.MapEvent;
import net.qmap.app.core.enums.EventPeriod;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.base.Strings;

public class MapEventDaoImpl implements MapEventDao {

	private static Logger LOG = LoggerFactory.getLogger(MapEventDaoImpl.class);
	
	private final static Map<EventPeriod, String> PERIODS;
	
	static {
		PERIODS = ImmutableMap.of(EventPeriod.DAY, "1 day", 
				          EventPeriod.HOUR, "1 hour",
				          EventPeriod.TWO_HOURS, "2 hour",
				          EventPeriod.SIX_HOURS, "6 hour",
				          EventPeriod.TWELVE_HOURS, "12 hour"
	        );
		
	}
	
	private SessionFactory sessionFactory;
	
	
	@Override
	public void create(MapEvent event) {
		LOG.debug("Creating: " + event);
		
		final Session session = getSession();
		final Integer savedEventId = (Integer) session.save(event);
		
		LOG.debug("Created MapEvent with id: " + savedEventId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MapEvent> getEventsByPeriodAndMagnitude(EventPeriod period, double minMagnitude) {
		final String sql = "select id from event where published_date > (current_timestamp - interval '" + PERIODS.get(period) + "') and magnitude >= " + minMagnitude;
		
		final Query sqlQuery = getSession().createSQLQuery(sql);
		
		final List<Integer> eventIds = sqlQuery.list();
		final Query hqlQuery = getSession().createQuery("from MapEvent as me where me.id in (" + idsToInClause(eventIds) + ")");
		
		return Optional.fromNullable(hqlQuery.list()).or(new ArrayList<MapEvent>(0));
	}

	private String idsToInClause(List<Integer> eventIds) {
		final StringBuilder resultBuffer = new StringBuilder();
		final String delimiter = ",";
		for (Integer id : eventIds) {
			
			resultBuffer.append(id).append(delimiter);
		}
		String result = resultBuffer.toString();
		
		final boolean resultsEndsWithDelimiter = Strings.commonSuffix(result, delimiter).length() > 0;
		if (result.length() > 0 && resultsEndsWithDelimiter) {
			
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	protected List<MapEvent> eventsFromNativeQuery(final String sql) {
		final Query query = getSession().createSQLQuery(sql);
		
		@SuppressWarnings("unchecked")
		final List<Object[]> rawResult = Optional.fromNullable((List<Object[]>) query.list()).or(new ArrayList<Object[]>(0));
		
		final List<MapEvent> result = Lists.newLinkedList();
		final Double zero = Double.valueOf(0.0d);
		for (Object[] row : rawResult) {
			
			final MapEvent event = new MapEvent();
			event.setId((Integer) row[0]);
			
			Object rowValue = row[1];
			final Double latitude = Optional.fromNullable((Double) rowValue).or(zero);
			event.setLatitude(latitude);
			
			rowValue = row[2];
			final Double longitude = Optional.fromNullable((Double) rowValue).or(zero);
			event.setLongitude(longitude);
			
			rowValue = row[3];
			final Double magnitude = Optional.fromNullable((Double) rowValue).or(zero);
			event.setMagnitude(magnitude);
			
			event.setDate(null);
			
			rowValue = row[5];
			final String location = Optional.fromNullable(rowValue.toString()).or("");
			event.setLocation(location);
			
			rowValue = row[6];
			final Date publishedDate = Optional.fromNullable((Date) rowValue).or(new Date());
			event.setPublishDate(publishedDate);
			
			result.add(event);
		}
		return result;
	}

	@Override
	public MapEvent read(Integer id) {
		return (MapEvent) getSession().load(MapEvent.class, id);
	}
	
	@Override
	public Date getLastEventPublishedDate() {
		final String sql = "select max(published_date) as date from event";
		final Query query = getSession().createSQLQuery(sql);
		
		return Optional.fromNullable((Date) query.uniqueResult()).orNull();
	}
	
	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	// IoC
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
