/**
 * 
 */
package fr.maven.dto.bean;

import java.util.List;

/**
 * Bean to test DTO generation.
 * 
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
	public void setBean(final Bean bean) {
		this.bean = bean;
	}

	/**
	 * @param beans
	 *            the beans to set
	 */
	public void setBeans(final List<Bean> beans) {
		this.beans = beans;
	}

	/**
	 * @return the beans
	 */
	public List<Bean> getBeans() {
		return this.beans;
	}

	/**
	 * @param strings
	 *            the strings to set
	 */
	public void setStrings(final List<String> strings) {
		this.strings = strings;
	}

	/**
	 * @return the strings
	 */
	public List<String> getStrings() {
		return this.strings;
	}

	/**
	 * @param beansList
	 *            the beansList to set
	 */
	public void setBeansList(final List<List<Bean>> beansList) {
		this.beansList = beansList;
	}

	/**
	 * @return the beansList
	 */
	public List<List<Bean>> getBeansList() {
		return this.beansList;
	}
}
