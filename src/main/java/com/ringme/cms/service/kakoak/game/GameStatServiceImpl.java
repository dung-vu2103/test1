package com.ringme.cms.service.kakoak.game;

import com.ringme.cms.common.ExportExcel;
import com.ringme.cms.common.Helper;
import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoak.game.GameStatDto;
import com.ringme.cms.model.kakoak.game.GameStat;
import com.ringme.cms.repository.kakoak.game.GameStatRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@Transactional(value = "kakoakTransactionManager")
public class GameStatServiceImpl implements GameStatService {
    @Autowired
    UploadFile uploadFile;

    @Autowired
    GameStatRepository gameStatRepository;

    @Autowired
    ExportExcel export;

    @Override
    public Page<GameStat> getList(Long gameId, String date, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Date startTime = null;
        Date endTime = null;

        if (gameId != null) {
            String gameIdStr = gameId.toString().trim();
            if (gameIdStr.trim().equals("")) {
                gameId = null;
            } else {
                gameId = Long.parseLong(gameIdStr);
            }
        }

        if (date != null && date != "") {
            if (!date.trim().equals("")) {
                String[] parts = date.split(" - ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate zonedDateTime = LocalDate.parse(parts[0], formatter);
                LocalDate zonedDateTime2 = LocalDate.parse(parts[1], formatter);
                startTime = Date.from(zonedDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
                endTime = Date.from(zonedDateTime2.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        }
        log.info("===publishedTime Search===>startTime2 {} endTime2 {}", startTime, endTime);

        return gameStatRepository.getList(gameId, startTime, endTime, pageable);
    }

    @Override
    public void exportExcel(Long gameId, String date, HttpServletResponse response) {
        String[] dateRangers = Helper.reportDate(date);
        List<GameStat> list = gameStatRepository.getDataExportExcel(gameId, dateRangers[0], dateRangers[1]);

        List<GameStatDto> gameStatDtos = new ArrayList<>();
        int i = 1;
        for (GameStat gameStat : list) {
            GameStatDto customGameData = new GameStatDto();
            customGameData.setNo(i);
            customGameData.setGameId(gameStat.getGameId());
            customGameData.setGameName(gameStat.getGameHtml5().getName());
            customGameData.setGetScore(gameStat.getGetScore());
            customGameData.setSubmit(gameStat.getSubmit());
            customGameData.setUserNumber(gameStat.getUserNumber());
            customGameData.setDate(gameStat.getDate());
            gameStatDtos.add(customGameData);
            i++;
        }

        String[] headers = {"No","ID", "Game", "Get Score", "submit", "User Number", "Date"};
        String title = "Game Stats";
        export.export(gameStatDtos, headers, response, date, title);
    }
}
