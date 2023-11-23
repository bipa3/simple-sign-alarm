package bitedu.bipa.simplesignalarm.dao;

import bitedu.bipa.simplesignalarm.mapper.CommonMapper;
import bitedu.bipa.simplesignalarm.model.dto.PositionAndGradeDTO;
import org.springframework.stereotype.Repository;


@Repository
public class CommonDAO {

    private final CommonMapper commonMapper;

    public CommonDAO(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public PositionAndGradeDTO getPositionAndGrade(int userId) {
        return commonMapper.getPositionAndGrade(userId);
    }
}
