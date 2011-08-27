/**
 * 
 */
package fr.maven.dto.bean;

import java.util.List;
import java.util.Map;

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

	private Map<Bean, AnotherBean> beansMap;

	private Bean[] beanArray;

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

	/**
	 * @param beansMap
	 *            the beansMap to set
	 */
	public void setBeansMap(final Map<Bean, AnotherBean> beansMap) {
		this.beansMap = beansMap;
	}

	/**
	 * @return the beansMap
	 */
	public Map<Bean, AnotherBean> getBeansMap() {
		return this.beansMap;
	}

	/**
	 * @param beanArray
	 *            the beanArray to set
	 */
	public void setBeanArray(final Bean[] beanArray) {
		this.beanArray = beanArray;
	}

	/**
	 * @return the beanArray
	 */
	public Bean[] getBeanArray() {
		return this.beanArray;
	}
}
