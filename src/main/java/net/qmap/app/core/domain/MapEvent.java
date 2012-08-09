package net.qmap.app.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.Objects;

@Entity
@Table(name = "event")
public class MapEvent {

	private Integer id;
	private double latitude;
	private double longitude;
	private double magnitude;
	private Date date;
	private Date publishDate;
	private String location;
	
	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "published_date")
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "latitude")
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "longitude")
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "magnitude")
	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object that) {
		if (!(that instanceof MapEvent)) {
			
			return false;
		}
		final MapEvent other = (MapEvent) that;
		
		if (!Objects.equal(this.id, other.getId())) {
			
			return false;
		}
		
		if (!Objects.equal(this.latitude, other.getLatitude())) {
			
			return false;
		}
		
		if (!Objects.equal(this.longitude, other.getLongitude())) {
			
			return false;
		}
		
		if (!Objects.equal(this.magnitude, other.getMagnitude())) {
			
			return false;
		}
		
		if (!Objects.equal(this.location, other.getLocation())) {
			
			return false;
		}
		
		if (!Objects.equal(this.date, other.getDate())) {
			
			return false;
		}
		
		if (!Objects.equal(this.publishDate, other.getPublishDate())) {
			
			return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
				id, 
				latitude, 
				longitude,
				location,
				magnitude,
				publishDate,
				date
			);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("latitude", latitude)
			.add("longitude", longitude)
			.add("magnitude", magnitude)
			.add("date", date)
			.toString();
	}
}
