/**
 * 
 */
package fr.maven.dto.bean;

import java.util.List;

/**
 * @author Wilfried Petit
 * 
 */
public class Bean2 {

	private Bean bean;

	private List<Bean> beans;

	private List<List<Bean>> beansList;

	private List<String> strings;

	/**
	 * @return the bean
	 */
	public Bean getBean() {
		return this.bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(Bean bean) {
		this.bean = bean;
	}
}
