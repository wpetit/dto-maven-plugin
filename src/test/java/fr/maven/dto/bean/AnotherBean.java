package fr.maven.dto.bean;

import java.util.Date;
import java.util.List;

/**
 * Bean to test DTO generation.
 * 
 * @author Wilfried Petit
 * 
 */
public class AnotherBean {
	private String attribut1;
	private boolean attribut2;
	private Date attribut3;
	private List<String> attribut4;

	/**
	 * @return the attribut1
	 */
	public String getAttribut1() {
		return this.attribut1;
	}

	/**
	 * 
	 * Blablabla
	 * 
	 * @param attribut1
	 *            the attribut1 to set
	 */
	public void setAttribut1(final String attribut1) {
		this.attribut1 = attribut1;
	}

	/**
	 * @return the attribut2
	 */
	public boolean isAttribut2() {
		return this.attribut2;
	}

	/**
	 * @param attribut2
	 *            the attribut2 to set
	 */
	public void setAttribut2(final boolean attribut2) {
		this.attribut2 = attribut2;
	}

	/**
	 * @param attribut3
	 *            the attribut3 to set
	 */
	public void setAttribut3(final Date attribut3) {
		this.attribut3 = attribut3;
	}

	/**
	 * @return the attribut3
	 */
	public Date getAttribut3() {
		return this.attribut3;
	}

	/**
	 * @param attribut4
	 *            the attribut4 to set
	 */
	public void setAttribut4(final List<String> attribut4) {
		this.attribut4 = attribut4;
	}

	/**
	 * @return the attribut4
	 */
	public List<String> getAttribut4() {
		return this.attribut4;
	}

}
