package com.d204.rumeet.game.model.service;

import com.d204.rumeet.game.model.dto.*;
import org.springframework.amqp.core.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameService {
    void makeRace(RaceDto gameDto);

    int inviteRace(RaceDto raceDto);

    int getRaceState(int raceId);

    RaceDto getRace(int raceId);

    void acceptRace(int raceId);

    void denyRace(int raceId);

    List<FriendRaceInfoDto> getInvitationList(int userId);

    void endGameToKafka(Message message) throws Exception;

    SoloPlayDto doSoloPlay(int userId, int mode, int ghost);

    List<RecommendDto> recommendMainPage(int userId);

    String savePoly(MultipartFile file);
}
