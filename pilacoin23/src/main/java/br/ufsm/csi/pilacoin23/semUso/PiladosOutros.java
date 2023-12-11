package br.ufsm.csi.pilacoin23.semUso;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//@Service
public class PiladosOutros {
   //@RabbitListener(queues = "ewerton-joaokunde")
   //@RabbitListener(queues = "luiz_felipe")

    //@RabbitListener(queues = "bilar-pedro")
    //@RabbitListener(queues = "christian_alves")
    public void receiveMsgFeedbackUser(@Payload String feedback) {
        try {
            System.out.println("** Mensagem de feedback : "+ feedback);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao receber mensagem de feedback! ", e);
        }
    }
}

