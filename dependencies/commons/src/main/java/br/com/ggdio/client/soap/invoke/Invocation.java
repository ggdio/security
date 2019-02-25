package br.com.ggdio.client.soap.invoke;

import br.com.ggdio.client.common.model.TO;
import br.com.ggdio.client.soap.model.Operation;
import br.com.ggdio.client.soap.model.Port;
import br.com.ggdio.client.soap.model.Service;

/**
 * SOAP Webservice Invocation Model
 * @author Guilherme Dio
 *
 */
public class Invocation extends TO {

	private static final long serialVersionUID = 1L;
	
	public static final String SERVICE = "SERVICE";
	public static final String PORT = "PORT";
	public static final String OPERATION = "OPERATION";
	public static final String INPUT = "INPUT";
	
	public Invocation() {
		
	}
	
	public Invocation(Service service, Port port, Operation operation, SchemaValue input) {
		setService(service);
		setPort(port);
		setOperation(operation);
		setInput(input);
	}
	
	public Invocation(Service service, Port port, Operation operation) {
		this(service, port, operation, null);
	}

	public Service getService(){
		return (Service) getData(SERVICE);
	}
	
	public void setService(Service service){
		addData(SERVICE, service);
	}
	
	public Port getPort(){
		return (Port) getData(PORT);
	}
	
	public void setPort(Port port){
		addData(PORT, port);
	}
	
	public Operation getOperation(){
		return (Operation) getData(OPERATION);
	}
	
	public void setOperation(Operation operation){
		addData(OPERATION, operation);
	}
	
	public SchemaValue getInput(){
		return (SchemaValue) getData(INPUT);
	}
	
	public void setInput(SchemaValue part){
		addData(INPUT, part);
	}
	
}