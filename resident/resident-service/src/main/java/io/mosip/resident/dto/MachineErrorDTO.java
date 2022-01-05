package io.mosip.resident.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"message", "errorCode"})
public class MachineErrorDTO implements Serializable {

    private static final long serialVersionUID = -5261464773892046294L;

    /**
     * The message.
     */
    private String message;

    /**
     * The errorcode.
     */
    private String errorCode;

}