package fr.maven.dto.bean;

/**
 * 
 */

/**
 * @author Wilfried Petit
 * 
 */
public class Bean {
	private String attribut1;
	private boolean attribut2;
	private String a;
	private static String B;

	/**
	 * @return the attribut1
	 */
	public String getAttribut1() {
		return this.attribut1;
	}

	/**
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
	 * @param a
	 *            the a to set
	 */
	public void setA(final String a) {
		this.a = a;
	}

	/**
	 * @return the a
	 */
	public String getA() {
		return this.a;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public static void setB(final String b) {
		Bean.B = b;
	}

	/**
	 * @return the b
	 */
	public static String getB() {
		return Bean.B;
	}

}
