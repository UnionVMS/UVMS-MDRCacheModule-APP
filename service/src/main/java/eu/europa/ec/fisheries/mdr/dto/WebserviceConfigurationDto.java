package eu.europa.ec.fisheries.mdr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebserviceConfigurationDto {
    
    String wsdlLocation;
    String webserviceName;
    String webserviceNamespace;
    
}