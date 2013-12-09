/**
 * NetworkKey.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.radware.defenseflow.dp.pojos.Classes.Networks;

/**
 * Identifies and entry in the Network table
 */
public class NetworkKey implements java.io.Serializable {
	/* The Name for the IP Object Rule, must be unique. */
	private java.lang.String	name;
	
	/* SubIndex. */
	private long				index;
	
	public NetworkKey() {
	}
	
	public NetworkKey(String name, long index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * Gets the name value for this NetworkKey.
	 * 
	 * @return name * The Name for the IP Object Rule, must be unique.
	 */
	public java.lang.String getName() {
		return name;
	}
	
	/**
	 * Sets the name value for this NetworkKey.
	 * 
	 * @param name
	 *            * The Name for the IP Object Rule, must be unique.
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	/**
	 * Gets the index value for this NetworkKey.
	 * 
	 * @return index * SubIndex.
	 */
	public long getIndex() {
		return index;
	}
	
	/**
	 * Sets the index value for this NetworkKey.
	 * 
	 * @param index
	 *            * SubIndex.
	 */
	public void setIndex(long index) {
		this.index = index;
	}
	
	private java.lang.Object	__equalsCalc	= null;
	
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof NetworkKey))
			return false;
		NetworkKey other = (NetworkKey) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other
						.getName()))) && this.index == other.getIndex();
		__equalsCalc = null;
		return _equals;
	}
	
	private boolean	__hashCodeCalc	= false;
	
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		_hashCode += new Long(getIndex()).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}
	
	// Type metadata
	private static org.apache.axis.description.TypeDesc	typeDesc	= new org.apache.axis.description.TypeDesc(
																			NetworkKey.class, true);
	
	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("radware.Classes.Networks", "NetworkKey"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("name");
		elemField.setXmlName(new javax.xml.namespace.QName("", "Name"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("index");
		elemField.setXmlName(new javax.xml.namespace.QName("", "Index"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}
	
	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}
	
	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}
	
	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}
	
	public String toString(){
		return name + ", " + index;
	}
}
