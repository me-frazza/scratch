package Basket;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;


public class DistributeOrderDSL {
    public static void main(String[] args) throws Exception {
       CamelContext context = new DefaultCamelContext();
       try {
          context.addRoutes(new RouteBuilder() {
             @Override
             public void configure() throws Exception {
                from("direct:DistributeOrderDSL")
                   .split(xpath("//order[@product='soaps']/items"))
                   .to("stream:out");
                  // .to("file:src/main/resources/order/");
             }
          });
          context.start();
          ProducerTemplate orderProducerTemplate = context.createProducerTemplate();
          
          // InputStream orderInputStream = new FileInputStream(ClassLoader.getSystemClassLoader().getResource("order.xml").getFile());
          File file = new File("order.xml");

          InputStream orderInputStream = new FileInputStream(file);

          orderProducerTemplate.sendBody("direct:DistributeOrderDSL", orderInputStream);
       } finally {
          context.stop();
       }
    }
 }
