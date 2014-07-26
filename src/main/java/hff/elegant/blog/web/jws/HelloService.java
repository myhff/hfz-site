package hff.elegant.blog.web.jws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

@WebService(name="helloService", portName="web-service", targetNamespace="http://www.webservice.com")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class HelloService {

    @WebMethod(action="sayHello", operationName="sayHello", exclude=false)
    public String sayHello(@WebParam(name="name")String name) {
        return "hiiiiiiiii, " + name;
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/bumpkins-service", new HelloService());
    }
}
