package fr.maven.dto.bean;

public class BeanExample {

	private AnotherBean bean;

	private String beanName;

	public AnotherBean getBean() {
		return this.bean;
	}

	public void setBean(AnotherBean bean) {
		this.bean = bean;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return this.beanName;
	}
}
