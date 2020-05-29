/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.service.websocketclient;

import com.google.gson.Gson;
import com.ivietech.demo.dao.RechagerRepository;
import com.ivietech.demo.entity.Recharge;
import com.ivietech.demo.model.app.Noti;
import com.ivietech.demo.service.BalanceService;
import com.ivietech.demo.utils.DataProcessing;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

/**
 *
 * @author HoangMinh
 */
@Service
public class SimpleWsHandler implements WebSocketHandler {

    @Autowired
    RechagerRepository rechagerRepository;
    @Autowired
    BalanceService balanceService;
    @Autowired
    private ApplicationContext appContext;

    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        Gson gson = new Gson();
        String mesage = wsm.getPayload().toString();
        System.out.println(mesage);
        long id = 0;
        long money = 0;
        String string = DataProcessing.removeAccent(mesage);
        try {
            Noti notiApp = gson.fromJson(string, Noti.class);
            if ("Nhan tien thanh cong".equalsIgnoreCase(notiApp.getPush().getTitle())) {
                List<Long> array = new ArrayList<>();
                String body = notiApp.getPush().getBody().replaceAll("[^0-9,-\\.]", ",");
                System.out.println(body);
                String[] item = body.split(",");
                for (int i = 0; i < item.length; i++) {
                    try {
                        item[i] = item[i].replaceAll("[^0-9]", "");
                        long s = Integer.parseInt(item[i]);
                        array.add(s);
                    } catch (NumberFormatException e) {

                    }
                }
                id = array.get(2);
                money = array.get(0);
            }
            if ("F@st Mobile".equalsIgnoreCase(notiApp.getPush().getTitle())) {
                String body = notiApp.getPush().getBody();
                String[] item = body.split("\n");
                String value = item[1].replaceAll("[^0-9]", "");
                id = Integer.parseInt(item[3]);
                money = Long.parseLong(value);
            }

        } catch (Exception e) {
            try {
                com.ivietech.demo.model.sms.Noti notiSms = gson.fromJson(string, com.ivietech.demo.model.sms.Noti.class);
                List<Long> array = new ArrayList<>();
                if ("BIDV".equalsIgnoreCase(notiSms.getPush().getNotifications().get(0).getTitle())) {
                    String body = notiSms.getPush().getNotifications().get(0).getBody().replaceAll("[^0-9 ]", "");
                    String[] item = body.split(" ");
                    for (int i = 0; i < item.length; i++) {
                        try {
                            long s = Long.parseLong(item[i]);
                            array.add(s);
                        } catch (Exception err) {
                        }
                    }
                    id = array.get(array.size() - 1);
                    money = array.get(1);

                }
                if ("VietinBank".equalsIgnoreCase(notiSms.getPush().getNotifications().get(0).getTitle())) {
                    String body = notiSms.getPush().getNotifications().get(0).getBody().replaceAll("[^0-9 |]", "");
                    String[] item = body.split("[|]");
                    String[] nd = item[4].split("[ ]");
                    id = Long.parseLong(nd[0]);
                    money = Long.parseLong(item[2]);

                }
                if ("Vietcombank".equalsIgnoreCase(notiSms.getPush().getNotifications().get(0).getTitle())) {
                    System.out.println("abc");
                    int rs = notiSms.getPush().getNotifications().get(0).getBody().lastIndexOf("Ref");
                    int rs1 = notiSms.getPush().getNotifications().get(0).getBody().lastIndexOf("FT");
                    if (rs1 != -1) {
                        String ref = notiSms.getPush().getNotifications().get(0).getBody().substring(rs, rs1);
                        String body = notiSms.getPush().getNotifications().get(0).getBody().replaceAll("[^0-9\\s]", "").substring(0, rs);
                        String[] item = body.split(" ");
                        for (int i = 0; i < item.length; i++) {
                            try {
                                long s = Long.parseLong(item[i]);
                                array.add(s);
                            } catch (NumberFormatException es) {
                            }
                        }
                        ref = ref.replaceAll("[^0-9]", ",");
                        String[] item1 = ref.split(",");
                        for (int i = 0; i < item1.length; i++) {
                            try {
                                long s = Long.parseLong(item1[i]);
                                array.add(s);
                            } catch (NumberFormatException ess) {
                            }
                        }
                        // String body = noti1.getPush().getBody().replaceAll("[^0-9\\s]", "");
                    } else {
                        System.out.println("abcd");
                        String ref = notiSms.getPush().getNotifications().get(0).getBody().substring(rs);
                        String ft = notiSms.getPush().getNotifications().get(0).getBody().substring(0, rs);
                        String body = ft.replaceAll("[^0-9\\s]", "");
                        String[] item = body.split(" ");
                        for (int i = 0; i < item.length; i++) {
                            try {
                                long s = Long.parseLong(item[i]);
                                array.add(s);
                            } catch (NumberFormatException ess) {
                            }
                        }
                        ref = ref.replaceAll("[^0-9]", ",");
                        String[] item1 = ref.split(",");
                        for (int i = 0; i < item1.length; i++) {
                            try {
                                long s = Long.parseLong(item1[i]);
                                array.add(s);
                            } catch (NumberFormatException ess) {
                            }
                        }
                    }
                    id = array.get(array.size() - 1);
                    money = array.get(0);
                }

            } catch (Exception er) {

            }
        }
        if (id != 0) {
            Optional<Recharge> recharge = rechagerRepository.findById(id);
            if (recharge.isPresent()) {
                if (recharge.get().getStatus().equalsIgnoreCase("Đang đợi")) {
                    if (recharge.get().getMoney() == money) {
                        recharge.get().setStatus("Thành Công");
                        balanceService.changeMoney(recharge.get().getUser().getBalance(), money);
                        rechagerRepository.save(recharge.get());
                    }
                }

            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println(cs.getCode());
        WebSocketConnectionManager manager = appContext.getBean(WebSocketConnectionManager.class);
        manager.start();
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }

}
