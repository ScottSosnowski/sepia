package edu.cwru.sepia.model.state;

public class ResourceNodeType {
	private String name;
	private ResourceType resource;
	
	public ResourceNodeType(String name, ResourceType resource) {
		this.name = name;
		this.resource = resource;
	}
	
	public String getName() {
		return name;
	}
	
	public ResourceType getResource() {
		return resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceNodeType other = (ResourceNodeType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResourceNodeType [name=" + name + "]";
	}
	
	
}
