package com.lojka.kurs.service.app;


import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.queriesV2.*;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.repository.app.ChartSelectionRepository;
import com.lojka.kurs.repository.app.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class ChartSelectionService {


    @Autowired
    private ChartSelectionRepository repository;

    public void createSelection(User user, Selection selection) throws SQLException, DbAccessException {
        repository.insertSelection(selection,user);
        repository.insertSelectionsHeroesAndItems(selection,user);
    }
    public void updateSelection(User user, Selection selection) throws SQLException, DbAccessException {
        repository.updateSelection(selection,user);
        repository.insertSelectionsHeroesAndItems(selection,user);
    }
    public void deleteSelection(Integer id)throws SQLException, DbAccessException{
        repository.deleteSelection(id);
    }

    public ArrayList<Selection> getSelections(User user)throws SQLException, DbAccessException{
        return repository.getSelections(user);
    }
    public Selection getSelection(Integer id)throws SQLException, DbAccessException{
        return repository.getSelection(id);
    }
}
