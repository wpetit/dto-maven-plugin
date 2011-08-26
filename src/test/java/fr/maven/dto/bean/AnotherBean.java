package fr.maven.dto.bean;

import java.util.Date;
import java.util.List;

/**
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
	public void setAttribut1(String attribut1) {
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
	public void setAttribut2(boolean attribut2) {
		this.attribut2 = attribut2;
	}

	/**
	 * @param attribut3
	 *            the attribut3 to set
	 */
	public void setAttribut3(Date attribut3) {
		this.attribut3 = attribut3;
	}

	/**
	 * @return the attribut3
	 */
	public Date getAttribut3() {
		return this.attribut3;
	}

}
