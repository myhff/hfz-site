package hff.elegant.blog.web.jws.client;

public class WebServiceClient {

    public static void main(String[] args) {

        HelloService service = new HelloServiceService().getWebService();

        String msg = service.sayHello("Bumpkins");
        System.out.println(msg);
    }

}
