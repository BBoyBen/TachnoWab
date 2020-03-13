package fr.noumeme.tachnowab.util;

import java.util.UUID;

public class ChangerMdp {
	
	private UUID id;
	private String odlMdp;
	private String newMdp;
	
	public ChangerMdp(UUID id, String odlMdp, String newMdp) {
		super();
		this.id = id;
		this.odlMdp = odlMdp;
		this.newMdp = newMdp;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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
