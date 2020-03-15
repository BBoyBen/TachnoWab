package fr.noumeme.tachnowab.util;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChangerMdp {
	
	@Id
	private UUID id;
	private UUID idUtil;
	private String odlMdp;
	private String newMdp;
	
	public ChangerMdp() {
		
	}
	
	public ChangerMdp(UUID idUtil, String odlMdp, String newMdp) {
		super();
		this.idUtil = idUtil;
		this.odlMdp = odlMdp;
		this.newMdp = newMdp;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIdUtil() {
		return idUtil;
	}

	public void setIdUtil(UUID idUtil) {
		this.idUtil = idUtil;
	}

	public String getOdlMdp() {
		return odlMdp;
	}

	public void setOdlMdp(String odlMdp) {
		this.odlMdp = odlMdp;
	}

	public String getNewMdp() {
		return newMdp;
	}

	public void setNewMdp(String newMdp) {
		this.newMdp = newMdp;
	}	
	
}
