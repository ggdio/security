package br.com.ggdio.security.infrastructure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity for recovering sysdate from database
 * 
 * @author Guilherme Dio
 *
 */
@Entity
public class SysdateEntity {

	@Id
    @Column(name="DT_SYSDATE")
    @Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public SysdateEntity() {
		
	}
	
	public Date getDate() {
		return (Date)date.clone();
	}
	
	public long getTimestamp() {
		return getDate().getTime();
	}
	
	public void setDate(Date date) {
		this.date = (Date)date.clone();
	}
	
}
