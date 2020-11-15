package com.lojka.kurs.service.app;


import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.queriesV2.*;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.linee_chart.LineChart;
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

    //bar chart
    public void createBarChart(User user, BarChart chart)throws SQLException, DbAccessException{
        repository.insertBarChart(chart,user);
        repository.insertBarChartLabels(chart);
        repository.insertBarChartSelections(chart);
    }
    public ArrayList<BarChart> getBarCharts(User user)throws SQLException, DbAccessException{
        return repository.getBarCharts(user);
    }
    public BarChart getBarChart(Integer id)throws SQLException, DbAccessException{
        return repository.getBarChart(id);
    }
    public void updateBarChart(BarChart chart) throws SQLException, DbAccessException {
        repository.updateBarChart(chart);
        repository.insertBarChartLabels(chart);
        repository.insertBarChartSelections(chart);
    }
    public void deleteBarChart(Integer id)throws SQLException, DbAccessException{
        repository.deleteBarChart(id);
    }

    //line chart
    public void createLineChart(User user, LineChart chart)throws SQLException, DbAccessException{
        repository.insertLineChart(chart,user);
    }
    public ArrayList<LineChart> getLineCharts(User user)throws SQLException, DbAccessException{
        return repository.getLineCharts(user);
    }
    public LineChart getLineChart(Integer id)throws SQLException, DbAccessException{
        return repository.getLineChart(id);
    }
    public void updateLineChart(LineChart chart) throws SQLException, DbAccessException {
        repository.updateLineChart(chart);
    }
    public void deleteLineChart(Integer id)throws SQLException, DbAccessException{
        repository.deleteLineChart(id);
    }


    //bubble chart
    public void createBubbleChart(User user, BubbleChart chart)throws SQLException, DbAccessException{
        repository.insertBubbleChart(chart,user);
        repository.insertBubbleChartSelections(chart);
    }
    public ArrayList<BubbleChart> getBubbleCharts(User user)throws SQLException, DbAccessException{
        return repository.getBubbleCharts(user);
    }
    public BubbleChart getBubbleChart(Integer id)throws SQLException, DbAccessException{
        return repository.getBubbleChart(id);
    }
    public void updateBubbleChart(BubbleChart chart) throws SQLException, DbAccessException {
        repository.updateBubbleChart(chart);
        repository.insertBubbleChartSelections(chart);
    }
    public void deleteBubbleChart(Integer id)throws SQLException, DbAccessException{
        repository.deleteBubbleChart(id);
    }
}
