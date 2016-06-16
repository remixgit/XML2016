//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.16 at 01:30:05 AM CEST 
//


package rs.ac.uns.ftn.pravniakt;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Prilog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Prilog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RedniBrojPriloga" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="NazivPriloga" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PutanjaDoPriloga" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Prilog", propOrder = {
    "redniBrojPriloga",
    "nazivPriloga",
    "putanjaDoPriloga"
})
public class Prilog {

    @XmlElement(name = "RedniBrojPriloga", required = true)
    protected BigInteger redniBrojPriloga;
    @XmlElement(name = "NazivPriloga", required = true)
    protected String nazivPriloga;
    @XmlElement(name = "PutanjaDoPriloga", required = true)
    protected String putanjaDoPriloga;

    /**
     * Gets the value of the redniBrojPriloga property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRedniBrojPriloga() {
        return redniBrojPriloga;
    }

    /**
     * Sets the value of the redniBrojPriloga property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRedniBrojPriloga(BigInteger value) {
        this.redniBrojPriloga = value;
    }

    /**
     * Gets the value of the nazivPriloga property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazivPriloga() {
        return nazivPriloga;
    }

    /**
     * Sets the value of the nazivPriloga property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazivPriloga(String value) {
        this.nazivPriloga = value;
    }

    /**
     * Gets the value of the putanjaDoPriloga property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPutanjaDoPriloga() {
        return putanjaDoPriloga;
    }

    /**
     * Sets the value of the putanjaDoPriloga property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPutanjaDoPriloga(String value) {
        this.putanjaDoPriloga = value;
    }

}
