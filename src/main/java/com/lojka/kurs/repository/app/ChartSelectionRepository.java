package com.lojka.kurs.repository.app;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.queriesV2.*;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.linee_chart.LineChart;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.super_service.SuperService;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class ChartSelectionRepository {

    static String hostName = "192.168.0.106";
    static String sid = "/pdb_kurs";
    static String userName = "pdb_kurs_app_admin";
    static String password = "password";
    Connection connection;

    String sqlInsertSelection = "begin insert_selection(?,?,?,?,?,?,?,?,?,?); end;";
    String sqlUpdateSelection = "begin update_selection(?,?,?,?,?,?,?,?,?,?); end;";
    String sqlDeleteSelection = "begin delete_selection(?); end;";
    String sqlInsertSelectionHeroes = "begin insert_selection_heroes(?,?,?); end;";
    String sqlClearSelectionHeroes = "begin clear_selection_heroes(?); end;";
    String sqlInsertSelectionItems = "begin insert_selection_items(?,?,?); end;";
    String sqlClearSelectionItems = "begin clear_selection_items(?); end;";

    String sqlSelectSelections = "begin SELECT_selections(?,?); end;";
    String sqlSelectSelection = "begin get_selection(?,?); end;";
    String sqlSelectSelectionsHeroes = "begin SELECT_selections_heroes(?,?); end;";
    String sqlSelectSelectionsItems = "begin SELECT_selections_items(?,?); end;";

    String sqlImport = "begin importapp(); end;";
    String sqlExport = "begin exportapp(); end;";


    public Connection getConnection() throws SQLException, DbAccessException {
        if (connection==null || connection.isClosed()){
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521" + sid;

                connection = DriverManager.getConnection(connectionURL, userName,
                        password);
                return connection;
            } catch (ClassNotFoundException e) {
                throw new DbAccessException("drive class not found");
            } catch (SQLException e){
                throw new DbAccessException("error with connection to db");
            }
        }
        return connection;
    }

    public void insertSelection(Selection selection, User user)throws SQLException, DbAccessException{
        log.debug("insert selection by " + user.getLogin() + " selection: " + selection.getSelectionName());
        CallableStatement cs = getConnection().prepareCall(sqlInsertSelection);
        cs.setString(1, selection.getSelectionName());
        cs.setInt(2, selection.getDurationMin());
        cs.setInt(3, selection.getDurationMax());
        cs.setInt(4, selection.getPatchMin());
        cs.setInt(5, selection.getPatchMax());
        cs.setDate(6, new Date(selection.getDateMin().getTime()));
        cs.setDate(7, new Date(selection.getDateMax().getTime()));
        cs.setInt(8, selection.getHero().getId());
        cs.setInt(9, user.getId());
        cs.registerOutParameter(10, OracleTypes.INTEGER);
        if (cs.executeQuery()!=null){
            if (cs.getInt(10)<0){
                throw new DbAccessException("Selection with this name is already exist");
            }
            selection.setId(cs.getInt(10));
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }

    }
    public void insertSelectionsHeroesAndItems(Selection selection, User user)throws SQLException, DbAccessException{
        log.debug("insert selection by " + user.getLogin());
        CallableStatement cs = getConnection().prepareCall(sqlClearSelectionHeroes);
        cs.setInt(1, selection.getId());
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            throw new DbAccessException("error with query");
        }
        log.debug("inserting allies...");
        for(Hero hero : selection.getAllies()){
            cs = getConnection().prepareCall(sqlInsertSelectionHeroes);
            cs.setInt(1,selection.getId());
            cs.setInt(2,hero.getId());
            cs.setString(3,"ally");
            if (cs.executeQuery()!=null){
                cs.close();
            }else {
                throw new DbAccessException("error with query");
            }
        }
        log.debug("inserting enemies...");
        for(Hero hero : selection.getEnemies()){
            cs = getConnection().prepareCall(sqlInsertSelectionHeroes);
            cs.setInt(1,selection.getId());
            cs.setInt(2,hero.getId());
            cs.setString(3,"enemy");
            if (cs.executeQuery()!=null){
                cs.close();
            }else {
                cs.close();
                throw new DbAccessException("error with query");
            }
        }

        log.debug("clear items");
        cs = getConnection().prepareCall(sqlClearSelectionItems);
        cs.setInt(1, selection.getId());
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
        log.debug("inserting items...");
        for(Item item : selection.getItems()){
            cs = getConnection().prepareCall(sqlInsertSelectionItems);
            cs.setInt(1,selection.getId());
            cs.setInt(2,item.getId());
            cs.setString(3,"boughtItem");
            if (cs.executeQuery()!=null){
                cs.close();
            }else {
                cs.close();
                throw new DbAccessException("error with query");
            }
        }
    }

    public void updateSelection(Selection selection, User user)throws SQLException, DbAccessException{
        log.debug("update selection by " + user.getLogin() + " selection: " + selection.getSelectionName());
        CallableStatement cs = getConnection().prepareCall(sqlUpdateSelection);
        cs.setString(1, selection.getSelectionName());
        cs.setInt(2, selection.getDurationMin());
        cs.setInt(3, selection.getDurationMax());
        cs.setInt(4, selection.getPatchMin());
        cs.setInt(5, selection.getPatchMax());
        cs.setDate(6, new Date(selection.getDateMin().getTime()));
        cs.setDate(7, new Date(selection.getDateMax().getTime()));
        cs.setInt(8, selection.getHero().getId());
        cs.setInt(9, user.getId());
        cs.setInt(10, selection.getId());
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }

    public void deleteSelection(Integer id)throws SQLException, DbAccessException{
        log.debug("delete selection by " + id);
        CallableStatement cs = getConnection().prepareCall(sqlDeleteSelection);
        cs.setInt(1,id);
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }


    public ArrayList<Selection> getSelections(User user)throws SQLException, DbAccessException{
        Map<Integer, Selection> selections = new HashMap<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectSelections);
        cs.setInt(2,user.getId());
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1,ResultSet.class);
        while(rs.next()){
            Selection selection = new Selection();
            selection.setId(rs.getInt("id"));
            selection.setSelectionName(rs.getString("name"));
            selection.setDurationMin(rs.getInt(3));
            selection.setDurationMax(rs.getInt(4));
            selection.setPatchMin(rs.getInt(5));
            selection.setPatchMax(rs.getInt(6));
            selection.setDateMin(rs.getDate(7));
            selection.setDateMax(rs.getDate(8));
            selection.setHero(SuperService.getHeroes().get(rs.getInt(9)));


            addHeroesAndItemsToSelection(selection);
            selections.put(selection.getId(),selection);

        }
        cs.close();



        ArrayList<Selection> result = new ArrayList<>();
        for(Integer key : selections.keySet()){
            result.add(selections.get(key));
        }
        return result;
    }

    public Selection getSelection(Integer id)throws SQLException, DbAccessException{

        CallableStatement cs = getConnection().prepareCall(sqlSelectSelection);
        cs.setInt(2,id);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1,ResultSet.class);
        if(rs.next()){
            Selection selection = new Selection();
            selection.setId(rs.getInt("id"));
            selection.setSelectionName(rs.getString("name"));
            selection.setDurationMin(rs.getInt(3));
            selection.setDurationMax(rs.getInt(4));
            selection.setPatchMin(rs.getInt(5));
            selection.setPatchMax(rs.getInt(6));
            selection.setDateMin(rs.getDate(7));
            selection.setDateMax(rs.getDate(8));
            selection.setHero(SuperService.getHeroes().get(rs.getInt(9)));

            addHeroesAndItemsToSelection(selection);
            cs.close();
            return selection;
        }else {
            cs.close();
            throw new DbAccessException("Error with query");
        }
    }

    void addHeroesAndItemsToSelection(Selection selection)throws SQLException, DbAccessException{
        CallableStatement cs2 =getConnection().prepareCall(sqlSelectSelectionsHeroes);
        cs2.registerOutParameter(1,OracleTypes.CURSOR);
        cs2.setInt(2,selection.getId());
        cs2.executeQuery();
        ResultSet rs2 = cs2.getObject(1,ResultSet.class);
        while(rs2.next()){
            String her = rs2.getString(3);
            if (rs2.getString(3).equals("enemy")){
                selection.enemies.add(SuperService.getHeroes().get(rs2.getInt(2)));
            }else  if (rs2.getString(3).equals("ally")){
                selection.allies.add(SuperService.getHeroes().get(rs2.getInt(2)));
            }
        }
        rs2.close();
        cs2.close();

        cs2 =getConnection().prepareCall(sqlSelectSelectionsItems);
        cs2.registerOutParameter(1,OracleTypes.CURSOR);
        cs2.setInt(2,selection.getId());
        cs2.executeQuery();
        rs2 = cs2.getObject(1,ResultSet.class);
        while(rs2.next()){
            if (rs2.getString(3).equals("boughtItem")){
                selection.items.add(SuperService.getItems().get(rs2.getInt(2)));
            }
        }
        rs2.close();
        cs2.close();
    }


    //bar charts
    String sqlInsertBarChart = "begin insert_barchart(?,?,?,?,?,?); end;";
    String sqlInsertBarChartSelections = "begin insert_selections_barchart(?,?); end;";
    String sqlClearBarChartSelections = "begin clear_selections_barchart(?); end;";
    String sqlInsertBarChartLabels = "begin insert_barchart_labels(?,?); end;";
    String sqlClearBarChartLabels = "begin clear_barchart_labels(?); end;";

    String sqlSelectBarCharts = "begin SELECT_barcharts(?,?); end;";
    String sqlSelectBarChart = "begin SELECT_barchart(?,?); end;";
    String sqlSelectBarChartsSelections = "begin SELECT_barcharts_selections(?,?); end;";
    String sqlSelectBarChartsLabels = "begin SELECT_barcharts_labels(?,?); end;";

    String sqlUpdateBarChart = "begin update_barchart(?,?,?,?,?); end;";
    String sqlDeleteBarChart = "begin delete_barchart(?); end;";

    public void insertBarChart(BarChart chart, User user)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlInsertBarChart);
        cs.setInt(1, chart.getMinCountOfMatches());
        cs.setString(2, chart.getName());
        cs.setString(3, chart.getxAxis());
        cs.setString(4, chart.getyAxis());
        cs.setInt(5, user.getId());
        cs.registerOutParameter(6,OracleTypes.INTEGER);
        cs.executeQuery();
        if (cs.getInt(6)<0){
            cs.close();
            throw new DbAccessException("chart with this name exist");
        }else{
            cs.close();
            chart.setId(cs.getInt(6));
        }
    }
    public void insertBarChartSelections(BarChart chart)throws SQLException, DbAccessException{
        CallableStatement clear = getConnection().prepareCall(sqlClearBarChartSelections);
        clear.setInt(1,chart.getId());
        clear.executeQuery();
        clear.close();
        for(Selection selection : chart.getSelections()){
            CallableStatement cs = getConnection().prepareCall(sqlInsertBarChartSelections);
            cs.setInt(1,selection.getId());
            cs.setInt(2,chart.getId());
            cs.executeQuery();
            cs.close();
        }
    }
    public void insertBarChartLabels(BarChart chart)throws SQLException, DbAccessException{
        CallableStatement clear = getConnection().prepareCall(sqlClearBarChartLabels);
        clear.setInt(1,chart.getId());
        clear.executeQuery();
        clear.close();
        for(String label : chart.getxLabels()){
            CallableStatement cs = getConnection().prepareCall(sqlInsertBarChartLabels);
            cs.setString(2,label);
            cs.setInt(1,chart.getId());
            cs.executeQuery();
            cs.close();
        }
    }

    public ArrayList<BarChart> getBarCharts(User user)throws SQLException, DbAccessException{
        ArrayList<BarChart> result = new ArrayList<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectBarCharts);
        cs.setInt(2,user.getId());
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1, ResultSet.class);
        while (rs.next()){
            BarChart chart = new BarChart();
            chart.setId(rs.getInt(1));
            chart.setName(rs.getString(3));
            chart.setMinCountOfMatches(rs.getInt(2));
            chart.setxAxis(rs.getString(4));
            chart.setyAxis(rs.getString(5));

            addLabelsAndSelectionsToBarChart(chart);
            result.add(chart);
        }
        rs.close();
        cs.close();
        return result;
    }
    public BarChart getBarChart(Integer id)throws SQLException, DbAccessException{
        ArrayList<BarChart> result = new ArrayList<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectBarChart);
        cs.setInt(2,id);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1, ResultSet.class);
        if (rs.next()){
            BarChart chart = new BarChart();
            chart.setId(rs.getInt(1));
            chart.setName(rs.getString(3));
            chart.setMinCountOfMatches(rs.getInt(2));
            chart.setxAxis(rs.getString(4));
            chart.setyAxis(rs.getString(5));

            addLabelsAndSelectionsToBarChart(chart);
            rs.close();
            cs.close();
            return chart;
        }else {
            rs.close();
            cs.close();
            throw new DbAccessException("there is no such chart...");
        }
    }
    void addLabelsAndSelectionsToBarChart(BarChart chart)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlSelectBarChartsLabels);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.setInt(2,chart.getId());
        cs.executeQuery();
        ResultSet rs = cs.getObject(1,ResultSet.class);
        while (rs.next()){
            chart.getxLabels().add(rs.getString(2));
        }
        cs.close();

        cs = getConnection().prepareCall(sqlSelectBarChartsSelections);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.setInt(2,chart.getId());
        cs.executeQuery();
        rs = cs.getObject(1,ResultSet.class);
        while (rs.next()){
            chart.getSelections().add(getSelection(rs.getInt(1)));
        }
        rs.close();
        cs.close();
    }

    public void updateBarChart(BarChart chart)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlUpdateBarChart);
        cs.setInt(1, chart.getMinCountOfMatches());
        cs.setString(2, chart.getName());
        cs.setString(3, chart.getxAxis());
        cs.setString(4, chart.getyAxis());
        cs.setInt(5, chart.getId());
        cs.executeQuery();
        cs.close();
    }
    public void deleteBarChart(Integer id)throws SQLException, DbAccessException{
        log.debug("delete barchart by " + id);
        CallableStatement cs = getConnection().prepareCall(sqlDeleteBarChart);
        cs.setInt(1,id);
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }

    //line charts
    String sqlInsertLineChart = "begin insert_linechart(?,?,?,?,?,?,?,?,?); end;";

    String sqlSelectLineCharts = "begin SELECT_linecharts(?,?); end;";
    String sqlSelectLineChart = "begin SELECT_linechart(?,?); end;";

    String sqlUpdateLineChart = "begin update_linechart(?,?,?,?,?,?,?,?); end;";
    String sqlDeleteLineChart = "begin delete_linechart(?); end;";

    public void insertLineChart(LineChart chart, User user)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlInsertLineChart);
        cs.setInt(1, chart.getMinCountOfMatches());
        cs.setString(2, chart.getName());
        cs.setString(3, chart.getxAxis());
        cs.setString(4, chart.getyAxis());
        cs.setInt(5, user.getId());
        cs.setInt(6,chart.getCountOfLabels());
        cs.setBoolean(7,chart.getDeasc());
        cs.setInt(8,chart.getSelection().getId());
        cs.registerOutParameter(9,OracleTypes.INTEGER);
        cs.executeQuery();
        if (cs.getInt(9)<0){
            cs.close();
            throw new DbAccessException("chart with this name exist");
        }else{
            chart.setId(cs.getInt(9));
            cs.close();
        }
    }

    public ArrayList<LineChart> getLineCharts(User user)throws SQLException, DbAccessException{
        ArrayList<LineChart> result = new ArrayList<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectLineCharts);
        cs.setInt(2,user.getId());
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1, ResultSet.class);
        while (rs.next()){
            LineChart chart = new LineChart();
            chart.setId(rs.getInt(1));
            chart.setName(rs.getString(3));
            chart.setMinCountOfMatches(rs.getInt(2));
            chart.setxAxis(rs.getString(4));
            chart.setyAxis(rs.getString(5));
            chart.setCountOfLabels(rs.getInt(6));
            chart.setDeasc(rs.getBoolean(7));
            chart.setSelection(getSelection(rs.getInt(8)));
            result.add(chart);
        }
        rs.close();
        cs.close();
        return result;
    }
    public LineChart getLineChart(Integer id)throws SQLException, DbAccessException{
        ArrayList<LineChart> result = new ArrayList<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectLineChart);
        cs.setInt(2,id);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1, ResultSet.class);
        if (rs.next()){
            LineChart chart = new LineChart();
            chart.setId(rs.getInt(1));
            chart.setName(rs.getString(3));
            chart.setMinCountOfMatches(rs.getInt(2));
            chart.setxAxis(rs.getString(4));
            chart.setyAxis(rs.getString(5));
            chart.setCountOfLabels(rs.getInt(6));
            chart.setDeasc(rs.getBoolean(7));
            chart.setSelection(getSelection(rs.getInt(8)));
            rs.close();
            cs.close();
            return chart;
        }else {
            rs.close();
            cs.close();
            throw new DbAccessException("there is no such chart...");
        }
    }

    public void updateLineChart(LineChart chart)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlUpdateLineChart);
        cs.setInt(1, chart.getMinCountOfMatches());
        cs.setString(2, chart.getName());
        cs.setString(3, chart.getxAxis());
        cs.setString(4, chart.getyAxis());
        cs.setInt(5,chart.getCountOfLabels());
        cs.setBoolean(6,chart.getDeasc());
        cs.setInt(7,chart.getSelection().getId());
        cs.setInt(8,chart.getId());
        cs.executeQuery();
        cs.close();
    }
    public void deleteLineChart(Integer id)throws SQLException, DbAccessException{
        log.debug("delete Linechart by " + id);
        CallableStatement cs = getConnection().prepareCall(sqlDeleteLineChart);
        cs.setInt(1,id);
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }


    //bubble charts
    String sqlInsertBubbleChart = "begin insert_bubblechart(?,?,?,?,?,?,?); end;";
    String sqlInsertBubbleChartSelections = "begin insert_selections_bubblechart(?,?); end;";
    String sqlClearBubbleChartSelections = "begin clear_selections_bubblechart(?); end;";

    String sqlSelectBubbleCharts = "begin SELECT_bubblecharts(?,?); end;";
    String sqlSelectBubbleChart = "begin SELECT_bubblechart(?,?); end;";
    String sqlSelectBubbleChartsSelections = "begin SELECT_bubblecharts_selections(?,?); end;";

    String sqlUpdateBubbleChart = "begin update_bubblechart(?,?,?,?,?,?); end;";
    String sqlDeleteBubbleChart = "begin delete_bubblechart(?); end;";

    public void insertBubbleChart(BubbleChart chart, User user)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlInsertBubbleChart);
        cs.setInt(1, chart.getMinCountOfMatches());
        cs.setString(2, chart.getName());
        cs.setString(3, chart.getxAxis());
        cs.setString(4, chart.getyAxis());
        cs.setFloat(5, chart.getxScale());
        cs.setInt(6, user.getId());
        cs.registerOutParameter(7,OracleTypes.INTEGER);
        cs.executeQuery();
        if (cs.getInt(7)<0){
            cs.close();
            throw new DbAccessException("chart with this name exist");
        }else{
            chart.setId(cs.getInt(7));
            cs.close();
        }
    }
    public void insertBubbleChartSelections(BubbleChart chart)throws SQLException, DbAccessException{
        CallableStatement clear = getConnection().prepareCall(sqlClearBubbleChartSelections);
        clear.setInt(1,chart.getId());
        clear.executeQuery();
        clear.close();
        for(Selection selection : chart.getSelections()){
            CallableStatement cs = getConnection().prepareCall(sqlInsertBubbleChartSelections);
            cs.setInt(1,selection.getId());
            cs.setInt(2,chart.getId());
            cs.executeQuery();
            cs.close();
        }
    }

    public ArrayList<BubbleChart> getBubbleCharts(User user)throws SQLException, DbAccessException{
        ArrayList<BubbleChart> result = new ArrayList<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectBubbleCharts);
        cs.setInt(2,user.getId());
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1, ResultSet.class);
        while (rs.next()){
            BubbleChart chart = new BubbleChart();
            chart.setId(rs.getInt(1));
            chart.setName(rs.getString(3));
            chart.setMinCountOfMatches(rs.getInt(2));
            chart.setxAxis(rs.getString(4));
            chart.setyAxis(rs.getString(5));
            chart.setxScale(rs.getFloat(6));
            addSelectionsToBubbleChart(chart);
            result.add(chart);
        }
        rs.close();
        cs.close();
        return result;
    }
    public BubbleChart getBubbleChart(Integer id)throws SQLException, DbAccessException{
        ArrayList<BubbleChart> result = new ArrayList<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectBubbleChart);
        cs.setInt(2,id);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1, ResultSet.class);
        if (rs.next()){
            BubbleChart chart = new BubbleChart();
            chart.setId(rs.getInt(1));
            chart.setName(rs.getString(3));
            chart.setMinCountOfMatches(rs.getInt(2));
            chart.setxAxis(rs.getString(4));
            chart.setyAxis(rs.getString(5));
            chart.setxScale(rs.getFloat(6));
            addSelectionsToBubbleChart(chart);
            rs.close();
            cs.close();
            return chart;
        }else {
            rs.close();
            cs.close();
            throw new DbAccessException("there is no such chart...");
        }
    }
    void addSelectionsToBubbleChart(BubbleChart chart)throws SQLException, DbAccessException{

        CallableStatement cs = getConnection().prepareCall(sqlSelectBubbleChartsSelections);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.setInt(2,chart.getId());
        cs.executeQuery();
        ResultSet rs = cs.getObject(1,ResultSet.class);
        while (rs.next()){
            chart.getSelections().add(getSelection(rs.getInt(1)));
        }
        cs.close();
    }

    public void updateBubbleChart(BubbleChart chart)throws SQLException, DbAccessException{
        CallableStatement cs = getConnection().prepareCall(sqlUpdateBubbleChart);
        cs.setInt(1, chart.getMinCountOfMatches());
        cs.setString(2, chart.getName());
        cs.setString(3, chart.getxAxis());
        cs.setString(4, chart.getyAxis());
        cs.setFloat(5, chart.getxScale());
        cs.setInt(6, chart.getId());
        cs.executeQuery();
        cs.close();
    }
    public void deleteBubbleChart(Integer id)throws SQLException, DbAccessException{
        log.debug("delete bubblechart by " + id);
        CallableStatement cs = getConnection().prepareCall(sqlDeleteBubbleChart);
        cs.setInt(1,id);
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }

    //export import
    public void importApp()throws SQLException, DbAccessException{
        log.debug("importing...");
        CallableStatement cs = getConnection().prepareCall(sqlImport);
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }
    public void exportApp()throws SQLException, DbAccessException{
        log.debug(" exporting...");
        CallableStatement cs = getConnection().prepareCall(sqlExport);
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            cs.close();
            throw new DbAccessException("error with query");
        }
    }

}
