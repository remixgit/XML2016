//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.16 at 05:49:38 PM CEST 
//


package rs.ac.uns.ftn.amandman;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clan" type="{http://www.ftn.uns.ac.rs/amandman}clan"/>
 *       &lt;/sequence>
 *       &lt;attribute name="rbr" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deo", propOrder = {
    "clan"
})
public class Deo {

    @XmlElement(required = true)
    protected Clan clan;
    @XmlAttribute(name = "rbr")
    protected Integer rbr;

    /**
     * Gets the value of the clan property.
     * 
     * @return
     *     possible object is
     *     {@link Clan }
     *     
     */
    public Clan getClan() {
        return clan;
    }

    /**
     * Sets the value of the clan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Clan }
     *     
     */
    public void setClan(Clan value) {
        this.clan = value;
    }

    /**
     * Gets the value of the rbr property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRbr() {
        return rbr;
    }

    /**
     * Sets the value of the rbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRbr(Integer value) {
        this.rbr = value;
    }

}
