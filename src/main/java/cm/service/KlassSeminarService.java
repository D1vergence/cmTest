package cm.service;


import cm.dao.KlassSeminarDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KlassSeminarService {

    @Autowired
    private KlassSeminarDAO klassSeminarDAO;

    public void deleteByKlassSeminarId(long klassSeminarId){
        klassSeminarDAO.deleteByKlassSeminarId(klassSeminarId);
    }
}
