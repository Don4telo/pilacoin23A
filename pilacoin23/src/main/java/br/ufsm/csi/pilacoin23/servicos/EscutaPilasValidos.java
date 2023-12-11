package br.ufsm.csi.pilacoin23.servicos;

import br.ufsm.csi.pilacoin23.Persistencia.QueryResponsePilaRepositorio;
import br.ufsm.csi.pilacoin23.model.QueryResponse;
import br.ufsm.csi.pilacoin23.model.QueryResponsePila;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscutaPilasValidos {
    @Autowired
    private QueryResponsePilaRepositorio queryResponsePilaRepositorio;

    //recebe as queries de respostas das requisições enviadas pro server.
    @RabbitListener(queues = "alexandre-query")
    public void recebeQueryResposta(@Payload String queryResponse) {
        try {
            System.out.println("*********** RECEBIDA QUERY RESPOSTA...");
            System.out.println("*********** QUERY RECEBIDA DE RESPOSTA: " + queryResponse);
            //se é USUARIOS envia para tratamento de usuarios;
            ObjectMapper objectMapper = new ObjectMapper();
            QueryResponse queryResposta = objectMapper
                    .readValue(queryResponse, QueryResponse.class);
            //idQuery para query de listagem de usuários.
            //System.out.println("**************** queryResposta = " + queryResponse);
            //if(queryResposta.getUsuariosResult() != null && !queryResposta.getUsuariosResult().isEmpty()) {
            //    salvaListaUsuarios(queryResposta);
            //} else
            if (queryResposta.getPilasResult() != null && !queryResposta.getPilasResult().isEmpty()) {
                List<QueryResponsePila> pilasServidor = queryResposta.getPilasResult();
                queryResponsePilaRepositorio.saveAll(pilasServidor);
            }
            else {
                System.out.println("************** NÃO ENTROU EM NADA!");
            }
//            String retornoQuery = nodeQueryResposta.get("blocosResult").toString();
//            System.out.println("*********** RECEBIDA QUERY RESPOSTA...");
//            if(retornoQuery.isEmpty()) {
//                System.out.println("******** RECEBIDA QUERY USUARIOS ***");
//
//            } else {
//                System.out.println("******** RECEBIDA QUERY BLOCOS ***");
//                salvaListaBlocos(queryResposta);
//            }
            //se é BLOCOS envia para tratamento de blocos.
            //System.out.println("***** Query resposta recebida: "+ queryResposta);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao receber query resposta! ", e);
        }
    }





}

